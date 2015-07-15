package com.lis99.mobile.entry.mypic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShowImageActivity extends ActivityPattern1{
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;
	private Button finish;
	private Bundle bundle;
	int id, album_id;

	private static final int SHOW_EEROR = 513;
	private static final int SHOW_XXXX_INFO = 514;
	private static final int REVERSE_DATA = 515;
	private String status;
	private String data;
	//private List<String> llll = new ArrayList<String>();
      private String[] imageBytes;
      private View camera;

      @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);
          StatusUtil.setStatusBar(this);

		bundle = this.getIntent().getExtras();
		id = bundle.getInt("id",1);
		album_id = bundle.getInt("album_id",1);

		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");
		finish = (Button) findViewById(R.id.finish);
            camera = findViewById(R.id.iv_camera);

            adapter = new ChildAdapter(this, list, mGridView);
            //adapter.setItemListener(this);
		mGridView.setAdapter(adapter);

		finish.setOnClickListener(this);
            camera.setOnClickListener(this);
		//mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "已选中 " + adapter.getSelectItems().size() + "张照片",
				Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}

	@Override
	public void onClick(View arg0) {
            switch (arg0.getId()){
                  case R.id.iv_camera:
                        getCamera();
                        break;
                  case R.id.finish:
                        postMessage(POPUP_PROGRESS,getString(R.string.sending));
                        new Thread() {
                              @Override
                              public void run() {
                            	  List<String> selected = adapter.getSlected();
	                                for (int i = 0; i < selected.size(); i++) {
	                                      uploadFile(selected.get(i));
	                                }
	                                postMessage(REVERSE_DATA);
                              }
                        }.start();
                        break;
            }
	}



      private void uploadFile(String path){
            File file = new File(path);
            String end ="\r\n";
            String twoHyphens ="--";
            String boundary ="*****";
            String urlString = C.ACTIVITY_SHAITUS_URL + id;
            try{
                  URL url =new URL(urlString);
                  HttpURLConnection con=(HttpURLConnection)url.openConnection();
          /* 允许Input、Output，不使用Cache */
                  con.setDoInput(true);
                  con.setDoOutput(true);
                  con.setUseCaches(false);
          /* 设置传送的method=POST */
                  con.setRequestMethod("POST");
          /* setRequestProperty */
                  con.setRequestProperty("Connection", "Keep-Alive");
                  con.setRequestProperty("Charset", "UTF-8");
                  con.setRequestProperty("Content-Type",
                          "multipart/form-data;boundary="+boundary);

                  String user_id = DataManager.getInstance().getUser().getUser_id();

                  StringBuilder sb = new StringBuilder();

          /* 设置DataOutputStream */
                  DataOutputStream ds =
                          new DataOutputStream(con.getOutputStream());

                  sb.append("--" + boundary + "\r\n");
                  sb.append("Content-Disposition: form-data; name=\"album_id\"" + "\r\n");
                  sb.append("\r\n");
                  sb.append(album_id + "\r\n");

                  sb.append("--" + boundary + "\r\n");
                  sb.append("Content-Disposition: form-data; name=\"user_id\"" + "\r\n");
                  sb.append("\r\n");
                  sb.append(user_id + "\r\n");


                  ds.writeBytes(sb.toString());

                  ds.writeBytes(twoHyphens + boundary + end);
                  ds.writeBytes("Content-Disposition: form-data; "+
                          "name=\"fileToUpload\";filename=\""+
                          file.getName() +"\""+ end);
                  ds.writeBytes(end);
          /* 取得文件的FileInputStream */
                  FileInputStream fStream =new FileInputStream(file);
          /* 设置每次写入1024bytes */
                  int bufferSize =1024;
                  byte[] buffer =new byte[bufferSize];
                  int length =-1;
          /* 从文件读取数据至缓冲区 */
                  while((length = fStream.read(buffer)) !=-1)
                  {
            /* 将资料写入DataOutputStream中 */
                        ds.write(buffer, 0, length);
                  }
                  ds.writeBytes(end);
                  ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* close streams */
                  fStream.close();
                  ds.flush();
          /* 取得Response内容 */
                  InputStream is = con.getInputStream();
                  int ch;
                  StringBuffer b =new StringBuffer();
                  while( ( ch = is.read() ) !=-1 )
                  {
                        b.append( (char)ch );
                  }
          /* 将Response显示于Dialog */
          /* 关闭DataOutputStream */
                  //postMessage(REVERSE_DATA, b.toString());
                  parserLoadup(b.toString());
                  ds.close();
            }
            catch(Exception e)
            {
                  postMessage(REVERSE_DATA,"error");
            }
      }



	private synchronized void getLoadup() {
		String url = C.ACTIVITY_SHAITUS_URL + id;
		Task task = new Task(null, url, null, "ACTIVITY_SHAITUS_URL", this);
		task.setPostData(getLoadParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	// File file = new File() ;

	private String getLoadParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		params.put("fileToUpload",imageBytes);
		params.put("album_id", album_id);
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter())
						.equals("ACTIVITY_SHAITUS_URL")) {
					parserLoadup(result);
				}
			}
		}
	}

	private void parserLoadup(String params) {
		try {
			JSONObject json = new JSONObject(params);
			status = json.getString("status");
			data = json.getString("data");
			postMessage(SHOW_XXXX_INFO);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_EEROR:
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_XXXX_INFO:
			if (status.equalsIgnoreCase("ok") && data.equalsIgnoreCase("ok")) {
				Toast.makeText(ShowImageActivity.this, "上传成功", 0).show();
			} else {
				Toast.makeText(ShowImageActivity.this, "服务器繁忙,请稍后再试", 0).show();
			}
			break;
            case REVERSE_DATA:
                  postMessage(DISMISS_PROGRESS);
                  break;
		}
		return true;
	}



      private String[] getBitmpData(String path) {
            File file = new File(path);
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            ByteArrayOutputStream bos = null;
            String[] bytes = null;
            try {
                  fis = new FileInputStream(file);
                  bis = new BufferedInputStream(fis);
                  int len = 0;
                  byte[] b = new byte[1024];
                  bos = new ByteArrayOutputStream(1024);
                  while ((len = bis.read(b)) != -1){
                        bos.write(b,0,len);
                        bos.flush();
                  }
                  bytes = toStringArray(bos.toByteArray());
            } catch (FileNotFoundException e) {
                  e.printStackTrace();
            } catch (IOException e) {
                  e.printStackTrace();
            } finally {
                  try {
                        if (null != bis)
                              bis.close();
                        if (null != bos)
                              bos.close();
                        if (null != fis)
                              fis.close();
                  } catch (IOException e) {
                        e.printStackTrace();
                  }
            }
            return bytes;
      }

      private String[] toStringArray(byte[] bytes) {
            String[] strings = new String[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                  strings[i] = String.valueOf(bytes[i]);
            }
            return strings;
      }

      private void getCamera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                  String sdStatus = Environment.getExternalStorageState();
                  if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        return;
                  }
                  String name = new android.text.format.DateFormat().format("yyyyMMdd_hhmmss",
                          Calendar.getInstance(Locale.CHINA))
                          + ".jpg";
                  Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                  Bundle bundle = data.getExtras();
                  Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                  FileOutputStream b = null;
                  // ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
                  File file = new File("/sdcard/myImage/");
                  file.mkdirs();// 创建文件夹
                  String fileName = "/sdcard/myImage/" + name;
                  list.add(fileName);
                  adapter.setIndexSelected(list.size()-1, true);
                  adapter.notifyDataSetChanged();
                  try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                  } catch (FileNotFoundException e) {
                        e.printStackTrace();
                  } finally {
                        try {
                              b.flush();
                              b.close();
                        } catch (IOException e) {
                              e.printStackTrace();
                        }
                  }
            }
      }

    
}