package com.its.vdv.data;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Builder;

@Builder
@Getter
public class ProfileInfo {
    @NonNull private User user;
    @NonNull private List<FeedItem> feedItems;
}
