package cn.com.hsh.platform.util;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class CItem {
    private String ID = "";
    private String Value = "";
    private String Number = "";
    private String F = "1";

    public CItem() {
        ID = "";
        Value = "";
        F = "1";
    }

    public CItem(String _ID, String _Value) {
        ID = _ID;
        Value = _Value;
        F = "1";
    }
    public CItem(String _ID, String _Value, String _F) {
        ID = _ID;
        Value = _Value;
        F = _F;
    }
    public CItem(String _ID, String _Value, String _F, String _Number ) {
        ID = _ID;
        Value = _Value;
        F = _F;
        Number=_Number;
    }
    @Override
    public String toString() {   //为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
        // TODO Auto-generated method stub
        return Value;
    }

    public String GetID() {
        return ID;
    }

    public String GetValue() {
        return Value;
    }
    public String GetF() {
        return F;
    }
    public String GetNumber() {
        return Number;
    }
}
