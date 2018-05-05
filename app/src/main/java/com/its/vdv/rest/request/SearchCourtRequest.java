package com.its.vdv.rest.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchCourtRequest {
    @Getter
    @Setter
    public class Prop {
        private List<Double> location_area;
    }

    private String object;
    private Prop prop = new Prop();
}
