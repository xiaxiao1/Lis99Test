package com.lis99.mobile.club;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.CheckBoxChange;
import com.lis99.mobile.club.adapter.LSImagePickerPreviewAdapter;
import com.lis99.mobile.club.topicstrimg.LSTopicStringImageActivity;

import java.util.ArrayList;

public class LSImagePickerPreviewActivity extends FragmentActivity implements CheckBoxChange, ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager viewPager;
    private LSImagePickerPreviewAdapter adapter;
    private ArrayList<String> totalUris;
    private ArrayList<String> selectedUris;
    private int position;
    private Button checkBox;

    private Button okButton;
    private TextView countView;

    String curretUrl;

    boolean isReply;
    protected ImageView titleLeftImage;

    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsimage_picker_preview);

        isReply = getIntent().getBooleanExtra("isReply", false);

        className = getIntent().getStringExtra("CLASSNAME");

        viewPager = (ViewPager) findViewById(R.id.gallery);

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

        if (savedInstanceState != null) {
            totalUris = (ArrayList<String>) savedInstanceState.getSerializable("totalUri");
            selectedUris = (ArrayList<String>) savedInstanceState.getSerializable("selectUri");
            position = savedInstanceState.getInt("position");
        } else {
            totalUris = (ArrayList<String>) getIntent().getSerializableExtra("totalUri");
            selectedUris = (ArrayList<String>) getIntent().getSerializableExtra("selectUri");

            position = getIntent().getIntExtra("position", 0);
        }
        if (selectedUris == null) {
            selectedUris = new ArrayList<String>();
        }

        adapter = new LSImagePickerPreviewAdapter(this, totalUris);

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        checkBox.setOnClickListener(this);

        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(position);
        if (selectedUris != null && selectedUris.contains(totalUris.get(position))) {
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }

        countView.setText((position+1) + "/" + totalUris.size());

        curretUrl = totalUris.get(position);

        resetButton();


    }

    protected void leftAction() {
        Intent intent = new Intent();
        intent.putExtra("uris", selectedUris);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("totalUri", totalUris);
        outState.putSerializable("selectUri", selectedUris);
        outState.putInt("position", position);
    }


    @Override
    public void onCheckBoxChange(String url) {
        if (selectedUris.contains(url)) {
            selectedUris.remove(url);
            resetButton();
            checkBox.setSelected(false);
        } else {
            if (selectedUris.size() >= LSImagePicker.MAX_COUNT) {
                Toast.makeText(this, "你最多能选择" + LSImagePicker.MAX_COUNT + "张照片", Toast.LENGTH_LONG).show();
                return;
            }
            selectedUris.add(url);
            resetButton();
            checkBox.setSelected(true);
        }
    }

    private void resetButton(){
        if (selectedUris.size() > 0) {
            okButton.setBackgroundResource(R.drawable.btn_bg_image_ok_enable);
            okButton.setTextColor(Color.WHITE);
            okButton.setText("确定" + selectedUris.size() + "/" + LSImagePicker.MAX_COUNT);


        } else {
            okButton.setBackgroundResource(R.drawable.btn_bg_image_ok_disable);
            okButton.setTextColor(Color.WHITE);
            okButton.setText("");
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        String url = totalUris.get(position);
        countView.setText((position+1) + "/" + totalUris.size());
        if (!selectedUris.contains(url)) {
            checkBox.setSelected(false);
        } else {
            checkBox.setSelected(true);
        }
        curretUrl = url;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okButton) {
            if (selectedUris != null && selectedUris.size() > 0) {
                Intent intent = null;
                if (isReply) {
                    intent = new Intent(this, LSClubTopicReplyActivity.class);
                } else {
                    intent = new Intent(this, LSTopicStringImageActivity.class);
                }

                if ( !TextUtils.isEmpty(className))
                {
                    intent = new Intent();
                    intent.setClassName(this, className);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("uris", selectedUris);
                startActivity(intent);
                finish();
            }
        } else if (v.getId() == R.id.checkbox) {
            if (checkBox.isSelected()) {
                onCheckBoxChange(curretUrl);
            } else {
                onCheckBoxChange(curretUrl);
            }
        }
    }
}
