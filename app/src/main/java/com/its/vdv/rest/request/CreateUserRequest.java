package com.its.vdv.rest.request;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @Getter
    @Setter
    public static class Prop {
        @JsonProperty("private")
        private boolean isPrivate;
    }

    private String username;
    private String e_mail;
    private Prop prop = new Prop();
}
