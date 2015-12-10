package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by labixiaoxin on 15-8-8.
 */
public class CustomCursorLoader extends CursorLoader {
    public CustomCursorLoader(Context context) {
        super(context);
    }

    public CustomCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected void onStopLoading() {
    }

}
