package com.its.vdv.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Builder;

@Builder
@Getter
public class User {
    @NonNull private Long id;
    @NonNull private String name;
    private String avatarPath;
    @NonNull private String position;
    @NonNull private Boolean is_me;
    @NonNull private Boolean followed;
    @NonNull private Long followers_amount;
    @NonNull private Long following_amount;
}
