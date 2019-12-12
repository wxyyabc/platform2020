package cn.com.hsh.platform.model;

import com.alibaba.fastjson.JSONObject;

public class HrUser {
    int returnCode;

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public void setExData(JSONObject exData) {
        this.exData = exData;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    String returnMsg;

    public int getReturnCode() {
        return returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public JSONObject getExData() {
        return exData;
    }

    public JSONObject getData() {
        return data;
    }

    JSONObject exData;
    JSONObject data;
}
