package com.its.vdv.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Builder;

@Builder
@Getter
public class GeoTag {
    @NonNull private String name;
    @NonNull private Double lat;
    @NonNull private Double lon;
}
