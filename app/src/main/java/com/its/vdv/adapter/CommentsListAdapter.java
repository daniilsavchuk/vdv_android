package com.its.vdv.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.its.vdv.data.Comment;
import com.its.vdv.views.CommentItemView;
import com.its.vdv.views.CommentItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

@EBean
public class CommentsListAdapter extends BaseAdapter {
    @RootContext
    Context context;

    @Setter public List<Comment> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Comment getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CommentItemView v = convertView == null ?
                CommentItemView_.build(context) :
                (CommentItemView) convertView;

        v.bind(getItem(position));

        return v;
    }
}
