package com.its.vdv.service;

import com.annimon.stream.Optional;
import com.its.vdv.dao.PostImageDao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class PostService {
    @Bean
    PostImageDao postImageDao;

    public Optional<byte []> getPostImage(long id, int imageIndex) {
        PostImageDao.Id daoId = new PostImageDao.Id();
        {
            daoId.setId(id);
            daoId.setImageIndex(imageIndex);
        }

        return Optional.ofNullable(postImageDao.getIcon(daoId));
    }

    public void setPostImage(long id, int imageIndex, byte [] image) {
        PostImageDao.Id daoId = new PostImageDao.Id();
        {
            daoId.setId(id);
            daoId.setImageIndex(imageIndex);
        }

        postImageDao.addIcon(daoId, image);
    }
}
