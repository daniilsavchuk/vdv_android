package com.its.vdv.data;

import com.annimon.stream.Optional;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Builder;

@Builder
public class FeedItem {
    @Getter @NonNull private Long id;
    @Getter @NonNull private UserInfo user;
    @Getter @NonNull private List<String> imagePaths;
    @Getter @NonNull private List<Comment> comments;
    @Getter @NonNull private List<UserInfo> likes;
    @Getter @NonNull private String text;
    private GeoTag geoTag;

    public Optional<GeoTag> getGeoTag() {
        return Optional.ofNullable(geoTag);
    }
}
