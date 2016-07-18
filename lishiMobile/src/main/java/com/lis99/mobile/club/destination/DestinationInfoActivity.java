package com.lis99.mobile.club.destination;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.service.ServiceManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationInfoActivity extends LSBaseActivity {

    @Bind(R.id.infoView)
    TextView infoView;

    @Bind(R.id.timeView)
    TextView timeView;
    @Bind(R.id.timeBottomView)
    View timeBottomView;

    @Bind(R.id.equiView)
    TextView equiView;
    @Bind(R.id.equiBottomView)
    View equiBottomView;

    @Bind(R.id.liveView)
    TextView liveView;
    @Bind(R.id.liveBottomView)
    View liveBottomView;

    @Bind(R.id.lineView)
    TextView lineView;
    @Bind(R.id.lineBottomView)
    View lineBottomView;

    TextView selectedTextView;
    View selectedBottomView;

    DestinationInfo info;
    int destID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destID = getIntent().getIntExtra("destID", 0);
        setContentView(R.layout.activity_destination_info);
        initViews();
        ButterKnife.bind(this);

        setTitle("详细信息");

        selectedTextView = timeView;
        selectedBottomView = timeBottomView;

        DestinationService service = ServiceManager.getHttpsApiService(DestinationService.class);
        Call<DestinationInfo> call = service.getDestinationInfo(destID);
        call.enqueue(new Callback<DestinationInfo>() {
            @Override
            public void onResponse(Call<DestinationInfo> call, Response<DestinationInfo> response) {
                info = response.body();
                if (info != null) {
                    infoView.setText(info.best_time);
                }
            }

            @Override
            public void onFailure(Call<DestinationInfo> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.timePanel, R.id.equiPanel, R.id.livePanel, R.id.linePanel})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.timePanel:
                setSelected(timeView, timeBottomView);
                break;
            case R.id.equiPanel:
                setSelected(equiView, equiBottomView);
                break;
            case R.id.livePanel:
                setSelected(liveView, liveBottomView);
                break;
            case R.id.linePanel:
                setSelected(lineView, lineBottomView);
                break;
        }
    }

    private void setSelected(TextView textView, View bottomView) {
        if (textView == selectedTextView) {
            return;
        }
        textView.setTextColor(Color.parseColor("#2bca63"));
        selectedTextView.setTextColor(Color.parseColor("#525252"));
        selectedTextView = textView;

        bottomView.setVisibility(View.VISIBLE);
        selectedBottomView.setVisibility(View.INVISIBLE);
        selectedBottomView = bottomView;

        if (info == null) {
            return;
        }
        if (selectedTextView == timeView) {
            infoView.setText(info.best_time);
        } else if (selectedTextView == equiView) {
            infoView.setText(info.equip);
        } else if (selectedTextView == liveView) {
            infoView.setText(info.living);
        } else if (selectedTextView == lineView) {
            infoView.setText(info.line);
        }
    }

}
