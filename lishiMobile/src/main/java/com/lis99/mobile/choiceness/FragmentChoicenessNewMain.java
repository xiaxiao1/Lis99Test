package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubPublish2Activity;
import com.lis99.mobile.club.adapter.LSClubFragmentAdapter;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.sysmassage.SysMassageActivity;
import com.lis99.mobile.util.RedDotUtil;
import com.lis99.mobile.util.ScrollTopUtil;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/16.
 */
public class FragmentChoicenessNewMain extends LSFragment implements View.OnClickListener, ScrollTopUtil.ToTop {


    private ViewPager viewPager;

    private Button tab_choiceness, tab_Dynamic, tab_column;

    private View v;

    private ArrayList<Fragment> fList = new ArrayList<Fragment>();

    private LSClubFragmentAdapter adapter;

    private FragmentChoicenessList choicenessList;

    private FragmentDinamicList dinamicList;

    private LSSelectColumnFragment columnFragment;
    private Fragment currentFragment;

    private View view_choiceness, view_dynamic, view_column, dynamic_line;

    private View titleLeft, titleRight;

    private TextView tvMassage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = View.inflate(getActivity(), R.layout.choiceness_new_main, null);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        tab_choiceness = (Button) v.findViewById(R.id.tab_choiceness);

        tab_Dynamic = (Button) v.findViewById(R.id.tab_Dynamic);
        tab_column = (Button) v.findViewById(R.id.tab_column);

        view_choiceness = v.findViewById(R.id.view_choiceness);

        view_dynamic = v.findViewById(R.id.view_dynamic);
        view_column = v.findViewById(R.id.view_column);

        dynamic_line = v.findViewById(R.id.dynamic_line);

        tvMassage = (TextView)v.findViewById(R.id.tv_massage);

        titleRight = v.findViewById(R.id.titleRight);
        titleLeft = v.findViewById(R.id.titleLeft);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);

        tab_choiceness.setOnClickListener(this);
        tab_Dynamic.setOnClickListener(this);
        tab_column.setOnClickListener(this);

        choicenessList = new FragmentChoicenessList();

        dinamicList = new FragmentDinamicList();

        columnFragment = new LSSelectColumnFragment();
        fList.add(choicenessList);
        fList.add(dinamicList);
        fList.add(columnFragment);

        adapter = new LSClubFragmentAdapter(getFragmentManager(), fList);

        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);

//        choicenessList.init();
        selectChoiceness();

        viewPager.setOnPageChangeListener(new MyPagerListener());

        redDotUtil.setRedText(tvMassage);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_choiceness:

                selectChoiceness();

                viewPager.setCurrentItem(0, true);

                break;
            case R.id.tab_Dynamic:

                selectDynamic();

                viewPager.setCurrentItem(1, true);

                break;
            case R.id.tab_column:

                selectColumn();

                viewPager.setCurrentItem(2, true);

                break;
            case R.id.titleLeft:
                RedDotUtil.getInstance().InVisibleDot();
                startActivity(new Intent(getActivity(), SysMassageActivity.class));

                break;
            case R.id.titleRight:

                Intent intent = new Intent(getActivity(), LSClubPublish2Activity.class);
			    startActivity(intent);

                break;

        }
    }


    private void selectChoiceness() {
        tab_choiceness.setTextColor(getResources().getColor(R.color.text_color_green));
        tab_column.setTextColor(getResources().getColor(R.color.color_nine));
        tab_Dynamic.setTextColor(getResources().getColor(R.color.color_nine));

        view_choiceness.setVisibility(View.VISIBLE);
        view_column.setVisibility(View.INVISIBLE);
        view_dynamic.setVisibility(View.INVISIBLE);

        dynamic_line.setVisibility(View.GONE);

        choicenessList.init();

        currentFragment = choicenessList;

    }

    private void selectDynamic()
    {
        tab_choiceness.setTextColor(getResources().getColor(R.color.color_nine));
        tab_column.setTextColor(getResources().getColor(R.color.color_nine));
        tab_Dynamic.setTextColor(getResources().getColor(R.color.text_color_green));

        view_dynamic.setVisibility(View.VISIBLE);
        view_column.setVisibility(View.INVISIBLE);
        view_choiceness.setVisibility(View.INVISIBLE);

        dynamic_line.setVisibility(View.VISIBLE);

        dinamicList.init();

        currentFragment = dinamicList;

    }

    private void selectColumn()
    {
        tab_choiceness.setTextColor(getResources().getColor(R.color.color_nine));
        tab_column.setTextColor(getResources().getColor(R.color.text_color_green));
        tab_Dynamic.setTextColor(getResources().getColor(R.color.color_nine));

        view_dynamic.setVisibility(View.INVISIBLE);
        view_column.setVisibility(View.VISIBLE);
        view_choiceness.setVisibility(View.INVISIBLE);

        dynamic_line.setVisibility(View.GONE);

        columnFragment.init();

        currentFragment = columnFragment;

    }

    RedDotUtil redDotUtil = RedDotUtil.getInstance();

    @Override
    public void handler() {

        redDotUtil.getRedDot();

        ScrollTopUtil.getInstance().setToTop(new ScrollTopUtil.ToTop() {
            @Override
            public void handler() {
                if (currentFragment == choicenessList) {
                    choicenessList.scrollToTop();
                } else if (currentFragment == dinamicList) {
                    dinamicList.scrollToTop();
                } else {
                    columnFragment.scrollToTop();
                }
            }
        });

    }

    public class MyPagerListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            if ( arg0 == 0 )
            {
                selectChoiceness();
            }
            else if (arg0 == 1)
            {
                selectDynamic();
            } else {
                selectColumn();
            }
        }
    }

}
