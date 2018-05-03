package com.its.vdv.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Builder;

@Builder
@Getter
public class Comment {
    @NonNull private UserInfo userInfo;
    @NonNull private String text;
}
