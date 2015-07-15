package com.lis99.mobile.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;



public class Doget {
	public String getResultForHttpGet(String url) throws ClientProtocolException, IOException{  
        /*建立HTTP Get对象*/  
        HttpGet httpRequest = new HttpGet(url);  
        String strResult="";
        try  
        {  
          /*发送请求并等待响应*/  
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);  
          /*若状态码为200 ok*/  
          if(httpResponse.getStatusLine().getStatusCode() == 200)   
          {  
            /*读*/  
             strResult = EntityUtils.toString(httpResponse.getEntity());  
            /*去没有用的字符*/  
            //strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);  
            
          }  
         
        }  
        catch (ClientProtocolException e)  
        {   
          
          e.printStackTrace();  
        }  
        catch (IOException e)  
        {   
         
          e.printStackTrace();  
        }  
        catch (Exception e)  
        {   
          
          e.printStackTrace();   
        }   
        return strResult;
}  
}
