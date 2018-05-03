package com.its.vdv.rest.wrapper;

import android.content.Context;

import com.annimon.stream.Stream;
import com.crashlytics.android.Crashlytics;
import com.its.vdv.data.Comment;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.GeoTag;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.raw.PostRest;
import com.its.vdv.rest.response.GetAllPostsResponse;
import com.its.vdv.service.AuthService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@EBean
public class FeedRestWrapper {
    @RootContext
    Context context;

    @RestService
    PostRest postRest;

    @Bean
    AuthService authService;

    @Background
    public void getFeedItems(RestListener<List<FeedItem>> listener) {
        try {
            listener.onStart();

            postRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            GetAllPostsResponse allPostsResponse = postRest.getAllPosts();

            List<FeedItem> res = Stream
                    .of(allPostsResponse.getPost())
                    .filter(it -> allPostsResponse.getUser().containsKey(it.getUserid()))
                    .map(it -> parseFeedItem(it, allPostsResponse.getUser()))
                    .filter(it -> it != null)
                    .toList();

            listener.onSuccess(res);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    private FeedItem parseFeedItem(GetAllPostsResponse.Post post, Map<String, GetAllPostsResponse.User> users) {
        try {
            if (post.getMedia().isEmpty()) {
                throw new RuntimeException("Media is empty for post " + post.getVdvid());
            }

            List<String> urls = Stream
                    .of(post.getMedia())
                    .map(it -> it.getUrl().split("/")[3])
                    .toList();

            return FeedItem
                    .builder()
                    .id(post.getVdvid())
                    .user(parseUser(users.get(post.getUserid())))
                    .imagePaths(urls)
                    .comments(parseComments(post.getComment(), users))
                    .likes(Collections.emptyList())
                    .text(post.getDescription())
                    .geoTag(GeoTag
                            .builder()
                            .name(post.getLocation().isEmpty() ? "" : post.getLocation().get(0).getName())
                            .lon(post.getLocation().isEmpty() ? 0.0 : Double.parseDouble(post.getLocation().get(0).getLongitude()))
                            .lat(post.getLocation().isEmpty() ? 0.0 : Double.parseDouble(post.getLocation().get(0).getLatitude()))
                            .build()
                    )
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    private List<Comment> parseComments(List<GetAllPostsResponse.Post.Comment> comments, Map<String, GetAllPostsResponse.User> users) {
        return Stream
                .of(comments)
                .filter(it -> users.containsKey("" + it.getUserid()))
                .map(it -> {
                    GetAllPostsResponse.User user = users.get("" + it.getUserid());

                    String url = user.getAvatar().get(0).getUrl().split("/")[3];

                    return Comment
                            .builder()
                            .userInfo(UserInfo.builder()
                                    .id(it.getUserid())
                                    .avatarUrl(url)
                                    .name(user.getVdvid())
                                    .build()
                            )
                            .text(it.getText())
                            .build();
                })
                .toList();
    }

    private UserInfo parseUser(GetAllPostsResponse.User user) {
        String url = user.getAvatar().isEmpty() ? null : user.getAvatar().get(0).getUrl().split("/")[3];

        return UserInfo.builder()
                .id(Long.parseLong(user.getVdvid()))
                .name(user.getName())
                .avatarUrl(url)
                .build();
    }
}
