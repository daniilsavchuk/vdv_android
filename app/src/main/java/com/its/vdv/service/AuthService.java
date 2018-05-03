package com.its.vdv.service;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class AuthService {
    private String authToken;

    public Optional<String> getAuthToken() {
        return Optional.ofNullable(authToken);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
