package me.jessyan.art.utils;

public class MsgException extends RuntimeException {


    public MsgException(String message) {
        super(message);
    }
    public int errorCode;
    public MsgException(String message,int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public MsgException(){
        super("请检查您的网络设置");
    }


}
