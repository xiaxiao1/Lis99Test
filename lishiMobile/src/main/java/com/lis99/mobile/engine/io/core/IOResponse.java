package com.lis99.mobile.engine.io.core;

import com.lis99.mobile.engine.io.IOTask;

/*******************************************
 * @ClassName: IOResponse 
 * @Description: IO响应的基础类 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:59:41 
 *  
 ******************************************/
public class IOResponse {

    public static final int OK                      = 10000;
    public static final int FAILED                  = 10001;
    public static final int OUT_OF_MEMORY           = 10002;
    public static final int UNKNOWN_OPERATION       = 10003;
    public static final int SECURITY_ISSUE          = 10004;
    public static final int FAIL_TO_DOWNLOAD        = 10005;
    public static final int CONNECTION_NOT_FOUND    = 10006;

    private int code;
    private IOTask task;
    private String errMessage;

    public IOResponse() {
        code = OK;
    }

    public void setTask(IOTask newTask) {
        task = newTask;
    }

    public IOTask getTask() {
        return task;
    }

    public void setCode(int newCode) {
        code = newCode;
    }

    public void setCode(int newCode, String msg) {
        code = newCode;
        setErrMessage(msg);
    }

    public int getCode() {
        return code;
    }

    public void setErrMessage(String msg) {
        if (errMessage == null)
            errMessage = msg;
        else
            errMessage = errMessage + "; and, " + msg;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void close() {}
}
