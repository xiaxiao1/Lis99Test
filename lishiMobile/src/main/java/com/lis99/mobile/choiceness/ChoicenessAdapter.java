package com.lis99.mobile.choiceness;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ChoicenessModel.Omnibuslist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ChoicenessAdapter extends BaseAdapter
{
	private Activity a;
	//活动
	private final int ACTIVE = 0;
	//话题
	private final int TOPIC = 1;
	//专题
	private final int SUBJECT = 2;
	//URL跳转
	private final int URLITEM = 3;
	//新的话题样式
	private final int IMGTOPIC = 4;

	private final int IMGACTIVE = 5;

	//	private final int CLUB = 0;
	//	总数
	private final int conut = 6;


	public ArrayList<Omnibuslist> list;
	
	private DisplayImageOptions optionshead, optionsclub, optionsBg;
	
	public ChoicenessAdapter ( Activity a, ArrayList<Omnibuslist> omnibuslist )
	{
		this.a = a;
		this.list = omnibuslist;
		optionshead = ImageUtil.getclub_topic_headImageOptions();
		optionsclub = ImageUtil.getImageOptionClubIcon();
		optionsBg = ImageUtil.getDefultImageOptions();
		
	}
	
	public void setList ( ArrayList<Omnibuslist> omnibuslist )
	{
		this.list.addAll(omnibuslist);
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		if ( list == null || list.size() == 0 ) return 0;
		return list.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		if ( list == null || list.size() == 0 ) return null;
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return arg0;
	}

	
	
	@Override
	public int getItemViewType(int position)
	{
		// TODO Auto-generated method stub
		int state = ((Omnibuslist)getItem(position)).type;
		int num = 0;
		switch ( state )
		{
			case 0:
				num = ACTIVE;
				break;
			case 1:
				num = ACTIVE;
				break;
			case 2:
				num = TOPIC;
				break;
			case 3:
				num = URLITEM;
				break;
			case 4:
				num = SUBJECT;
				break;
			case 5:
				num = IMGACTIVE;
				break;
			case 6:
				num = IMGTOPIC;
				break;
		}
		
		return num;
	}
	

	@Override
	public int getViewTypeCount()
	{
		// TODO Auto-generated method stub
		return conut;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
		int num = getItemViewType(position);
		switch ( num )
		{
			case ACTIVE:
			case TOPIC:
				return getClub(position, arg1, num);
			case SUBJECT:
			case URLITEM:
				return getSubject(position, arg1, num);
			case IMGACTIVE:
			case IMGTOPIC:
				return getSubjectTopic(position, arg1, num);
		}
		
		return arg1;
	}
	
	private View getClub ( int position, View view, int num )
	{
		ClubHolder holder = null;
		if ( view == null )
		{
			view = View.inflate(a, R.layout.choiceness_item_club, null);
			holder = new ClubHolder();
			holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
			holder.iv_icon_club = (RoundedImageView) view.findViewById(R.id.iv_icon_club);
			holder.iv_icon_head = (RoundedImageView) view.findViewById(R.id.iv_icon_head);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
			holder.tv_location = (TextView) view.findViewById(R.id.tv_location);
			holder.tv_reply = (TextView) view.findViewById(R.id.tv_reply);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			
			holder.layout_head = (RelativeLayout) view.findViewById(R.id.layout_head);
			holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
			
			view.setTag(holder);
		}
		else
		{
			holder = (ClubHolder) view.getTag();
		}
		
		Omnibuslist item = (Omnibuslist) getItem(position);
		if ( item == null ) return view;
		
		if ( num == ACTIVE )
		{
			holder.iv_icon_club.setVisibility(View.VISIBLE);
			holder.layout_head.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(item.club_logo, holder.iv_icon_club, optionsclub);
			holder.tv_name.setText("by "+item.club_title + " ");
		}
		else
		{
			holder.iv_icon_club.setVisibility(View.GONE);
			holder.layout_head.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(item.headicon, holder.iv_icon_head, optionshead);
			holder.tv_name.setText("by "+item.nickname + " ");
			if ( item.is_vip == 1 )
			{
				holder.vipStar.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.vipStar.setVisibility(View.GONE);
			}
			
		}
		
		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
		
		
		holder.tv_title.setText(item.title);
		holder.tv_info.setText(item.subhead);
		holder.tv_location.setText(item.position);
		holder.tv_reply.setText(item.reply_num + "个回复 " );
		
		
		return view;
	}
	
	private View getSubject ( int position, View view, int num )
	{
		SubjectHolder holder = null;
		if ( view == null )
		{
			holder = new SubjectHolder();
			view = View.inflate(a, R.layout.choiceness_item_subject, null);
			
			holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
			holder.iv_subject = (ImageView) view.findViewById(R.id.iv_subject);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
			view.setTag(holder);
		}
		else
		{
			holder = (SubjectHolder) view.getTag();
		}
		
		Omnibuslist item = (Omnibuslist) getItem(position);
		if ( item == null ) return view;
		
		if ( num == URLITEM )
		{
			holder.iv_subject.setVisibility(View.GONE);
		}
		else
		{
			holder.iv_subject.setVisibility(View.VISIBLE);
		}
		
		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
		
		holder.tv_title.setText(item.title);
		holder.tv_info.setText(item.subhead);
		
		
		return view;
	}

	private View getSubjectTopic ( int position, View view, int num )
	{
		SubjectHolder holder = null;
		if ( view == null )
		{
			holder = new SubjectHolder();
			view = View.inflate(a, R.layout.choiceness_item_subject, null);

			holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
			holder.iv_subject = (ImageView) view.findViewById(R.id.iv_subject);
			view.setTag(holder);
		}
		else
		{
			holder = (SubjectHolder) view.getTag();
		}
		holder.iv_subject.setVisibility(View.GONE);
		Omnibuslist item = (Omnibuslist) getItem(position);
		if ( item == null ) return view;

		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));

		holder.tv_title.setText(item.title);
		holder.tv_info.setText(item.subhead);

		return view;
	}

	class ClubHolder
	{
		RoundedImageView iv_bg, iv_icon_club, iv_icon_head;
		TextView tv_title, tv_info, tv_location, tv_reply, tv_name;
		RelativeLayout layout_head;
		ImageView vipStar;
		ImageView iv_load;
	}
	
	class SubjectHolder
	{
		ImageView iv_load;
		RoundedImageView iv_bg;
		TextView tv_title, tv_info;
		ImageView iv_subject;
	}

}
