package com.its.vdv.dao;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import lombok.Getter;
import lombok.Setter;

@EBean(scope = EBean.Scope.Singleton)
public class PostImageDao extends CommonImageDao<PostImageDao.Id> {
    @Getter
    @Setter
    public static class Id {
        private long id;
        private int imageIndex;
    }

    @RootContext
    Context context;

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected String getFileName(PostImageDao.Id id) {
        return PostImageDao.class.getCanonicalName() + "." + id.getId() + "_" + id.getImageIndex();
    }

    @Override
    protected void logError(String message) {
        Log.e(PostImageDao.class.getSimpleName(), message);
    }
}
