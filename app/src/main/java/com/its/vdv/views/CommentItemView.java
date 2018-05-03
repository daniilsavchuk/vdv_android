package com.its.vdv.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.its.vdv.R;
import com.its.vdv.data.Comment;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.rest.wrapper.UserRestWrapper;
import com.its.vdv.service.UserService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.view_list_item_comment)
public class CommentItemView extends LinearLayout {
    @ViewById(R.id.avatar)
    LoadableImageView avatarView;
    @ViewById(R.id.comment)
    TextView commentView;

    @Bean
    UserService userService;

    @Bean
    UserRestWrapper userRestWrapper;

    public CommentItemView(Context context) {
        super(context);
    }

    public CommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(Comment comment) {
        commentView.setText(comment.getText());

        avatarView.configure(
                "" + comment.getUserInfo().getId(),
                R.color.gray_4,
                () -> userService.getUserImage(comment.getUserInfo().getId()).orElse(null),
                () -> comment.getUserInfo().getAvatarUrl() == null ? null : userRestWrapper.getUserImage(
                        comment.getUserInfo().getId(),
                        comment.getUserInfo().getAvatarUrl(),
                        new RestListener<>()
                )
        );
    }
}
