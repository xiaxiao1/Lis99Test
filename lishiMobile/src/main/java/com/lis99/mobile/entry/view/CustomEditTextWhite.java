package com.lis99.mobile.entry.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.util.ResourceUtil;

public class CustomEditTextWhite extends LinearLayout {

	private EditText input;  
	private ImageView clear;
	private String hint;
	private String password; 
	private String emailType;
//	private RelativeLayout rl_edit_bg;
	private RelativeLayout gw_bg_common_input_rl;
	private Context mContext;
	public CustomEditTextWhite(Context context) {
		super(context);
		mContext = context;
	}

	public EditText getInput() {
		return input;
	}

	public void setInput(EditText input) {
		this.input = input;
	}
	
	public ImageView getClear() {
		return clear;
	}

//	public RelativeLayout getRl_edit_bg() {
//		return rl_edit_bg;
//	}

	public CustomEditTextWhite(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		int resouceId1 = -1;
		int resouceId2 = -1;
		int resouceId3 = -1;

        resouceId1 = attrs.getAttributeResourceValue(null, "hint", 0);
        if (resouceId1 > 0) {
            hint = context.getResources().getText(resouceId1).toString();
        } else {
        	hint = "";
        }
        resouceId2 = attrs.getAttributeResourceValue(null, "password", 0);
        if (resouceId2 > 0) {
            password = context.getResources().getText(resouceId2).toString();
        } else {
        	password = "no";
        }
        resouceId3 = attrs.getAttributeResourceValue(null, "emailType", 0);
        if (resouceId3 > 0) {
        	emailType = context.getResources().getText(resouceId3).toString();
        } else {
        	emailType = "no";
        }
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.ls_common_edittext, this);  
		init_widget();  
		addListener();
	}

	public String getText(){
		return input.getText().toString();
	}
	public void setText(String text){
		input.setText(text);
	}
	public void setHint(String text){
		input.setHint(text);
	}
	
	public void setInputBg(String bgname){
		gw_bg_common_input_rl.setBackgroundResource(ResourceUtil.getDrawableId(mContext, bgname));
	}
	
	private void init_widget() {
		input = (EditText) findViewById(R.id.input);
		clear = (ImageView) findViewById(R.id.clear);
//		rl_edit_bg = (RelativeLayout) findViewById(ResourceUtil.getId(mContext, "rl_edit_bg"));
		gw_bg_common_input_rl = (RelativeLayout) findViewById(R.id.gw_bg_common_input_rl);
		//rl = (RelativeLayout) findViewById(ResourceUtil.getId(mContext, "custom_edittext"));
		//rl.setClickable(true);
		//rl.setFocusable(true);
		if(input != null){
			input.setHint(hint);
			input.setSingleLine(true);
			input.setEllipsize(TruncateAt.END);
			if(password!=null && "yes".equals(password)){
				input.setTransformationMethod(PasswordTransformationMethod.getInstance());
				input.postInvalidate();
			}
			if(emailType!=null && "yes".equals(emailType)){
				input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			}
			if(!"".equals(input.getText().toString())){
				clear.setVisibility(View.VISIBLE);
			}else{
				clear.setVisibility(View.GONE);
			}
		}
	
	}

	private void addListener() {
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				input.setText("");
			}
		});
		input.addTextChangedListener(new MyTextWatcher());
		input.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(clickEdittext!=null){
					 clickEdittext.onClickForEdittext();
				 }
				return false;
			}
		});
		
		/*rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				input.requestFocus();
				InputMethodManager imm = (InputMethodManager)(mContext.getSystemService(Context.INPUT_METHOD_SERVICE));        
				 imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
				 
			}
		});*/
	}
	
	public void setTextChangedListener(MyTextWatcher watcher){
		input.addTextChangedListener(watcher);
	}
	
	public class MyTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			changeClear();
			
		}
		
	}

	public void changeClear() {
		if(!"".equals(input.getText().toString())){
			clear.setVisibility(View.VISIBLE);
		}else{
			clear.setVisibility(View.GONE);
		}		
	}
	
	private ClickEdittext clickEdittext;
	
	public void setClickEdittextListener(ClickEdittext clickedit){
		this.clickEdittext = clickedit;
	}
	
	public interface ClickEdittext{
		public void onClickForEdittext();
	}

}
