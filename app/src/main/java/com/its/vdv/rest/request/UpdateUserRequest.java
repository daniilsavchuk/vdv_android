package com.its.vdv.rest.request;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @Getter
    @Setter
    public static class Prop {
        @JsonProperty("private")
        private boolean isPrivate;
        private String avatar;
    }

    private long id;
    private Prop prop = new Prop();
}
