package com.lis99.mobile.club.widget.applywidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

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
                visibleItem.add("1");
                visibleItem.add("1");
                visibleItem.add("1");
                visibleItem.add("1");
                visibleItem.add("1");
                visibleItem.add("1");
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

            holder.v_name = view.findViewById(R.id.v_name);
            holder.v_idcode = view.findViewById(R.id.v_idcode);
            holder.v_man = view.findViewById(R.id.v_man);
            holder.v_phone = view.findViewById(R.id.v_phone);
            holder.v_telOhter = view.findViewById(R.id.v_telOhter);
            holder.v_qq = view.findViewById(R.id.v_qq);

            holder.layout_name = view.findViewById(R.id.layout_name);
            holder.layout_idcode = view.findViewById(R.id.layout_idcode);
            holder.layout_phone = view.findViewById(R.id.layout_phone);
            holder.layout_telOhter = view.findViewById(R.id.layout_telOhter);
            holder.layout_qq = view.findViewById(R.id.layout_qq);
            holder.layout_address = view.findViewById(R.id.layout_address);
            holder.delete = view.findViewById(R.id.delete);

            holder.title = (TextView) view.findViewById(R.id.title);


            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        if ( i == 0 )
        {
            holder.delete.setVisibility(View.GONE);
        }
        else
        {
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.title.setText("报名人" + (i + 1));


//        设置显示内容
        setVisibleInfo(holder);


        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.radioMan) {
                    // Common.log("man");
//                    sex = "1";
                } else {
                    // Common.log("Woman");
//                    sex = "0";
                }
            }
        });




        return view;
    }

    //设置显示内容    0 gone, 1 visible
    private void setVisibleInfo ( Holder holder )
    {
        //控制显示哪个选项
        if ("0".equals(visibleItem.get(0)))
        {
            holder.layout_name.setVisibility(View.GONE);
            holder.v_name.setVisibility(View.GONE);
        } else
        {
            holder.layout_name.setVisibility(View.VISIBLE);
            holder.v_name.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(1)))
        {
            holder.layout_idcode.setVisibility(View.GONE);
            holder.v_idcode.setVisibility(View.GONE);
        } else
        {
            holder.layout_idcode.setVisibility(View.VISIBLE);
            holder.v_idcode.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(2)))
        {
            holder.radioGroup.setVisibility(View.GONE);
            holder.v_man.setVisibility(View.GONE);
        } else
        {
            holder.radioGroup.setVisibility(View.VISIBLE);
            holder.v_man.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(3)))
        {
            holder.layout_phone.setVisibility(View.GONE);
            holder.v_phone.setVisibility(View.GONE);
        } else
        {
            holder.layout_phone.setVisibility(View.VISIBLE);
            holder.v_phone.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(4)))
        {
            holder.layout_telOhter.setVisibility(View.GONE);
            holder.v_telOhter.setVisibility(View.GONE);
        } else
        {
            holder.layout_telOhter.setVisibility(View.VISIBLE);
            holder.v_telOhter.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(5)))
        {
            holder.layout_qq.setVisibility(View.GONE);
            holder.v_qq.setVisibility(View.GONE);
        } else
        {
            holder.layout_qq.setVisibility(View.VISIBLE);
            holder.v_qq.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(6)))
        {
            holder.layout_address.setVisibility(View.GONE);
        } else
        {
            holder.layout_address.setVisibility(View.VISIBLE);
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

        View v_name, v_idcode, v_man, v_phone, v_telOhter, v_qq;

        View layout_name, layout_idcode, layout_phone, layout_telOhter, layout_qq, layout_address,delete;

        TextView title;

    }

}
