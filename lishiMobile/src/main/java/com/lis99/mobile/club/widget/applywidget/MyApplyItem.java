package com.lis99.mobile.club.widget.applywidget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.util.DialogManager;
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
                visibleItem.add("1");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        getItemViewType(i);

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

        final NewApplyUpData item = (NewApplyUpData) getItem(i);

        final int position = i;

        holder.nameView.setText(item.name);
        holder.phoneView.setText(item.mobile);
        holder.idNumView.setText(item.credentials);
        holder.et_telOhter.setText(item.phone);
        holder.et_QQ.setText(item.qq);
        holder.et_address.setText(item.postaladdress);
        holder.btn_address.setText(TextUtils.isEmpty(item.address) ? "选择居住城市" : item.address);

        if ("1".equals(visibleItem.get(2)) )
        {
            if ( !TextUtils.isEmpty(item.sex) )
            {
                if ( "男".equals(item.sex))
                {
                    holder.radioGroup.check(R.id.radioMan);
                }
                else if ( "女".equals(item.sex))
                {
                    holder.radioGroup.check(R.id.radioWoman);
                }
            }
            else
            {
                holder.radioGroup.check(R.id.radioMan);
                item.sex = "男";
            }
        }

        setEditTextChanged(holder.nameView, holder.phoneView, holder.idNumView, holder.et_telOhter, holder.et_QQ, holder.et_address, item);

        final Holder finalHolder = holder;
        holder.btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().showCityChooseDialog(LSBaseActivity.activity,
                        new DialogManager.callBack()
                        {

                            @Override
                            public void onCallBack(Object o)
                            {
                                // TODO Auto-generated method stub
                                if ( o == null ) return;
                                item.address = o.toString();
                                finalHolder.btn_address.setText(o.toString());
                            }
                        });
            }
        });


        //删除当前条
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               removeAt(position);

            }
        });

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.radioMan) {
                    // Common.log("man");
//                    sex = "1";
                    item.sex = "男";
                } else {
                    // Common.log("Woman");
//                    sex = "0";
                    item.sex = "女";
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
        if ("0".equals(visibleItem.get(7)))
        {
            holder.layout_address.setVisibility(View.GONE);
        } else
        {
            holder.layout_address.setVisibility(View.VISIBLE);
        }
        if ("0".equals(visibleItem.get(8)))
        {
            holder.btn_address.setVisibility(View.GONE);
        } else
        {
            holder.btn_address.setVisibility(View.VISIBLE);
        }
    }

    public void getInfo ()
    {

    }


        /**
         *      输入框监听
         * @param name
         * @param phoneView
         * @param idNumView
         * @param et_telOhter
         * @param et_QQ
         * @param et_address
         * @param item
         */
        private void setEditTextChanged ( EditText name, EditText phoneView, EditText idNumView, EditText et_telOhter, EditText et_QQ, EditText et_address,  final NewApplyUpData item )
        {
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    item.name = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            phoneView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    item.mobile = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            idNumView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    item.credentials = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_telOhter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    item.phone = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_QQ.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    item.qq = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    item.postaladdress = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

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
