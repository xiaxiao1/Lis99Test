package com.lis99.mobile.club;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.LSImageGralleryAdapter;
import com.lis99.mobile.entry.ActivityPattern1;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LSImageGralleryActivity extends ActivityPattern1 {

//	private GestureDetector gestureDetector;

    ViewPager vp;
    LSImageGralleryAdapter adapter;
    View backView;
    TextView posTextView;
    View saveView;
    ArrayList<String> photos;
    ProgressDialog saveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ls_line_big_image);

//        StatusUtil.setStatusBar(this);

        //gestureDetector = new GestureDetector(this,this);

        int page = getIntent().getIntExtra("page", 0);

        photos = (ArrayList<String>) getIntent().getSerializableExtra("photos");

        adapter = new LSImageGralleryAdapter(this, photos);

        vp = (ViewPager) findViewById(R.id.gallery);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(adapter);
        vp.setCurrentItem(page);

        backView = findViewById(R.id.iv_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        posTextView = (TextView) findViewById(R.id.imagePos);

        posTextView.setText((page+1) + "/" + adapter.getCount());

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                posTextView.setText((arg0+1) + "/" + adapter.getCount());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        saveView = findViewById(R.id.iv_save);
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = vp.getCurrentItem();
                if(photos != null){

                    saveDialog = ProgressDialog.show(LSImageGralleryActivity.this, "保存图片", "图片正在保存中，请稍等...", true);

                    imageLoader.loadImage(photos.get(current), new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            if(saveDialog != null){
                                saveDialog.dismiss();
                                saveDialog = null;
                            }
                            Toast.makeText(LSImageGralleryActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


                            String sdStatus = Environment.getExternalStorageState();
                            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                                if(saveDialog != null){
                                    saveDialog.dismiss();
                                    saveDialog = null;
                                }
                                Toast.makeText(LSImageGralleryActivity.this, "sd卡不可用", Toast.LENGTH_LONG).show();
                                return;
                            }

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                            Date curDate = new Date(System.currentTimeMillis());
                            String rel = formatter.format(curDate);
                            String album_path = Environment.getExternalStorageDirectory() + "/lis99/Picture/"+rel+".jpg";

                            try {
                                saveBitmapToFile(loadedImage, album_path);
                                Toast.makeText(LSImageGralleryActivity.this, "图片已经成功保存到lis99/Picture", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                //e.printStackTrace();
                                Toast.makeText(LSImageGralleryActivity.this, "写入sd卡失败", Toast.LENGTH_LONG).show();
                            }

                            if(saveDialog != null){
                                saveDialog.dismiss();
                                saveDialog = null;
                            }

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            if(saveDialog != null){
                                saveDialog.dismiss();
                                saveDialog = null;
                            }
                            Toast.makeText(LSImageGralleryActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }

    public static void saveBitmapToFile(Bitmap bitmap, String _file)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }


	/*
	@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();

        if(Math.abs(y) < 100){
        	if (x > 150) {
                goRight();
            } else if (x < 150) {
                goLeft();
            }
        }
        return true;
    }

	private void goLeft(){

	}

	private void goRight(){

	}

	public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	*/


}
