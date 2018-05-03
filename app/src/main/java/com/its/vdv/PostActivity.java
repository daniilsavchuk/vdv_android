package com.its.vdv;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.its.vdv.rest.wrapper.PostRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.utils.BitmapUtils;
import com.its.vdv.views.NavigationFooterView;
import com.its.vdv.views.PostPopup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.its.vdv.utils.BitmapUtils.scale;

@EActivity(R.layout.activity_post)
public class PostActivity extends BaseActivity {
    private static final String AUTHORITY = "com.its.vdv.provider";

    private static final int TAKE_PHOTO_ACTION_ID = 0;
    private static final int CHOOSE_FROM_GALLERY_ACTION_ID = 1;

    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;

    @ViewById(R.id.image_1)
    ImageView imageView1;
    @ViewById(R.id.image_2)
    ImageView imageView2;
    @ViewById(R.id.image_3)
    ImageView imageView3;
    @ViewById(R.id.image_4)
    ImageView imageView4;

    @ViewById(R.id.post_popup)
    PostPopup postPopup;

    @ViewById(R.id.post_progress)
    View postProgressView;
    @ViewById(R.id.post_progress_spinner)
    View postProgressSpinnerView;

    @ViewById(R.id.description)
    EditText descriptionView;

    @Bean
    PostRestWrapper postRestWrapper;

    @AnimationRes(R.anim.spinner)
    Animation loadingAnim;

    private byte [] image1 = null;
    private byte [] image2 = null;
    private byte [] image3 = null;
    private byte [] image4 = null;

    @AfterViews
    public void init() {
        navigationFooterView.setPage(NavigationFooterView.Page.FITNESS);

        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1
        );
    }

    @Click(R.id.image_1)
    void onImage1Click() {
        postPopup.bindImage(1, imageView1);
    }

    @Click(R.id.image_2)
    void onImage2Click() {
        postPopup.bindImage(2, imageView2);
    }

    @Click(R.id.image_3)
    void onImage3Click() {
        postPopup.bindImage(3, imageView3);
    }

    @Click(R.id.image_4)
    void onImage4Click() {
        postPopup.bindImage(4, imageView4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            Uri url = requestCode == TAKE_PHOTO_ACTION_ID ?
                    postPopup.getPhotoUri() : Uri.parse("content://media/" + data.getData().getPath());

            Bitmap bm = getInitialBitmap(url);

            switch (postPopup.getIndex()) {
                case 1:
                    image1 = BitmapUtils.iconToBytes(bm);
                    break;
                case 2:
                    image2 = BitmapUtils.iconToBytes(bm);
                    break;
                case 3:
                    image3 = BitmapUtils.iconToBytes(bm);
                    break;
                case 4:
                    image4 = BitmapUtils.iconToBytes(bm);
                    break;
            }

            postPopup.getImageView().setImageBitmap(bm);
            postPopup.setVisibility(View.GONE);
        }
    }

    @Click(R.id.post)
    void onPostClick() {
        List<byte []> images = Stream
                .of(image1, image2, image3, image4)
                .filter(it -> it != null)
                .toList();

        postRestWrapper.addPost(39L, descriptionView.getText().toString(), images, new RestListener<Void>() {
            @Override
            public void onStart() {
                onPostStarted();
            }

            @Override
            public void onSuccess(Void data) {
                onPostSendSuccess();
            }

            @Override
            public void onFailure(Exception e) {
                onPostSendSuccess();
            }
        });
    }

    @UiThread
    void onPostStarted() {
        postProgressView.setVisibility(View.VISIBLE);
        postProgressSpinnerView.setAnimation(loadingAnim);
    }

    @UiThread
    void onPostSendSuccess() {
        postProgressView.setVisibility(View.INVISIBLE);

        postProgressSpinnerView.clearAnimation();
        postProgressSpinnerView.setAnimation(loadingAnim);

        redirect(ProfileActivity_.class, 0, 0, true, new HashMap<>());
    }

    private Bitmap getInitialBitmap(Uri url) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), url);

            int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

            return scale(
                    bitmap,
                    bitmap.getWidth() * 800 / minSize,
                    bitmap.getHeight() * 800 / minSize
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
