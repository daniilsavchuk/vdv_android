package com.its.vdv.rest.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCourtResponse {
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
        private String url;
    }

    @Getter
    @Setter
    public static class Location {
        private String vdvid;
        private String name;
        private String latitude;
        private String longitude;
    }

    private Long vdvid;
    private String name;
    private String desc;
    private List<Media> media;
    private List<Equipment> equipment;
    private List<Location> location;
    private Boolean is_mine;
    private Boolean followed;
    private Long followers_amount;
    private Boolean liked;
    private Double my_rate;
    private Long rate_count;
    private Double rate_avg;
}
