package com.lis99.mobile.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.model.UpdataModel;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by yy on 16/4/5.
 */
public class UpdataUtil {

    private static UpdataUtil instance;
    private UpdataModel model;

    public static UpdataUtil getInstance ()
    {
        if ( instance == null ) instance = new UpdataUtil();
        return instance;
    }

//    获取储存目录
    public File getStorage (  )
    {
        File file = StorageUtils.getOwnCacheDirectory(
                LSBaseActivity.activity.getApplicationContext(), "/lishi99/down");
        return file;
    }

    /**
     *      获取新版本， 0没有更新， 1 有更新， 2强制更新
     */
    public void getUpData (  )
    {

        HashMap<String, Object> map = new HashMap<>();

        map.put("version", DeviceInfo.CLIENTVERSIONCODE);

        model = new UpdataModel();

        MyRequestManager.getInstance().requestPostNoDialog(C.MAIN_CHECKVERSION_URL, map, model,
                new CallBack() {

                    @Override
                    public void handler(MyTask mTask) {
                        model = (UpdataModel) mTask.getResultModel();

                        if (model == null) return;

                        if (0 == model.type) {


                        } else {

                            DialogManager.getInstance().showUpdataDialog(model, upDataCallBack);

                        }

                    }
                });
    }

    private CallBack upDataCallBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {

            if ( "ok".equals(mTask.getresult()))
            {
                model.filePath = getStorage().toString();
//                model.name = "/lis99.apk";
                new DownLoadAPK().execute();
            }
            else if ( "cancel".equals(mTask.getresult()))
            {
                // 强制更新
                if ( 2 == model.type )
                {
//                    退出应用
                    Common.ExitLis();
                }
            }


        }
    };


    // 下载APK
    private class DownLoadAPK extends AsyncTask<String, Integer, Boolean> {

        public ProgressDialog progressDialog; // 进度条对话框引用
        int provalue = 0;
        int downloadedSize = 0;

        Boolean result = true;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LSBaseActivity.activity);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);// 进度条不能回退
            progressDialog.setTitle("下载更新");// 设置标题
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... params) {

//            if (Common.hasSDCard()) {
//                String folderName = Environment.getExternalStorageDirectory()
//                        .getPath() + "/" + filePath;
                String fullPath = model.filePath + model.appName;//folderName + "/" + name;
                File file = new File(fullPath);
                if (file.exists()) {
                    file.delete();
                }
                file = new File(model.filePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                try {
                    HttpGet httpGet = new HttpGet(model.url);
                    HttpResponse httpResponse = new DefaultHttpClient()
                            .execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        InputStream is = httpResponse.getEntity().getContent();
                        long sizeoffile = httpResponse.getEntity()
                                .getContentLength();
                        if (sizeoffile < 0) {
                            result = false;
                        } else {
                            FileOutputStream fos = new FileOutputStream(
                                    fullPath);
                            byte[] buffer = new byte[4096];
                            int count = 0;
                            while ((count = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, count);
                                downloadedSize += count;
                                provalue = (int) (downloadedSize * 100.0 / sizeoffile);
                                publishProgress(provalue);
                            }
                            fos.flush();
                            fos.close();
                            is.close();
                            if (provalue < 100) {
                                result = false;
                            } else {
                                result = true;
                            }
                        }

                    }
                } catch (Exception e) {
                    Common.log("downloadFile Exception = " + e.toString());
                    result = false;
                }
//            } else {
//                DialogManager.getInstance().alert(((Activity) mContext), "错误信息", "没有SD卡");
////                Common.showHintDialog(AppUtil.currentActivity, "错误信息", "没有SD卡");
//                result = false;
//            }
            Common.log("result:" + result + "<><><><><>");
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result) {
                String path = model.filePath + model.appName;//"/" + filePath + "/" + name;
                Common.installAPK(LSBaseActivity.activity, path);
            } else {
                DialogManager.getInstance().alert(LSBaseActivity.activity, "更新失败");
            }
        }
    }

}
