package com.example.mydemok.model.main;

public class ResponseBase<T> {
    public int error_code;
    public String error_msg;
    public T data;
    public String msg;
    public int max_id;
    public int max;
}
