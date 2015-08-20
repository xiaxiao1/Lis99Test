package com.lis99.mobile.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.ClearEditText;
import com.lis99.mobile.util.Common;

/**
 * Created by yy on 15/7/7.
 */
abstract public class SearchInfoBase extends LSBaseActivity{

    protected ClearEditText et_search;

    protected ListView list;

    protected Button btn_type;

    protected PullToRefreshView pull_refresh_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_info_list);

        initViews();
    }

    @Override
    protected void initViews() {
        super.initViews();

        et_search = (ClearEditText) findViewById(R.id.et_search);

        et_search.setText(getIntent().getStringExtra("SEARCHTEXT"));

        list = (ListView) findViewById(R.id.list);

        btn_type = (Button) findViewById(R.id.btn_type);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something;
                    String search = et_search.getText().toString();
                    if (TextUtils.isEmpty(search))
                    {
                        Common.toast("不能为空");
                        return true;
                    }
                    search = Common.getSearchText(search);
                    Common.hideSoftInput(activity);
                    searchNew(search);
                    return true;
                }
                return false;
            }
        });

    }

    abstract public void searchNew ( String str );


}
