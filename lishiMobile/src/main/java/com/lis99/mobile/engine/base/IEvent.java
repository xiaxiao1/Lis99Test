package com.lis99.mobile.engine.base;

/*******************************************
 * @ClassName: IEvent 
 * @Description: 操作定义类 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:49:36 
 *  
 ******************************************/
public interface IEvent {

    public static final int OP_COMPLETE = 0;//操作完成
    public static final int OP_NONE = 999; // 无操作

    public static final int IO = 1;
    public static final int PARSER = 2;
}
