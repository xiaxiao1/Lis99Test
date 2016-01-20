package com.lis99.mobile.newhome.activeline;

import android.os.Bundle;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.choiceness.ActiveAllCityAdapter;
import com.lis99.mobile.club.LSBaseActivity;

/**
 * Created by yy on 16/1/19.
 */
public class LSActiveLineChoseLocation extends LSBaseActivity {

    private ListView list_city;

    private ActiveAllCityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.active_line_chose_location);

        initViews();

        setTitle("选择地区");



    }



    @Override
    protected void initViews() {
        super.initViews();

        list_city = (ListView) findViewById(R.id.list_city);



    }
}
