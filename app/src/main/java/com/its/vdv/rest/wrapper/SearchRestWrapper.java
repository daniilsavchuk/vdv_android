package com.its.vdv.rest.wrapper;

import android.util.Log;

import com.annimon.stream.Stream;
import com.crashlytics.android.Crashlytics;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.request.SearchRequest;
import com.its.vdv.rest.raw.SearchRest;
import com.its.vdv.service.AuthService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

@EBean
public class SearchRestWrapper {
    @RestService
    SearchRest searchRest;

    @Bean
    AuthService authService;

    @Background
    public void searchUser(String nameSubstring, RestListener<List<UserInfo>> listener) {
        try {
            listener.onStart();

            searchRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            SearchRequest request = new SearchRequest();
            {
                request.setObject("user");
                request.getProp().setName(nameSubstring);
            }

            List<UserInfo> res = Stream.of(searchRest.search(request))
                    .map(it ->
                            UserInfo.builder()
                                    .id(Long.parseLong(it.getVdvid()))
                                    .name(it.getName())
                                    .avatarUrl(it.getAvatar().isEmpty() ? null : it.getAvatar().get(0).getUrl().split("/")[3])
                                    .build()
                    )
                    .toList();

            listener.onSuccess(res);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }
}
