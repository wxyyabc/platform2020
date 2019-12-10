package cn.com.hsh.platform.zz;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * k3 cloud接口生成工具类
 */

public class zz_API {
    /*
    * 单据标识
    * */
    private String FormId;
    /*
    * 字段值
    * */
    private String FieldKeys;
    /*
    * 过滤条件
    * */
    private String FilterString;
    /*
    * 排序条件
    * */
    private String OrderString;
    /*
     * 分页取数开始行索引，从0开始，例如每页10行数据，第2页开始是10，第3页开始是20
     * */

    /*
    * 显示条数
    * */
    private String Limit;
    private LinkedHashMap<String, String> APIMAP;

    private String APIJSON;

    public  zz_API(HashMap<String, String> APIMAP) {

        this.FormId=APIMAP.get("FormId");
        this.FieldKeys=APIMAP.get("FieldKeys");
        this.FilterString=APIMAP.get("FilterString");
        this.OrderString=APIMAP.get("OrderString");
        APIMAP=new LinkedHashMap<String, String>();

    }
    public String getAPIJSON(){
        String data = "{\"FormId\":\""+FormId+"\",\"FieldKeys\":\""+FieldKeys+"\",\"FilterString\":\""+FilterString+"\",\"OrderString\":\""+OrderString+"\",\"TopRowCount\":\"0\",\"StartRow\":\"0\",\"Limit\":\"2000\"}";
        return data;
    }
    public void setFilterString(String FilterString){
        this.FilterString= FilterString;
    }
    public String[] getFieldKeys() {
        return FieldKeys.replace(".","_").split(",");//replaceAll
    }
}
