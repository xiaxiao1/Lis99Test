package com.lis99.mobile.util;

import android.view.View;
import android.widget.ImageView;

import com.lis99.mobile.R;

/**
 * Created by yy on 15/7/28.
 *  启动页配置
 */
public class StartLogoOption {

    public static void showStartLogoOption ( ImageView iv_channel )
    {
        if ( "baidu".equals(DeviceInfo.CHANNELVERSION))
        {
            iv_channel.setVisibility(View.VISIBLE);
            iv_channel.setImageResource(R.drawable.star_page_channel_baidu);
        }
        else if ( "pp".equals(DeviceInfo.CHANNELVERSION))
        {
            iv_channel.setVisibility(View.VISIBLE);
            iv_channel.setImageResource(R.drawable.star_logo_pp);
        }
        else
        {
            iv_channel.setVisibility(View.INVISIBLE);
        }

    }

}
