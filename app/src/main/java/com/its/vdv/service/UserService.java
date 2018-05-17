package com.its.vdv.service;

import com.annimon.stream.Optional;
import com.its.vdv.dao.UserImageDao;
import com.its.vdv.data.User;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import lombok.Getter;
import lombok.Setter;

@EBean
public class UserService {
    @Bean
    UserImageDao userImageDao;

    @Getter @Setter private long myId;

    public Optional<byte []> getUserImage(long id) {
        return Optional.ofNullable(userImageDao.getIcon(id));
    }

    public void setUserImage(long id, byte [] image) {
        userImageDao.addIcon(id, image);
    }
}
