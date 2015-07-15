package com.lis99.mobile.engine.io;

import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.engine.io.core.IOProtocol;
import com.lis99.mobile.engine.io.core.IOProtocolFactory;
import com.lis99.mobile.engine.io.core.IOResponse;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.L;

import java.util.Vector;

/*******************************************
 * @ClassName: IOThread 
 * @Description: IO请求处理线程 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:57:36 
 *  
 ******************************************/
public class IOThread implements Runnable {

	/** 当前正在处理的IO任务 */
	private IOTask mCurTask;
    private boolean mIsExit;
    private String mName;
    private Vector<IOTask> mTaskQueue;

	public IOThread(String name, Vector<IOTask> queue) {
        mName = name;
        mIsExit = false;
        
        if (queue != null) {
            mTaskQueue = queue;
        } else {
            mTaskQueue = new Vector<IOTask>();
        }
        new Thread(this).start();
	}

    // 线程主体循环
    public void run() {
        while (!mIsExit || !mTaskQueue.isEmpty()) {
            synchronized (mTaskQueue) {
                // 检查任务列表, 为空则等
                while (!mIsExit && mTaskQueue.isEmpty()) {
                    try {
                        mTaskQueue.wait();
                    } catch (InterruptedException e) {
                        if (L.ERROR) L.e("Thread", "[" + mName + "]: " 
                                + "Failed to retrieve task", e);
                    }
                }
                // 如果不为空, 则取出一个任务
                if (!mTaskQueue.isEmpty()) {
                    mCurTask = mTaskQueue.elementAt(0);
                    mTaskQueue.removeElementAt(0);
                }
            }

            // 如果取出的任务为空, 继续循环等待
            if (mCurTask == null) continue;
            // 在处理任务前再次检查线程是否被强制结束
            if (mIsExit) break;
            
            // 调用子类接口, 来处理任务
            try {
                mainLoop();
            } catch (Exception x) {
                if (L.ERROR) L.e("Thread", "[" + mName + "]: " 
                        + "Failed to handle task", x);
            }

            mCurTask = null;
        }
    }

	/**
	 * 任务处理接口
	 */
	private void mainLoop() {
		IOResponse response = null;

		try {
			// ------------------------------------------------------------------
			// 1. 解码获取到的任务
			IOProtocol protocol = mCurTask.getProtocol();

			// ------------------------------------------------------------------
			// 2. 处理IO请求
			/** 调用 handleRequest 之前,不可以close此Response,但可将此task的状态至为cancel */
			closeResponse = false;
			response = protocol.handleRequest(mCurTask);
			/** 调用 handleRequest 之后,恢复response的close机制 */
			closeResponse = true;
			// ------------------------------------------------------------------
			// 3. 处理响应
			int isOK = protocol.handleResponse(response);

			// ------------------------------------------------------------------
			// 4. 若任务失败, 自动重新尝试; 否则, 通知任务Owner, 任务处理完毕
			if (mCurTask.isCanceled()) {
				return; // 无需Callback

			} else if (isOK == C.OK) {
				mCurTask.complete();
				doCallBack(mCurTask);
				// 任务成功后, 调用协议接口完成扫尾工作
				protocol.finishRequest(mCurTask);

			} else if (response == null) {
				// 如果系统无法创建Response对象, 表示内存已经溢出
				mCurTask.fail(IOResponse.OUT_OF_MEMORY, "IO: Out of Memory");
				doCallBack(mCurTask);

			} else if (response.getCode() == IOResponse.OUT_OF_MEMORY
					|| response.getCode() == IOResponse.SECURITY_ISSUE) {
				// 内存不够, 或用户不允许此操作时, 无需重试
				mCurTask.fail(response.getCode(), "IO:" + response.getErrMessage());
				doCallBack(mCurTask); // 通过task中的code和errMessage来传递内部错误信息

			} else if (response.getCode() == IOResponse.FAIL_TO_DOWNLOAD
					|| response.getCode() == HttpResponse.SOCKET_TIME_OUT_EXCEPTION
					|| response.getCode() == HttpResponse.IO_EXCEPTION) {

				// 当下载失败 或连接超时时, 才进行重试
				if (protocol.retryRequest(mCurTask) == false) {
					if (response != null) {
						mCurTask.fail(response.getCode(), "IO: Retry failed - "
								+ response.getErrMessage());
					} else {
						mCurTask.fail(IOResponse.FAILED, "IO: Retry failed.");
					}
					doCallBack(mCurTask); // 通过task中的code和errMessage来传递内部错误信息
				} else {
					// 因为重试只是重新发送请求, 所以retryRequest返回true,
					// 并不代表新的task已经处理完毕
				}

			} else {
				// 其它错误则直接返回, 比如网络连接无法工作, 找不到目标服务器等等
				mCurTask.fail(IOResponse.FAILED, "IO: other failures");
				doCallBack(mCurTask); // 通过task中的code和errMessage来传递内部错误信息
			}

		} catch (Exception e) {
			if (L.ERROR)
				L.e("IOThread", "Unknown Exception in handling Task ["
						+ (mCurTask == null ? mCurTask : mCurTask.getData()) + "]", e);
			if (mCurTask != null) {
				mCurTask.fail(IOResponse.FAILED,
						"Exception in IO: " + e.getMessage());
				doCallBack(mCurTask);
			}

		} catch (OutOfMemoryError oomx) {
			//OOM.collectMemory();

			if (L.ERROR)
				L.e("IOThread", "OOM Exception in handling Task ["
						+ (mCurTask == null ? mCurTask : mCurTask.getData()) + "]", oomx);
			if (mCurTask != null) {
				mCurTask.fail(IOResponse.FAILED,
						"Exception in IO: " + oomx.getMessage());
				doCallBack(mCurTask);
			}

		} finally {
			// 必须保障网络链接在使用完毕后被关闭
			if (response != null)
				response.close();
			mCurTask = null;
		}
	}

	/* handleRequest前,设置此变量状态 */
	private boolean closeResponse = true;

	/**
	 * 取消当前正在运行的任务
	 */
	public void cancelCurTask() {
		if (mCurTask == null)
			return;

		try {
			String data = (String) mCurTask.getData();
			IOProtocol protocol = IOProtocolFactory.getHandler(data);
			if (protocol == null)
				return;

			mCurTask.cancel();
			// 执行取消操作
			if (closeResponse) {
				protocol.cancelRequest(mCurTask);
			}
		} catch (Exception e) {
			if (L.DEBUG)
				L.d("IOThread",
						"Exception in cancel current task: " + e.toString());
		}
	}

    public Object getCurTask() {
        return mCurTask;
    }

	/**
	 * 负责任务的后续处理
	 * 
	 * @param task 指定任务
	 * @param param 要传递给处理者的参数
	 */
	private void doCallBack(Task task) {
		if (task == null)
			return;
		task.callback(IEvent.IO, IEvent.OP_COMPLETE);
	}
}
