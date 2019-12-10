package cn.com.hsh.platform.zz;

/**
 * sqlite表创建及读取
 */

public class zz_CREATE {
    private zz_Table ZCDHEAD = new zz_Table("ZCDHEAD");
    private zz_Table ZCDBODY = new zz_Table("ZCDBODY");
    private zz_Table ZT = new zz_Table("ZT");
    private zz_Table ZZ = new zz_Table("ZZ");
    private zz_Table Login_Info = new zz_Table("Login_Info");
    private zz_Table FHTZD_XL = new zz_Table("FHTZD_XL");
    private zz_Table CK = new zz_Table("CK");
    private zz_Table MD = new zz_Table("MD");
    private zz_Table FHTZD_Head = new zz_Table("FHTZD_Head");
    private zz_Table FHTZD_Body = new zz_Table("FHTZD_Body");
    private zz_Table FHTZD_XH_Body = new zz_Table("FHTZD_XH_Body");
    private zz_Table XL = new zz_Table("XL");
    private zz_Table CARNEW = new zz_Table("CARNEW");
    private zz_Table SCANINFO = new zz_Table("SCANINFO");//扫描信息表

    private zz_Table F_pack_Entity = new zz_Table("F_pack_Entity");
    public zz_Table ZCDHEAD() {
        return ZCDHEAD;
    }

    public zz_Table ZCDBODY() {
        return ZCDBODY;
    }

    public zz_Table ZT() {
        return ZT;
    }

    public zz_Table ZZ() {
        return ZZ;
    }

    public zz_Table Login_Info() {
        return Login_Info;
    }

    public zz_Table FHTZD_XL() {
        return FHTZD_XL;
    }

    public zz_Table CK() {
        return CK;
    }

    public zz_Table MD() {
        return MD;
    }

    public zz_Table FHTZD_Head() {
        return FHTZD_Head;
    }

    public zz_Table FHTZD_Body() {
        return FHTZD_Body;
    }

    public zz_Table FHTZD_XH_Body() {
        return FHTZD_XH_Body;
    }

    public zz_Table XL() {
        return XL;
    }
    public zz_Table CARNEW() {
        return CARNEW;
    }
    public zz_Table F_pack_Entity() {
        return F_pack_Entity;
    }
    public zz_Table SCANINFO(){
        return  SCANINFO;
    }
    public zz_CREATE() {
        F_pack_Entity.add("FID","NVARCHAR(50)");
        F_pack_Entity.add("FMaterialID3", "NVARCHAR(50)");//FMaterialID.NUMBER
        F_pack_Entity.add("FPACKNUMBER3", "NVARCHAR(50)");
        F_pack_Entity.add("FCODE3", "NVARCHAR(50)");
        F_pack_Entity.add("FQty3", "double");
        F_pack_Entity.add("FEntryID", "NVARCHAR(50)");
      /*  FMaterialID3 物料
        FPACKNUMBER3 箱号
        FCODE3 扫的条码
        FQty3 数量*/
        ZT.add("ZTID", "NVARCHAR(50)");
        ZT.add("ZTIP", "NVARCHAR(50)");
        ZT.add("TYPE", "NVARCHAR(50)");

        ZZ.add("ZZID", "NVARCHAR(50)");
        ZZ.add("ZZNAME", "NVARCHAR(50)");
        ZZ.add("TYPE", "NVARCHAR(50)");

        Login_Info.add("username", "NVARCHAR(50)");
        Login_Info.add("password", "NVARCHAR(50)");
        Login_Info.add("loginInTime", "NVARCHAR(50)");
        Login_Info.add("loginOutTime", "NVARCHAR(50)");

        FHTZD_XL.add("FBILLNO", "NVARCHAR(50)");
        FHTZD_XL.add("FCUSTID", "NVARCHAR(50)");
        FHTZD_XL.add("FCUSTNUMBER", "NVARCHAR(50)");
        FHTZD_XL.add("FCUSTNAME", "NVARCHAR(50)");
        FHTZD_XL.add("FORDER", "NVARCHAR(50)");
        FHTZD_XL.add("FWAY", "NVARCHAR(50)");
        FHTZD_XL.add("FXH", "NVARCHAR(50)");
        FHTZD_XL.add("FXQty", "NVARCHAR(50)");
        FHTZD_XL.add("ZZ", "NVARCHAR(50)");
        FHTZD_XL.add("RQ", "NVARCHAR(50)");
        FHTZD_XL.add("XL", "NVARCHAR(50)");
        FHTZD_XL.add("SFZC", "int");


        CARNEW.add("FID","NVARCHAR(50)");
        CARNEW.add("FBILLNO","NVARCHAR(50)");
        CARNEW.add("FCUSTNAME","NVARCHAR(50)");
        CARNEW.add("FNAME","NVARCHAR(50)");
        CARNEW.add("FQTY","NVARCHAR(50)");
        CARNEW.add("FOUTMINQTY","NVARCHAR(50)");




        CK.add("Fstockid", "NVARCHAR(50)");
        CK.add("FSTOCKNAME", "NVARCHAR(50)");
        CK.add("FSTOCKNUMBER", "NVARCHAR(50)");

        MD.add("FCUSTOMERID", "NVARCHAR(50)");
        MD.add("FCUSTNAME", "NVARCHAR(50)");
        MD.add("FCUSTNUMBER", "NVARCHAR(50)");

        FHTZD_Head.add("FID", "NVARCHAR(50)");
        FHTZD_Head.add("FBillTypeID_FNumber", "NVARCHAR(50)");
        FHTZD_Head.add("FBillNo", "NVARCHAR(50)");
        FHTZD_Head.add("FDate", "NVARCHAR(50)");
        FHTZD_Head.add("FCustomerID", "NVARCHAR(50)");
        FHTZD_Head.add("FCustomerID_FNumber", "NVARCHAR(50)");
        FHTZD_Head.add("FSettleCurrID_FNumber", "NVARCHAR(50)");
        FHTZD_Head.add("FSaleOrgId_FNumber", "NVARCHAR(50)");
        FHTZD_Head.add("FDeliveryOrgID_FNumber", "NVARCHAR(50)");
        FHTZD_Head.add("FWAY", "NVARCHAR(50)");
        FHTZD_Head.add("FORDER", "NVARCHAR(50)");//FLOGID.FNAME
        FHTZD_Head.add("FSFJH", "NVARCHAR(50)");
        FHTZD_Head.add("FSFSD", "NVARCHAR(50)");
        FHTZD_Head.add("FCustomerID_FName", "NVARCHAR(50)");


        FHTZD_Body.add("FID", "NVARCHAR(50)");
        FHTZD_Body.add("FEntity_FEntryID", "NVARCHAR(50)");
        FHTZD_Body.add("FMaterialID", "NVARCHAR(50)");
        FHTZD_Body.add("FMaterialName", "NVARCHAR(50)");
        FHTZD_Body.add("FBARCODE", "NVARCHAR(50)");
        FHTZD_Body.add("FUnitID_FNumber", "NVARCHAR(50)");
        FHTZD_Body.add("FQty", "double");
        FHTZD_Body.add("FACTQTY", "double");//应发数量
        FHTZD_Body.add("FMWEIGTH", "NVARCHAR(50)");
        FHTZD_Body.add("FStockID", "NVARCHAR(50)");
        FHTZD_Body.add("FStockID_FNumber", "NVARCHAR(50)");
        FHTZD_Body.add("FStockID_FNAME", "NVARCHAR(50)");
        FHTZD_Body.add("FPDAQTY", "double");
        FHTZD_Body.add("FOUTSTOCKLMTH", "double");
        FHTZD_Body.add("FOUTSTOCKLMTL", "double");
        FHTZD_Body.add("FMaterialID_FNumber", "NVARCHAR(50)");
        FHTZD_Body.add("FMaterialID_FOrder", "double");

        FHTZD_XH_Body.add("FID", "NVARCHAR(50)");
        FHTZD_XH_Body.add("FZXDEntity_FEntryID", "NVARCHAR(50)");
        FHTZD_XH_Body.add("FXH", "NVARCHAR(50)");
        FHTZD_XH_Body.add("FSFYZC", "NVARCHAR(50)");



        ZCDHEAD.add("FDate", "NVARCHAR(50)");
        ZCDHEAD.add("FSJ", "NVARCHAR(50)");
        ZCDHEAD.add("FCP", "NVARCHAR(50)");
        ZCDHEAD.add("FXL", "NVARCHAR(50)");
        ZCDHEAD.add("FZZ", "NVARCHAR(50)");
        ZCDHEAD.add("FID", "NVARCHAR(50)");
        ZCDHEAD.add("FXL_name", "NVARCHAR(50)");

        ZCDBODY.add("FID", "NVARCHAR(50)");
        ZCDBODY.add("FEntryID", "NVARCHAR(50)");
        ZCDBODY.add("FXH", "NVARCHAR(50)");
        ZCDBODY.add("FCUSTID_FNumber", "NVARCHAR(50)");
        ZCDBODY.add("FXL", "NVARCHAR(50)");
        ZCDBODY.add("FQTY", "NVARCHAR(50)");
        ZCDBODY.add("FBILLNO", "NVARCHAR(50)");
        ZCDBODY.add("FORDER", "NVARCHAR(50)");
        ZCDBODY.add("FCUSTNAME", "NVARCHAR(50)");
        ZCDBODY.add("XL_name", "NVARCHAR(50)");

        XL.add("FWAY", "NVARCHAR(50)");
        XL.add("FNUMBER", "NVARCHAR(50)");
        //扫描信息表
        SCANINFO.add("SCANNUM","NVARCHAR(50)");
        SCANINFO.add("BARCODE","NVARCHAR(50)");
        SCANINFO.add("SCANDATE","NVARCHAR(50)");
    }
}
