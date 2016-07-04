package com.lis99.mobile.choiceness;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.LSSelectAllClubAdapter;
import com.lis99.mobile.club.model.LSSelectClub;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;

import java.util.List;

/**
 * Created by yy on 16/6/22.
 */
public class FragmentAllClub extends Fragment{

    ExpandableListView listView;
    LSSelectAllClubAdapter adapter;

    List<LSSelectClub> djClubs;
    List<LSSelectClub> tsClubs;
    List<LSSelectClub> ppClubs;

    public FragmentAllClub() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.activity_lsselect_all_club, null);

        view.findViewById(R.id.titlehead).setVisibility(View.GONE);

        listView = (ExpandableListView) view.findViewById(R.id.listView);
        loadClubList();

        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    public void init ()
    {
        if ( djClubs == null )
        loadClubList();
    }


    public void scrollToTop ()
    {

    }

    private void loadClubList(){

        String url = C.CLUB_ALL_LIST;

        MyRequestManager.getInstance().requestGetNoModel(url, null, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                String result = mTask.getresult();
                if (!TextUtils.isEmpty(result))
                {
                    parserClubInfo(result);
                }
            }
        });


    }

    private void parserClubInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                Common.toast(errCode);
                return;
            }
            JsonNode data = root.get("data").get("djclublist");
            djClubs = LSFragment.mapper.readValue(data.traverse(), new TypeReference<List<LSSelectClub>>() {
            });

            data = root.get("data").get("tsclublist");
            tsClubs = LSFragment.mapper.readValue(data.traverse(), new TypeReference<List<LSSelectClub>>() {
            });

            data = root.get("data").get("ppclublist");
            ppClubs = LSFragment.mapper.readValue(data.traverse(), new TypeReference<List<LSSelectClub>>() {
            });

            adapter = new LSSelectAllClubAdapter(djClubs, tsClubs, ppClubs, getActivity());
            listView.setAdapter(adapter);
            listView.expandGroup(0);
            listView.expandGroup(1);
            listView.expandGroup(2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
