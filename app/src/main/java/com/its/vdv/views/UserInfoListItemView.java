package com.its.vdv.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.its.vdv.R;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.rest.wrapper.UserRestWrapper;
import com.its.vdv.service.UserService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.view_list_item_user_info)
public class UserInfoListItemView extends LinearLayout {
    @ViewById(R.id.avatar)
    LoadableImageView avatarView;
    @ViewById(R.id.name)
    TextView nameView;

    @Bean
    UserService userService;

    @Bean
    UserRestWrapper userRestWrapper;

    public UserInfoListItemView(Context context) {
        super(context);
    }

    public UserInfoListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(UserInfo userInfo) {
        avatarView.configure(
                "" + userInfo.getId(),
                R.color.gray_4,
                () -> userService.getUserImage(userInfo.getId()).orElse(null),
                () -> userInfo.getAvatarUrl() == null ? null : userRestWrapper.getUserImage(userInfo.getId(), userInfo.getAvatarUrl(), new RestListener<>())
        );

        nameView.setText(userInfo.getName());
    }
}
