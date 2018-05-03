package com.its.vdv.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.its.vdv.data.UserInfo;
import com.its.vdv.views.UserInfoListItemView;
import com.its.vdv.views.UserInfoListItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

@EBean
public class SearchListAdapter extends BaseAdapter {
    @RootContext
    Context context;

    @Setter private List<UserInfo> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public UserInfo getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        UserInfoListItemView v = convertView == null ?
                UserInfoListItemView_.build(context) :
                (UserInfoListItemView) convertView;

        v.bind(getItem(position));

        return v;
    }
}
