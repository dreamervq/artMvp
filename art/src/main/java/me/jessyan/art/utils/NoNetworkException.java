package me.jessyan.art.utils;

public class NoNetworkException extends RuntimeException {


    public NoNetworkException(String message) {
        super(message);
    }
    public NoNetworkException(){
        super("请检查您的网络设置");
    }
}
