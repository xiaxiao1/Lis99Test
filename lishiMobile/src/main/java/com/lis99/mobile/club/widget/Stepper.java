package com.lis99.mobile.club.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;

public class Stepper extends RelativeLayout implements OnClickListener {
	
	ImageView minusView;
	ImageView plusView;
	EditText valueView;
	int value = 1;

	public Stepper(Context context) {
		this(context, null);
	}

	public Stepper(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.club_stepper, this, true);
		
		minusView = (ImageView) this.findViewById(R.id.minusView);
		plusView = (ImageView) this.findViewById(R.id.plusView);
		valueView = (EditText) this.findViewById(R.id.valueView);
		
		minusView.setOnClickListener(this);
		plusView.setOnClickListener(this);
		
	}
	
	public int getStep(){
		return value;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.minusView) {
			value = Integer.parseInt(valueView.getText().toString());
			if (value > 1) {
				valueView.setText(String.valueOf(--value));
			} 
			if (value <= 1) {
				minusView.setImageResource(R.drawable.club_icon_minus_disable);
			}
		} else {
			value = Integer.parseInt(valueView.getText().toString());
			valueView.setText(String.valueOf(++value));
			if (value > 1) {
				minusView.setImageResource(R.drawable.club_icon_minus);
			}
		}
	}

}
