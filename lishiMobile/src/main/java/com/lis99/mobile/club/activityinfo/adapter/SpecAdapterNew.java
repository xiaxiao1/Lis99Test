package com.lis99.mobile.club.activityinfo.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.activityinfo.SericeCalendarActivity;
import com.lis99.mobile.club.model.SpecInfoListModel;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/8/26.
 *  选择规格列表
 */
public class SpecAdapterNew extends MyBaseAdapter {

    private int select;

    public SpecAdapterNew(Activity c, List listItem) {
        super(c, listItem);
        select = 0;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext,R.layout.series_spec_adapter_new, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag(), i);
        return view;
    }

    private void initializeViews(Object object, final ViewHolder holder, int i) {
        //TODO implement
        final SpecInfoListModel.GuigelistEntity item = (SpecInfoListModel.GuigelistEntity) object;

        holder.name.setText(item.name);
        holder.tvNum.setText(""+item.selectNum);
        holder.btnAdd.setBackgroundResource(R.drawable.series_add);
        if ( item.selectNum == 0 )
        {
            holder.btnRemove.setBackgroundResource(R.drawable.series_reomve_no);
        }
        else
        {
            holder.btnRemove.setBackgroundResource(R.drawable.series_remove);
        }

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.btnRemove.setClickable(true);
                item.selectNum = addTextNum(holder.tvNum);
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.selectNum = removeTextNum(holder.tvNum);
                if ( item.selectNum == 0 )
                {
                    holder.btnRemove.setBackgroundResource(R.drawable.series_reomve_no);
                    ((SericeCalendarActivity)mContext).setBtnClick(false);
                }
                else
                {
                    holder.btnRemove.setBackgroundResource(R.drawable.series_remove);
                }
                refresh();
            }
        });


    }

    private void refresh ()
    {
        notifyDataSetChanged();
    }

    private int addTextNum ( TextView tv )
    {
        int num = Common.string2int(tv.getText().toString());
        select++;
        num++;
        tv.setText(""+num);
        refresh();
        ((SericeCalendarActivity)mContext).setBtnClick(true);
        return num;
    }

    private int removeTextNum ( TextView tv )
    {
        int num = Common.string2int(tv.getText().toString());
        if ( num == 0 )
        {
            tv.setText(""+num);
            return 0;
        }
            select--;
            num--;
            tv.setText(""+num);
        return num;
    }

    public int getSelectNum ()
    {
        return select;
    }

    protected class ViewHolder {
        private Button btnAdd;
        private TextView tvNum;
        private Button btnRemove;
        private TextView name;

        public ViewHolder(View view) {
            btnAdd = (Button) view.findViewById(R.id.btn_add);
            tvNum = (TextView) view.findViewById(R.id.tv_num);
            btnRemove = (Button) view.findViewById(R.id.btn_remove);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

}
