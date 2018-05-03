package com.its.vdv.rest.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    @Getter
    @Setter
    public class Prop {
        private String name;
    }

    private String object;
    private Prop prop = new Prop();
}
