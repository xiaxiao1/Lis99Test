package com.lis99.mobile.engine.base;

/*******************************************
 * @ClassName: ITaskManager 
 * @Description: 任务队列的管理接口定义
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:51:54 
 *  
 ******************************************/
public interface ITaskManager {
    
    /**
     * 添加任务
     */
    public void addTask(Task task);
    
    /**
     * 取消当前正在处理的任务
     * 
     * @param owner 任务的Owner必须与该值一致
     */
    public void cancelCurrentTask(Object owner);
    
    /**
     * 删除某一个任务
     *
     * @param task 需要被删除的任务
     */
    public void removeTask(Object task);
    
    /**
     * 清空任务列表
     */
    public void removeAllTasks();
    
    /**
     * 删除属于某一个元素的所有任务
     *
     * @param parent 属于该元素的任务会被删除
     */
    public void removeAllTasks(Object parent);
}
