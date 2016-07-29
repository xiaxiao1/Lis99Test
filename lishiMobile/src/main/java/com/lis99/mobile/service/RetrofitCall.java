package com.lis99.mobile.service;

import com.lis99.mobile.util.DialogManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yy on 16/7/29.
 */
public abstract class RetrofitCall<T> implements Callback<T>{
    /**
     * Invoked for a received HTTP response.
     * <p/>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        DialogManager.getInstance().stopWaitting();
        myResponse(call, response);
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        DialogManager.getInstance().stopWaitting();
        myFailure(call, t);
    }

    public abstract void myResponse (Call<T> call, Response<T> response);

    public abstract void myFailure (Call<T> call, Throwable t);

}
