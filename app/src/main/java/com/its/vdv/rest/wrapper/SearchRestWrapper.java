package com.its.vdv.rest.wrapper;

import com.annimon.stream.Stream;
import com.crashlytics.android.Crashlytics;
import com.its.vdv.data.Court;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.request.SearchCourtRequest;
import com.its.vdv.rest.request.SearchRequest;
import com.its.vdv.rest.raw.SearchRest;
import com.its.vdv.service.AuthService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

            List<UserInfo> res = Stream.of(searchRest.searchUser(request))
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

    @Background
    public void searchCourt(Double lat, Double lon, Double r, RestListener<List<Court>> listener) {
        try {
            listener.onStart();

            searchRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            SearchCourtRequest request = new SearchCourtRequest();
            {
                request.setObject("court");
                List<Double> req = Arrays.asList(lat, lon, r);
                request.getProp().setLocation_area(req);
            }

            List<Court> res = Stream.of(searchRest.searchCourts(request))
                    .map(it ->
                            Court.builder()
                                    .id(Long.parseLong(it.getVdvid()))
                                    .name(it.getName())
                                    .lat(it.getLocation().isEmpty() ? 0.0 : Double.parseDouble(it.getLocation().get(0).getLatitude()))
                                    .lon(it.getLocation().isEmpty() ? 0.0 : Double.parseDouble(it.getLocation().get(0).getLongitude()))
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
