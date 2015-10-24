package com.org.photodemo.util;

import android.util.Log;
/**
 * 
 * @author GZK
 */
public class MyLogger {

    private static MyLogger mgLogger;

    private final static String tag = "PhonoDemo";

    public static int logLevel = Log.VERBOSE;
    private static boolean isPrintlogFlag = true; //打印开关

    private MyLogger(){}

    public static MyLogger getMyLogger(){
        if(mgLogger==null){
            mgLogger=new MyLogger();
        }
        return mgLogger;
    }


    /**
     * 打印不同级别的日志 VERBOSE INFO WARN DEBUG ERROR
     * @param str
     */

    public void v(Object str) {
        if(!isPrintlogFlag){//打印开关
            return ;
        }
        String name=getFunctionName();
        printLog(name,str,Log.VERBOSE);
    }
    public void i(Object str) {
        if(!isPrintlogFlag){//打印开关
            return ;
        }
        String name=getFunctionName();
        printLog(name,str,Log.INFO);
    }
    public void w(Object str) {
        if(!isPrintlogFlag){//打印开关
            return ;
        }
        String name=getFunctionName();
        printLog(name,str,Log.WARN);
    }
    public void d(Object str) {
        if(!isPrintlogFlag){//打印开关
            return ;
        }
        String name=getFunctionName();
        printLog(name,str,Log.DEBUG);
    }
    public void e(Object str) {
        if(!isPrintlogFlag){//打印开关
            return ;
        }
        String name=getFunctionName();
        printLog(name,str,Log.ERROR);
    }


    /**
     * 打印Log日志
     * @param name
     * @param logmsg
     * @param logLevel
     */
    private void printLog(String name,Object logmsg,int logLevel){

        if(!isPrintlogFlag){//打印开关
            return ;
        }
        if(logLevel==Log.INFO){
            if (name != null) {
                Log.i(tag, name + " - " + logmsg);
            } else {
                Log.i(tag, logmsg.toString());
            }
        }else if(logLevel==Log.VERBOSE){
            if (name != null) {
                Log.v(tag, name + " - " + logmsg);
            } else {
                Log.v(tag, logmsg.toString());
            }
        }else if(logLevel==Log.WARN){
            if (name != null) {
                Log.w(tag, name + " - " + logmsg);
            } else {
                Log.w(tag, logmsg.toString());
            }
        }else if(logLevel==Log.DEBUG){
            if (name != null) {
                Log.d(tag, name + " - " + logmsg);
            } else {
                Log.d(tag, logmsg.toString());
            }
        }else if(logLevel==Log.ERROR){
            if (name != null) {
                Log.e(tag, name + " - " + logmsg);
            } else {
                Log.e(tag, logmsg.toString());
            }
        }
    }




    /**
     * 获取打印当前类 的类名  行号
     * @return
     */
    private String getFunctionName(){
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            return "[ "+ st.getFileName() + ":" + st.getLineNumber() + " ]";
//			return "[ " + Thread.currentThread().getName() + ": "
//			+ st.getFileName() + ":" + st.getLineNumber() + " ]";
        }

        return null;
    }





}
