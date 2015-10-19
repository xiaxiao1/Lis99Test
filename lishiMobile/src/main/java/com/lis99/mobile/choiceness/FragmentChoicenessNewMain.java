package com.lis99.mobile.choiceness;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lis99.mobile.R;
import com.lis99.mobile.newhome.LSFragment;

/**
 * Created by yy on 15/10/16.
 */
public class FragmentChoicenessNewMain extends LSFragment implements View.OnClickListener{


    private ViewPager viewPager;

    private Button tab_choiceness, tab_Dynamic;

    private View head;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        head = View.inflate(getActivity(), R.layout.choiceness_new_main, null );

        viewPager = (ViewPager) head.findViewById(R.id.viewPager);

        tab_choiceness = (Button) head.findViewById(R.id.tab_choiceness);

        tab_Dynamic = (Button) head.findViewById(R.id.tab_Dynamic);



        return head;
    }

    @Override
    public void onClick(View view) {

    }
}
