package com.gjj.avgle;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.junit.Test;

import okhttp3.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void name() {
        Rx2AndroidNetworking.get("https://avgle.com//mp4.php?vid=213755&ts=1538057346&hash=OWVkYjhkMzAyZTdjYjY2ZDdmNGNlZTNiZjI4Zjg4YTk=&m3u8").build().getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {
                Log.d("response", response.body().toString());
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

}