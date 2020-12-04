package com.example.mydemok.constant;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.List;

import me.jessyan.art.http.log.FormatPrinter;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import timber.log.Timber;

public class MyFormatPrinter implements FormatPrinter {
    @SuppressLint("StringFormatInTimber")
    @Override
    public void printJsonRequest(Request request, String bodyString) {
        HttpUrl httpUrl = request.url();
        String method = request.method();
        String url = String.format("%s://%s:%s%s", httpUrl.scheme(), httpUrl.host(), httpUrl.port(), httpUrl.encodedPath());
        Headers headers = request.headers();
        String params;
        if (TextUtils.equals("POST", method)) {
            RequestBody body = request.body();
            if (body instanceof FormBody) {
                StringBuilder sb = new StringBuilder();
                FormBody formBody = (FormBody) request.body();
                for (int i = 0; i < formBody.size(); i++) {
                    sb.append(formBody.encodedName(i)).append("=").append(formBody.encodedValue(i)).append("&");
                }
                if (sb.length() > 1) {
                    sb.delete(sb.length() - 1, sb.length());
                }
                params = sb.toString();
            } else {
                params = body.toString();
            }
        } else {
            params = request.url().encodedQuery();
        }
        Timber.i("\nmethod\t ---> %s\nurl\t ---> %s\nheaders---> %s\nparams\t ---> %s\n",
                method, url, headers != null ? headers.toString().replaceAll("[\r|\n]", " & ") : "", params);
    }

    @Override
    public void printFileRequest(Request request) {
    }

    @Override
    public void printJsonResponse(long chainMs, boolean isSuccessful, int code,
                                  String headers, MediaType contentType, String bodyString,
                                  List<String> segments, String message, String responseUrl) {
        if (responseUrl.endsWith("png") || responseUrl.endsWith("jpg"))
            return;

    }

    @Override
    public void printFileResponse(long chainMs, boolean isSuccessful, int code, String headers,
                                  List<String> segments, String message, String responseUrl) {
    }
}
