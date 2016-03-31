package com.lis99.mobile.equip;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.webview.MyActivityWebView;

public class ActivityTest extends LSBaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        findViewById(R.id.button).setOnClickListener(this);
    }

    private EditText getEditText3(){
        return (EditText) findViewById(R.id.editText3);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                //TODO implement

                String id = getEditText3().getText().toString();
//// 1214
//                Intent intent = new Intent(this, LSClubTopicActiveOffLine.class);
//                if ( !TextUtils.isEmpty(id))
//                    intent.putExtra("topicID", Common.string2int(id));
//                else
//                    intent.putExtra("topicID", 1214);
//
//                startActivity(intent);



//                Intent intent = new Intent(this, LSClubNewTopicListMain.class);
//
//                String id = getEditText3().getText().toString();
//
//                if ( !TextUtils.isEmpty(id))
//                    intent.putExtra("TOPICID", id);
//                else
//                    intent.putExtra("TOPICID", "1");
//                startActivity(intent);



                Intent intent = new Intent(activity, MyActivityWebView.class);
                Bundle bundle = new Bundle();
                bundle.putString("URL", TextUtils.isEmpty(id) ? "www.baidu.com" : id);
                bundle.putString("TITLE", "title");
                bundle.putString("IMAGE_URL", "null");
                bundle.putInt("TOPIC_ID", 0);
                intent.putExtras(bundle);
                startActivity(intent);



                break;
        }
    }
}
