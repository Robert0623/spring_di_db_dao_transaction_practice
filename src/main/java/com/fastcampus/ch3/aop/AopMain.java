package com.fastcampus.ch3.aop;

import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AopMain {
    public static void main(String[] args) throws Exception {
        MyAdvice myAdvice = new MyAdvice();

        Class myclass = Class.forName("com.fastcampus.ch3.aop.MyClass");
        Object obj = myclass.newInstance();

        for (Method m : myclass.getDeclaredMethods()) {
            myAdvice.invoke(m, obj, null);
        }
    }
}

class MyAdvice {
    Pattern p = Pattern.compile("a.*");

    boolean matches(Method m) {
        Matcher macher = p.matcher(m.getName());
        return macher.matches();
    }
    void invoke(Method m, Object obj, Object... args) throws Exception {
        //특정 패턴이나, 애너테이션이 붙었을 때 AOP작업을 하도록 할 수 있다.
        if(m.getAnnotation(Transactional.class)!=null)
            System.out.println("[before]");

        m.invoke(obj, args);

        if(m.getAnnotation(Transactional.class)!=null)
            System.out.println("[after]");
    }
}

class MyClass {
    @Transactional
    void aaa() {
        System.out.println("aaa() is called.");
    }
    void aaa2() {
        System.out.println("aaa2() is called.");
    }
    void bbb() {
        System.out.println("bbb() is called.");
    }
}
