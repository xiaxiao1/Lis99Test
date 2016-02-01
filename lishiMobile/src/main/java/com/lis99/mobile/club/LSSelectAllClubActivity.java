package com.lis99.mobile.club;

import android.os.Bundle;
import android.os.Message;
import android.widget.ExpandableListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.LSSelectAllClubAdapter;
import com.lis99.mobile.club.model.LSSelectClub;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;

import java.util.List;

public class LSSelectAllClubActivity extends LSBaseActivity {

    ExpandableListView listView;
    LSSelectAllClubAdapter adapter;

    List<LSSelectClub> djClubs;
    List<LSSelectClub> tsClubs;
    List<LSSelectClub> ppClubs;

    private final static int SHOW_CLUB = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsselect_all_club);

        initViews();
        setTitle("全部俱乐部");

        loadClubList();
    }

    @Override
    protected void initViews() {
        super.initViews();
        listView = (ExpandableListView) findViewById(R.id.listView);
    }



    private void loadClubList(){
        postMessage(ActivityPattern1.POPUP_PROGRESS,
                getString(R.string.sending));
        String url = C.CLUB_ALL_LIST;
        Task task = new Task(null, url, C.HTTP_GET, C.CLUB_ALL_LIST, this);
        publishTask(task, IEvent.IO);
    }

    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals(C.CLUB_ALL_LIST)) {
                        parserClubInfo(result);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void parserClubInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                postMessage(ActivityPattern1.POPUP_TOAST, errCode);
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

            postMessage(SHOW_CLUB);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == SHOW_CLUB) {
            adapter = new LSSelectAllClubAdapter(djClubs, tsClubs, ppClubs, this);
            listView.setAdapter(adapter);
            listView.expandGroup(0);
            listView.expandGroup(1);
            listView.expandGroup(2);
            return true;
        }
        return super.handleMessage(msg);
    }
}
