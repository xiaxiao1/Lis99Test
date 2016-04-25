package com.lis99.mobile.equip;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityTest extends LSBaseActivity implements View.OnClickListener {


    private ListView list;
    private View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

        list = (ListView) findViewById(R.id.list);

        parent = findViewById(R.id.parent);

        ViewTreeObserver vto = parent.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });


        ArrayList<String> al = new ArrayList<>();
        for ( int i = 0; i < 20; i++ )
        {
            String s = "测试" + i;
            al.add(s);
        }

        myAdapter a = new myAdapter(activity, al);

        list.setAdapter(a);



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:


                break;
            case R.id.button2:


                break;
            case R.id.button3:



                break;
            case R.id.button4:


                break;
        }
    }

    static class myAdapter extends MyBaseAdapter {
        public myAdapter(Context c, List listItem) {
            super(c, listItem);
        }

        EditText newEt;
        int index = -1;

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {
            Holder holder = null;
            if ( view == null )
            {
                view = View.inflate(activity, R.layout.test_lit_item, null);
                holder = new Holder(view);
                view.setTag(holder);
            }
            else
            {
                holder = (Holder) view.getTag();
//                if ( newEt != null )
//                {
//                    newEt.requestFocus();
//                }
            }

            holder.tv.setText(getItem(i).toString());

            holder.et.setTag(i+"");


            final Holder finalHolder = holder;
            holder.et.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if ( event.getAction() == MotionEvent.ACTION_UP )
                    {
                        index = Integer.parseInt(finalHolder.et.getTag().toString());
                        newEt = (EditText) v;
                    }

                    return false;
                }
            });

            holder.et.clearFocus();

            if ( index != -1 && index == i )
            {
                holder.et.requestFocus();
            }

//            if ( newEt != null )
//            {
//                newEt.requestFocus();
//            }



            return view;
        }

        static class Holder
        {
            TextView tv;
            EditText et;

            public Holder(View view) {
                tv = (TextView) view.findViewById(R.id.tv);
                et = (EditText) view.findViewById(R.id.et);
            }
        }

    }


}
