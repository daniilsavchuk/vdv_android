package com.its.vdv;

import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.its.vdv.adapter.FeedListAdapter;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.ProfileInfo;
import com.its.vdv.data.User;
import com.its.vdv.rest.raw.ImageRest;
import com.its.vdv.rest.wrapper.ProfileRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.service.UserService;
import com.its.vdv.views.LoadableImageView;
import com.its.vdv.views.NavigationFooterView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends BaseActivity {
    @ViewById(R.id.avatar)
    LoadableImageView avatarView;
    @ViewById(R.id.name)
    TextView nameView;
    @ViewById(R.id.followers_amount)
    TextView followersAmount;
    @ViewById(R.id.following_amount)
    TextView followingAmount;
    @ViewById(R.id.subscribe)
    TextView subscribeButton;
    @ViewById(R.id.settings)
    ImageView settingsButton;
    @ViewById(R.id.feed_list)
    ListView feedListView;
    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;

    @ViewById(R.id.profile)
    View profileView;
    @ViewById(R.id.profile_loading)
    View profileLoadingView;

    @Bean
    FeedListAdapter feedListAdapter;

    @Bean
    UserService userService;

    @Bean
    ProfileRestWrapper profileRestWrapper;

    @RestService
    ImageRest imageRest;

    @Extra("userId")
    Long userId;

    Boolean followed;

    @AnimationRes(R.anim.spinner)
    Animation loadingAnim;

    @AfterViews
    public void init() {
        profileView.setVisibility(View.INVISIBLE);

        profileLoadingView.setAnimation(loadingAnim);

        profileRestWrapper.getProfileByUserId(userId, new RestListener<ProfileInfo>() {
            @Override
            public void onSuccess(ProfileInfo data) {
                fillProfile(data.getUser(), data.getFeedItems());
            }
        });

        navigationFooterView.setPage(NavigationFooterView.Page.PROFILE);
    }

    @UiThread
    void fillProfile(User user, List<FeedItem> feedItems) {
        profileView.setVisibility(View.VISIBLE);

        profileLoadingView.clearAnimation();
        profileLoadingView.setVisibility(View.GONE);

        avatarView.configure(
                "avatar",
                R.color.gray_1,
                () -> null, /* ToDo: add caching */
                () -> user.getAvatarPath() == null ? null : imageRest.getImage(user.getAvatarPath())
        );

        nameView.setText(user.getName());

        followed = user.getFollowed();

        if (user.getIs_me()) {
            subscribeButton.setVisibility(View.INVISIBLE);
        } else {
            settingsButton.setVisibility(View.INVISIBLE);
        }

        updateFollowingButtonState();

        followersAmount.setText(getResources().getString(R.string.profile_users_amount, user.getFollowers_amount()));
        followingAmount.setText(getResources().getString(R.string.profile_users_amount, user.getFollowing_amount()));

        feedListAdapter.setItems(feedItems);
        feedListView.setAdapter(feedListAdapter);
    }

    @Click(R.id.subscribe)
    void onSubscribe() {
        profileRestWrapper.postFollowing(followed, userId, new RestListener<>());
        followed = !followed;
        Long count = Long.parseLong(followersAmount.getText().toString()) + (followed ? 1 : -1);
        followersAmount.setText(getResources().getString(R.string.profile_users_amount, count));
        updateFollowingButtonState();
    }

    void updateFollowingButtonState() {
        if (followed) {
            subscribeButton.setBackgroundColor(0xFFAAAAAA);
            subscribeButton.setText("Отписаться");
            subscribeButton.setTextColor(0xFF000000);
        } else {
            subscribeButton.setBackgroundColor(0xFF005FBF);
            subscribeButton.setText("Подписаться");
            subscribeButton.setTextColor(0xFFFFFFFF);
        }
    }

    @Click(R.id.avatar)
    void onAvatarClick() {
        // ToDO
    }

    @Click(R.id.settings)
    void onSettingsClick() {
        redirect(SettingsActivity_.class, 0, 0, false, new HashMap<>());
    }
}
