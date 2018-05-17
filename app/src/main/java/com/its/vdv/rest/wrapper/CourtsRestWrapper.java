package com.its.vdv.rest.wrapper;

import com.annimon.stream.Stream;
import com.crashlytics.android.Crashlytics;
import com.its.vdv.data.Court;
import com.its.vdv.data.CourtInfo;
import com.its.vdv.data.GeoTag;
import com.its.vdv.rest.raw.CourtRest;
import com.its.vdv.rest.response.GetAllCourtsResponse;
import com.its.vdv.rest.response.GetCourtResponse;
import com.its.vdv.service.AuthService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;
import java.util.Random;

@EBean
public class CourtsRestWrapper {
    @Bean
    AuthService authService;

    @RestService
    CourtRest courtRest;

    @Background
    public void getCourt(long id, RestListener<CourtInfo> listener) {
        try {
            listener.onStart();

            courtRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            GetCourtResponse response = courtRest.getCourt(id).get(0);

            CourtInfo courtInfo = CourtInfo.builder()
                    .id(response.getVdvid())
                    .name(response.getName())
                    .description(response.getDesc())
                    .location(GeoTag
                            .builder()
                            .name(response.getLocation().get(0).getName())
                            .lat(Double.parseDouble(response.getLocation().get(0).getLatitude()))
                            .lon(Double.parseDouble(response.getLocation().get(0).getLongitude()))
                            .build()
                    )
                    .photos(Stream.of(response.getMedia())
                            .map(it -> CourtInfo.Photo
                                    .builder()
                                    .id(Long.parseLong(it.getVdvid()))
                                    .url(it.getUrl().split("/")[3])
                                    .build()
                            )
                            .toList()
                    )
                    .equipments(Stream.of(response.getEquipment())
                            .map(it -> CourtInfo.Equipment
                                    .builder()
                                    .id(Long.parseLong(it.getVdvid()))
                                    .name(it.getName())
                                    .url(it.getUrl().split("/")[3])
                                    .build()
                            )
                            .toList()
                    )
                    .mine(response.getIs_mine())
                    .followed(response.getFollowed())
                    .followersAmount(response.getFollowers_amount())
                    .liked(response.getLiked())
                    .myRate(response.getMy_rate())
                    .rateCount(response.getRate_count())
                    .rateAvg(response.getRate_avg())
                    .build();

            listener.onSuccess(courtInfo);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }

    @Background
    public void getAllCourts(RestListener<List<Court>> listener) {
        try {
            listener.onStart();

            courtRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            List<Court> courts = Stream.of(courtRest.getAllCourts())
                    .map(it -> Court.builder()
                            .id(Long.parseLong(it.getVdvid()))
                            .name(it.getName())
                            .lat(59.8944444 + new Random().nextInt(300000) / 10000000.0) // ToDo
                            .lon(30.2641667 + new Random().nextInt(300000) / 10000000.0) // ToDo
                            .build()
                    )
                    .toList();

            listener.onSuccess(courts);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }
}
