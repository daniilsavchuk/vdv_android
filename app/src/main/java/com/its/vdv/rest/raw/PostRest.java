package com.its.vdv.rest.raw;

import com.its.vdv.rest.configuration.RestConfiguration;
import com.its.vdv.rest.mappers.JsonMapper;
import com.its.vdv.rest.request.CreatePostRequest;
import com.its.vdv.rest.response.GetAllPostsResponse;
import com.its.vdv.rest.response.GetPostByIdResponse;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;

@Rest(rootUrl = RestConfiguration.BACKEND_ROOT, converters = JsonMapper.class)
public interface PostRest extends RestClientHeaders {
    @Get("/following/post")
    @RequiresHeader(Headers.AUTHORIZATION)
    GetAllPostsResponse getAllPosts();

    @Get("/post/{postId}")
    @RequiresHeader(Headers.AUTHORIZATION)
    GetPostByIdResponse getPost(
            @Path long postId
    );

    @Post("/post")
    @RequiresHeader(Headers.AUTHORIZATION)
    void createPost(
            @Body CreatePostRequest request
    );
}
