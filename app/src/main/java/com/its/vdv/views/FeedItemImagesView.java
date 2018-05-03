package com.its.vdv.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.its.vdv.R;
import com.its.vdv.rest.wrapper.PostRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.service.PostService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EViewGroup(R.layout.view_feed_item_images)
public class FeedItemImagesView extends RelativeLayout {
    @ViewById(R.id.image)
    LoadableImageView imageView;
    @ViewById(R.id.scroll_left)
    View scrollLeftView;
    @ViewById(R.id.scroll_right)
    View scrollRighttView;

    @Bean
    PostService postService;
    @Bean
    PostRestWrapper postRestWrapper;

    public FeedItemImagesView(Context context) {
        super(context);
    }

    public FeedItemImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private long id;
    private int currentIndex = 0;
    private List<String> urls = new ArrayList<>();

    public void bind(long id, List<String> urls) {
        this.id = id;
        this.urls = urls;
        this.currentIndex = 0;

        configureImage();
    }

    @Click(R.id.scroll_left)
    void onScrollLeft() {
        currentIndex--;

        configureImage();
    }

    @Click(R.id.scroll_right)
    void onRightLeft() {
        currentIndex++;

        configureImage();
    }

    private void configureImage() {
        scrollLeftView.setVisibility(currentIndex == 0 ? INVISIBLE : VISIBLE);
        scrollRighttView.setVisibility(currentIndex == urls.size() - 1 ? INVISIBLE : VISIBLE);

        imageView.configure(
                id + "_" + currentIndex,
                R.color.gray_4,
                () -> postService.getPostImage(id, currentIndex).orElse(null),
                () -> postRestWrapper.getPostImage(id, currentIndex, urls.get(currentIndex), new RestListener<>())
        );
    }
}
