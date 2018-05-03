package com.its.vdv.rest.raw;

import com.its.vdv.rest.configuration.RestConfiguration;
import com.its.vdv.rest.mappers.JsonMapper;
import com.its.vdv.rest.response.GetAllCourtsResponse;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;

import java.util.List;


@Rest(rootUrl = RestConfiguration.BACKEND_ROOT, converters = JsonMapper.class)
public interface CourtRest extends RestClientHeaders {
    @Get("/court/all")
    @RequiresHeader(Headers.AUTHORIZATION)
    List<GetAllCourtsResponse> getAllCourts();
}
