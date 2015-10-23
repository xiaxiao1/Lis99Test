package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.LSClubFragmentAdapter;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.search.SearchActivity;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/16.
 */
public class FragmentChoicenessNewMain extends LSFragment implements View.OnClickListener {


    private ViewPager viewPager;

    private Button tab_choiceness, tab_Dynamic;

    private View v;

    private ArrayList<Fragment> fList = new ArrayList<Fragment>();

    private LSClubFragmentAdapter adapter;

    private FragmentChoicenessList choicenessList;

    private FragmentDinamicList dinamicList;

    private View view_choiceness, view_dynamic, include_search, dynamic_line;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = View.inflate(getActivity(), R.layout.choiceness_new_main, null);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        tab_choiceness = (Button) v.findViewById(R.id.tab_choiceness);

        tab_Dynamic = (Button) v.findViewById(R.id.tab_Dynamic);

        view_choiceness = v.findViewById(R.id.view_choiceness);

        view_dynamic = v.findViewById(R.id.view_dynamic);

        include_search = v.findViewById(R.id.include_search);

        dynamic_line = v.findViewById(R.id.dynamic_line);

        include_search.setOnClickListener(this);
        tab_choiceness.setOnClickListener(this);
        tab_Dynamic.setOnClickListener(this);

        choicenessList = new FragmentChoicenessList();

        dinamicList = new FragmentDinamicList();

        fList.add(choicenessList);
        fList.add(dinamicList);

        adapter = new LSClubFragmentAdapter(getFragmentManager(), fList);

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);

//        choicenessList.init();
        selectChoiceness();

        viewPager.setOnPageChangeListener( new MyPagerListener() );


        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.include_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.tab_choiceness:

                selectChoiceness();

                viewPager.setCurrentItem(0, true);

                break;
            case R.id.tab_Dynamic:

                selectDynamic();

                viewPager.setCurrentItem(1, true);

                break;

        }
    }


    private void selectChoiceness() {
        tab_choiceness.setTextColor(getResources().getColor(R.color.text_color_blue));
        tab_Dynamic.setTextColor(getResources().getColor(R.color.color_nine));

        view_choiceness.setVisibility(View.VISIBLE);
        view_dynamic.setVisibility(View.INVISIBLE);

        include_search.setVisibility(View.VISIBLE);
        dynamic_line.setVisibility(View.GONE);

        choicenessList.init();

    }

    private void selectDynamic()
    {
        tab_choiceness.setTextColor(getResources().getColor(R.color.color_nine));
        tab_Dynamic.setTextColor(getResources().getColor(R.color.text_color_blue));

        view_dynamic.setVisibility(View.VISIBLE);
        view_choiceness.setVisibility(View.INVISIBLE);

        include_search.setVisibility(View.GONE);
        dynamic_line.setVisibility(View.VISIBLE);

        dinamicList.init();

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
            else
            {
                selectDynamic();
            }
        }
    }

}
