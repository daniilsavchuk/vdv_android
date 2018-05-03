package com.its.vdv;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.its.vdv.rest.wrapper.AuthRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int ACTION_SIGN_IN = 1;

    private static final String SCOPE_EMAIL = "email";
    private static final String SCOPE_PROFILE = "profile";

    @ViewById(R.id.sign_in_spinner)
    View signInSpinnerView;
    @ViewById(R.id.sign_in_button)
    View signInButtonView;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnim;

    private GoogleApiClient googleApiClient;

    @Bean
    AuthRestWrapper authRestWrapper;

    @AfterViews
    void init() {
        Fabric.with(this, new Crashlytics());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getResources().getString(R.string.google_auth_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addScope(new Scope(SCOPE_EMAIL))
                .addScope(new Scope(SCOPE_PROFILE))
                .build();
    }

    @Click(R.id.sign_in_button)
    public void onSignInClick() {
        signInButtonView.setVisibility(View.INVISIBLE);

        signInSpinnerView.setAnimation(spinnerAnim);
        signInSpinnerView.setVisibility(View.VISIBLE);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(signInIntent, ACTION_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        signInButtonView.setVisibility(View.VISIBLE);

        signInSpinnerView.clearAnimation();
        signInSpinnerView.setVisibility(View.INVISIBLE);

        Log.e(LoginActivity.class.getCanonicalName(), "Authentication failed - no connection");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                authRestWrapper.login(account.getServerAuthCode(), new RestListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        onBackendAuthSuccess(
                                account.getEmail(),
                                account.getDisplayName()
                        );
                    }

                    @Override
                    public void onFailure(Exception e) {
                        signInButtonView.setVisibility(View.VISIBLE);

                        signInSpinnerView.clearAnimation();
                        signInSpinnerView.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (ApiException e) {
                signInButtonView.setVisibility(View.VISIBLE);

                signInSpinnerView.clearAnimation();
                signInSpinnerView.setVisibility(View.INVISIBLE);

                Crashlytics.logException(e);
            }
        }
    }

    @UiThread
    void onBackendAuthSuccess(String email, String name) {
        Map<String, Serializable> extras = new HashMap<>();
        {
           extras.put("email", email);
           extras.put("name", name);
        }

        redirect(UserCreationActivity_.class, 0, 0, true, extras);
    }
}
