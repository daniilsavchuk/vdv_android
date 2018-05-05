package com.its.vdv.rest.raw;

import com.its.vdv.rest.configuration.RestConfiguration;
import com.its.vdv.rest.mappers.JsonMapper;
import com.its.vdv.rest.request.SearchCourtRequest;
import com.its.vdv.rest.request.SearchRequest;
import com.its.vdv.rest.response.GetSearchResultsResponse;
import com.its.vdv.rest.response.GetSearchCourtResultsResponse;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;

import java.util.List;

@Rest(rootUrl = RestConfiguration.BACKEND_ROOT, converters = JsonMapper.class)
public interface SearchRest extends RestClientHeaders {
    @Post("/search")
    @RequiresHeader(Headers.AUTHORIZATION)
    List<GetSearchResultsResponse> searchUser(
            @Body SearchRequest request
    );

    @Post("/search")
    @RequiresHeader(Headers.AUTHORIZATION)
    List<GetSearchCourtResultsResponse> searchCourts(
            @Body SearchCourtRequest request
    );
}
