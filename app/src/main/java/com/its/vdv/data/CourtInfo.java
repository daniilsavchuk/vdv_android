package com.its.vdv.data;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Builder;

@Builder
@Getter
public class CourtInfo {
    @Builder
    @Getter
    public static class Photo {
        @NonNull private Long id;
        @NonNull private String url;
    }

    @Builder
    @Getter
    public static class Equipment {
        @NonNull private Long id;
        @NonNull private String name;
        @NonNull private String url;
    }

    @NonNull private Long id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private GeoTag location;
    @NonNull private List<Photo> photos;
    @NonNull private List<Equipment> equipments;
    @NonNull private Boolean mine;
    @NonNull private Boolean followed;
    @NonNull private Long followersAmount;
}
