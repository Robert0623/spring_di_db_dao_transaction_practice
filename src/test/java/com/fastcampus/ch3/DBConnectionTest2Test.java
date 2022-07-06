package com.fastcampus.ch3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class DBConnectionTest2Test {
    @Autowired
    DataSource ds;

    @Test
    public void insertUserTest() throws Exception {
        deleteAll();
        User user = new User("asdf2", "1234", "abc", "aaaa@aaa.com", new Date(), "fb", new Date());
        int rowCnt = insertUser(user);

        assertTrue(rowCnt==1);
    }

    @Test
    public void selectUserTest() throws Exception {
        deleteAll();
        User user = new User("asdf2", "1234", "abc", "aaaa@aaa.com", new Date(), "fb", new Date());
        int rowCnt = insertUser(user);
        User user2 = selectUser("asdf2");
        assertTrue(user.getId().equals("asdf2"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        deleteAll();
        int rowCnt = deleteUser("asdf");
        assertTrue(rowCnt==0);

        User user = new User("asdf2", "1234", "abc", "aaaa@aaa.com", new Date(), "fb", new Date());
        rowCnt = insertUser(user);
        assertTrue(rowCnt==1);

        rowCnt = deleteUser(user.getId());
        assertTrue(rowCnt==1);

        assertTrue(selectUser(user.getId())==null);
    }

    @Test
    public void updateUserTest() throws Exception {
        deleteAll();
        User user = new User("asdf1", "1234", "abc", "aaaa@aaa.com", new Date(), "fb", new Date());
        User user2 = new User("asdf1", "2222", "ddd", "ddd@aaa.com", new Date(), "kakao", new Date());
        int rowCnt = insertUser(user);
        assertTrue(rowCnt==1);

        rowCnt = updateUser(user2);
        System.out.println("user = " + user);
        System.out.println("user2 = " + user2);
        System.out.println("rowCnt = " + rowCnt);
        assertTrue(rowCnt==1);
    }

    //매개변수로 받은 사용자 정보로 user_info테이블을 update하는 메서드
    public int updateUser(User user) throws Exception {
        Connection conn = ds.getConnection();
        String sql = "update user_info set pwd=?,name=?,email=?,birth=?,sns=?, reg_date=? where id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getPwd());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setDate(4, new java.sql.Date(user.getBirth().getTime()));
        pstmt.setString(5, user.getSns());
        pstmt.setDate(6, new java.sql.Date(user.getReg_date().getTime()));
        pstmt.setString(7, user.getId());

        int rowCnt = pstmt.executeUpdate();
        return rowCnt;
    }

    public int deleteUser(String id) throws Exception {
        Connection conn = ds.getConnection();
        String sql = "delete from user_info where id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
//        int rowCnt = pstmt.executeUpdate();
//        return rowCnt;
        return pstmt.executeUpdate();
    }

    public User selectUser(String id) throws Exception {
        Connection conn = ds.getConnection();
        String sql = "select * from user_info where id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery(); //select는 executeQuery() - 테이블인 ResultSet으로 받아야 한다.

        if (rs.next()) { //rs에서 값을 받아서, user객체에 채워서 반환하면 된다.
            User user = new User(); //이 user객체에 값을 채워서 반환한다.
            user.setId(rs.getString(1));
            user.setPwd(rs.getString(2));
            user.setName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setBirth(new Date(rs.getDate(5).getTime()));
            user.setSns(rs.getString(6));
            user.setReg_date(new Date(rs.getTimestamp(7).getTime()));

            return user;
        }
        return null;
    }

    private void deleteAll() throws Exception {
        Connection conn = ds.getConnection();
        String sql = "delete from user_info";
        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Infection, 성능 향상에 유리(재사용 가능)
        pstmt.executeUpdate();
    }

    @Test
    public void transactionTest() throws Exception {
        Connection conn = null;
        try {
            deleteAll();
            conn = ds.getConnection();
            conn.setAutoCommit(false); //true가 기본

            String sql = "insert into user_info values (?, ?, ?, ?, ?, ?, now())";

            PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Infection, 성능 향상에 유리(재사용 가능)
            pstmt.setString(1, "asdf");
            pstmt.setString(2, "1234");
            pstmt.setString(3, "abc");
            pstmt.setString(4, "aaa@aaa.com");
            pstmt.setDate(5, new java.sql.Date(new Date().getTime()));
            pstmt.setString(6, "fb");

            int rowCnt = pstmt.executeUpdate();

            pstmt.setString(1, "asdf2");
            rowCnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
        }
    }

    public int insertUser(User user) throws Exception {
        Connection conn = ds.getConnection();
//        insert into user_info (id, pwd, name, email, birth, sns, reg_date)
//        values ('asdf2','1234','smith','aaa@aaa.com','2021-01-01','facebook',now());
        String sql = "insert into user_info values (?, ?, ?, ?, ?, ?, now())";

        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Infection, 성능 향상에 유리(재사용 가능)
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getPwd());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
        pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
        pstmt.setString(6, user.getSns());

        int rowCnt = pstmt.executeUpdate();
        return rowCnt;
    }

    @Test
    public void springJdbcConnectionTest() throws Exception {
//        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
//        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // 데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);
        assertTrue(conn!=null); //괄호 안의 조건식이 true면, 테스트 성공. 아니면 실패.
    }
}