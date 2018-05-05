package com.its.vdv.rest.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSearchCourtResultsResponse {
    @Getter
    @Setter
    public static class Media {
        private String vdvid;
        private String url;
    }

    @Getter
    @Setter
    public static class Equipment {
        private String vdvid;
        private String name;
        private String desc;
        private String url;
    }

    @Getter
    @Setter
    public static class Location {
        private String vdvid;
        private String latitude;
        private String longitude;
    }
    private String vdvid;
    private Integer ownerid;
    private String name;
    private String desc;
    private List<Media> media;
    private List<Equipment> equipment;
    private List<Location> location;
}
