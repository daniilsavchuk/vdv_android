package com.its.vdv;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.its.vdv.adapter.CommentsListAdapter;
import com.its.vdv.data.Comment;
import com.its.vdv.data.FeedItem;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.wrapper.PostRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_comments)
public class CommentsActivity extends BaseActivity {
    @ViewById(R.id.comments)
    ListView commentsView;
    @ViewById(R.id.comment)
    EditText commentView;

    @Extra("postId")
    Long postId;

    @Bean
    PostRestWrapper postRestWrapper;

    @Bean
    CommentsListAdapter commentsListAdapter;

    private List<Comment> comments = new ArrayList<>();

    @AfterViews
    void init() {
        postRestWrapper.getPost(postId, new RestListener<FeedItem>() {
            @Override
            public void onSuccess(FeedItem feedItem) {
                comments = feedItem.getComments();

                showComments(feedItem.getComments());
            }
        });
    }

    @Click(R.id.add_comment)
    void onAddCommentClick() {
        comments.add(Comment
                .builder()
                .userInfo(UserInfo
                        .builder()
                        .id(12L)
                        .name("")
                        .avatarUrl(null)
                        .build()
                )
                .text(commentView.getText().toString())
                .build()
        );

        showComments(comments);

        postRestWrapper.addComment(postId, commentView.getText().toString(), new RestListener<>());

        commentView.setText("", TextView.BufferType.NORMAL);
    }

    @UiThread
    void showComments(List<Comment> comments) {
        commentsListAdapter.setItems(comments);
        commentsView.setAdapter(commentsListAdapter);
    }
}
