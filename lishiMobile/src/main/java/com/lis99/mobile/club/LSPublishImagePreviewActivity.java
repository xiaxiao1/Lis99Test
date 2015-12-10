package com.lis99.mobile.club;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entry.application.DemoApplication;

public class LSPublishImagePreviewActivity extends Activity implements View.OnClickListener {

    private Bitmap bitmap;

    private Button okButton;
    private TextView countView;


    protected ImageView titleLeftImage;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lspublish_image_preview);


        okButton = (Button) findViewById(R.id.okButton);
        countView = (TextView) findViewById(R.id.countView);

        titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        View titleLeft = findViewById(R.id.titleLeft);
        if (titleLeft != null) {
            titleLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftAction();
                }
            });
        }

        if (titleLeftImage != null) {
            titleLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftAction();
                }
            });
        }

        bitmap = DemoApplication.publishBitmap.get();

        imageView = (ImageView) findViewById(R.id.gallery);
        imageView.setImageBitmap(bitmap);


    }

    protected void leftAction() {

        finish();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okButton) {
            Intent intent = new Intent();
            intent.putExtra("delete", true);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}

