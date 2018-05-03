package com.its.vdv.rest.response;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPostByIdResponse {
    private List<GetAllPostsResponse.Post> post;
    private Map<String, GetAllPostsResponse.User> user;
}
