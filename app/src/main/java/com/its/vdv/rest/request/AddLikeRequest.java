package com.its.vdv.rest.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddLikeRequest {
    private long vdvid;
    private int weight = 1;
}
