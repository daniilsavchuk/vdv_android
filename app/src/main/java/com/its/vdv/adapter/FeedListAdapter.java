package com.its.vdv.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.its.vdv.data.FeedItem;
import com.its.vdv.views.FeedItemView;
import com.its.vdv.views.FeedItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class FeedListAdapter extends BaseAdapter {
    @RootContext
    Context context;

    private List<FeedItem> feedItems;

    public void setItems(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public FeedItem getItem(int position) {
        return feedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public FeedItemView getView(int position, View convertView, ViewGroup parent) {
        FeedItemView v = convertView == null ?
                FeedItemView_.build(context) :
                (FeedItemView) convertView;

        v.bind(getItem(position));

        return v;
    }
}