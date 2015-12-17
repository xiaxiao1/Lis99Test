package com.lis99.mobile.club;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.CustomCursorLoader;
import com.lis99.mobile.club.adapter.ListAdapter;
import com.lis99.mobile.club.adapter.onCheckboxChange;
import com.lis99.mobile.club.widget.LoadFileImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LSImagePicker extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, ListView.OnItemClickListener, onCheckboxChange {
    private GridView gridView;
    private SelectPictureAdapter adapter;
    private HashMap<Integer, ArrayList<String>> items = new HashMap<Integer, ArrayList<String>>();
    private HashMap<Integer, String> clonumsName = new HashMap<Integer, String>();
    private WindowManager manager;
    ListView listView;
    ListAdapter listAdapter = null;
    private int bucketId = -1;
    private ArrayList<String> selectedUri = new ArrayList<String>();
    private HashSet<String> totalUri = new HashSet<String>();
    private Integer selectedId;
    private int PREVIEW = 1;
    public static int MAX_COUNT = 1;
    private int selectSize = 0;

    Button previewButton;
    Button okButton;

    Button maskButton;

    boolean isReply;


    TextView allAlbumView;

    ImageView iv_title_bg;
    protected ImageView titleLeftImage;

    TextView title;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsimage_picker);

        isReply = getIntent().getBooleanExtra("isReply", false);

        okButton = (Button) findViewById(R.id.okButton);
        previewButton = (Button) findViewById(R.id.preview);

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ListAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

        allAlbumView = (TextView) findViewById(R.id.all);

        maskButton = (Button) findViewById(R.id.maskView);

        titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        View titleLeft = findViewById(R.id.titleLeft);
        if (titleLeft != null) {
            titleLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftAction();
                }
            });
        }

        if (titleLeftImage != null) {
            titleLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftAction();
                }
            });
        }



        iv_title_bg = (ImageView) findViewById(R.id.iv_title_bg);
        if (this.title == null)
            this.title = (TextView) findViewById(R.id.title);



        if (getIntent().hasExtra("selectedSize")) {
            selectSize = getIntent().getIntExtra("selectedSize", 0);
        }

        findViewById(R.id.all).setOnClickListener(this);
        findViewById(R.id.preview).setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridview);
        getSupportLoaderManager().initLoader(0, null, this);

        adapter = new SelectPictureAdapter(this, this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = (String) adapter.getItem(position);
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.addAll(adapter.getData());
                Intent intent = new Intent(LSImagePicker.this, LSImagePickerPreviewActivity.class);
                intent.putExtra("totalUri", arrayList);
                intent.putExtra("selectUri", selectedUri);
                intent.putExtra("position", position);

                intent.putExtra("selectedSize", selectSize);
                intent.putExtra("isReply", isReply);
                startActivityForResult(intent, PREVIEW);

            }
        });




    }




    protected void leftAction() {
        finish();
    }

    protected TextView setTitle(String title) {
        if (this.title == null) {
            this.title = (TextView) findViewById(R.id.title);
        }
        if (this.title != null) {
            this.title.setText(title);
        }
        return this.title;
    }



    //设置左边按钮图片
    protected void setLeftView(int res) {
//        if (titleLeftImage == null)
//            titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        if (titleLeftImage != null) {
            titleLeftImage.setImageResource(res);
        }
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String order = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        return new CustomCursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME}, null, null, order);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        items.clear();
        if (data != null) {
            while (data.moveToNext()) {
                Integer bucketId = data.getInt(data.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                String name = data.getString(data.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                clonumsName.put(bucketId, name);
                ArrayList<String> arrayList;
                if (!items.containsKey(bucketId)) {
                    arrayList = new ArrayList<String>();
                    items.put(bucketId, arrayList);
                } else {
                    arrayList = items.get(bucketId);
                }
                String pictureUrl = data.getString(data.getColumnIndex(MediaStore.Images.Media.DATA));
                arrayList.add(pictureUrl);
            }
            ArrayList<String> gridItems = new ArrayList<String>();
            for (Integer key : items.keySet()) {
                gridItems.addAll(items.get(key));
            }
            adapter.setData(gridItems, selectedUri);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setData(null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PREVIEW && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> set = (ArrayList<String>) data.getSerializableExtra("uris");
                selectedUri.clear();
                selectedUri.addAll(set);
                adapter.setSelectedUri(selectedUri);
                resetButton();
            }
        }
    }

    private void showAlbum(){
        allAlbumView.setEnabled(false);
        maskButton.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        maskButton.startAnimation(animation);
        listView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                allAlbumView.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void hideAlbum(){
        allAlbumView.setEnabled(false);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        maskButton.startAnimation(animation);
        listView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                maskButton.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                allAlbumView.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                if (listView.getVisibility() == View.VISIBLE) {
                    hideAlbum();
                    return;
                }

                ArrayList<ListAdapter.ListItem> listItems = new ArrayList<ListAdapter.ListItem>();
                int index = 0;
                for (Integer key : items.keySet()) {
                    System.out.println("test___" + key);
                    if (index == 0) {
                        ListAdapter.ListItem item = new ListAdapter.ListItem();
                        item.pictureUrl = items.get(key).get(0);
                        item.name = "全部照片";
                        item.id = -1;
                        listItems.add(item);
                    }
                    ListAdapter.ListItem item = new ListAdapter.ListItem();
                    item.pictureUrl = items.get(key).get(0);
                    item.name = clonumsName.get(key);
                    item.id = key;
                    listItems.add(item);
                    index++;
                }
                listAdapter.setData(listItems);
                listAdapter.selectID = bucketId;
                showAlbum();
                break;
            case R.id.preview:
                if (selectedUri != null && selectedUri.size() > 0) {
                    Intent intent = new Intent(LSImagePicker.this, LSImagePickerPreviewActivity.class);
                    intent.putExtra("totalUri", selectedUri);
                    intent.putExtra("selectUri", selectedUri);
                    intent.putExtra("position", 0);
                    intent.putExtra("isReply", isReply);
                    startActivityForResult(intent, PREVIEW);
                }
                break;
            case R.id.maskView:
                hideAlbum();
                break;
            case R.id.okButton:
                if (selectedUri != null && selectedUri.size() > 0) {

                    Intent intent = null;
                    if (isReply) {
                        intent = new Intent(this, LSClubTopicReplyActivity.class);
                    } else {
                        intent = new Intent(this, LSClubPublish2Activity.class);
                    }
                    intent.putExtra("uris", selectedUri);
                    startActivity(intent);
                    finish();
                }
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = ((ListAdapter.ListItem) listAdapter.getItem(position)).name;
        if (TextUtils.equals(name, "全部照片")) {
            if (selectedId != null) {
                ArrayList<String> gridItems = new ArrayList<String>();
                for (Integer key : items.keySet()) {
                    gridItems.addAll(items.get(key));
                    adapter.setData(gridItems, selectedUri);
                }
            }
            bucketId = -1;
            allAlbumView.setText("全部照片");
            selectedId = null;
           // getWindowManager().removeViewImmediate(listView);
        } else {
            ListAdapter.ListItem listItem = (ListAdapter.ListItem) listAdapter.getItem(position);
            bucketId = listItem.id;
            selectedId = bucketId;
            allAlbumView.setText(listItem.name);
            ArrayList<String> listItems = items.get(bucketId);
            adapter.setData(listItems, selectedUri);

           // getWindowManager().removeViewImmediate(listView);
        }

        hideAlbum();
    }

    private void resetButton(){
        if (selectedUri.size() > 0) {
            okButton.setEnabled(true);
            okButton.setTextColor(Color.WHITE);
            okButton.setText("确定" + selectedUri.size() + "/" + MAX_COUNT);

            previewButton.setTextColor(Color.rgb(0x49, 0xa3, 0x4b));
            previewButton.setText("预览(" + selectedUri.size() + ")");
        } else {
            okButton.setEnabled(false);
            okButton.setTextColor(Color.WHITE);
            okButton.setText("");

            previewButton.setTextColor(Color.rgb(0x99, 0x99, 0x99));
            previewButton.setText("预览");
        }
    }

    @Override
    public void onCheckboxChange(String url) {
        if (selectedUri.contains(url)) {
            selectedUri.remove(url);
        } else {
            if (selectedUri.size() + selectSize >= MAX_COUNT) {
                adapter.setSelectedUri(selectedUri);
                Toast.makeText(this, "你最多能选择" + MAX_COUNT + "张照片", Toast.LENGTH_LONG).show();
                return;
            }

            selectedUri.add(url);

        }

        resetButton();


        adapter.setSelectedUri(selectedUri);


       // titleBar.setRightText("(确定" + selectedUri.size() + ")");

    }

    public class SelectPictureAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;
        private ArrayList<String> items = new ArrayList<String>();
        private HashSet<String> selectedUri = new HashSet<String>();
        private LayoutInflater layoutInflater;
        private onCheckboxChange checkboxChange;
        private int width;

        public SelectPictureAdapter(Context context, onCheckboxChange checkboxChange) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.checkboxChange = checkboxChange;
            width = context.getResources().getDimensionPixelOffset(R.dimen.width);
        }

        public void setData(ArrayList<String> data, ArrayList<String> selectUri) {
            items.clear();
            selectedUri.clear();
            if (data != null) {
                items.addAll(data);
            }
            if (selectUri != null) {
                selectedUri.addAll(selectUri);
            }
            notifyDataSetChanged();
        }

        public void setSelectedUri(ArrayList<String> data) {
            selectedUri.clear();

            if (data != null) {
                selectedUri.addAll(data);
            }
            notifyDataSetChanged();
        }

        public ArrayList<String> getData() {
            return items;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = layoutInflater.inflate(R.layout.item_friend_select_picture, null);
                holder.imageView = (LoadFileImageView) convertView.findViewById(R.id.image);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.select);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            String pictureUrl = items.get(position);
            holder.imageView.loadUrl(pictureUrl, width, width);
            holder.checkBox.setOnClickListener(this);
            holder.checkBox.setTag(pictureUrl);
            if (selectedUri.contains(pictureUrl)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.select) {
                String url = (String) v.getTag();
                if (checkboxChange != null) {
                    checkboxChange.onCheckboxChange(url);
                }
            }

        }

        private class Holder {
            LoadFileImageView imageView;
            CheckBox checkBox;
        }
    }


}
