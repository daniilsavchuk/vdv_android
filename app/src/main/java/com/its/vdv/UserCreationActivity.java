package com.its.vdv;

import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.rest.wrapper.UserRestWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;

import java.util.HashMap;

@EActivity(R.layout.activity_user_creation)
public class UserCreationActivity extends BaseActivity {
    @Bean
    UserRestWrapper userRestWrapper;

    @Extra("email")
    String email;
    @Extra("name")
    String name;

    @AfterViews
    void init() {
        userRestWrapper.createUser(email, name, new RestListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                onUserCreated();
            }
        });
    }

    @UiThread
    void onUserCreated() {
        redirect(FeedActivity_.class, 0, 0, true, new HashMap<>());
    }
}
