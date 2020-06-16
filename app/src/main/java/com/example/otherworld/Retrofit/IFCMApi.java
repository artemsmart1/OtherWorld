package com.example.otherworld.Retrofit;

import com.example.otherworld.Model.FCMResponse;
import com.example.otherworld.Model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key= AAAAzJN4z7o:APA91bHjULq2jOGa4lIt6S_BuGWdu_O1VtG2NqlB2FXfTqy_klSqFghyGibrDTLz8fkUsrCIjU_K3pPtwBPzNzIU0q5jxKFlu1XbSSh01OHWBU9Lob7vfBzgWXBWUnCAevqrDRvdqPdN"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}
