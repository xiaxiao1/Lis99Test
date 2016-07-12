package com.lis99.mobile.club.destination;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.model.DestinationOneModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;

/**
 * Created by yy on 16/7/11.
 *  目的地列表
 */
public class DestinationMainActivity extends ActivityPattern1 {


    private DestinationTabAdapter listAdapter;


    private ListView list;
    private FrameLayout frameLayout;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private Fragment currentFragnment;

    private DestinationFragnmentImg fragnmentImg;
    private DestinationFragnmentString fragnmentString;

    private DestinationOneModel model;

    private TextView title;
    private ImageView titleLeftImage;
    private int currentSelect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.destination_main);

        list = (ListView) findViewById(R.id.list);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        initViews();

        getList();


    }

    protected void initViews() {


        titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        View titleLeft = findViewById(R.id.titleLeft);
        if (titleLeft != null) {
            titleLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        this.title = (TextView) findViewById(R.id.title);
        title.setText("全部目的地");

        fragmentManager = getSupportFragmentManager();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( listAdapter == null || listAdapter.getItem(position) == null ) return;

//                DestinationOneModel.FirstdestEntity item = (DestinationOneModel.FirstdestEntity) listAdapter.getItem(position);

                DestinationOneModel.FirstdestEntity item = (DestinationOneModel.FirstdestEntity) listAdapter.getItem(currentSelect);
                item.isSelect = 0;
                currentSelect = position;
                item = (DestinationOneModel.FirstdestEntity) listAdapter.getItem(position);
                item.isSelect = 1;

                listAdapter.notifyDataSetChanged();

                showFragment(item.id);

            }
        });

    }

    private void showFragment (String id )
    {
        int index = -1;
        if ( "3".equals(id) || "4".equals(id))
        {
            index = 1;
        }
        else
        {
            index = 0;
        }

        switch (index)
        {
            case 0:
                if ( fragnmentImg == null )
                {
                    fragnmentImg = new DestinationFragnmentImg();
                }
                setFragment(fragnmentImg);
                fragnmentImg.setId(id);
                break;
            case 1:
                if ( fragnmentString == null )
                {
                    fragnmentString = new DestinationFragnmentString();
                }
                setFragment(fragnmentString);
                fragnmentString.setId(id);
                break;
        }
    }


    private void setFragment (Fragment fragment)
    {
        if ( currentFragnment == fragment ) return;

        fragmentTransaction = fragmentManager.beginTransaction();

        if ( fragment.isAdded())
        {
            if ( currentFragnment != null )
            fragmentTransaction.hide(currentFragnment).show(fragment);
            else
                fragmentTransaction.show(fragment);
        }
        else
        {
            if ( currentFragnment != null )
            fragmentTransaction.hide(currentFragnment).add(R.id.frameLayout, fragment);
            else
                fragmentTransaction.add(R.id.frameLayout, fragment);
        }

        fragmentTransaction.commit();

        currentFragnment = fragment;
    }

    private void getList ()
    {
        String url = C.DESTINATION_LIST_ONE;

        model = new DestinationOneModel();

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (DestinationOneModel) mTask.getResultModel();
                if ( model == null || model.firstdest == null ) return;
                currentSelect = 0;
                model.firstdest.get(currentSelect).isSelect = 1;
                listAdapter = new DestinationTabAdapter(DestinationMainActivity.this, model.firstdest);
                list.setAdapter(listAdapter);

                DestinationOneModel.FirstdestEntity item = model.firstdest.get(0);

                showFragment(item.id);

            }
        });

    }

    private void cleanList ()
    {
        if ( list == null ) return;
        list.setAdapter(null);
        listAdapter = null;
    }





}
