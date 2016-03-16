package com.lis99.mobile.equip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lis99.mobile.R;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;

public class ActivityTest extends Activity implements View.OnClickListener {


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

                Intent intent = new Intent(this, LSClubNewTopicListMain.class);

                String id = getEditText3().getText().toString();

                if ( !TextUtils.isEmpty(id))
                    intent.putExtra("TOPICID", id);
                else
                    intent.putExtra("TOPICID", "1");
                startActivity(intent);

                break;
        }
    }
}
