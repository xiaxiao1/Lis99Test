package com.lis99.mobile.equip;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.dbhelp.DataHelp;

public class ActivityTest extends LSBaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
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



//                Intent intent = new Intent(activity, MyActivityWebView.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("URL", TextUtils.isEmpty(id) ? "www.baidu.com" : id);
//                bundle.putString("TITLE", "title");
//                bundle.putString("IMAGE_URL", "null");
//                bundle.putInt("TOPIC_ID", 0);
//                intent.putExtras(bundle);
//                startActivity(intent);

//                DataHelp.getInstance().search();

                DataHelp.getInstance().cheange();

                break;
            case R.id.button2:

                DataHelp.getInstance().add();

                break;
            case R.id.button3:

//                DataHelp.getInstance().search1();
                if ( DataHelp.getInstance().remove() )
                {
                    Common.toast("OK");
                }


                break;
            case R.id.button4:

                DataHelp.getInstance().search2();

                break;
        }
    }
}
