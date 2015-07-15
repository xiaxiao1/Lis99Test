package com.lis99.mobile.engine.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.lis99.mobile.R;

abstract public class BaseActivity extends Activity implements InitInterface {
	public Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutId());
		context = this;

		setListener();
		initData();
	};

	abstract protected View getView();

	Dialog dialogView = null;

	protected void showWaiting(Context context) {
		dialogView = new Dialog(context, R.style.theme_dialog_alert);
		dialogView.setContentView(R.layout.window_layout);
		dialogView.setCancelable(true);
		dialogView
				.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						hideDialog();
					}
				});
		dialogView.show();
	}

	/**
	 * 閸欐牗绉风粵澶婄窡鐎电鐦介敓锟�
	 */
	protected void hideDialog() {
		if (dialogView != null) {
			dialogView.dismiss();
			dialogView = null;
		}
	}

	protected void finishButton(Button button) {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

}
