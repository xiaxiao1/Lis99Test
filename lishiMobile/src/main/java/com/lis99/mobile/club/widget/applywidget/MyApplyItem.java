package com.lis99.mobile.club.widget.applywidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/11/17.
 *
 *      报名填写列表
 */
public class MyApplyItem extends MyBaseAdapter{

    //需要显示的选项
    private ArrayList<String> visibleItem;


    public MyApplyItem(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    public void setVisibleItem ( ArrayList<String> item )
    {
        visibleItem = item;
        if ( visibleItem == null || visibleItem.size() == 0 )
        {
            if (visibleItem == null)
            {
                visibleItem = new ArrayList<String>();
                visibleItem.add("1");
                visibleItem.add("1");
                visibleItem.add("0");
                visibleItem.add("1");
                visibleItem.add("0");
                visibleItem.add("0");
                visibleItem.add("0");
                visibleItem.add("0");
            }
        }
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.lsclub_apply_item, null);

            holder = new Holder();

            holder.nameView = (EditText) view.findViewById(R.id.nameView);
            holder.phoneView = (EditText) view.findViewById(R.id.phoneView);
            holder.idNumView = (EditText) view.findViewById(R.id.idNumView);
            holder.et_telOhter = (EditText) view.findViewById(R.id.et_telOhter);
            holder.et_QQ = (EditText) view.findViewById(R.id.et_QQ);
            holder.et_address = (EditText) view.findViewById(R.id.et_address);

            holder.btn_address = (Button) view.findViewById(R.id.btn_address);

            holder.radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        }

//        设置显示内容
        setVisibleInfo(holder);





        return view;
    }

    //设置显示内容    0 gone, 1 visible
    private void setVisibleInfo ( Holder holder )
    {
        //控制显示哪个选项
        if ("0".equals(visibleItem.get(0)))
        {
            holder.nameView.setVisibility(View.GONE);
        } else
        {
            holder.nameView.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(1)))
        {
            holder.idNumView.setVisibility(View.GONE);
        } else
        {
            holder.idNumView.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(2)))
        {
            holder.radioGroup.setVisibility(View.GONE);
        } else
        {
            holder.radioGroup.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(3)))
        {
            holder.phoneView.setVisibility(View.GONE);
        } else
        {
            holder.phoneView.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(4)))
        {
            holder.et_telOhter.setVisibility(View.GONE);
        } else
        {
            holder.et_telOhter.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(5)))
        {
            holder.et_QQ.setVisibility(View.GONE);
        } else
        {
            holder.et_QQ.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(6)))
        {
            holder.et_address.setVisibility(View.GONE);
        } else
        {
            holder.et_address.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(7)))
        {
            holder.btn_address.setVisibility(View.GONE);
        } else
        {
            holder.btn_address.setVisibility(View.VISIBLE);
        }
    }


    class Holder
    {
        EditText nameView;
        EditText phoneView;
        EditText idNumView;

        EditText et_telOhter, et_QQ, et_address;

        Button btn_address;

        RadioGroup radioGroup;
    }

}
