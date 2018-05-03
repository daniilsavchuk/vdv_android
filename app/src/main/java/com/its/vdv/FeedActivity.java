package com.its.vdv;

import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.its.vdv.adapter.FeedListAdapter;
import com.its.vdv.data.FeedItem;
import com.its.vdv.rest.wrapper.FeedRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.views.NavigationFooterView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.List;

@EActivity(R.layout.activity_feed)
public class FeedActivity extends BaseActivity {
    @ViewById(R.id.feed_list)
    ListView feedListView;
    @ViewById(R.id.feed_list_loading)
    ImageView feedListLoadingView;
    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;

    @Bean
    FeedListAdapter feedListAdapter;

    @Bean
    FeedRestWrapper feedRestWrapper;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnim;

    @AfterViews
    public void init() {
        feedListLoadingView.setAnimation(spinnerAnim);

        navigationFooterView.setPage(NavigationFooterView.Page.HOME);

        feedRestWrapper.getFeedItems(new RestListener<List<FeedItem>>() {
            @Override
            public void onSuccess(List<FeedItem> feedItems) {
                onFeedLoadingSuccess(feedItems);
            }

            @Override
            public void onFailure(Exception e) {
                onFeedLoadingFailure();
            }
        });
    }

    @UiThread
    void onFeedLoadingSuccess(List<FeedItem> feedItems) {
        feedListLoadingView.clearAnimation();
        feedListLoadingView.setVisibility(View.GONE);

        feedListAdapter.setItems(feedItems);
        feedListView.setAdapter(feedListAdapter);
    }

    @UiThread
    void onFeedLoadingFailure() {
        feedListLoadingView.clearAnimation();
        feedListLoadingView.setVisibility(View.GONE);

        Toast.makeText(this, "Loading failed", Toast.LENGTH_SHORT).show();
    }
}
