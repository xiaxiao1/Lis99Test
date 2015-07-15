package com.lis99.mobile.engine.io;

import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.engine.io.core.IOProtocol;
import com.lis99.mobile.engine.io.core.IOResponse;

/*******************************************
 * @ClassName: IOTask 
 * @Description: IO任务相关的特殊属性
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:57:14 
 *  
 ******************************************/
public class IOTask extends Task {

    /** 任务的协议句柄  */
    protected IOProtocol mProtocol;
    /** 任务处理过程中的响应 */
    protected IOResponse mResponse;
    /** 服务器告知的文件总长度 */
    private int mContentLength;
    /** 已经下载了的数据长度 */
    private int mDataCurrentSize;
    /** 记录真识的任务信息 */
    private Task mTask;

    public void reset() {
        mContentLength = 0;
        mDataCurrentSize = 0;
        mProtocol = null;
        
        super.reset();
    }
    
    public IOTask(Task task) {
        super(task);
        mTask = task;
    }

    public void setProtocol(IOProtocol protocol) {
        mProtocol = protocol;
    }
    
    public IOProtocol getProtocol() {
        return mProtocol;
    }

    public int getContentLength() {
        return mContentLength;
    }

    public void setContentLength(int dataLength) {
        mContentLength = dataLength;
    }

    public int getCurDataSize() {
        return mDataCurrentSize;
    }

    public void setCurDataSize(int size) {
        mDataCurrentSize = size;
    }

    public void setResponse(IOResponse response) {
        mResponse = response;
    }

    public IOResponse getResponse() {
        return mResponse;
    }

    /**========== mTask相关接口,根据状态将mTask的信息拷贝到IOTask中 =========*/
    public void complete() {
        if (mTask != null) {
            if (mTask.isCanceled()) {
                super.cancel();
            } else if (mTask.isFailed()) {
                super.fail();
            } else {
                super.complete();
            }            
        } else {
            super.complete();
        }
    }

    public void fail(){
        super.fail();
        if (mTask != null) {
            mTask.fail();
        }
    }

    public void cancel(){
        super.cancel();
        if (mTask != null) {
            mTask.cancel();
        }
    }
}
