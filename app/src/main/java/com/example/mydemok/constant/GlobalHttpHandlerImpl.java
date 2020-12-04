/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mydemok.constant;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;


import com.example.mydemok.application.Constants;
import com.example.mydemok.utils.AppUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import me.jessyan.art.http.GlobalHttpHandler;
import me.jessyan.art.http.log.RequestInterceptor;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link GlobalHttpHandler} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:06
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalHttpHandlerImpl implements GlobalHttpHandler {
    private Context context;

    public GlobalHttpHandlerImpl(Context context) {
        this.context = context;
    }

    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain      {@link Interceptor.Chain}
     * @param response   {@link Response}
     * @return {@link Response}
     */
    @NonNull
    @Override
    public Response onHttpResultResponse(@Nullable String httpResult, @NonNull Interceptor.Chain chain, @NonNull Response response) {
        Request request = chain.request();
        if (request.url().toString().endsWith("png") || request.url().toString().endsWith("jpg"))
            return response;

        if (!TextUtils.isEmpty(httpResult) && RequestInterceptor.isJson(response.body().contentType())) {

        }
        Timber.e("------------------printJsonResponse:"  + request.url().toString()  );
        Timber.e("------------------printJsonResponse: "+httpResult);
        /* 这里如果发现 token 过期, 可以先请求最新的 token, 然后在拿新的 token 放入 Request 里去重新请求
        注意在这个回调之前已经调用过 proceed(), 所以这里必须自己去建立网络请求, 如使用 Okhttp 使用新的 Request 去请求
        create a new request and modify it accordingly using the new token
        Request newRequest = chain.request().newBuilder().header("token", newToken)
                             .build();

        retry the request

        response.body().close();
        如果使用 Okhttp 将新的请求, 请求成功后, 再将 Okhttp 返回的 Response return 出去即可
        如果不需要返回新的结果, 则直接把参数 response 返回出去即可*/
        return response;
    }

    public static String getEncoding(String str) {

        String encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GB2312
                String s = encode;
                return s; //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        return "";
    }

    /**
     * 这里可以在请求服务器之前拿到 {@link Request}, 做一些操作比如给 {@link Request} 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain   {@link Interceptor.Chain}
     * @param request {@link Request}
     * @return {@link Request}
     */
    @NonNull
    @Override
    public Request onHttpRequestBefore(@NonNull Interceptor.Chain chain, @NonNull Request request) {
        /* 如果需要在请求服务器之前做一些操作, 则重新构建一个做过操作的 Request 并 return, 如增加 Header、Params 等请求信息, 不做操作则直接返回参数 request
        return chain.request().newBuilder().header("token", tokenId)
                              .build(); */
//        StringBuilder bodyStr = new StringBuilder();

        // 取出参数做签名
        HashMap<String, String> params = new HashMap<>();
        RequestBody requestBody = request.body();
        boolean isUpload = false;
        boolean isFirst = true;
        if (requestBody instanceof FormBody) {
            FormBody body = (FormBody) requestBody;
            for (int i = 0, count = body.size(); i < count; i++) {
                String name = body.name(i);
                String value = body.value(i);
                params.put(name, value);

            }
            Timber.e("---------------------" + params.toString());
        } else if (requestBody instanceof MultipartBody) {
            MultipartBody body = (MultipartBody) requestBody;
            List<String> names = new ArrayList<>();
            List<MultipartBody.Part> parts = body.parts();
            for (int i = 0; i < parts.size(); i++) {
                MultipartBody.Part part = parts.get(i);
                Buffer buffer1 = new Buffer();
                try {
                    body.writeTo(buffer1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String postParams = buffer1.readUtf8();
                String[] split = postParams.split("\n");
                for (String s : split) {
                    if (s.contains("Content-Disposition")) {
                        String replace = s.replace("Content-Disposition: form-data; name=", "").replace("\"", "").trim();
                        names.add(replace);
                        if ("pic_name".equals(replace)) {
                            params.put("pic_name", "Filedata");
                        } else if ("thumb_limit".equals(replace)) {
                            params.put("thumb_limit", "1");
                        } else if ("thumb_w".equals(replace)) {
                            params.put("thumb_w", "750");
                        } else if ("sessionid".equals(replace)) {
//                            params.put("sessionid", LoginManager.getInstance().getUserSession());
                        }
                    }
                }
            }
            isUpload = true;
        }
        Request newRequest;
        if (isUpload) {
            String sign = signature(params, Constants.ART_SECRET, true);
            // header中添加统一参数
            newRequest = chain.request().newBuilder().addHeader("APPKEY", Constants.ART_KEY)
                    .addHeader("VERSION", AppUtil.getAppVersion())
                    .addHeader("PLATFORM", "ANDROID")
                    .addHeader("SIGN", sign)
                    .addHeader("devID", AppUtil.getReqDeviceId())
                    .build();
        } else {
            // header中添加统一参数
            String sign = signature(params, Constants.ART_SECRET, false);
            newRequest = chain.request().newBuilder().addHeader("APPKEY", Constants.ART_KEY)
                    .addHeader("VERSION", AppUtil.getAppVersion())
                    .addHeader("PLATFORM", "ANDROID")
                    .addHeader("SIGN", sign)
                    .addHeader("devID", AppUtil.getReqDeviceId())
                    .build();
        }

        return newRequest;
    }

    public String signature(Map<String, String> map, String salt, boolean isImage) {
        final StringBuilder builder = new StringBuilder();
        if (null != map && !map.isEmpty()) {
            List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });


            for (final HashMap.Entry<String, String> entry : list) {
                builder.append(URLEncoder.encode(entry.getKey()));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue()));
                builder.append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append(salt);
        String source = builder.toString();
        String s = source.replace("+", "%20");
        s = s.replace("*", "%2A");
        s = s.replace("%7e", "~");
        s = s.replace("%7E", "~");
        String sign = AppUtil.encrypt(s, "MD5");
        return sign;
    }
}
