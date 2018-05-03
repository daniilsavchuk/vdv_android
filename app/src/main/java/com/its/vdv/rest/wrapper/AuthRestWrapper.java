package com.its.vdv.rest.wrapper;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.its.vdv.rest.mappers.JsonMapper;
import com.its.vdv.service.AuthService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@EBean
public class AuthRestWrapper {
    @Getter
    @Setter
    public static class AccessTokenWrapper {
        private String access_token;
    }

    @Bean
    AuthService authService;

    @Background
    public void login(String authCode, RestListener<String> listener) {
        try {
            listener.onStart();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://www.googleapis.com/oauth2/v4/token");

            List<NameValuePair> nameValuePairs = Arrays.asList(
                    new BasicNameValuePair("grant_type", "authorization_code"),
                    new BasicNameValuePair("code", authCode),
                    new BasicNameValuePair("client_id", "624089359951-jmnqgadh7e4jg28k677n0618puc5q7tv.apps.googleusercontent.com"),
                    new BasicNameValuePair("client_secret", "kvL7ku63K82y9fjCF7vD-Sv1")
            );

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new RuntimeException("Status code is not ok: " + statusCode);
            }

            AccessTokenWrapper accessTokenWrapper = new JsonMapper().getObjectMapper().readValue(
                    EntityUtils.toString(response.getEntity()),
                    AccessTokenWrapper.class
            );

            authService.setAuthToken(accessTokenWrapper.getAccess_token());

            listener.onSuccess(accessTokenWrapper.getAccess_token());
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }
}
