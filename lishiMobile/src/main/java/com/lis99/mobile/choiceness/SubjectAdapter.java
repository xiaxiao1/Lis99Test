package com.lis99.mobile.choiceness;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.SubjectModel.Topiclist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class SubjectAdapter extends BaseAdapter
{
	
	private Activity a;
	
	public ArrayList<Topiclist> topiclist;
	
	private DisplayImageOptions optionsHead, optionsBg;
	
	public SubjectAdapter ( Activity a, ArrayList<Topiclist> topiclist )
	{
		this.a = a;
		this.topiclist = topiclist;
		
		optionsBg = ImageUtil.getDefultImageOptions();
		optionsHead = ImageUtil.getclub_topic_headImageOptions();
		
	}
	
	public void setList ( ArrayList<Topiclist> topiclist )
	{
		this.topiclist.addAll(topiclist);
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		if ( topiclist == null || topiclist.size() == 0 ) return 0;
		return topiclist.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		if ( topiclist == null || topiclist.size() == 0) return null;
		return topiclist.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
		Holder holder = null;
		if ( arg1 == null )
		{
			arg1 = View.inflate(a, R.layout.choiceness_subject_item, null);
			holder = new Holder();
			holder.iv_bg = (RoundedImageView) arg1.findViewById(R.id.iv_bg);
			holder.iv_icon_head = (RoundedImageView) arg1.findViewById(R.id.iv_icon_head);
			holder.tv_title = (TextView) arg1.findViewById(R.id.tv_title);
			holder.tv_name = (TextView) arg1.findViewById(R.id.tv_name);
			holder.tv_data = (TextView) arg1.findViewById(R.id.tv_data);
			holder.tv_reply = (TextView) arg1.findViewById(R.id.tv_reply);
			holder.vipStar = (ImageView) arg1.findViewById(R.id.vipStar);
			holder.iv_load = (ImageView) arg1.findViewById(R.id.iv_load);
			
			arg1.setTag(holder);
		}
		else
		{
			holder = (Holder) arg1.getTag();
		}
		
		Topiclist item = (Topiclist) getItem(arg0);
		if ( item == null ) return arg1;
		
		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
		ImageLoader.getInstance().displayImage(item.headicon, holder.iv_icon_head, optionsHead);
		
		if ( item.is_vip == 1 )
		{
			holder.vipStar.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.vipStar.setVisibility(View.GONE);
		}
		
		holder.tv_title.setText(item.title);
		holder.tv_name.setText(item.nickname);
		holder.tv_data.setText(item.createdate);
		holder.tv_reply.setText(item.reply_num + "则回复");
		
		
		return arg1;
	}

	class Holder
	{
		RoundedImageView iv_bg, iv_icon_head;
		TextView tv_title, tv_name, tv_data, tv_reply;
		ImageView vipStar, iv_load;
	}
	
}
