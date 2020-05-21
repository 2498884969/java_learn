package com.qxh.sb22.except;

public class ExceptionTest {

    public static void main(String[] args) {

        try {
            throw new AException(new BException(new CException(new Exception("test"))));
        } catch (Throwable t) {
            while (t!=null){
                System.err.println(t);
                t=t.getCause();
            }
        }
    }

}
