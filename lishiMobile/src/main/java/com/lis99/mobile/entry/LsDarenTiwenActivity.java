package com.lis99.mobile.entry;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ImageBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class LsDarenTiwenActivity extends ActivityPattern {

	
	TextView tv_cancel,tv_submit,tv_cate,tv_name;
	LinearLayout ll_camera,ll_cate,ll_images;
	AsyncLoadImageView iv_head;
	EditText et_content;
	private static final int SHOW_UPLOAD_PIC = 200;
	private static final int SHOW_SEND_SUCCESS = 201;
	Bitmap bmp;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_daren_tiwen);

		StatusUtil.setStatusBar(this);
		id = getIntent().getStringExtra("id");
		Resources res = getResources();  
		bmp = BitmapFactory.decodeResource(res, R.drawable.ls_nologin_header_icon);
		setView();
		setListener();
	}
	
	

	private void setView() {
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		tv_cate = (TextView) findViewById(R.id.tv_cate);
		ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
		ll_cate = (LinearLayout) findViewById(R.id.ll_cate);
		ll_images = (LinearLayout) findViewById(R.id.ll_images);
		et_content = (EditText) findViewById(R.id.et_content);
		iv_head = (AsyncLoadImageView) findViewById(R.id.iv_head);
		iv_head.setImage(DataManager.getInstance().getUser().getHeadicon(), bmp, bmp, "zhuangbei_detail");
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_name.setText(getIntent().getStringExtra("nickname"));
	}

	private void setListener() {
		tv_cancel.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		ll_camera.setOnClickListener(this);
		ll_cate.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == tv_cancel.getId()){
			finish();
		}else if(v.getId() == tv_submit.getId()){
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			sendTask();
		}else if(v.getId() == ll_camera.getId()){
			setUserHead();
		}else if(v.getId() == ll_cate.getId()){
			showCateList();
		}
	}
	private void sendTask() {
		Task task = new Task(null, C.MAIN_COMMENT_URL, null, "WENDA_ASK_URL", this);
		task.setPostData(getSendParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	String cateId = "1";
	private String getSendParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pid", "0");
		params.put("rid", "0");
		params.put("type", "user");
		params.put("comment", content + et_content.getText().toString().trim());
		params.put("id", id);
		params.put("touser", id);
		params.put("title", et_content.getText().toString().trim());
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}


	private void showCateList() {
		postMessage(POPUP_DIALOG_LIST, "问题分类",
				R.array.cate_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							tv_cate.setText("跑步装备");
							cateId = "3";
							break;
						case 1:
							tv_cate.setText("徒步装备");
							cateId = "5";
							break;
						case 2:
							tv_cate.setText("登山装备");
							cateId = "2";
							break;
						case 3:
							tv_cate.setText("滑雪装备");
							cateId = "4";
							break;
						case 4:
							tv_cate.setText("攀岩装备");
							cateId = "6";
							break;
						case 5:
							tv_cate.setText("骑行装备");
							cateId = "7";
							break;
						case 6:
							tv_cate.setText("自驾露营装备");
							cateId = "9";
							break;
						case 7:
							tv_cate.setText("其他装备");
							cateId = "1";
							break;
						}
					}
				});
	}

	private void setUserHead() {
		postMessage(POPUP_DIALOG_LIST, "选择图片",
				R.array.select_head_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 拍摄
							BitmapUtil.doTakePhoto(LsDarenTiwenActivity.this);
							break;
						case 1:
							// 相册
							BitmapUtil.doPickPhotoFromGallery(LsDarenTiwenActivity.this); 
							break;
						}
					}
				});
	}
	Bitmap bitmap;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			switch (requestCode) {
			case C.PHOTO_PICKED_WITH_DATA:
				Uri photo_uri = data.getData();
				bitmap = BitmapUtil.getThumbnail(photo_uri,LsDarenTiwenActivity.this);
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				new GetDataTask(bitmap).execute();
				//BitmapUtil.doCropPhoto(bitmap, LsDarenTiwenActivity.this);
				break;
			case C.PICKED_WITH_DATA:
				//Bitmap bt = data.getParcelableExtra("data");
				
				break;
			case C.CAMERA_WITH_DATA:
				File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
				bitmap =  BitmapUtil.getThumbnail(file,LsDarenTiwenActivity.this);
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				new GetDataTask(bitmap).execute();
				//BitmapUtil.doCropPhoto(bitmap, LsDarenTiwenActivity.this);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 上传文件至Server的方法 */
    private void uploadFile()
    {
    	String savePath = Environment.getExternalStorageDirectory()
				+ "/temp.jpg";
		ImageUtil.savePic(savePath, bitmap,100);
      String end ="\r\n";
      String twoHyphens ="--";
      String boundary ="*****";
      try
      {
        URL url =new URL(C.WENDA_UPLOAD_URL);
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
       // con.setRequestProperty("user_id", DataManager.getInstance().getUser().getUser_id());
        StringBuilder sb = new StringBuilder();
        sb.append(C.PREFIX);
		sb.append(C.BOUNDARY);
		sb.append(C.LINEND);
		sb.append("Content-Disposition: form-data;name=\"user_id\"" + C.LINEND);
		sb.append("Content-Type: text/plain; charset=" + C.CHARSET
				+ C.LINEND);
		sb.append("Content-Transfer-Encoding: 8bit" + C.LINEND);
		sb.append(C.LINEND);
		sb.append("2566");
		sb.append(C.LINEND);
		OutputStream os = con.getOutputStream();
		DataOutputStream outStream = new DataOutputStream(os);
		outStream.write(sb.toString().getBytes());
		
		StringBuilder split = new StringBuilder();
		split.append(C.PREFIX);
		split.append(C.BOUNDARY);
		split.append(C.LINEND);
		split.append("Content-Disposition: form-data;name=\"image\";filename=\"image\"" + C.LINEND);
		split.append("Content-Type:" + "application/octet-stream"
				+ "; charset=" + C.CHARSET + C.LINEND);
		split.append(C.LINEND);
		outStream.write(split.toString().getBytes());
		InputStream fStream =new FileInputStream(savePath);
		if (fStream != null) {
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			fStream.close();
		}
		outStream.write(C.LINEND.getBytes());
		byte[] end_data = (C.PREFIX + C.BOUNDARY + C.PREFIX + C.LINEND)
				.getBytes();
		outStream.write(end_data);
		outStream.flush();
		InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) !=-1 )
        {
          b.append( (char)ch );
        }
        /* 将Response显示于Dialog */
        System.out.println("ddddddd====="+b.toString().trim());
        /* 关闭DataOutputStream */
		outStream.close();
		
      }
      catch(Exception e)
      {
        //showDialog("上传失败"+e);
      }
    }
 // 把文件转换成字节数组
 	/*private static byte[] getBytes(File f) throws Exception
 	{
 		FileInputStream in = new FileInputStream(f);
 		ByteArrayOutputStream out = new ByteArrayOutputStream();
 		byte[] b = new byte[1024];
 		int n;
 		while ((n = in.read(b)) != -1)
 		{
 			out.write(b, 0, n);
 		}
 		in.close();
 		return out.toByteArray();
 	}*/
	
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

    	Bitmap btt;
		public GetDataTask(Bitmap bt) {
		// TODO Auto-generated constructor stub
			btt=bt;
	}

		@Override
		protected String[] doInBackground(Void... params) {
			String[] netResult = new String[10];
			 //photo_data2 = ImageUtil.resizeBitmap(bitmap, 100, 100);
			//String url = FConstant.TASKID_WORK_ADDBOLG_URL;
			//String img_source = String.valueOf(photo_source);
			//String savePath2 = Environment.getExternalStorageDirectory()
			//		+ "/tmp_icon.jpg";
			String savePath = Environment.getExternalStorageDirectory()
					+ "/temp.jpg";
			ImageUtil.savePic(savePath, btt,100);
			//PostMethod filePost = new PostMethod("http://10.0.207.151/upload1.php");
			PostMethod filePost = new PostMethod(C.WENDA_UPLOAD_URL);
			try {
				// 组拼post数据的实体
				// image参数
				File file = new File(savePath);
				FilePart f_part = new FilePart("image", file);
				f_part.setCharSet("utf-8");
				f_part.setContentType("image/jpeg");
				// user_id参数
				StringPart s_part = new StringPart("user_id", DataManager.getInstance().getUser().getUser_id(),"utf-8");
				Part[] parts = {s_part,f_part};
				filePost.setRequestEntity(new MultipartRequestEntity(parts,
						filePost.getParams()));
				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(60000);
				// 完成文件上的post请求
				client.executeMethod(filePost);
				String receiveMsg = filePost.getResponseBodyAsString();
				netResult[0] = receiveMsg;
			} catch (Exception e) {
			} finally {
				filePost.releaseConnection();
			}

			return netResult;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// mWaittingDialog.dismiss();
			String result1 = DataManager.getInstance().validateResult(result[0]);
			if (result1 != null) {
				if("OK".equals(result1)){
					ib = (ImageBean) DataManager.getInstance().jsonParse(result[0], DataManager.TYPE_WENDA_UPLOAD);
					postMessage(LOGIN_SUCCESS);
				}else{
					postMessage(POPUP_TOAST, result);
					postMessage(DISMISS_PROGRESS);
				}
			}	

		}
	}
	private static final int LOGIN_SUCCESS = 213;
	ImageBean ib; 
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void uploadPicTask(Bitmap bt) {
		Task task = new Task(null, C.WENDA_UPLOAD_URL, null, "WENDA_UPLOAD_URL", this);
		task.setPostData(getUploadPicParams(bt).getBytes());
		publishTask(task, IEvent.IO);		
	}
	private String getUploadPicParams(Bitmap bt) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("image", BitmapUtil.Bitmap2Bytes(bt));
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
				if (((String) task.getParameter()).equals("WENDA_UPLOAD_URL")) {
					parserUploadPic(result);
				}else if (((String) task.getParameter()).equals("WENDA_ASK_URL")) {
					parserSend(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserSend(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				postMessage(SHOW_SEND_SUCCESS);
			}else{
				postMessage(POPUP_TOAST, result);
			}
		}				
	}



	private void parserUploadPic(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				DataManager.getInstance().jsonParse(params, DataManager.TYPE_UPLOAD_PIC);
				postMessage(SHOW_UPLOAD_PIC);
			}else{
				postMessage(POPUP_TOAST, result);
			}
		}		
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_UPLOAD_PIC:
			
			break;
		case SHOW_SEND_SUCCESS:
			Toast.makeText(this, "提交成功", 0).show();
			finish();
			break;
			
		case LOGIN_SUCCESS:
			com.lis99.mobile.entry.view.AsyncLoadImageView ii = new AsyncLoadImageView(this);
			LinearLayout.LayoutParams ll1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			ll1.width = StringUtil.dip2px(this, 40);
			ll1.height = StringUtil.dip2px(this, 40);
			ii.setLayoutParams(ll1);
			ii.setScaleType(ScaleType.FIT_XY);
			ii.setImage(ib.getShowimg(), null, null);
			ll_images.addView(ii);
			content += "<img src=\'"+ib.getImgurl()+"\' />";
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	String content="";
}
