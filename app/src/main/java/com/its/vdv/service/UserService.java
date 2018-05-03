package com.its.vdv.service;

import com.annimon.stream.Optional;
import com.its.vdv.dao.UserImageDao;
import com.its.vdv.data.User;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class UserService {
    @Bean
    UserImageDao userImageDao;

    public User getUser() {
        return User.builder()
                .id(12L)
                .name("Sergey Budnik")
                .position("UX Designer")
                .followers_amount(201L)
                .following_amount(416L)
                .build();
    }

    public Optional<byte []> getUserImage(long id) {
        return Optional.ofNullable(userImageDao.getIcon(id));
    }

    public void setUserImage(long id, byte [] image) {
        userImageDao.addIcon(id, image);
    }
}
