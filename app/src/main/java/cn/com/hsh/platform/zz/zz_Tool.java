package cn.com.hsh.platform.zz;

import org.apache.commons.collections.MapUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class zz_Tool {


    //执行内容


   /***
            *  获取指定日后 后 dayAddNum 天的 日期
 *  @param day  日期，格式为String："2013-9-3";
 *  @param dayAddNum 增加天数 格式为int;
 *  @return
         */
    public static String getDateStr(String day, long dayAddNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    public static  String[] crossfix(String data[], String data1[])
    {
        String result[] = new String[data.length + data1.length];
        int common = Math.min(data.length, data1.length);
        String long_data[] = data.length > data1.length ? data:data1;
        int max = long_data.length;
        int j = 0;
        for(int i = 0; i < common; i++)
        {
            result[j++] = data[i];
            result[j++] = data1[i];
        }
        for(int i = common; i < max; i++)
        {
            result[j++] = long_data[i];
        }
        return result;
    }

    public static ArrayList<Map> getApiMap(String StrArrsStr, String[] keys)
    {
        String[] StrArrs=StrArrsStr.split("],");
        ArrayList<Map> arrayList=new ArrayList<Map>();
        for(String StrArr:StrArrs){
            String[] values = StrArr.split(",");
            int n = 0;
            for (String str : values) {
                values[n] = str.replace("]", "").replace("[", "").replace("\"", "");
                n++;
            }
            if(values[2].equals("HelpLink:null")){
                return null;
            }
            String[] HB= zz_Tool.crossfix(keys,values);
            Map map = MapUtils.putAll(new LinkedHashMap<String, String>(), HB);
            arrayList.add(map);
        }
        return arrayList;
    }
}
