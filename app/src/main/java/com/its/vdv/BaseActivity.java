package com.its.vdv;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class BaseActivity extends FragmentActivity {
    public void redirect(
            Class<? extends BaseActivity> redirectTo,
            int enterAnim,
            int exitAnim,
            boolean doFinish,
            Map<String, Serializable> extras
    ) {
        Intent i = new Intent(this, redirectTo);

        for (String extraKey : extras.keySet()) {
            i.putExtra(extraKey, extras.get(extraKey));
        }

        startActivity(i);
        overridePendingTransition(enterAnim, exitAnim);

        if (doFinish) {
            finish();
        }
    }

    public void redirectForResult(Class<? extends BaseActivity> redirectTo, int enterAnim, int exitAnim, int code) {
        redirectForResult(redirectTo, enterAnim, exitAnim, code, Collections.emptyMap());
    }

    public void redirectForResult(
            Class<? extends BaseActivity> redirectTo,
            int enterAnim,
            int exitAnim,
            int code,
            Map<String, Serializable> extras) {

        Intent i = new Intent(this, redirectTo);

        for (String extraKey : extras.keySet()) {
            i.putExtra(extraKey, extras.get(extraKey));
        }

        startActivityForResult(i, code);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void finishRedirectForResult(int enterAnim, int exitAnim, int result) {
        finishRedirectForResult(enterAnim, exitAnim, result, Collections.emptyMap());
    }

    public void finishRedirectForResult(int enterAnim, int exitAnim, int result, Map<String, Serializable> extras) {
        Intent i = new Intent();

        for (String extraKey : extras.keySet()) {
            i.putExtra(extraKey, extras.get(extraKey));
        }

        setResult(result, i);

        finish();
        overridePendingTransition(enterAnim, exitAnim);
    }
}
