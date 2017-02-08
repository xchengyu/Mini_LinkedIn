package com.example.chengyu.mini_linkedin;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.chengyu.mini_linkedin.model.BasicInfo;
import com.example.chengyu.mini_linkedin.util.ImageUtils;
import com.example.chengyu.mini_linkedin.util.ModelUtils;
import com.example.chengyu.mini_linkedin.util.PermissionUtils;

import java.io.IOException;

/**
 * Created by Chengyu on 8/24/2016.
 * copy showImage() and pickImage()
 */
public class BasicInfoEditActivity extends EditBaseActivity<BasicInfo> {
    public static final String KEY_BASIC_INFO = "basic_info";
    private static final int REQ_CODE_PICK_IMAGE = 100;
    private static final String ACTIVITY_TITLE = "Edit Personal Information";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info_edit;
    }

    @Override
    protected String getSpecificTitle() {
        return ACTIVITY_TITLE;
    }

    @Override
    protected BasicInfo initializeData() {
        return getIntent().getParcelableExtra(KEY_BASIC_INFO);
    }

    @Override
    protected void setupUIForCreate() { }

    @Override
    protected void setupUIForEdit(@NonNull BasicInfo data) {
        ((EditText) findViewById(R.id.basic_info_edit_name))
                .setText(data.name);
        ((EditText) findViewById(R.id.basic_info_edit_email))
                .setText(data.email);

//        if (data.path != null && !data.path.equals("")) {
//            showImage(data.path);
//        }//test
        if (data.imageUri != null) {
            showImage(data.imageUri);
        }

        findViewById(R.id.basic_info_edit_image_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.checkPermission(BasicInfoEditActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionUtils.requestReadExternalStoragePermission(BasicInfoEditActivity.this);
                } else {
                    pickImage();
                }
            }
        });
    }

    @Override
    protected void saveAndExit(@Nullable BasicInfo data) {
        if (data == null) {
            data = new BasicInfo();
        }

        data.name = ((EditText) findViewById(R.id.basic_info_edit_name)).getText().toString();
        data.email = ((EditText) findViewById(R.id.basic_info_edit_email)).getText().toString();
        data.imageUri = (Uri) findViewById(R.id.basic_info_edit_image).getTag();
        //data.path = (String) findViewById(R.id.basic_info_edit_image).getTag();//test
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_BASIC_INFO, data);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void showImage(@NonNull String path) {

        ImageView imageView = (ImageView) findViewById(R.id.basic_info_edit_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setTag(path);
        ImageUtils.loadImage(this, path, imageView);
    }//test

    private void showImage(@NonNull Uri uri) {
        ImageView imageView = (ImageView) findViewById(R.id.basic_info_edit_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setTag(uri);
        ImageUtils.loadImage(this, uri, imageView);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
                REQ_CODE_PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                //String path = ModelUtils.saveToInternalStorage(getApplicationContext(), imageUri);//test
                imageUri = Uri.parse(ImageUtils.getRealPathFromURI(BasicInfoEditActivity.this, imageUri));
                //showImage(path);//test
                showImage(imageUri);
            }
        }
    }

}
