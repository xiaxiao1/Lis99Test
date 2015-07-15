package com.lis99.mobile.entry;

import android.app.Application;
import android.graphics.Bitmap;

import com.lis99.mobile.application.data.UserBean;

public class PublicApp extends Application {

	public Bitmap bitmap;
      private PublicApp instance;
      private UserBean userBean;


      public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

      @Override
      public void onCreate() {
            super.onCreate();
            instance = this;
      }

      public PublicApp getInstance (){
            return instance;
      }

      public UserBean getUserBean(){
            return userBean;
      }


}
