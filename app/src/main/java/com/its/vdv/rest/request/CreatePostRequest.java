package com.its.vdv.rest.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {
    @Getter
    @Setter
    public static class Prop {
        private List<String> media;
        private int location = 0;
    }

    private Long userId;
    private String description;
    private Prop prop;
}
