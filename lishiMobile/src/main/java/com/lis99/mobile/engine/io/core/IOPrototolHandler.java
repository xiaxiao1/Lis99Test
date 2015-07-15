package com.lis99.mobile.engine.io.core;

import com.lis99.mobile.engine.base.Task;

/*******************************************
 * @ClassName: IOPrototolHandler 
 * @Description: IO 通信协议处理任务的公开接口 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:59:23 
 *  
 ******************************************/
public interface IOPrototolHandler {
    /**
     * 处理IO请求
     *
     * @param task
     * @return 响应
     */
    public IOResponse handleRequest(Task task);

    /**
     * 处理IO响应
     *
     * @param response
     * @return 成功与否
     */
    public boolean handleResponse(IOResponse response);
}
