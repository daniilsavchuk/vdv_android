package com.its.vdv.service;

import android.graphics.Bitmap;

import com.annimon.stream.Optional;
import com.its.vdv.dao.PostImageDao;
import com.its.vdv.utils.BitmapUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import static com.its.vdv.utils.BitmapUtils.iconFromBytes;
import static com.its.vdv.utils.BitmapUtils.iconToBytes;
import static com.its.vdv.utils.BitmapUtils.scale;

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

        postImageDao.addIcon(daoId, scalePostImage(image));
    }

    private byte [] scalePostImage(byte [] imageBytes) {
        Bitmap image = iconFromBytes(imageBytes);

        int newWidth = image.getWidth() * 500 / image.getHeight();
        int newHeight = 500;

        return iconToBytes(scale(image, newWidth, newHeight));
    }
}
