package com.its.vdv.rest.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserByIdResponse {
    @Getter
    @Setter
    public static class Post {
        @Getter
        @Setter
        public static class Media {
            private String vdvid;
            private String url;
        }

        private long vdvid;
        private List<Media> media;
        private String description;
    }

    @Getter
    @Setter
    public static class Avatar {
        private String vdvid;
        private String url;
    }

    private String vdvid;
    private String name;
    private Boolean is_me;
    private Boolean followed;
    private Long following_amount;
    private Long followers_amount;
    private List<Avatar> avatar;
    private List<Post> post;
}
