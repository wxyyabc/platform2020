package cn.com.hsh.platform.util;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import cn.com.hsh.platform.zz.zz_CREATE_API;
import cn.com.hsh.platform.zz.zz_OtherUrl;

//可执行的main 文件调用类，文本main或者控制台main
public class DatabasePublic {
    public static String POST_K3CloudURL = "";
    // Cookie 值
    public static zz_CREATE_API zz_create_api = new zz_CREATE_API();
    private static String CookieVal = null;
    // K3对接的api接口 用在哪？
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

    // HttpURLConnection  格式化 json 格式
    private static HttpURLConnection initUrlConn(String url, JSONArray paras) throws Exception {
        URL postUrl = new URL(POST_K3CloudURL.concat(url));
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        if (CookieVal != null) {
            connection.setRequestProperty("Cookie", CookieVal);
        }
        if (!connection.getDoOutput()) {
            connection.setDoOutput(true);
        }
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(1000 * 60 * 10);//10min 设置时间
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        UUID uuid = UUID.randomUUID();
        int hashCode = uuid.toString().hashCode();
        JSONObject jObj = new JSONObject();
        jObj.put("format", 1);
        jObj.put("useragent", "ApiClient");
        jObj.put("rid", hashCode);
        jObj.put("parameters", chinaToUnicode(paras.toString()));
        //jObj.put("parameters", getUTF8XMLString(paras.toString()));
        //getUTF8XMLString
        jObj.put("timestamp", new Date().toString());
        jObj.put("v", "1.0");
        out.writeBytes(jObj.toString());
        out.flush();
        out.close();
        return connection;
    }

    // Login 方法
    public static boolean Login(String dbId, String user, String pwd, int lang) throws Exception {
        //  DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        boolean bResult = false;
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";
        JSONArray jParas = new JSONArray();
        jParas.add(dbId);// 帐套Id
        jParas.add(user);// 用户名
        jParas.add(pwd);// 密码
        jParas.add(lang);// 语言
        HttpURLConnection connection = initUrlConn(sUrl, jParas);
        // 获取Cookie
        String key = null;
        for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("Set-Cookie")) {
                String tempCookieVal = connection.getHeaderField(i);
                if (tempCookieVal.startsWith("kdservice-sessionid")) {
                    CookieVal = tempCookieVal;
                    break;
                }
            }
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        System.out.println(" ============================= ");
        System.out.println(" Contents of post request ");
        System.out.println(" ============================= ");
        while ((line = reader.readLine()) != null) {
            String sResult = new String(line.getBytes(), "utf-8");
            System.out.println(sResult);
            bResult = line.contains("\"LoginResultType\":1");//登陆类型验证
        }
        System.out.println(" ============================= ");
        System.out.println(" Contents of post request ends ");
        System.out.println(" ============================= ");
        reader.close();

        connection.disconnect();
        return bResult;
    }

    public static String ExcuteOperation(String formId, String content) throws Exception {
        return Invoke("ExcuteOperation", formId, content);
    }

    public static String Query(String formId, String content) throws Exception {
        return Invoke("Query", formId, content);
    }

    public static String OrderQuery(String formId, String content) throws Exception {
        return Invoke("OrderQuery", formId, content);
    }

    public static String Save(String formId, String content) throws Exception {
        return Invoke("Save", formId, content);
    }

    public static String View(String formId, String content) throws Exception {
        return Invoke("View", formId, content);
    }

    public static String Submit(String formId, String content) throws Exception {
        return Invoke("Submit", formId, content);
    }

    public static String Audit(String formId, String content) throws Exception {
        return Invoke("Audit", formId, content);
    }

    public static String UnAudit(String formId, String content) throws Exception {
        return Invoke("UnAudit", formId, content);
    }

    public static String StatusConvert(String formId, String content) throws Exception {
        return Invoke("StatusConvert", formId, content);
    }
    //调用单据时异常处理
    private static String Invoke(String deal, String formId, String content) throws Exception {
        String sUrl = map.get(deal).toString();
        JSONArray jParas = new JSONArray();
        jParas.add(formId);
        jParas.add(content);
        HttpURLConnection connectionInvoke = initUrlConn(sUrl, jParas);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connectionInvoke.getInputStream()));
        String line;
        String sResult = "";
        while ((line = reader.readLine()) != null) {
            sResult = sResult + new String(line.getBytes(), "utf-8");
        }
        reader.close();
        connectionInvoke.disconnect();
        return sResult;
    }

    /**
     * 把中文转成Unicode码
     *
     * @param str
     * @return
     */
    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
   /* public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8="";
        try {

            //String a = URLEncoder.encode("中文测试", "UTF-8");
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8) ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }*/

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 保存接口发货通知单
    public static String hsh_save(String sJson) throws Exception {
        String r = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            String sFormId = "SAL_DELIVERYNOTICE";
            r = DatabasePublic.Save(sFormId, sJson);
        }
        return r;
    }

    // 保存接口装车单
    public static String hsh_save_zcd(String sJson) throws Exception {
        String r = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            String sFormId = "LSQ_ZCD";
            r = DatabasePublic.Save(sFormId, sJson);
        }
        return r;
    }

    //查询接口发货通知单
    public static String lsq_query(String md, String ck, String time) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz_create_api.set_FHTZD_FilterString(md, time, ck));
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    //查询接口发货通知单
    public static String lsq_query_view(String FBillNo) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc";

        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add("SAL_DELIVERYNOTICE");
            jParas.add("{\"CreateOrgId\":0,\"Id\":0,\"Number\":\""+FBillNo+"\"}");
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }
    //查询接口发货通知单
    public static String lsq_query_FBillNo(String FBillNo) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz_create_api.set_FHTZD_FilterString_FBillNo(FBillNo));
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    //查询接口发货通知单
    public static String lsq_queryxh(String md, String ck, String time) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz_create_api.set_XH_FilterString(md, time, ck));// 帐套Id
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    public static String lsq_queryF_pack_Entity_FBillNoStartRow(String FBillNo, String StartRow) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz_create_api.set_F_pack_Entity_FilterString_FBillNoStartRow(FBillNo,StartRow));// 帐套Id
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("d-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }
    //查询接口发货通知单
    public static String lsq_queryF_pack_Entity_FBillNo(String FBillNo) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz_create_api.set_F_pack_Entity_FilterString_FBillNo(FBillNo));// 帐套Id
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("d-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    //查询接口发货通知单
    public static String lsq_queryxh_FBillNo(String FBillNo) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz_create_api.set_XH_FilterString_FBillNo(FBillNo));// 帐套Id
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("d-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    //查询物料
    public static String lsq_querywl(String tm) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz_create_api.set_WL_FilterString(tm));
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static String lsq_shazd_order_query(String mobile, String date) throws Exception {
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetWay,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(mobile);//
            jParas.add(date);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public static String lsq_shazd_order_query_CARNEW(String FOrgID, String date2, String FWAYNUMBER) throws Exception {
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetCARNEW,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(FOrgID);//
            jParas.add(date2);//
            jParas.add(FWAYNUMBER);//

            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    /////////////////////////////////////////////////////////////////////////
    public static String lsq_shazd_order_query(String str1, String str2, String str3) throws Exception {
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetCustomerNEW,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        //ExecuteGetStock
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(str1);//
            jParas.add(str2);//
            jParas.add(str3);//
            //byte[] btBodys = Xml.Encoding.UTF8.GetBytes(body);
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;

    }
//JSONArray ExecuteGetCustomerNEW2(string FOrgID, string date, string FWAYNUMBER,string FSTOCKNUMBER)(组织编码，日期，线路名，仓库编码) 添加线路、仓库过滤
    public static String lsq_shazd_order_query2(String FOrgID, String date, String FWAYNUMBER, String FSTOCKNUMBER) throws Exception {
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetCustomerNEW2,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        //ExecuteGetStock
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(FOrgID);//
            jParas.add(date);//
            jParas.add(FWAYNUMBER);//
            jParas.add(FSTOCKNUMBER);//
            //byte[] btBodys = Xml.Encoding.UTF8.GetBytes(body);
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;

    }

    public static String lsq_shazd_order_query_COLOR(String str1, String str2, String str3) throws Exception {
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetCustomerNEW1,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        //ExecuteGetStock
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(str1);//
            jParas.add(str2);//
            jParas.add(str3);//
            //byte[] btBodys = Xml.Encoding.UTF8.GetBytes(body);
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;

    }

    public static String lsq_shazd_order_query(String str1, String str2, String str3, String str4) throws Exception {
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetStockNEW,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(str1);//
            jParas.add(str2);//
            jParas.add(str3);//
            jParas.add(str4);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    public static String lsq_shazd_order_queryzcxx(String str1, String str2, String str3) throws Exception {
//ExecuteGetINCAR
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetINCARNEW,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(str1);//
            jParas.add(str2);//
            jParas.add(str3);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    /*获取组织*/
    public static String lsq_shazd_order_queryzz() throws Exception {

        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetOrgID,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    /*获取线路
    * */
    public static String lsq_shazd_order_queryxl() throws Exception {

        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetWayNEW,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();

            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }
    /*获取线路传组织
     * */
    public static String lsq_shazd_order_queryxl_zz(String zz) throws Exception {

        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetWayNEW1,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    /*获取仓库传组织
     * */
    public static String lsq_shazd_order_queryck_zz(String zz) throws Exception {

        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteGetStockNEW2,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(zz);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }
    /* 锁单
    * */
    public static String lsq_shazd_order_sd(String FID) throws Exception {
//Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteLockBill,Kingdee.WebAPI.HSHAPI.common.kdsvc
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteLockBill,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(FID);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);

            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }
    /* 锁单（新）
     * */
    public static String lsq_shazd_order_sdNEW(String FID) throws Exception {
//Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteLockBill,Kingdee.WebAPI.HSHAPI.common.kdsvc
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteLOCKBILL2,Kingdee.WebAPI.HSHAPI.common.kdsvc";

        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
           // ForgId int 组织内码，FBillNo 单据编号
            jParas.add(zz_OtherUrl.ZZID);
            jParas.add(FID);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);

            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }
    /*反锁单
    * */
    public static String lsq_shazd_order_fsd(String FID) throws Exception {

        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteUNLockBill,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            jParas.add(FID);//
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }


    /**
     * 向指定URL发送POST请求
     * @param url
     * @param paramMap
     * @return 响应结果
     */
    public static String sendPost(String url, Map<String, String> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // conn.setRequestProperty("Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 设置请求属性
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                while (ite.hasNext()) {
                    String key = ite.next();// key
                    String value = paramMap.get(key);
                    param += key + "=" + value + "&";
                }
                param = param.substring(0, param.length() - 1);
            }

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 中台hr登录接口
     * @param usercode
     * @param password
     * @return
     */
    public static boolean loginHr(String usercode, String password){

        String url="https://main.hsh.com.cn/api/hrcontroller/checkuser"; // 中台hr登录接口

        // map
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appkey", "5A69586C78744F7977574A33784D3271442F30595942516C33747557643849393368576F5962374F7653554F544F367941477355672B50544B374C3479704C70");
        paramMap.put("usercode", usercode);
        paramMap.put("password", password);

        // 调用hr登录接口
        String list1 = sendPost(url, paramMap);

        // 转换
        JSONObject jsonObj = JSONObject.fromObject(list1);
        if(jsonObj.getInt("returnCode")==-1){
            return false;
        }else {
            zz_OtherUrl.loginName=jsonObj.getJSONObject("data").getString("fnameL2");//登录成功后传值 用户名
            zz_OtherUrl.loginId=jsonObj.getJSONObject("data").getString("fnumber");//登录成功后传值 用户ID
            return true;
        }
    }


    //查询接口发货通知单单据状态FDocumentStatus单据状态,FCLOSESTATUS 关闭状态,FCancelStatus 作废状态
    public static String lsq_query_status(String Fbillno) throws Exception {
        String sUrl = "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";//这里的方法要用ExecuteBillQuery才可以自定义字段view就是全部字段

        String sResult = "";
        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();
            //jParas.add("SAL_DELIVERYNOTICE");
            jParas.add("{\"FormId\":\"SAL_DELIVERYNOTICE\",\"FieldKeys\":\"FBillNo,FDocumentStatus,FCLOSESTATUS,FCancelStatus\",\"FilterString\":\"FBillNo='"+Fbillno+"'\",\"OrderString\":\"\",\"TopRowCount\":\"0\",\"StartRow\":\"0\",\"Limit\":\"0\"}");
            HttpURLConnection connection = initUrlConn(sUrl, jParas);
            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }

    /* 反锁单（新）20191129
     * */
    public static String lsq_shazd_order_fsdNEW(String FBILLO) throws Exception {
//Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteLockBill,Kingdee.WebAPI.HSHAPI.common.kdsvc
        //jyx_HSH_webApi.jyx_HSH.ExecuteService,jyx_HSH_webApi
        //Kingdee.Bos.WebApiService.GetCust.ExecuteService,Kingdee.Bos.WebApiService
        String sUrl = "Kingdee.WebAPI.HSHAPI.HSHGETINFO.ExecuteService,Kingdee.WebAPI.HSHAPI.common.kdsvc";
        //String sUrl = "jyx_HSH_webApi.jyx_HSH.ExecuteService,jyx_HSH_webApi.common.kdsvc";

        String sResult = "";

        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
        String dbId = zz_OtherUrl.ZTID;
        String uid = zz_OtherUrl.Admin;
        String pwd = zz_OtherUrl.pwd;
        int lang = zz_OtherUrl.lang;
        if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
            JSONArray jParas = new JSONArray();


            JSONObject jObj = new JSONObject();
            jObj.put("FBILLO",FBILLO);
            jObj.put("ORG", zz_OtherUrl.ZZID);
            jObj.put("FSFSD", false);
            jParas.add(jObj);
            HttpURLConnection connection = initUrlConn(sUrl, jParas);

            // 获取Cookie
            String key = null;
            for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    String tempCookieVal = connection.getHeaderField(i);
                    if (tempCookieVal.startsWith("kdservice-sessionid")) {
                        CookieVal = tempCookieVal;
                        break;
                    }
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sResult = sResult + new String(line.getBytes(), "utf-8");
            }
            reader.close();
            connection.disconnect();
        }
        return sResult;
    }


    public static void main(String[] args) throws Exception {
        //下面写你要测试的方法，如：
//       String ss2 = lsq_shazd_order_query2("101", "2019-10-16", "116601","010025");
//       System.out.println(ss2);

       String ss3 = lsq_query_status("1909263436");
       com.alibaba.fastjson.JSONArray djStatusArray = JSON.parseArray(ss3).getJSONArray(0);
       System.out.println(ss3);
       System.out.println(djStatusArray.get(3));
    }
}
