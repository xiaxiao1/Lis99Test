package com.lis99.mobile.engine.io.core;

import com.lis99.mobile.engine.io.IOTask;
import com.lis99.mobile.util.C;

/*******************************************
 * @ClassName: IOProtocol 
 * @Description: 基本类: IO通用协议 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:58:33 
 *  
 ******************************************/
public class IOProtocol {
    
    /** 协议类型 */
    private int mType;

    /** 本地获取*/
    public static final int RESOURCE   = 1;
    /** 网络获取 */
	public static final int HTTP       = 2;

    /** 协议初始化 */ 
    public boolean init() {
        return true; 
    }

    /**
     * 检查任务的有效性
     */
    public boolean validateRequest(IOTask task) {
        return true;
    }

    /**
     * 处理IO请求
     * 
     * @param task
     * @return 响应
     */
    public IOResponse handleRequest(IOTask task) {
        return null;
    }

    /**
     * 处理IO响应
     * 
     * @param response
     * @return INVALID = -3,OK = 0,FAILED = -1;
     */
    public int handleResponse(IOResponse response) {
        return C.OK;
    }

    /**
     * 处理IO响应
     *
     * @param response
     * @return 成功与否
     */
    public void finishRequest(IOTask task) {};
    
    /**
     * 取消IO请求
     *
     * @param task
     * @return 响应
     */
    public void cancelRequest(IOTask task) {}
    
    /**
     * 重新尝试处理IO请求
     *
     * @param task
     * @return true - 成功重试; false - 重试失败
     */
    public boolean retryRequest(IOTask task) {
        return false;
    }

    /**
     * 退出处理相关资源回收
     *
     */
    public boolean exit() {
        return true;
    }

    /**
     * 设置协议类型
     */
    public void setType(int type) {
        mType = type;
    }

    /**
     * 获取协议类型
     */
    public int getType() {
        return mType;
    }
}
