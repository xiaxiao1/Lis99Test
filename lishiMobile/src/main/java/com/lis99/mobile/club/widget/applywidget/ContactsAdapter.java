package com.lis99.mobile.club.widget.applywidget;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/8/5.
 */
public class ContactsAdapter extends MyBaseAdapter {

    public ContactsAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.contacts_list_item, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement

        final NewApplyUpData info = (NewApplyUpData) object;

        holder.tvDefault.setVisibility(View.GONE);

        holder.tvName.setText(info.name);
        holder.tvSex.setText(info.sex);
        holder.tvPhone.setText(info.mobile);
        holder.idNumView.setText(info.credentials);
        if ( "1".equals(info.is_default))
        {
            holder.tvDefault.setVisibility(View.VISIBLE);
        }

        holder.ivEdting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactsEdtingActivity.class);
                intent.putExtra("INFO", info);
                mContext.startActivityForResult(intent, ContactsActivity.ADD_EDIT_CONTACTS);
            }
        });


    }

    protected class ViewHolder {
        private TextView tvName;
        private TextView tvDefault;
        private ImageView ivEdting;
        private TextView tvSex;
        private TextView tvPhone;
        private TextView idNumView;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDefault = (TextView) view.findViewById(R.id.tv_default);
            ivEdting = (ImageView) view.findViewById(R.id.iv_edting);
            tvSex = (TextView) view.findViewById(R.id.tv_sex);
            tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            idNumView = (TextView) view.findViewById(R.id.idNumView);
        }
    }

}
