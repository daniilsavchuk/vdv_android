package com.its.vdv.rest.wrapper;

import android.util.Base64;
import android.util.Log;

import com.annimon.stream.Stream;
import com.crashlytics.android.Crashlytics;
import com.its.vdv.data.Comment;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.GeoTag;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.raw.CommentRest;
import com.its.vdv.rest.raw.ImageRest;
import com.its.vdv.rest.raw.PostRest;
import com.its.vdv.rest.request.AddCommentRequest;
import com.its.vdv.rest.request.CreatePostRequest;
import com.its.vdv.rest.response.GetAllPostsResponse;
import com.its.vdv.rest.response.GetPostByIdResponse;
import com.its.vdv.service.AuthService;
import com.its.vdv.service.PostService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@EBean
public class PostRestWrapper {
    @Bean
    PostService postService;
    @Bean
    AuthService authService;

    @RestService
    ImageRest imageRest;
    @RestService
    CommentRest commentRest;
    @RestService
    PostRest postRest;

    @Background
    public void addPost(long userId, String description, List<byte []> images, RestListener<Void> listener) {
        try {
            listener.onStart();

            CreatePostRequest createPostRequest = new CreatePostRequest();
            {
                createPostRequest.setUserId(userId);
                createPostRequest.setDescription(description);
                createPostRequest.setProp(new CreatePostRequest.Prop());
                {
                    createPostRequest.getProp().setLocation(0);
                    createPostRequest.getProp().setMedia(Stream
                            .of(images)
                            .map(it -> Base64.encodeToString(it, Base64.DEFAULT))
                            .toList()
                    );
                }
            }

            postRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            postRest.createPost(createPostRequest);

            listener.onSuccess(null);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }

    @Background
    public void addComment(long id, String comment, RestListener<Void> listener) {
        try {
            listener.onStart();

            commentRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            AddCommentRequest request = new AddCommentRequest();
            {
                request.setId(id);
                request.setText(comment);
            }

            commentRest.addComment(request);

            listener.onSuccess(null);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }

    @Background
    public void getPost(long id, RestListener<FeedItem> listener) {
        try {
            listener.onStart();

            postRest.setHeader("authorization", "Bearer " + authService.getAuthToken().orElseThrow(RuntimeException::new));

            GetPostByIdResponse response = postRest.getPost(id);

            FeedItem feedItem = parseFeedItem(
                    response.getPost().get(0),
                    response.getUser()
            );

            listener.onSuccess(feedItem);
        } catch (Exception e) {
            Crashlytics.logException(e);

            listener.onFailure(e);
        }
    }

    private FeedItem parseFeedItem(GetAllPostsResponse.Post post, Map<String, GetAllPostsResponse.User> users) {
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
                .user(UserInfo.builder().id(Long.parseLong(post.getUserid())).avatarUrl("").name("").build())
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

    public byte[] getPostImage(long id, int imageIndex, String path, RestListener<byte[]> listener) {
        try {
            listener.onStart();

            byte[] image = imageRest.getImage(path);

            postService.setPostImage(id, imageIndex, image);

            listener.onSuccess(image);

            return image;
        } catch (Exception e) {
            Log.e(
                    PostRestWrapper.class.getCanonicalName(),
                    "Failed to download post image by id '" + id + "'",
                    e
            );

            listener.onFailure(e);

            return null;
        }
    }
}
