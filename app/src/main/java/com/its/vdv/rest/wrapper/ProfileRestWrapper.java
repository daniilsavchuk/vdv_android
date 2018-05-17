package com.its.vdv.rest.wrapper;

import android.util.Base64;
import android.util.Log;

import com.annimon.stream.Stream;
import com.crashlytics.android.Crashlytics;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.GeoTag;
import com.its.vdv.data.ProfileInfo;
import com.its.vdv.data.User;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.raw.FollowRest;
import com.its.vdv.rest.raw.UserRest;
import com.its.vdv.rest.request.UpdateUserRequest;
import com.its.vdv.rest.response.GetUserByIdResponse;
import com.its.vdv.service.AuthService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.its.vdv.rest.configuration.RestConfiguration.BACKEND_ROOT;

@EBean
public class ProfileRestWrapper {
    @RestService
    UserRest userRest;

    @RestService
    FollowRest followRest;

    @Bean
    AuthService authService;

    @Background
    public void updateAvatar(long userId, List<byte []> images, RestListener<Void> listener) {
        try {
            listener.onStart();

            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            {
                updateUserRequest.setId(userId);
                updateUserRequest.setProp(new UpdateUserRequest.Prop());
                {
                    updateUserRequest.getProp().setAvatar(Base64.encodeToString(images.get(0), Base64.DEFAULT));
                }
            }

            userRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            userRest.updateUser(updateUserRequest);

            listener.onSuccess(null);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }

    @Background
    public void getProfileByUserId(Long userId, RestListener<ProfileInfo> listener) {
        try {
            listener.onStart();

            userRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            GetUserByIdResponse response = userId == null ?
                    userRest.getYourInfo().get(0) :
                    userRest.getUserInfo(userId).get(0);

            ProfileInfo profileInfo = ProfileInfo
                    .builder()
                    .user(parseUser(response))
                    .feedItems(parseFeedItems(response))
                    .build();

            listener.onSuccess(profileInfo);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }

    @Background
    public void postFollowing(Boolean delete, Long userId, RestListener<Void> listener) {
        try {
            listener.onStart();

            followRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));
            if (delete) {
                followRest.delFollowing(userId);
            } else {
                followRest.addFollowing(userId);
            }

            listener.onSuccess(null);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }

    private User parseUser(GetUserByIdResponse response) {
        return User
                .builder()
                .id(Long.parseLong(response.getVdvid()))
                .name(response.getName())
                .avatarPath(response.getAvatar().isEmpty() ? null : response.getAvatar().get(0).getUrl().split("/")[3])
                .position("")
                .is_me(response.getIs_me())
                .followed(response.getFollowed())
                .following_amount(response.getFollowing_amount())
                .followers_amount(response.getFollowers_amount())
                .build();
    }

    private List<FeedItem> parseFeedItems(GetUserByIdResponse response) {
        return Stream
                .of(response.getPost())
                .map(it -> parseFeedItem(
                        response, it
                ))
                .filter(it -> it != null)
                .toList();
    }

    private FeedItem parseFeedItem(GetUserByIdResponse response, GetUserByIdResponse.Post post) {
        try {
            if (post.getMedia().isEmpty()) {
                throw new RuntimeException("Media is empty for post " + 1L);
            }

            List<String> urls = Stream
                    .of(post.getMedia())
                    .map(it -> it.getUrl().split("/")[3])
                    .toList();

            return FeedItem
                    .builder()
                    .id(post.getVdvid())
                    .user(UserInfo
                            .builder()
                            .id(Long.parseLong(response.getVdvid()))
                            .name(response.getName())
                            .avatarUrl(response.getAvatar().isEmpty() ? null : response.getAvatar().get(0).getUrl().split("/")[3])
                            .build()
                    )
                    .imagePaths(urls)
                    .comments(new ArrayList<>())
                    .likes(Collections.emptyList())
                    .text("")
                    .geoTag(GeoTag.builder().name("").lat(0.0).lon(0.0).build())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
