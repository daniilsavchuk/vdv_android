package com.its.vdv.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Builder;

@Builder
@Getter
public class UserInfo {
    private Long id;
    @NonNull private String name;
    private String avatarUrl;
}
