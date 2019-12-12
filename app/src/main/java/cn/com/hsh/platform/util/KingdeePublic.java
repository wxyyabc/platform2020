package cn.com.hsh.platform.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

import cn.com.hsh.platform.zz.zz_CREATE_API;
import cz.msebera.android.httpclient.HttpEntity;

public class KingdeePublic {

    //先定义一个String类型来接收接口相同的部分

    public static String POST_K3CloudURL = "192.168.1.88";
    public static zz_CREATE_API zz_create_api = new zz_CREATE_API();
    private static String CookieVal = null;
    private static Map map = new HashMap<String, String>();

    static {
        map.put("Save", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc");
        map.put("View", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc");
        map.put("Submit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc");
        map.put("Audit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc");
        map.put("UnAudit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc");
        map.put("StatusConvert", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.StatusConvert.common.kdsvc");
        map.put("ExcuteOperation", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExcuteOperation.common.kdsvc");
        map.put("Query", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc");
        map.put("OrderQuery", "Kingdee.WebAPI.YJYS.XSDDCX.XSDDCX.ExecuteXSCX..common.kdsvc");
    }
    //建立静态的AsyncHttpClient
    private static AsyncHttpClient client = new AsyncHttpClient();
    //AsyncHttpClient中有get和post方法，需要用到public方法来修饰，以便调用


    public  static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    //post方法中HttpEntity参数是后面发送JSON格式所用到的一个方法
    public static void post(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.post(context,getAbsoluteUrl(url),entity, contentType, responseHandler);
    }
    //单独写一个方法添加URL
    private static String getAbsoluteUrl(String url) {
        return POST_K3CloudURL + url;
    }

    public static void main(String[] args) throws Exception {

    }
}
