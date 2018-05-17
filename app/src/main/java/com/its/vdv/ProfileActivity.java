package com.its.vdv;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.crashlytics.android.Crashlytics;
import com.its.vdv.adapter.FeedListAdapter;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.ProfileInfo;
import com.its.vdv.data.User;
import com.its.vdv.rest.raw.ImageRest;
import com.its.vdv.rest.wrapper.ProfileRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.service.UserService;
import com.its.vdv.utils.BitmapUtils;
import com.its.vdv.utils.GalleryUtils;
import com.its.vdv.views.LoadableImageView;
import com.its.vdv.views.NavigationFooterView;
import com.its.vdv.views.PostPopup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.its.vdv.utils.BitmapUtils.scale;
import static java.lang.String.format;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends BaseActivity {
    private static final int TAKE_PHOTO_ACTION_ID = 0;
    private static final int CHOOSE_FROM_GALLERY_ACTION_ID = 1;

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

    @ViewById(R.id.avatar_progress)
    View avatarProgressView;
    @ViewById(R.id.avatar_progress_spinner)
    View avatarProgressSpinnerView;

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

    @ViewById(R.id.avatar_popup)
    PostPopup avatartPopup;

    User user;

    private byte [] image = null;

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

        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1
        );
    }

    @UiThread
    void fillProfile(User user, List<FeedItem> feedItems) {
        this.user = user;
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
        if (this.user.getIs_me()) {
            avatartPopup.bindImage(0, avatarView);
        }
    }

    @Click(R.id.settings)
    void onSettingsClick() {
        redirect(SettingsActivity_.class, 0, 0, false, new HashMap<>());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                Uri url = requestCode == TAKE_PHOTO_ACTION_ID ?
                        avatartPopup.getPhotoUri() : Uri.fromFile(new File(GalleryUtils.getPath(this, data.getData())));

                Bitmap bm = getInitialBitmap(url);

                switch (avatartPopup.getIndex()) {
                    case 0:
                        image = BitmapUtils.iconToBytes(bm);
                        break;
                }

                //avatartPopup.getImageView().setImageBitmap(bm);
                avatartPopup.setVisibility(View.GONE);
                onPostClick();
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
        } else {
            Crashlytics.log(format(
                    Locale.ENGLISH,
                    "Request code: %d, Result code: %d",
                    requestCode,
                    resultCode
            ));
        }
    }

    void onPostClick() {
        List<byte []> images = Stream
                .of(image)
                .filter(it -> it != null)
                .toList();

        if (!images.isEmpty()) {
            profileRestWrapper.updateAvatar(this.user.getId(), images, new RestListener<Void>() {
                @Override
                public void onStart() {
                    onAvatarUploadStarted();
                }

                @Override
                public void onSuccess(Void data) {
                    onAvatarUploadSendSuccess();
                }

                @Override
                public void onFailure(Exception e) {
                    onAvatarUploadSendSuccess();
                }
            });
        }
    }

    @UiThread
    void onAvatarUploadStarted() {
        avatarProgressView.setVisibility(View.VISIBLE);
        avatarProgressSpinnerView.setAnimation(loadingAnim);
    }

    @UiThread
    void onAvatarUploadSendSuccess() {
        avatarProgressView.setVisibility(View.INVISIBLE);

        avatarProgressSpinnerView.clearAnimation();
        avatarProgressSpinnerView.setAnimation(loadingAnim);

        redirect(ProfileActivity_.class, 0, 0, true, new HashMap<>());
    }

    private Bitmap getInitialBitmap(Uri url) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), url);

            int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

            return scale(
                    bitmap,
                    bitmap.getWidth() * 800 / minSize,
                    bitmap.getHeight() * 800 / minSize
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
