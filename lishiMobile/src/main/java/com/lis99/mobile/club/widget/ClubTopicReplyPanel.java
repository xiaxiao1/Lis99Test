package com.lis99.mobile.club.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lis99.mobile.R;

public class ClubTopicReplyPanel extends PopupWindow {

	private Button publishButton, delButton, addImageButton; 
	View cancelButton;
    private View body;  
    
    public View imagePanel;
    public ImageView imageView;
    public EditText contentView;
    public TextView replyNameView;
  
    public ClubTopicReplyPanel(Activity context,OnClickListener itemsOnClick) {  
        super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        body = inflater.inflate(R.layout.club_topic_replay_panel, null);
        
        publishButton = (Button) body.findViewById(R.id.publishButton);  
        delButton = (Button) body.findViewById(R.id.replyDelButton);  
        cancelButton =  body.findViewById(R.id.closeButton); 
        addImageButton = (Button) body.findViewById(R.id.addImage);
        
        imagePanel = body.findViewById(R.id.imagePanel);
        imageView = (ImageView) body.findViewById(R.id.replyImageView);
        replyNameView = (TextView) body.findViewById(R.id.replyNameView);
        contentView = (EditText) body.findViewById(R.id.replyContentView);
        
        //取消按钮  
        cancelButton.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
                //销毁弹出框  
                dismiss();  
            }  
        });  
        //设置按钮监听  
        publishButton.setOnClickListener(itemsOnClick);  
        delButton.setOnClickListener(itemsOnClick);  
        addImageButton.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View  
        this.setContentView(body);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        body.setOnTouchListener(new OnTouchListener() {  
              
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = body.findViewById(R.id.pop_layout).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
  
    }  
	
	
}
