package com.its.vdv.dao;

import android.content.Context;

import com.its.vdv.utils.StorageUtils;

import java.io.IOException;

abstract class CommonImageDao<T> {
    public byte [] getIcon(T id) {
        try {
            return StorageUtils.readData(getContext(), getFileName(id));
        } catch (IOException e) {
            logError("Icon collect failed");

            return null;
        }
    }

    public void addIcon(T id, byte [] iconBytes) {
        try {
            StorageUtils.writeData(getContext(), getFileName(id), iconBytes);
        } catch (IOException e) {
            logError("Icon save failed");
        }
    }

    protected abstract Context getContext();
    protected abstract String getFileName(T id);
    protected abstract void logError(String message);
}
