package com.its.vdv;

import android.widget.EditText;
import android.widget.ListView;

import com.its.vdv.adapter.SearchListAdapter;
import com.its.vdv.data.UserInfo;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.rest.wrapper.SearchRestWrapper;
import com.its.vdv.views.NavigationFooterView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.List;

@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseActivity {
    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;
    @ViewById(R.id.search)
    EditText searchView;
    @ViewById(R.id.search_list)
    ListView searchListView;

    @Bean
    SearchListAdapter searchListAdapter;

    @Bean
    SearchRestWrapper searchRestWrapper;

    @AfterViews
    public void init() {
        navigationFooterView.setPage(NavigationFooterView.Page.SEARCH);

        searchListView.setAdapter(searchListAdapter);

        search("");
    }

    @TextChange(R.id.search)
    void onSearchChange() {
        search(searchView.getText().toString());
    }

    @ItemClick(R.id.search_list)
    void onUser(UserInfo userInfo) {
        redirect(ProfileActivity_.class, 0, 0, false, Collections.singletonMap("userId", userInfo.getId()));
    }

    @UiThread
    void fillSearch(List<UserInfo> users) {
        searchListAdapter.setItems(users);
        searchListAdapter.notifyDataSetChanged();
    }

    private void search(String nameSubstring) {
        searchRestWrapper.searchUser(nameSubstring, new RestListener<List<UserInfo>>() {
            @Override
            public void onSuccess(List<UserInfo> users) {
                fillSearch(users);
            }
        });
    }
}
