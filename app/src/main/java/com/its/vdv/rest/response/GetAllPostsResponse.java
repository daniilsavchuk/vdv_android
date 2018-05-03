package com.its.vdv.rest.response;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllPostsResponse {
    @Getter
    @Setter
    public static class Post {
        @Getter
        @Setter
        public static class Media {
            private String vdvid;
            private String url;
        }

        @Getter
        @Setter
        public static class Location {
            private String name;
            private String latitude;
            private String longitude;
        }

        @Getter
        @Setter
        public static class Comment {
            private Long userid;
            private String text;
        }

        private long vdvid;
        private String userid;
        private String description;
        private List<Media> media;
        private List<Comment> comment;
        private List<Location> location;
    }

    @Getter
    @Setter
    public static class User {
        @Getter
        @Setter
        public static class Avatar {
            private String vdvid;
            private String url;
        }

        private String vdvid;
        private String name;
        private List<Avatar> avatar;
    }

    private List<Post> post;
    private Map<String, User> user;
}
