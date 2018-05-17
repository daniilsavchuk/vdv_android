package com.its.vdv.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Objects;
import com.annimon.stream.Stream;
import com.its.vdv.BaseActivity;
import com.its.vdv.CommentsActivity_;
import com.its.vdv.ProfileActivity_;
import com.its.vdv.R;
import com.its.vdv.data.Comment;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.GeoTag;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.wrapper.FeedRestWrapper;
import com.its.vdv.rest.wrapper.PostRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.rest.wrapper.UserRestWrapper;
import com.its.vdv.service.PostService;
import com.its.vdv.service.UserService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EViewGroup(R.layout.view_feed_item)
public class FeedItemView extends RelativeLayout {
    private static final int MAX_LIKES_SHOWN = 2;

    @ViewById(R.id.name)
    TextView nameView;
    @ViewById(R.id.text)
    TextView textView;
    @ViewById(R.id.likes)
    TextView likesView;
    @ViewById(R.id.geo_tag)
    TextView geoTagView;

    @ViewById(R.id.avatar)
    LoadableImageView avatarView;

    @ViewById(R.id.likes_label_empty)
    View likesLabelEmptyView;
    @ViewById(R.id.likes_label_not_empty)
    View likesLabelNotEmptyView;

    @ViewById(R.id.comments_list)
    LinearLayout commentsListView;

    @ViewById(R.id.see_more_comments)
    TextView seeMoreCommentsView;

    @ViewById(R.id.feed_images)
    FeedItemImagesView feedItemImagesView;

    @Bean
    PostService postService;
    @Bean
    UserService userService;

    @Bean
    PostRestWrapper postRestWrapper;
    @Bean
    UserRestWrapper userRestWrapper;
    @Bean
    FeedRestWrapper feedRestWrapper;

    private FeedItem feedItem;
    private boolean commentsVisible = false;
    private boolean alreadyLiked = false;
    private boolean liked = false;

    public FeedItemView(Context context) {
        super(context);
    }

    public FeedItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Click(R.id.comments)
    void onCommentsClick() {
        Map<String, Serializable> extras = new HashMap<>();
        {
           extras.put("postId", feedItem.getId());
        }

        ((BaseActivity) getContext()).redirect(CommentsActivity_.class, 0, 0, false, extras);
    }

    @Click(R.id.like)
    void onLike() {
        if (!liked) {
            feedRestWrapper.addLike(feedItem.getId(), 1, new RestListener<>());

            liked = !liked;

            initLikes();
        }
    }

    public void bind(FeedItem feedItem) {
        this.feedItem = feedItem;

        nameView.setText(feedItem.getUser().getName());
        textView.setText(feedItem.getText());
        geoTagView.setText(feedItem.getGeoTag().map(GeoTag::getName).orElse(null));

        avatarView.configure(
                "" + feedItem.getId(),
                R.color.gray_4,
                () -> userService.getUserImage(feedItem.getUser().getId()).orElse(null),
                () -> feedItem.getUser().getAvatarUrl() == null ? null : userRestWrapper.getUserImage(feedItem.getUser().getId(), feedItem.getUser().getAvatarUrl(), new RestListener<>())
        );

        alreadyLiked = Stream.of(feedItem.getLikes())
                .filter(it -> Objects.equals(userService.getMyId(), it.getId()))
                .findFirst()
                .isPresent();

        liked = alreadyLiked;

        initLikes();

        commentsListView.removeAllViews();
        for (Comment comment : feedItem.getComments()) {
            CommentItemView v = CommentItemView_.build(getContext());

            v.bind(comment);

            commentsListView.addView(v);
        }

        commentsListView.setVisibility(GONE);

        seeMoreCommentsView.setText("Show more (" + feedItem.getComments().size() + ")");

        feedItemImagesView.bind(feedItem.getId(), feedItem.getImagePaths());
    }

    private void initLikes() {
        boolean hasLikes = !feedItem.getLikes().isEmpty() || liked;

        likesLabelEmptyView.setVisibility(hasLikes ? GONE : VISIBLE);
        likesLabelNotEmptyView.setVisibility(hasLikes ? VISIBLE : GONE);
        likesView.setVisibility(hasLikes ? VISIBLE : GONE);

        if (hasLikes) {
            likesView.setText(getLikes(feedItem));
        }
    }

    @Click(R.id.header)
    void onHeaderClick() {
        ((BaseActivity) getContext()).redirect(
                ProfileActivity_.class, 0, 0, false, Collections.singletonMap("userId", feedItem.getUser().getId())
        );
    }

    @Click(R.id.see_more_comments)
    void onSeeMoreCommentsClick() {
        commentsVisible = !commentsVisible;

        commentsListView.setVisibility(commentsVisible ? VISIBLE : GONE);
        seeMoreCommentsView.setText(commentsVisible ? "Hide comments" : "Show more (" + feedItem.getComments().size() + ")");
    }

    private String getLikes(FeedItem feedItem) {
        List<UserInfo> likes = liked ?
                feedItem.getLikes() :
                Stream.of(feedItem.getLikes()).filter(it -> !Objects.equals(it.getId(), userService.getMyId())).toList();

        if (likes.size() == 0 && !liked) {
            throw new RuntimeException();
        } else {
            StringBuilder sb = new StringBuilder();

            if (!alreadyLiked && liked) {
                sb.append("you");

                if (!feedItem.getLikes().isEmpty()) {
                    sb.append(", ");
                }
            }

            if (!feedItem.getLikes().isEmpty()) {
                sb.append(feedItem.getLikes().get(0).getName());

                for (int i = 1; i < MAX_LIKES_SHOWN && i < likes.size(); i++) {
                    sb.append(", ");
                    sb.append(likes.get(i).getName());
                }

                if (feedItem.getLikes().size() > MAX_LIKES_SHOWN) {
                    sb.append(" and ").append(likes.size() - 2).append(" more");
                }
            }

            return sb.toString();
        }
    }
}
