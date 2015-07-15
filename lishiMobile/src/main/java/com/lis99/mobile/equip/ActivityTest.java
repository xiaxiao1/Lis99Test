package com.lis99.mobile.equip;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;

public class ActivityTest extends LSBaseActivity {

    private AnimationDrawable animationDrawable;

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.equip_title_chose);

        ImageView iv_1 = (ImageView) findViewById(R.id.iv_1);

        iv_1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showNewVersionDialog("地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地地地人 会人地 有地有圾地肝有圾地");
            }
        });

		img = (ImageView) findViewById(R.id.img);

        img.setImageResource(R.drawable.load_image_temp_bg);

		animationDrawable = (AnimationDrawable) img.getDrawable();

		animationDrawable.start();


    }


    private void showNewVersionDialog(String str) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage(str);
        builder.setTitle("新版本提示");
        builder.setPositiveButton("去更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("暂不", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 敢点暂不，就退出程序
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				android.os.Process.killProcess(Process.myPid());
            }
        });
        builder.create().show();
    }


    private void init() {

    }


}
