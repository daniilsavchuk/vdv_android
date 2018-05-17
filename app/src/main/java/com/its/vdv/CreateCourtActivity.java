package com.its.vdv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.its.vdv.adapter.FeedListAdapter;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.ProfileInfo;
import com.its.vdv.data.User;
import com.its.vdv.rest.raw.ImageRest;
import com.its.vdv.rest.wrapper.ProfileRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
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

@EActivity(R.layout.activity_create_court)
public class CreateCourtActivity extends BaseActivity {
    private static final int TAKE_PHOTO_ACTION_ID = 0;

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
    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;

    @ViewById(R.id.profile)
    View profileView;
    @ViewById(R.id.profile_loading)
    View profileLoadingView;

    @Bean
    FeedListAdapter feedListAdapter;

    @Bean
    ProfileRestWrapper profileRestWrapper;

    @RestService
    ImageRest imageRest;

    @Extra("userId")
    Long userId;

    Boolean followed;

    @AnimationRes(R.anim.spinner)
    Animation loadingAnim;

    @ViewById(R.id.post_popup)
    PostPopup postPopup;

    @ViewById(R.id.court_photo_1)
    ImageView courtPhotoView1;
    @ViewById(R.id.court_photo_2)
    ImageView courtPhotoView2;
    @ViewById(R.id.court_photo_3)
    ImageView courtPhotoView3;
    @ViewById(R.id.court_photo_4)
    ImageView courtPhotoView4;
    @ViewById(R.id.court_photo_5)
    ImageView courtPhotoView5;

    @ViewById(R.id.equipment_photo_1)
    ImageView equipmentPhotoView1;
    @ViewById(R.id.equipment_photo_2)
    ImageView equipmentPhotoView2;
    @ViewById(R.id.equipment_photo_3)
    ImageView equipmentPhotoView3;
    @ViewById(R.id.equipment_photo_4)
    ImageView equipmentPhotoView4;
    @ViewById(R.id.equipment_photo_5)
    ImageView equipmentPhotoView5;

    private byte [][] courtPhotos = new byte [5][];
    private byte [][] equipmentPhotos = new byte [5][];

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

    @Click(R.id.court_photo_1)
    void onImage1Click() {
        postPopup.bindImage(1, courtPhotoView1);
    }

    @Click(R.id.court_photo_2)
    void onImage2Click() {
        postPopup.bindImage(2, courtPhotoView2);
    }

    @Click(R.id.court_photo_3)
    void onImage3Click() {
        postPopup.bindImage(3, courtPhotoView3);
    }

    @Click(R.id.court_photo_4)
    void onImage4Click() {
        postPopup.bindImage(4, courtPhotoView4);
    }

    @Click(R.id.court_photo_5)
    void onImage5Click() {
        postPopup.bindImage(5, courtPhotoView5);
    }

    @Click(R.id.equipment_photo_1)
    void onEquipmentImage1Click() {
        postPopup.bindImage(6, equipmentPhotoView1);
    }

    @Click(R.id.equipment_photo_2)
    void onEquipmentImage2Click() {
        postPopup.bindImage(7, equipmentPhotoView2);
    }

    @Click(R.id.equipment_photo_3)
    void onEquipmentImage3Click() {
        postPopup.bindImage(8, equipmentPhotoView3);
    }

    @Click(R.id.equipment_photo_4)
    void onEquipmentImage4Click() {
        postPopup.bindImage(9, equipmentPhotoView4);
    }

    @Click(R.id.equipment_photo_5)
    void onEquipmentImage5Click() {
        postPopup.bindImage(10, equipmentPhotoView5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                Uri url = requestCode == TAKE_PHOTO_ACTION_ID ?
                        postPopup.getPhotoUri() : Uri.fromFile(new File(GalleryUtils.getPath(this, data.getData())));

                Bitmap bm = getInitialBitmap(url);

                switch (postPopup.getIndex()) {
                    case 1:
                        courtPhotos[0] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 2:
                        courtPhotos[1] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 3:
                        courtPhotos[2] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 4:
                        courtPhotos[3] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 5:
                        courtPhotos[4] = BitmapUtils.iconToBytes(bm);
                        break;

                    case 6:
                        equipmentPhotos[0] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 7:
                        equipmentPhotos[1] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 8:
                        equipmentPhotos[2] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 9:
                        equipmentPhotos[3] = BitmapUtils.iconToBytes(bm);
                        break;
                    case 10:
                        equipmentPhotos[4] = BitmapUtils.iconToBytes(bm);
                        break;
                }

                postPopup.getImageView().setImageBitmap(bm);
                postPopup.setVisibility(View.GONE);
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

    private Bitmap getInitialBitmap(Uri url) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), url);

            int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

            return scale(
                    bitmap,
                    bitmap.getWidth() * 400 / minSize,
                    bitmap.getHeight() * 400 / minSize
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
