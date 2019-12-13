package cn.com.hsh.platform.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import cn.com.hsh.platform.zz.zz_CREATE_API;


public class KingdeePublic {

    //kingdee 服务器地址

    public static final String KINGDEE_BASE_PATH = "http://192.168.1.88";
    /**
     * 用户验证
     */
    public static final String KINGDEE_VALIDATE_USER_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";

    /**
     * 保存
     */
    public static final String KINGDEE_SAVE_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc";

    /**
     * 暂存
     */
    public static final String KINGDEE_DRAFT_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Draft.common.kdsvc";

    /**
     * 推送
     */
    public static final String KINGDEE_PUSH_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Push.common.kdsvc";

    /**
     * 审核
     */
    public static final String KINGDEE_AUDIT_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc";
    /**
     * 删除
     */
    public static final String KINGDEE_DELETE_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Delete.common.kdsvc";
    /**
     * 反审核
     */
    public static final String KINGDEE_UNAUDIT_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc";
    /**
     * 提交
     */
    public static final String KINGDEE_SUBMIT_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc";
    /**
     * 查看
     */
    public static final String KINGDEE_VIEW_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc";
    /**
     * 状态转换
     */
    public static final String KINGDEE_STATUS_CONVERT_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.StatusConvert.common.kdsvc";
    /**
     * 批量查询
     */
    public static final String KINGDEE_EXCCUTE_BILL_QUERY_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc";
    /**
     * 批量保存
     */
    public static final String KINGDEE_BATCH_SAVE_URL=KINGDEE_BASE_PATH+"/K3Cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.BatchSave.common.kdsvc";

    public static zz_CREATE_API zz_create_api = new zz_CREATE_API();
    private static String CookieVal = null;
    private static Map map = new HashMap<String, String>();


    public static void main(String[] args) throws Exception {

    }
}
