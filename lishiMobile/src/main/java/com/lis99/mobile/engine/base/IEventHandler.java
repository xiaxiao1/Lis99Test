package com.lis99.mobile.engine.base;

/*******************************************
 * @ClassName: IEventHandler 
 * @Description: 任务完成后的回调 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:50:34 
 *  
 ******************************************/
public interface IEventHandler {
    
    /**
     * 接收由其它模块处理完成任务后的事件
     *
     * @param initiator 发起此次调用的模块ID
     * @param task 处理的任务
     * @param operation 操作类型
     */
    public void handleTask(int initiator, Task task, int operation);
}