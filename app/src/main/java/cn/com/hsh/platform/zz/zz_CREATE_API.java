package cn.com.hsh.platform.zz;

import java.util.HashMap;

/**
 * k3 cloud API 接口生成字段
 */

public class zz_CREATE_API {
    private  zz_API FHTZD,MD,CK,WL,XH,F_pack_Entity;
    public zz_CREATE_API(){
        HashMap<String, String> FHTZDmap=new HashMap<String, String>();
        FHTZDmap.put("FormId","SAL_DELIVERYNOTICE");
        FHTZDmap.put("FieldKeys","FID,FBillTypeID.FNumber,FBillNo,FDate," +
                "FCustomerID,FCustomerID.FNumber,FSettleCurrID.FNumber," +
                "FSaleOrgId.FNumber,FDeliveryOrgID.FNumber," +
                "FLOGID.FNAME,FORDER,FSFJH,FSFSD,FCustomerID.FName," +
                "FEntity_FEntryID,FMaterialID,FMaterialName,FBARCODE,FUnitID.FNumber,FQty,FACTQTY,FMWEIGTH," +
                "FStockID,FStockID.FNumber,FStockID.FNAME," +
                "FPDAQTY,FOUTSTOCKLMTH,FOUTSTOCKLMTL,FMaterialID.FNumber,FMaterialID.FOrder");
        FHTZDmap.put("FilterString","");
        FHTZDmap.put("OrderString","FMaterialID.FOrder ASC");
       // FHTZDmap.put("OrderString","FID ASC");
        //

        FHTZD=new zz_API(FHTZDmap);
        //////////////////////////////////////////////////////
        HashMap<String, String> MDmap=new HashMap<String, String>();
        MDmap.put("FormId","BD_Customer");
        MDmap.put("FieldKeys","FCUSTID,FName,FNumber,FCreateOrgId,FUseOrgId");
        MDmap.put("FilterString","");
        MDmap.put("OrderString","");
        MD=new zz_API(MDmap);
        /////////////////////////////////////////////////////
        HashMap<String, String> CKmap=new HashMap<String, String>();
        CKmap.put("FormId","BD_STOCK");
        CKmap.put("FieldKeys","FStockId,FName,FNumber,FCreateOrgId,FUseOrgId");
        CKmap.put("FilterString","");
        CKmap.put("OrderString","");
        CK=new zz_API(CKmap);
        /////////////////////////////////////////////////////
        HashMap<String, String> WLmap=new HashMap<String, String>();
        WLmap.put("FormId","BD_MATERIAL");
        WLmap.put("FieldKeys","FMATERIALID,FBaseUnitId,FName,FNumber,FCreateOrgId,FUseOrgId, FUseOrgId.FNumber");
        WLmap.put("FilterString","");
        WLmap.put("OrderString","");
        WL=new zz_API(WLmap);
        //////////////////////////////////////////////////////
        HashMap<String, String> XHmap=new HashMap<String, String>();
        XHmap.put("FormId","SAL_DELIVERYNOTICE");
        XHmap.put("FieldKeys","FID,FZXDEntity_FEntryID,FXH,FSFYZC");
        XHmap.put("FilterString","");
        XHmap.put("OrderString","");
        XH=new zz_API(XHmap);
        //////////////////////////////////////////////////////

        HashMap<String, String> F_pack_Entitymap=new HashMap<String, String>();
        F_pack_Entitymap.put("FormId","SAL_DELIVERYNOTICE");
        F_pack_Entitymap.put("FieldKeys","FID,FMaterialID3.FNumber,FPACKNUMBER3,FCODE3,FQty3,F_pack_Entity_FEntryID");
        F_pack_Entitymap.put("FilterString","");
        F_pack_Entitymap.put("OrderString","");
        F_pack_Entity=new zz_API(F_pack_Entitymap);
        //////////////////////////////////////////////////////



    }
    public String set_FHTZD_FilterString(String md, String time, String ck){
        FHTZD.setFilterString("FCustomerID="+md+" and FDate=  to_date('"+time+"','yyyy-mm-dd " +
                "hh24:mi:ss') and (FDOCUMENTSTATUS='Z'or FDOCUMENTSTATUS='D'or " +
                "FDOCUMENTSTATUS='A') and FCLOSESTATUS='A' and FCANCELSTATUS='A' and FStockID="+ck+" and FDeliveryOrgID.FNumber="+zz_OtherUrl.ZZID);

      /*  FHTZD.setFilterString("FCustomerID="+md+" and FDate=  to_date('"+time+"','yyyy-mm-dd " +
                "hh24:mi:ss') and FStockID="+ck+" ");*/
        return FHTZD.getAPIJSON();
    }
    public String set_FHTZD_FilterString_FBillNo(String FBillNo){
       FHTZD.setFilterString("FBillNo='"+FBillNo+"'  and (FDOCUMENTSTATUS='Z'or FDOCUMENTSTATUS='D'or " +
                "FDOCUMENTSTATUS='A')  and FCLOSESTATUS='A' and FCANCELSTATUS='A'  and FDeliveryOrgID.FNumber="+zz_OtherUrl.ZZID);
/*        FHTZD.setFilterString("FBillNo='"+FBillNo+"'   ");*/
        return FHTZD.getAPIJSON();
    }
    public String set_WL_FilterString(String tm){
       // WL.setFilterString("FBARCODE = '"+tm+"'"+" and FDOCUMENTSTATUS='C'");
       // WL.setFilterString("FBARCODE = '"+tm+"'"+" and FDOCUMENTSTATUS='C' and FUseOrgId=100"+zz_OtherUrl.ZZID);
        WL.setFilterString("FBARCODE = '"+tm+"'"+" and FDOCUMENTSTATUS='C' and FUseOrgId.FNumber="+zz_OtherUrl.ZZID);
        //WL.setFilterString("FBARCODE = '"+tm+"'"+" and FDOCUMENTSTATUS='C' and FUseOrgId='100' ");
        //.CreateOrgId
        return WL.getAPIJSON();
    }
    public String set_XH_FilterString(String md, String time, String ck){
        XH.setFilterString("FCustomerID="+md+" and FDate=  to_date('"+time+"','yyyy-mm-dd hh24:mi:ss')   and FStockID="+ck+" ");
        return XH.getAPIJSON();
    }
    public String set_XH_FilterString_FBillNo(String FBillNo){
        XH.setFilterString("FBillNo='"+FBillNo+"' ");
        return XH.getAPIJSON();
    }
    public String set_F_pack_Entity_FilterString_FBillNo(String FBillNo){
        F_pack_Entity.setFilterString("FBillNo='"+FBillNo+"' ");
        return F_pack_Entity.getAPIJSON();
    }
    public String set_F_pack_Entity_FilterString_FBillNoStartRow(String FBillNo, String StartRow){
        String data = "{\"FormId\":\"SAL_DELIVERYNOTICE\",\"FieldKeys\":\"FID,FMaterialID3.FNumber,FPACKNUMBER3,FCODE3,FQty3,F_pack_Entity_FEntryID\",\"FilterString\":\"FBillNo='"+FBillNo+"'\",\"OrderString\":\"\",\"TopRowCount\":\"0\",\"StartRow\":\""+StartRow+"\",\"Limit\":\"2000\"}";
        return data;
    }
    public zz_API getFHTZD(){
        return FHTZD;
    }

}
