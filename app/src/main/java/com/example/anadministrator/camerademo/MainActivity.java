package com.example.anadministrator.camerademo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ASK_CALL_PHONE = 26944;
    //获取Sdcarad根目录
    private String path = Environment.getExternalStorageDirectory().getPath() + "/zqf.jpg";
    /**
     * 照相
     */
    private Button mButCamera;
    /**
     * 相册
     */
    private Button mButPics;
    /**
     * 裁剪
     */
    private Button mButCut;
    private ImageView mImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //请求权限
        RequestPermission();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            //1.接受传回来的图片
            Bitmap bitmap = data.getParcelableExtra("data");
            mImageview.setImageBitmap(bitmap);
        }


        if (requestCode == 3000 && resultCode == RESULT_OK) {
            //2.1将裁剪好图片设置
            Bitmap bitmap = data.getParcelableExtra("data");
            mImageview.setImageBitmap(bitmap);
        }
        if (requestCode == 4000 && resultCode == RESULT_OK) {
            //3.拿到相册的图片
            Uri uri = data.getData();
            crop(uri);
        }
    }

    private void crop(Uri uri) {
        //调取裁剪
        Intent it = new Intent("com.android.camera.action.CROP");
        it.setDataAndType(uri, "image/*");
        //裁剪
        it.putExtra("crop", true);
        //设置宽高比
        it.putExtra("aspectX", 1);
        it.putExtra("aspectY", 1);

        it.putExtra("outputX", 250);
        it.putExtra("outputY", 250);
        //取消人脸识别
        it.putExtra("onFaceDetection", false);
        it.putExtra("return-data", true);
        startActivityForResult(it, 3000);
    }


    public void RequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCAMERAPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCAMERAPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MainActivity.this,"3Q3Q",Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "不给拉几把倒", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void initView() {
        mButCamera = (Button) findViewById(R.id.butCamera);
        mButCamera.setOnClickListener(this);


        mButCut = (Button) findViewById(R.id.butCut);
        mButCut.setOnClickListener(this);
        mImageview = (ImageView) findViewById(R.id.imageview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butCamera:
                // TODO Auto-generated method stub
                //1. 通过隐示Intent调取系统的相机
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it, 1000);
                break;

            case R.id.butCut:
                // 裁剪图片
                Intent it2 = new Intent(Intent.ACTION_PICK);
                it2.setType("image/*");
                startActivityForResult(it2, 4000);
                break;
        }
    }
}
