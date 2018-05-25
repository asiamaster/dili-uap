package com.dili.uap.sdk.service;

import com.dili.uap.sdk.session.SessionContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jk on 2017/5/11.
 */
public abstract class AbstractApiService {
    private static final Logger log = LoggerFactory.getLogger(AbstractApiService.class);

    private String token;
    private String baseUrl;

    public AbstractApiService(String token, String baseUrl) {
        this.token = token;
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        this.baseUrl = baseUrl;
    }

    public String httpGet(String url, Map<String, String> param) throws IOException {
        String u = makeGetUrl(url, param);
        Response resp = getHttp(u);
        return resp.body().string();
    }


    private Response getHttp(String url) throws IOException {
        OkHttpClient.Builder build = new OkHttpClient.Builder();
        build.connectTimeout(60, TimeUnit.SECONDS);
        build.readTimeout(60, TimeUnit.SECONDS);
        OkHttpClient cli = build.build();
        Request.Builder reqBuild = new Request.Builder()
                .url(url)
                .addHeader("token",this.token)
                .addHeader("Accept-Language","zh-CN,en-US;q=0.8")
                .addHeader("User-Agent","Mozilla/5.0 (Linux; Android 4.4.4; Nexus 5 Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 baapp/null baversion/1.5.1 NetType/WIFI clientType/phone")
                .addHeader("X-Requested-With",SessionContext.getSessionContext().getManageConfig().getDomain());
        Request res = reqBuild.build();
        return cli.newCall(res).execute();
    }

    private String makeGetUrl(String url, Map<String, String> param){
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        url = this.baseUrl + url;
        if (param == null){
            return url;
        }
        StringBuffer query = new StringBuffer();
        for (Map.Entry<String, String> en : param.entrySet()) {
            query.append("&");
            query.append(en.getKey());
            query.append("=");
            query.append(en.getValue());
        }
        if (url.indexOf("?") > 0) {
            url += query;
        } else {
            url += "?" + query.substring(1);
        }
        return url;
    }
}
