package com.example.dhairyapujara.perfit_submit;

import javax.xml.transform.Result;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Dhairya Pujara on 28-04-2017.
 */

interface ApiService {

    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of Contacts
    */
    @Multipart
    @POST("/getdefaultmodel")
    Call<Result> uploadImage(@Part MultipartBody.Part file);
}
