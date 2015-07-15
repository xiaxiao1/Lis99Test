package com.lis99.mobile.engine.base;

/*******************************************
 * @ClassName: ISocketHandler 
 * @Description: socket读取数据回调接口定义 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:51:32 
 *  
 ******************************************/
public interface ISocketHandler {
    
    /**
     *
     * @param object 结果集对象,一般为输入流
     */
    public void resolveObject(Object object) throws Exception;
}