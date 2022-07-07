package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class A1Dao {
    @Autowired
    DataSource ds;

    public int insert(int key, int value) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
//            conn = ds.getConnection(); //Tx적용하려면 아래와 같이 변경
            conn = DataSourceUtils.getConnection(ds);
            System.out.println("conn = " + conn);
            pstmt = conn.prepareStatement("insert into a1 values(?, ?)");
            pstmt.setInt(1, key);
            pstmt.setInt(2, value);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; //예외를 먹어버리지 않고,
                    //테스트코드에서 예외발생한 것을 알 수 있도록 던져줘야 한다.
        } finally {
//            close(pstmt, conn); //Tx적용하려면 아래 2줄로 변경
            close(pstmt);
            DataSourceUtils.releaseConnection(conn, ds);
        }
    }
    private void close(AutoCloseable... acs) {
        for(AutoCloseable ac :acs)
            try { if(ac!=null) ac.close(); } catch(Exception e) { e.printStackTrace(); }
    }
    public void deleteAll() throws Exception {
        Connection conn = ds.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("delete from a1");
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
//        close(pstmt, conn);
    }
}
