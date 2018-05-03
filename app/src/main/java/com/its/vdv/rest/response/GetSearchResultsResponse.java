package com.its.vdv.rest.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSearchResultsResponse {
    @Getter
    @Setter
    public static class Avatar {
        private String vdvid;
        private String url;
    }

    private String vdvid;
    private String name;
    private String email;
    private List<Avatar> avatar;
}
