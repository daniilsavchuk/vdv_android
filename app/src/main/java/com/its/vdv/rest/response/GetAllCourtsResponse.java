package com.its.vdv.rest.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllCourtsResponse {
    private String vdvid;
    private String ownerid;
    private String name;
    private String description;
}
