package com.lis99.mobile.club.destination;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zhangjie on 7/13/16.
 */

public interface DestinationService {

    @GET("destination/detail/{destinationID}")
    Call<DestinationInfo> getDestinationInfo(@Path("destinationID") int destinationID);

    @FormUrlEncoded
    @POST("destination/destInfo")
    Call<Destination> getDestinationDetail(@Field("dest_id") int dest_id);


    @FormUrlEncoded
    @POST("destination/destinationDetail/1/{page}")
    Call<DestinationEventListModel> getDestinationEventList(@Path("page") int page, @Field("dest_id") int dest_id, @Field("tag_id") int tag_id);

    @FormUrlEncoded
    @POST("destination/destinationDetail/2/{page}")
    Call<DestinationNoteListModel> getDestinationNoteList(@Path("page") int page, @Field("dest_id") int dest_id, @Field("tag_id") int tag_id);



}
