package com.its.vdv.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.its.vdv.BaseActivity;
import com.its.vdv.FeedActivity_;
import com.its.vdv.MapActivity_;
import com.its.vdv.PostActivity_;
import com.its.vdv.ProfileActivity_;
import com.its.vdv.R;
import com.its.vdv.SearchActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;

@EViewGroup(R.layout.view_navigation_footer)
public class NavigationFooterView extends RelativeLayout {
    public enum Page {
        HOME, SEARCH, FITNESS, MAP, PROFILE
    }

    @ViewById(R.id.home_icon)
    ImageView homeIconView;
    @ViewById(R.id.search_icon)
    ImageView searchIconView;
    @ViewById(R.id.fitness_icon)
    ImageView fitnessIconView;
    @ViewById(R.id.map_icon)
    ImageView mapIconView;
    @ViewById(R.id.profile_icon)
    ImageView profileIconView;

    public NavigationFooterView(Context context) {
        super(context);
    }

    public NavigationFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Click(R.id.home)
    void onHomeClick() {
        ((BaseActivity) getContext()).redirect(
                FeedActivity_.class, 0, 0, true, Collections.emptyMap()
        );
    }

    @Click(R.id.search)
    void onSearchClick() {
        ((BaseActivity) getContext()).redirect(
                SearchActivity_.class, 0, 0, true, Collections.emptyMap()
        );
    }

    @Click(R.id.fitness)
    void onFitnessClick() {
        ((BaseActivity) getContext()).redirect(
                PostActivity_.class, 0, 0, true, Collections.emptyMap()
        );
    }

    @Click(R.id.map)
    void onMapClick() {
        ((BaseActivity) getContext()).redirect(
                MapActivity_.class, 0, 0, true, Collections.emptyMap()
        );
    }

    @Click(R.id.profile)
    void onProfileClick() {
        ((BaseActivity) getContext()).redirect(
                ProfileActivity_.class, 0, 0, true, Collections.emptyMap()
        );
    }

    public void setPage(Page page) {
        switch (page) {
            case HOME:
                setPage(homeIconView);
                break;
            case SEARCH:
                setPage(searchIconView);
                break;
            case FITNESS:
                setPage(fitnessIconView);
                break;
            case MAP:
                setPage(mapIconView);
                break;
            case PROFILE:
                setPage(profileIconView);
                break;
        }
    }

    private void setPage(ImageView currentPageIcon) {
        homeIconView.getDrawable().setColorFilter(null);
        searchIconView.getDrawable().setColorFilter(null);
        fitnessIconView.getDrawable().setColorFilter(null);
        mapIconView.getDrawable().setColorFilter(null);
        profileIconView.getDrawable().setColorFilter(null);

        currentPageIcon.getDrawable().setColorFilter(
                getResources().getColor(R.color.vdv),
                PorterDuff.Mode.SRC_IN
        );
    }
}
