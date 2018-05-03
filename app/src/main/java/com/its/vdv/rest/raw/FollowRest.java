package com.its.vdv.rest.raw;

import com.its.vdv.rest.configuration.RestConfiguration;
import com.its.vdv.rest.mappers.JsonMapper;

import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Delete;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;

@Rest(rootUrl = RestConfiguration.BACKEND_ROOT, converters = JsonMapper.class)
public interface FollowRest extends RestClientHeaders {
    @Post("/following/{followingId}")
    @RequiresHeader(Headers.AUTHORIZATION)
    void addFollowing(
            @Path long followingId
    );

    @Delete("/following/{followingId}")
    @RequiresHeader(Headers.AUTHORIZATION)
    void delFollowing(
            @Path long followingId
    );
}
