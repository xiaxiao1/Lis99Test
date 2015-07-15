package com.lis99.mobile.engine.base;

import com.lis99.mobile.util.C;

/*******************************************
 * @ClassName: Task 
 * @Description: 任务封装 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:53:43 
 *  
 ******************************************/
public class Task {

    /***************************************************************************
     * 任务状态相关的属性
     **************************************************************************/

    /** 任务的状态*/
    protected int state;

    /** 任务状态定义 */
    public static final int STATE_START      = 1;    //任务开始执行
    public static final int STATE_INPROGRESS = 2;    //下载中
    public static final int STATE_COMPLETE   = 3;    //任务处理完成
    public static final int STATE_FAILED     = 4;    //任务处理失败
    public static final int STATE_CANCELED   = 5;    //任务取消
    
    /***************************************************************************
     * 任务的错误信息
     **************************************************************************/
    
    /** 任务的错误代码 */
    protected int errCode;
    /** 任务的错误信息 */
    protected String errMessage;

    /***************************************************************************
     * 任务的通用属性
     **************************************************************************/
    
    /** 任务携带的中间数据 */
    protected Object mData;
    /** 后期处理任务数据/结果时, 需要的额外参数 */
    protected Object mParam;
    /** 任务类型: XML, image, video, GET, POST等等 */
    protected String mType;

    /***************************************************************************
     * 任务的拥有者和处理者
     **************************************************************************/
    /** 发起任务的元素 */
    protected Object mElement;
    /** 任务的拥有者, 它不一定是发起任务的元素 */
    protected Object mOwner;
    /** post数据*/
    protected Object mPostData;
    /***************************************************************************
     * 任务初始化接口
     **************************************************************************/
    
    public Task() {
        reset();
    }
    
    public Task(Task task) {
        if (task == null) return;
        
        mElement = task.mElement;
        mType = task.mType;
        mData = task.mData;
        mParam = task.mParam;
        mOwner = task.mOwner;
        mPostData = task.mPostData;
        reset();
    }

    public Task(Object srcElement) {
        this(srcElement, null, null, null, null);
    }

    // URL 复用了 task.data 来保存
    public Task(Object srcElement, String url) {
        this(srcElement, url, C.HTTP_GET, null, null);
    }

    // URL 复用了 task.data 来保存
    public Task(Object srcElement, String url, String taskType, Object param) {
        this(srcElement, url, taskType, param, null);
        
        if (taskType == null)
            setType(C.HTTP_GET);
    }

    public Task(Object srcElement, Object taskData, Object param) {
        this(srcElement, taskData, null, param, null);
    }

    public Task(Object srcElement, Object taskData, 
            String taskType, Object param) {
        
        this(srcElement, taskData, taskType, param, null);
    }
    
    public Task(Object srcElement, Object taskData, 
            String taskType, Object param, Object taskOwner) {
        
        mElement = srcElement;
        mType = taskType;
        mData = taskData;
        mParam = param;
        mOwner = taskOwner;
        reset();        
    }


    /***************************************************************************
     * 任务状态机管理
     **************************************************************************/

    public void reset() {
        state = STATE_START;
        errCode = 0;
        errMessage = null;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 设置任务为正在处理中状态
     */
    public void start() {
        state = STATE_INPROGRESS;
    }

    /**
     * 设置任务为已经完成状态
     */
    public void complete() {
        state = STATE_COMPLETE;
    }

    public boolean isComplete() {
        return state == STATE_COMPLETE;
    }

    public void cancel() {
        state = STATE_CANCELED;
    }

    public boolean isCanceled() {
        return state == STATE_CANCELED;
    }

    public void fail() {
        state = STATE_FAILED;
    }
    
    public void fail(int err, String msg) {
        state = STATE_FAILED;
        setCode(err, msg);
    }

    public boolean isFailed() {
        return state == STATE_FAILED;
    }

    public void setCode(int err) {
        errCode = err;
    }
    
    public void setCode(int err, String msg) {
       errCode = err;
       setErrMessage(msg);
    }
    
    public int getErrCode() {
        return errCode;
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
    
    public String getErrCodeMessage() {
        return "[" + errCode + "]: " + errMessage;
    }
    
    /***************************************************************************
     * 任务的操作接口
     **************************************************************************/
    /**
     * @return the parameter
     */
    public Object getParameter() {
        return mParam;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(Object parameter) {
        this.mParam = parameter;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return mData;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.mData = data;
    }

    /**
     * @return the parent
     */
    public Object getOwner() {
        return mOwner;
    }

    /**
     * @param parent the parent to set
     */
    public void setOwner(Object parent) {
        mOwner = parent;
    }

    /**
     * @return the postData
     */
    public Object getPostData() {
        return mPostData;
    }

    /**
     * @param element the postData to set
     */
    public void setPostData(Object mPostData) {
        this.mPostData = mPostData;
    }

    /**
     * @return the element
     */
    public Object getElement() {
        return mElement;
    }

    /**
     * @param element the element to set
     */
    public void setElement(Object element) {
        this.mElement = element;
    }

    /**
     * @return the type
     */
    public String getType() {
    	if (mType == null) {
    		return C.HTTP_POST;
    	}
        return mType;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.mType = type;
    }

    public void callback(int initiator, int operation) {
        if (mOwner == null) return;
        
        try {
            IEventHandler boss = (IEventHandler) mOwner;
            boss.handleTask(initiator, this, operation);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    // @Override
    public String toString() {
        return "Task - element = " + this.mElement + ", "
                + "data = " + this.mData + ", "
                + "type = " + this.mType + ", "
                + "param = " + this.mParam + ", "
                + "state = " + this.state;
    }
}
