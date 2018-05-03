package com.its.vdv.rest.raw;

import com.its.vdv.rest.configuration.RestConfiguration;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

@Rest(rootUrl = RestConfiguration.BACKEND_ROOT, converters = {ByteArrayHttpMessageConverter.class})
public interface ImageRest {
    @Get("/images/{imageLink}")
    byte [] getImage(
            @Path String imageLink
    );
}
