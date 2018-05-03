package com.its.vdv.rest.wrapper;

public class RestListener<T> {
    public void onStart() {}
    public void onSuccess(T data) {}
    public void onFailure(Exception e) {}
}
