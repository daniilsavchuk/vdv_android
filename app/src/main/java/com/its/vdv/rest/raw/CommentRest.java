package com.its.vdv.rest.raw;

import com.its.vdv.rest.configuration.RestConfiguration;
import com.its.vdv.rest.mappers.JsonMapper;
import com.its.vdv.rest.request.AddCommentRequest;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;

@Rest(rootUrl = RestConfiguration.BACKEND_ROOT, converters = JsonMapper.class)
public interface CommentRest extends RestClientHeaders {
    @Post("/comment")
    @RequiresHeader(Headers.AUTHORIZATION)
    void addComment(
            @Body AddCommentRequest request
    );
}
