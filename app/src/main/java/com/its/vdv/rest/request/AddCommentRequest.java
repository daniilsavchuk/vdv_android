package com.its.vdv.rest.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AddCommentRequest {
    @NonNull private Long id;
    @NonNull private Long vdvid = 0L;
    @NonNull private String text;
    @NonNull private String created = "";
    @NonNull private String updated = "";
}
