package com.lis99.mobile.newhome.activeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.adapter.ListAdapter;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LSAllLineCateActivity extends LSBaseActivity {

    GridView gridView;
    LSLineCate lsLineCateModel;
    private int cityId = -1;
    private double latitude = -1, longitude = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsall_line_cate);
        cityId = getIntent().getIntExtra("cityId", -1);
        latitude = getIntent().getDoubleExtra("latitude", -1);
        longitude = getIntent().getDoubleExtra("longitude", -1);
        initViews();
        loadData();
    }

    @Override
    protected void initViews() {
        super.initViews();
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(null);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LSLineCate.LSLineCateItem item = lsLineCateModel.catelist.get(position);
                Intent intent = new Intent(LSAllLineCateActivity.this, LSLineCateListActivity.class);
                intent.putExtra("cityId", cityId);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("cateId", item.getId());
                intent.putExtra("cateName", item.getName());
                startActivity(intent);
            }
        });
    }


    private void loadData() {
        lsLineCateModel = new LSLineCate();

        String url = C.LINE_CATE_INFO;


        MyRequestManager.getInstance().requestGet(url, lsLineCateModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                lsLineCateModel = (LSLineCate) mTask.getResultModel();

                if ( lsLineCateModel == null ) return;
                gridView.setAdapter(new LSAllLineCateAdapter(LSAllLineCateActivity.this, lsLineCateModel.catelist));
            }
        });
    }


    static class LSAllLineCateAdapter extends ListAdapter<LSLineCate.LSLineCateItem> {

        public LSAllLineCateAdapter(Context context, List<LSLineCate.LSLineCateItem> dataList) {
            super(context, dataList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.all_line_item, null);
            }
            LSLineCate.LSLineCateItem item = this.dataList.get(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
            ImageLoader.getInstance().displayImage(item.images, imageView);
            nameView.setText(item.name);
            return convertView;
        }

    }


}
