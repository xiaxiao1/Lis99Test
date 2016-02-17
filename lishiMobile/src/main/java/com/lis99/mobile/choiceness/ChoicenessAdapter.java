package com.lis99.mobile.choiceness;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ChoicenessModel.Omnibuslist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ChoicenessAdapter extends BaseAdapter
{
	private Activity a;
	//活动
	private final int ACTIVE = 0;
	//话题
//	private final int TOPIC = 1;
	//专题
	private final int SUBJECT = 1;
	//URL跳转
//	private final int URLITEM = 3;
	//新的话题样式
//	private final int IMGTOPIC = 4;
//只有一张图片
	private final int IMGACTIVE = 2;
	//标签列表
//	private final int TAG = 6;

	//	private final int CLUB = 0;
	//	总数
	private final int conut = 3;


	public ArrayList<Omnibuslist> list;
	
	private DisplayImageOptions optionshead, optionsclub, optionsBg;

	private Animation animation;
	
	public ChoicenessAdapter ( Activity a, ArrayList<Omnibuslist> omnibuslist )
	{
		this.a = a;
		this.list = omnibuslist;
		optionshead = ImageUtil.getclub_topic_headImageOptions();
		optionsclub = ImageUtil.getImageOptionClubIcon();
		optionsBg = ImageUtil.getDynamicImageOptions();
		animation = AnimationUtils.loadAnimation(a, R.anim.like_anim_rotate);
		
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
		if ( list == null || list.size() == 0 || arg0 < 0 ) return null;
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
			case 1:
			case 2:
			case 9:
				num = ACTIVE;
				break;
			case 5:
			case 6:
			case 8:
			case 3:
			case 7:
				num = IMGACTIVE;
				break;
			case 4:
				num = SUBJECT;
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
				return getClub(position, arg1, num);
			case SUBJECT:
				return getSubject(position, arg1, num);
			case IMGACTIVE:
				return getSubjectTopic(position, arg1, num);
		}
		
		return arg1;
	}
	
	private View getClub ( int position, View view, int num )
	{
		ClubHolder holder = null;
		if ( view == null )
		{
//			view = View.inflate(a, R.layout.choiceness_item_club, null);
			view = View.inflate(a, R.layout.choiceness_new_topic, null);
			holder = new ClubHolder();
			holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
			holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_like = (TextView) view.findViewById(R.id.tv_like);
			holder.tv_reply = (TextView) view.findViewById(R.id.tv_reply);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			
			holder.layout_like =  view.findViewById(R.id.layout_like);
			holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);

			holder.iv_like = (ImageView) view.findViewById(R.id.iv_like);

			holder.btn_concern = (Button) view.findViewById(R.id.btn_concern);

			holder.layout = view.findViewById(R.id.layout);

			holder.layout.setVisibility(View.GONE);
			
			view.setTag(holder);
		}
		else
		{
			holder = (ClubHolder) view.getTag();
		}
		
		final Omnibuslist item = (Omnibuslist) getItem(position);
		if ( item == null ) return view;
		


		if ( item.is_vip == 1 )
		{
			holder.vipStar.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.vipStar.setVisibility(View.GONE);
		}
		
		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));

		ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, optionshead);

		
		holder.tv_title.setText(item.title);
		holder.tv_like.setText(""+item.likeNum);
		holder.tv_name.setText(item.nickname);
		holder.tv_reply.setText(item.reply_num + "则回复" );

		if ( item.LikeStatus == 1 )
		{
			holder.iv_like.setImageResource(R.drawable.like_button_press);
		}
		else
		{
			holder.iv_like.setImageResource(R.drawable.like_button);
		}

		if ( item.attenStatus == 1 )
		{
			holder.btn_concern.setVisibility(View.GONE);
		}
		else
		{
			holder.btn_concern.setVisibility(View.VISIBLE);
		}

		final ClubHolder finalHolder = holder;

		holder.roundedImageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Common.goUserHomeActivit(a, item.user_id);
			}
		});

		holder.btn_concern.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				int id = Common.string2int(item.user_id);

				if ( id == -1 ) return;

				LSRequestManager.getInstance().getFriendsAddAttention(id, new CallBack() {
					@Override
					public void handler(MyTask mTask) {
						finalHolder.btn_concern.setVisibility(View.GONE);
						item.attenStatus = 1;
					}
				});

			}
		});


		holder.layout_like.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ( item.LikeStatus == 1 ) return;

				if ( !Common.isLogin(LSBaseActivity.activity))
				{
					return;
				}

				item.LikeStatus = 1;

				item.likeNum += 1;

				finalHolder.tv_like.setText(""+item.likeNum);

				finalHolder.iv_like.setImageResource(R.drawable.like_button_press);

				finalHolder.iv_like.startAnimation(animation);

				LSRequestManager.getInstance().clubTopicLike(item.topic_id,null);
			}
		});
		
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
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
			holder.choiceness_subject_forground = (RoundedImageView) view.findViewById(R.id.choiceness_subject_forground);

			view.setTag(holder);
		}
		else
		{
			holder = (SubjectHolder) view.getTag();
		}
		
		Omnibuslist item = (Omnibuslist) getItem(position);
		if ( item == null ) return view;

		if ( !TextUtils.isEmpty(item.title) )
		{
			holder.choiceness_subject_forground.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.choiceness_subject_forground.setVisibility(View.GONE);
		}
		
		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
		
		holder.tv_title.setText(item.title);
		holder.tv_info.setText(item.subhead);
		
		
		return view;
	}

	//是否添加渐变层
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
			holder.choiceness_subject_forground = (RoundedImageView) view.findViewById(R.id.choiceness_subject_forground);
			view.setTag(holder);
		}
		else
		{
			holder = (SubjectHolder) view.getTag();
		}

		Omnibuslist item = (Omnibuslist) getItem(position);
		if ( item == null ) return view;

		if ( !TextUtils.isEmpty(item.title) )
		{
			holder.choiceness_subject_forground.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.choiceness_subject_forground.setVisibility(View.GONE);
		}

		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));

		holder.tv_title.setText(item.title);
		holder.tv_info.setText(item.subhead);

		return view;
	}

	class ClubHolder
	{
//		RoundedImageView iv_bg, iv_icon_club, iv_icon_head;
//		TextView tv_title, tv_info, tv_location, tv_reply, tv_name;
//		RelativeLayout layout_head;
//		ImageView vipStar;
//		ImageView iv_load;
		RoundedImageView roundedImageView1;
		ImageView vipStar, iv_bg, iv_like, iv_load;
		TextView tv_name, tv_like, tv_title, tv_reply;
		Button btn_concern;
		View layout_like, layout;


	}
	
	class SubjectHolder
	{
		ImageView iv_load;
		RoundedImageView iv_bg, choiceness_subject_forground;
		TextView tv_title, tv_info;
		ImageView iv_subject;
	}

}
