package com.its.vdv.rest.raw;

import com.its.vdv.rest.configuration.RestConfiguration;
import com.its.vdv.rest.mappers.JsonMapper;
import com.its.vdv.rest.request.CreateUserRequest;
import com.its.vdv.rest.response.GetUserByIdResponse;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;

import java.util.List;

@Rest(rootUrl = RestConfiguration.BACKEND_ROOT, converters = JsonMapper.class)
public interface UserRest extends RestClientHeaders {
    @Post("/user")
    @RequiresHeader(Headers.AUTHORIZATION)
    void createUser(
            @Body CreateUserRequest request
    );

    @Get("/user/{userId}")
    @RequiresHeader(Headers.AUTHORIZATION)
    List<GetUserByIdResponse> getUserInfo(
            @Path long userId
    );

    @Get("/user/me")
    @RequiresHeader(Headers.AUTHORIZATION)
    List<GetUserByIdResponse> getYourInfo();
}
