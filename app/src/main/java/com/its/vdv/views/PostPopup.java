package com.its.vdv.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.its.vdv.BaseActivity;
import com.its.vdv.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Getter;

@EViewGroup(R.layout.view_post_popup)
public class PostPopup extends RelativeLayout {
    private static final String AUTHORITY = "com.its.vdv.provider";

    private static final int TAKE_PHOTO_ACTION_ID = 0;
    private static final int CHOOSE_FROM_GALLERY_ACTION_ID = 1;

    @Getter private Uri photoUri;
    @Getter private ImageView imageView;
    @Getter private int index;

    public PostPopup(Context context) {
        super(context);
    }

    public PostPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bindImage(int index, ImageView imageView) {
        this.index = index;
        this.imageView = imageView;

        setVisibility(VISIBLE);
    }

    @Click(R.id.take_photo)
    void onTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();

                photoUri = FileProvider.getUriForFile(getContext(), AUTHORITY, photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                ((BaseActivity) getContext()).startActivityForResult(takePictureIntent, TAKE_PHOTO_ACTION_ID);
            } catch (IOException e) {
                /* Do nothing */
            }
        }
    }

    @Click(R.id.choose_from_gallery)
    void onChooseFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        ((BaseActivity) getContext()).startActivityForResult(galleryIntent, CHOOSE_FROM_GALLERY_ACTION_ID);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "VDV_Photo" + timeStamp + "_";

        return File.createTempFile(
                imageFileName,
                ".jpg",
                getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );
    }
}
