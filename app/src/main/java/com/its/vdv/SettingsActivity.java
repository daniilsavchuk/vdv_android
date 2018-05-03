package com.its.vdv;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {
    @Click(R.id.make_private)
    void makePrivate() {
        // ToDo
    }

    @Click(R.id.create_court)
    void createCourt() {
        redirect(CreateCourtActivity_.class, 0, 0, false, new HashMap<>());
    }
}
