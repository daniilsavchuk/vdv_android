package com.its.vdv;

import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.its.vdv.adapter.FeedListAdapter;
import com.its.vdv.data.CourtInfo;
import com.its.vdv.data.FeedItem;
import com.its.vdv.rest.raw.ImageRest;
import com.its.vdv.rest.wrapper.CourtsRestWrapper;
import com.its.vdv.rest.wrapper.FeedRestWrapper;
import com.its.vdv.rest.wrapper.ProfileRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.service.UserService;
import com.its.vdv.views.FeedItemImagesView;
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

@EActivity(R.layout.activity_court)
public class CourtActivity extends BaseActivity {
    @ViewById(R.id.avatar)
    LoadableImageView avatarView;
    @ViewById(R.id.name)
    TextView nameView;
    @ViewById(R.id.followers_amount)
    TextView followersAmountView;
    @ViewById(R.id.subscribe)
    TextView subscribeButton;
    @ViewById(R.id.settings)
    ImageView settingsButton;
    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;
    @ViewById(R.id.photos)
    FeedItemImagesView feedItemImagesView;

    @ViewById(R.id.profile)
    View profileView;
    @ViewById(R.id.profile_loading)
    View profileLoadingView;

    @ViewById(R.id.description)
    TextView descriptionView;

    @ViewById(R.id.equipment_photo_1)
    LoadableImageView equipmentPhotoView1;
    @ViewById(R.id.equipment_photo_2)
    LoadableImageView equipmentPhotoView2;
    @ViewById(R.id.equipment_photo_3)
    LoadableImageView equipmentPhotoView3;
    @ViewById(R.id.equipment_photo_4)
    LoadableImageView equipmentPhotoView4;
    @ViewById(R.id.equipment_photo_5)
    LoadableImageView equipmentPhotoView5;
    @ViewById(R.id.equipment_photo_6)
    LoadableImageView equipmentPhotoView6;
    @ViewById(R.id.equipment_photo_7)
    LoadableImageView equipmentPhotoView7;
    @ViewById(R.id.equipment_photo_8)
    LoadableImageView equipmentPhotoView8;
    @ViewById(R.id.equipment_photo_9)
    LoadableImageView equipmentPhotoView9;

    @ViewById(R.id.rate0)
    ImageView rate0;
    @ViewById(R.id.rate1)
    ImageView rate1;
    @ViewById(R.id.rate2)
    ImageView rate2;
    @ViewById(R.id.rate3)
    ImageView rate3;
    @ViewById(R.id.rate4)
    ImageView rate4;
    @ViewById(R.id.rate_by)
    TextView rateBy;

    @ViewById(R.id.geo_tag)
    TextView geoTagView;

    @Bean
    FeedListAdapter feedListAdapter;

    @Bean
    UserService userService;

    @Bean
    ProfileRestWrapper profileRestWrapper;
    @Bean
    CourtsRestWrapper courtsRestWrapper;
    @Bean
    FeedRestWrapper feedRestWrapper;

    @RestService
    ImageRest imageRest;

    @Extra("courtId")
    Long courtId;

    @AnimationRes(R.anim.spinner)
    Animation loadingAnim;

    private boolean followed;
    private long followingAmount;

    private CourtInfo courtInfo;

    @AfterViews
    public void init() {
        profileView.setVisibility(View.INVISIBLE);

        profileLoadingView.setAnimation(loadingAnim);

        courtsRestWrapper.getCourt(courtId, new RestListener<CourtInfo>() {
            @Override
            public void onSuccess(CourtInfo courtInfo) {
                fillCourt(courtInfo);
            }
        });

        navigationFooterView.setPage(NavigationFooterView.Page.PROFILE);
    }

    @UiThread
    void fillCourt(CourtInfo courtInfo) {
        this.courtInfo = courtInfo;

        profileView.setVisibility(View.VISIBLE);

        profileLoadingView.clearAnimation();
        profileLoadingView.setVisibility(View.GONE);

        avatarView.configure(
                "avatar",
                R.color.gray_1,
                () -> null, /* ToDo: add caching */
                () -> imageRest.getImage(courtInfo.getPhotos().get(0).getUrl())
        );

        feedItemImagesView.bind(
                courtInfo.getId(),
                Stream.of(courtInfo.getPhotos()).map(CourtInfo.Photo::getUrl).toList()
        );

        descriptionView.setText(courtInfo.getDescription());

        nameView.setText(courtInfo.getName());

        followersAmountView.setText("" + courtInfo.getFollowersAmount());

        setEquipmentPhoto(courtInfo, equipmentPhotoView1, 0);
        setEquipmentPhoto(courtInfo, equipmentPhotoView2, 1);
        setEquipmentPhoto(courtInfo, equipmentPhotoView3, 2);
        setEquipmentPhoto(courtInfo, equipmentPhotoView4, 3);
        setEquipmentPhoto(courtInfo, equipmentPhotoView5, 4);
        setEquipmentPhoto(courtInfo, equipmentPhotoView6, 5);
        setEquipmentPhoto(courtInfo, equipmentPhotoView7, 6);
        setEquipmentPhoto(courtInfo, equipmentPhotoView8, 7);
        setEquipmentPhoto(courtInfo, equipmentPhotoView9, 8);

        geoTagView.setText(courtInfo.getLocation().getName());

        followed = courtInfo.getFollowed();
        followingAmount = courtInfo.getFollowersAmount();

        updateFollowingButtonState(courtInfo);

        updateRateState(courtInfo);

        //followersAmount.setText(getResources().getString(R.string.profile_users_amount, user.getFollowers_amount()));
        //followingAmount.setText(getResources().getString(R.string.profile_users_amount, user.getFollowing_amount()));
    }

    private void setEquipmentPhoto(CourtInfo courtInfo, LoadableImageView imageView, int index) {
        if (courtInfo.getEquipments().size() > index) {
            imageView.setVisibility(View.VISIBLE);
            imageView.configure(
                    "court_" + index,
                    R.color.gray_1,
                    () -> null,
                    () -> imageRest.getImage(courtInfo.getEquipments().get(index).getUrl())
            );
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    @Click(R.id.subscribe)
    void onSubscribe() {
        profileRestWrapper.postFollowing(courtInfo.getFollowed(), courtInfo.getId(), new RestListener<>());
        followed = !followed;
        followingAmount = followed ? followingAmount + 1 : followingAmount - 1;
        followersAmountView.setText(getResources().getString(R.string.profile_users_amount, followingAmount));
        updateFollowingButtonState(courtInfo);
    }

    void updateRatePanel(long weight) {
        double sum = 0.0;
        long rateCount = this.courtInfo.getRateCount();
        if (rateCount > 0) {
            sum = this.courtInfo.getRateAvg() / rateCount;
        }

        sum += weight;

        if (!this.courtInfo.getLiked()) {
            this.courtInfo.setRateCount(rateCount + 1);
            this.courtInfo.setLiked(true);
        } else {
            sum -= this.courtInfo.getMyRate();
        }
        this.courtInfo.setMyRate((double)weight);
        this.courtInfo.setRateAvg(sum / this.courtInfo.getRateCount());

        updateRateState(this.courtInfo);
    }

    @Click(R.id.rate0)
    void onRate0Click() {
        feedRestWrapper.addLike(courtId, 1, new RestListener<>());
        updateRatePanel(1);
    }

    @Click(R.id.rate1)
    void onRate1Click() {
        feedRestWrapper.addLike(courtId, 2, new RestListener<>());
        updateRatePanel(2);
    }

    @Click(R.id.rate2)
    void onRate2Click() {
        feedRestWrapper.addLike(courtId, 3, new RestListener<>());
        updateRatePanel(3);
    }

    @Click(R.id.rate3)
    void onRate3Click() {
        feedRestWrapper.addLike(courtId, 4, new RestListener<>());
        updateRatePanel(4);
    }

    @Click(R.id.rate4)
    void onRate4Click() {
        feedRestWrapper.addLike(courtId, 5, new RestListener<>());
        updateRatePanel(5);
    }

    void updateRateState(CourtInfo courtInfo) {
        double rate_average = courtInfo.getRateAvg();
        long rate_count = courtInfo.getRateCount();
        rateBy.setText("rate by: " + rate_count);

        rate0.setColorFilter(null);
        rate1.setColorFilter(null);
        rate2.setColorFilter(null);
        rate3.setColorFilter(null);
        rate4.setColorFilter(null);

        if (1 <= rate_average) {
            rate0.setColorFilter(
                    getResources().getColor(R.color.vdv),
                    PorterDuff.Mode.SRC_IN
            );
        }

        if (2 <= rate_average) {
            rate1.setColorFilter(
                    getResources().getColor(R.color.vdv),
                    PorterDuff.Mode.SRC_IN
            );
        }

        if (3 <= rate_average) {
            rate2.setColorFilter(
                    getResources().getColor(R.color.vdv),
                    PorterDuff.Mode.SRC_IN
            );
        }

        if (4 <= rate_average) {
            rate3.setColorFilter(
                    getResources().getColor(R.color.vdv),
                    PorterDuff.Mode.SRC_IN
            );
        }

        if (5 <= rate_average) {
            rate4.setColorFilter(
                    getResources().getColor(R.color.vdv),
                    PorterDuff.Mode.SRC_IN
            );
        }
    }

    void updateFollowingButtonState(CourtInfo courtInfo) {
        if (courtInfo.getMine()) {
            subscribeButton.setVisibility(View.GONE);
        } else if (followed) {
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
