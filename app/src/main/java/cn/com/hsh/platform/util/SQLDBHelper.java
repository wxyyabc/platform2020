package cn.com.hsh.platform.util;
/**
 * sqlit数据库工具类
 * 只使用了扫描拣货功能，装车功能没有使用
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import cn.com.hsh.platform.zz.zz_CREATE;


public class SQLDBHelper extends SQLiteOpenHelper {
    public static zz_CREATE zz_create = new zz_CREATE();//调用

 public SQLDBHelper(Context context) {
        super(context, "HSH1120.db", null, 1);
    }


    // 数据库第一次创建时候调用
    public void onCreate(SQLiteDatabase arg0) {

        arg0.execSQL(zz_create.ZCDHEAD().getCr());
        arg0.execSQL(zz_create.ZCDBODY().getCr());
        arg0.execSQL(zz_create.ZT().getCr());
        arg0.execSQL(zz_create.ZZ().getCr());
        arg0.execSQL(zz_create.Login_Info().getCr());
        arg0.execSQL(zz_create.FHTZD_XL().getCr());
        arg0.execSQL(zz_create.CK().getCr());
        arg0.execSQL(zz_create.MD().getCr());
        arg0.execSQL(zz_create.FHTZD_Head().getCr());
        arg0.execSQL(zz_create.FHTZD_Body().getCr());
        arg0.execSQL(zz_create.FHTZD_XH_Body().getCr());
        arg0.execSQL(zz_create.XL().getCr());
        arg0.execSQL(zz_create.CARNEW().getCr());
        arg0.execSQL(zz_create.F_pack_Entity().getCr());
        arg0.execSQL(zz_create.ZT().getIn_D(), new String[]{"5acf551052a25c", "192.168.1.88", "1"});//内网88   5acf551052a25c  45 5d9bd4136dc2b4
//        arg0.execSQL(zz_create.ZT().getIn_D(), new String[]{"5d9bd4136dc2b4", "192.168.1.45", "1"});//内网88   5acf551052a25c  45 5d9bd4136dc2b4

    }

    // 数据库文件版本号发生变化时调用
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.v("sendBill", "Upgrating database will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS sendBill");
        onCreate(db);
    }

    /**
     * 插入装车单单据体
     * @param strings
     * @param db
     * @return
     */
    public String insertZCDBODY(String[] strings, SQLiteDatabase db) {
        //排除重复

        Cursor body4 = db.rawQuery(zz_create.ZCDBODY().getSe(new String[]{}, new String[]{"FXH", "FCUSTID_FNumber"}), new String[]{strings[2], strings[3]});
        if (body4 != null && body4.getCount() > 0) {
            return "箱号已经装车";
        } else {

        }
        int FORDER = 999;
        int sx = 0;
        Cursor body = db.rawQuery(zz_create.FHTZD_XL().getSe(new String[]{"FORDER"}, new String[]{"FXH", "FCUSTNUMBER"}), new String[]{strings[2], strings[3]});
        if (body != null && body.getCount() > 0) {
            while (body.moveToNext()) {
                FORDER = body.getInt(0);//顺序
                break;
            }
        } else {
            return "没有装车顺序";
        }
        ////////////////////////////////////////////////////////////////////////////
        /// ////////////////////////////////////////判断当前装车单有几个门店，和门店的有几个箱子
        // body = db.rawQuery(zz_create.FHTZD_XL().getSe(new String[]{"FORDER"},new String[]{"FXH","FCUSTNUMBER"}), new String[]{strings[2],strings[3]});
        body = db.rawQuery("select FCUSTID_FNumber,COUNT(*) from ZCDBODY  GROUP BY FCUSTID_FNumber", new String[]{});//最大先装车
        ArrayList<CItem> arrayList1 = new ArrayList<CItem>();
        if (body != null && body.getCount() > 0) {
            while (body.moveToNext()) {
                CItem ct = new CItem(body.getString(0), String.valueOf(body.getInt(1)));
                arrayList1.add(ct);
            }
        }
        ////////////////////////////////////////////////////////////////////////////
        /// ////////////////////////////////////////判断当前取到的信息有几个门店，和门店的有几个箱子
        body = db.rawQuery("select FCUSTNUMBER,COUNT(*) from FHTZD_XL  GROUP BY FCUSTNUMBER", new String[]{});//最大先装车
        ArrayList<CItem> arrayList2 = new ArrayList<CItem>();
        if (body != null && body.getCount() > 0) {
            while (body.moveToNext()) {
                CItem ct = new CItem(body.getString(0), String.valueOf(body.getInt(1)));
                arrayList2.add(ct);
            }
        }
        //////////////////////////////////////////////////////////
        /// //////////////////////////////////////////////////////////比对两者的数据是否一致
        ArrayList<String> arrayList3 = new ArrayList<String>();
        for (CItem a1 : arrayList1) {
            for (CItem a2 : arrayList2) {
                if (a1.GetID().equals(a2.GetID())) {
                    //门店一样比对箱数
                    if (!a1.GetValue().equals(a2.GetValue())) {
                        arrayList3.add(a1.GetID());
                        //箱数不一样
                        break;
                    }
                }

            }
        }

        //////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////判断是否可以添加箱号
        if (arrayList3.size() > 0) {
            for (String a2 : arrayList3) {
                if (!a2.equals(strings[3])) {
                    return "还有门店的箱号未扫描完。";
                }

            }
        }


        //////////////////////////////////////////////////////////
        /// /////////////////////////////////////////////////////////

        //查询A门店所有的箱号装完没有
        //if(Integer.valueOf(sx)==Integer.valueOf(FORDER)){
        //插入
        String sql = zz_create.ZCDBODY().getIn_D();

        db.execSQL(sql, strings);
        //更新
        db.execSQL("update FHTZD_XL  set SFZC='1' where FXH=? and FCUSTNUMBER=?", new String[]{strings[2], strings[3]});
        // } else
        // {
        //    return "该箱号不能装车";
        // }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////查询提示按顺序还有多少箱子没扫
        Cursor body1 = db.rawQuery("select FORDER from FHTZD_XL where SFZC='0' ", new String[]{});//最大先装车
        if (body1 != null && body1.getCount() > 0) {
            while (body1.moveToNext()) {
                if (body1.getInt(0) > sx) {
                    sx = body1.getInt(0);
                }
                //顺序
            }
            int wxh = 0;
            int wxh2 = 0;
            Cursor body2 = db.rawQuery("select FORDER from FHTZD_XL where SFZC='0' and FORDER>=?", new String[]{String.valueOf(FORDER)});//前面有多少没装
            if (body2 != null && body2.getCount() > 0) {
                while (body2.moveToNext()) {
                    int n = body2.getInt(0);
                    int n2 = Integer.valueOf(FORDER);
                    if (n > n2) {
                        wxh++;
                    }
                    if (n == n2) {
                        wxh2++;
                    }
                }
            }
            if (Integer.valueOf(sx) == Integer.valueOf(FORDER)) {

            } else {
                return "前面还有其他门店" + wxh + "条箱号未扫，本门店" + wxh2 + "条箱号未扫";
            }
        }
        return "true";
    }

    //ZCDBODY
    public Cursor[] selectaZCDBODYID(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.ZCDBODY().getSe(new String[]{}, new String[]{"FID"}), strings);
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    //
    public void insertZCDHEAD(String[] strings, SQLiteDatabase db) {
        //插入除_id 之外的所有字段
        String sql = zz_create.ZCDHEAD().getIn_D();
        db.execSQL(sql, strings);
    }

    public Cursor[] selectaZCDHEAD(SQLiteDatabase db) {
        String[] strings = new String[]{};
        Cursor cursor = db.rawQuery(zz_create.ZCDHEAD().getSe_D(), strings);
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    //selectaZCDHEADONE
    public Cursor[] selectaZCDHEADONE(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.ZCDHEAD().getSe(new String[]{}, new String[]{"FDate", "FSJ", "FCP", "FXL", "FZZ"}), strings);
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    public Cursor[] selectaZCDHEADID(String[] strings, SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(zz_create.ZCDHEAD().getSe(new String[]{}, new String[]{"FID"}), strings);
        Cursor[] cursors = new Cursor[]{cursor};


        return cursors;//返回一个Cursor[] 数组
    }
    //selectaZCDHEADID

    public Cursor[] selectCARNEW_D(SQLiteDatabase db) {
        //strings传入一个参数FID
        Cursor cursor = db.rawQuery(zz_create.CARNEW().getSe_D(), new String[]{});
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    public void insertF_pack_Entity(String[] strings, SQLiteDatabase db) {
        //插入除_id 之外的所有字段5
        String sql = zz_create.F_pack_Entity().getIn_D();
        db.execSQL(sql, strings);
    }
    public void insertF_pack_EntitySZ(String[] strings, SQLiteDatabase db) {
        //插入除_id 之外的所有字段5


        Cursor body4 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID","FCODE3"}), new String[]{strings[0],strings[3]});
        if (body4 != null && body4.getCount() > 0) {

        } else {
            String sql = zz_create.F_pack_Entity().getIn_D();
            db.execSQL(sql, strings);
        }

    }
    public void deleteF_pack_Entity(String[] strings, SQLiteDatabase db) {
        //删除  之外的所有字段5
        db.execSQL(zz_create.F_pack_Entity().getDe(new String[]{"FID","FCODE3"}), new String[]{strings[0],strings[3]});

    }
    public void deleteF_pack_Entity_FMaterialID3_Fnumber(String[] strings, SQLiteDatabase db) {
        //删除  之外的所有字段5
        db.execSQL(zz_create.F_pack_Entity().getDe(new String[]{"FID","FMaterialID3"}), new String[]{strings[0],strings[1]});

    }
    public void deleteF_pack_Entity_row_xh(String[] strings, SQLiteDatabase db) {
        //删除行//之后把数量xh干掉
        db.execSQL(zz_create.F_pack_Entity().getDe(new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}),  new String[]{strings[0],strings[1],strings[2],strings[3]});

    }
    public void updataF_pack_Entity_Duplicate(String[] strings, SQLiteDatabase db) {
        //删除  之外的所有字段5//更新操作
        db.execSQL(zz_create.F_pack_Entity().getUp(new String[]{"FQty3"}, new String[]{ "FQty3-?"}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[4],strings[0],strings[1],strings[2],strings[3]});

       String str= zz_create.F_pack_Entity().getDe(new String[]{"FID","FMaterialID3","FCODE3","FQty3"});
       db.execSQL(str, new String[]{strings[0],strings[1],strings[3],"0"});
    }

    public void insertF_pack_Entity_Duplicate_xhcf(String[] strings, SQLiteDatabase db) {
        //判断有没有重复有--—更新+1  没有插入
        //插入除_id 之外的所有字段5
        Cursor body4 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[0],strings[1],strings[2],strings[3]});
        if (body4 != null && body4.getCount() > 0) {
            db.execSQL(zz_create.F_pack_Entity().getUp(new String[]{"FQty3"}, new String[]{ "?"}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[4],strings[0],strings[1],strings[2],strings[3]});

        } else {
            db.execSQL(zz_create.F_pack_Entity().getIn_D(), strings);

        }

    }
    public void insertF_pack_Entity_Duplicate(String[] strings, SQLiteDatabase db) {
        //判断有没有重复有--—更新+1  没有插入
        //插入除_id 之外的所有字段5
        Cursor body4 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[0],strings[1],strings[2],strings[3]});
        if (body4 != null && body4.getCount() > 0) {
            db.execSQL(zz_create.F_pack_Entity().getUp(new String[]{"FQty3"}, new String[]{ "FQty3+?"}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[4],strings[0],strings[1],strings[2],strings[3]});

        } else {
            db.execSQL(zz_create.F_pack_Entity().getIn_D(), strings);

        }

    }

    public void insertF_pack_Entity_Duplicate_tiaoma(String[] strings, SQLiteDatabase db) {
        //判断有没有重复有--—更新+1  没有插入

        //获取strings[3]---
        //
         String FCODE3=strings[3];
        Cursor body = db.rawQuery("select  FBARCODE from FHTZD_Body where FID=? and FMaterialID_FNumber=?", new String[]{strings[0],strings[1]});
        if (body != null && body.getCount() > 0) {

            while (body.moveToNext()) {
                FCODE3=body.getString(0);
                break;

            }
        }

        strings[3]=FCODE3;
        //插入除_id 之外的所有字段5
        Cursor body4 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[0],strings[1],strings[2],strings[3]});
        if (body4 != null && body4.getCount() > 0) {
            db.execSQL(zz_create.F_pack_Entity().getUp(new String[]{"FQty3"}, new String[]{ "FQty3+?"}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[4],strings[0],strings[1],strings[2],strings[3]});

        } else {
            db.execSQL(zz_create.F_pack_Entity().getIn_D(), strings);

        }

    }

    public boolean selectF_pack_Entity_Duplicate(String[] strings, SQLiteDatabase db) {
        //判断有没有重复有--—更新+1  没有插入
        //插入除_id 之外的所有字段5
        Cursor body4 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID","FCODE3"}), new String[]{strings[0],strings[3]});
        if (body4 != null && body4.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }
    public void updataF_pack_Entity_Duplicate_row(String[] strings, SQLiteDatabase db) {
        //判断有没有重复有--—更新+1  没有插入
        //插入除_id 之外的所有字段5

            db.execSQL(zz_create.F_pack_Entity().getUp(new String[]{"FQty3"}, new String[]{ "?"}, new String[]{"FID","FMaterialID3","FPACKNUMBER3","FCODE3"}), new String[]{strings[4],strings[0],strings[1],strings[2],strings[3]});



    }


    //插入装车详细
    public void insertFHTZD_XL(String[] strings, SQLiteDatabase db) {
        //插入除_id 之外的所有字段
        String sql = zz_create.FHTZD_XL().getIn_D();
        db.execSQL(sql, strings);
    }

    //查询装车详细
    public Cursor[] selectFHTZD_XL_D(SQLiteDatabase db) {
        //strings传入一个参数FID
        Cursor cursor = db.rawQuery(zz_create.FHTZD_XL().getSe_D(), new String[]{});
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    public Cursor[] selectFHTZD_XL_BT(String[] strings, SQLiteDatabase db) {
        //strings传入一个参数FID
        Cursor cursor = db.rawQuery(zz_create.FHTZD_XL().getSe(new String[]{}, new String[]{"ZZ", "RQ", "XL", "FBILLNO", "FXH"}), strings);
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    public Cursor[] selectFHTZD_XL_BT_XH(String[] strings, SQLiteDatabase db) {
        //strings传入一个参数FID
        Cursor cursor = db.rawQuery(zz_create.FHTZD_XL().getSe(new String[]{}, new String[]{"RQ", "XL", "FXH"}), strings);
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    //插入线路
    public void insertXL(String[] strings, SQLiteDatabase db) {
        //插入除_id 之外的所有字段
        String sql = zz_create.XL().getIn_D();
        db.execSQL(sql, strings);
    }

    public void insertCARNEW(String[] strings, SQLiteDatabase db) {
        //插入除_id 之外的所有字段
        String sql = zz_create.CARNEW().getIn_D();
        db.execSQL(sql, strings);
    }

    public void deleteCARNEW(SQLiteDatabase db) {
        String[] strings = new String[]{};
        db.execSQL(zz_create.CARNEW().getDe_D(), strings);
    }

    //删除线路
    public void deleteXL(SQLiteDatabase db) {
        String[] strings = new String[]{};
        db.execSQL(zz_create.XL().getDe_D(), strings);
    }    //查询线路

    public Cursor selectXL(SQLiteDatabase db) {
        String[] strings = new String[]{};
        Cursor cursor = db.rawQuery(zz_create.XL().getSe_D(), strings);
        return cursor;
    }

    public Cursor selectZZ(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.ZZ().getSe(new String[]{}, new String[]{"TYPE"}), new String[]{"1"});
        return cursor;
    }

    public Cursor selectZT(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.ZT().getSe(new String[]{}, new String[]{"TYPE"}), new String[]{"1"});
        return cursor;
    }

    //插入仓库
    public void insertCK(String[] strings, SQLiteDatabase db) {
        //插入除_id 之外的所有字段
        db.execSQL(zz_create.CK().getIn_D(), strings);
    }

    //插入客户
    public void insertMD(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.MD().getSe(new String[]{}, new String[]{"FCUSTOMERID", "FCUSTNAME", "FCUSTNUMBER"}), strings);
        if (cursor != null && cursor.getCount() > 0) {
        } else {
            db.execSQL(zz_create.MD().getIn_D(), strings);
        }
    }

    //删除客户和仓库
    public void deleteMDandCK(SQLiteDatabase db) {
        String[] strings = new String[]{};
        db.execSQL(zz_create.CK().getDe_D(), strings);
        db.execSQL(zz_create.MD().getDe_D(), strings);
    }

    //删除装车
    public void deleteZCXL(SQLiteDatabase db) {
        String[] strings = new String[]{};
        db.execSQL(zz_create.FHTZD_XL().getDe_D(), strings);
        db.execSQL(zz_create.ZCDHEAD().getDe_D(), strings);
        db.execSQL(zz_create.ZCDBODY().getDe_D(), strings);
    }


    //删除客户和仓库
    public void deleteCK(SQLiteDatabase db) {
        String[] strings = new String[]{};
        db.execSQL(zz_create.CK().getDe_D(), strings);

    }

    //插入发货通知单表头
    public void insertFHTZDHead(String[] strings, SQLiteDatabase db) {
        String SQL = zz_create.FHTZD_Head().getIn_D();
        Log.i("a", SQL);
        db.execSQL(SQL, strings);
    }

    //插入发货通知单表体
    public void insertFHTZDBody(String[] strings, SQLiteDatabase db) {

        db.execSQL(zz_create.FHTZD_Body().getIn_D(), strings);
    }

    //插入发货通知单表体没有的物料----==========================
    public void insertFHTZDBodyBCZ_number_sanzhuang(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), new String[]{ strings[0],strings[1]});
        if (cursor != null && cursor.getCount() > 0) {


        } else {
            //计算----------------
            String FEntryID = "10000";
            String FStockID = "";
            String FStockID_FNumber = "";
            String FStockID_FNAME = "";
            try {
                Cursor body = db.rawQuery("select  FEntity_FEntryID ,FStockID,FStockID_FNumber,FStockID_FNAME from FHTZD_Body ", new String[]{});
                if (body != null && body.getCount() > 0) {

                    while (body.moveToNext()) {

                        int i = Integer.parseInt(body.getString(0));
                        FEntryID = String.valueOf(i + 1);
                        FStockID = body.getString(1);
                        FStockID_FNumber = body.getString(2);
                        FStockID_FNAME = body.getString(3);
                        break;

                    }
                } else {

                }

                db.execSQL(zz_create.FHTZD_Body().getIn_D(), new String[]{strings[0], "0", strings[1], strings[3], strings[4], "10101"
                        , strings[5], "0", "0", FStockID, FStockID_FNumber, FStockID_FNAME, strings[7], "0", "0",
                        strings[6], "999999"});//15
            } catch (Exception e) {
                Log.i("sss", e.getMessage());
            }
        }


    }

    //插入发货通知单表体没有的物料----==========================
    public void insertFHTZDBodyBCZ_number(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), new String[]{ strings[0],strings[1]});
        if (cursor != null && cursor.getCount() > 0) {


        } else {
            String FEntryID = "10000";
            String FStockID = "";
            String FStockID_FNumber = "";
            String FStockID_FNAME = "";
            try {
                Cursor body = db.rawQuery("select  FEntity_FEntryID ,FStockID,FStockID_FNumber,FStockID_FNAME from FHTZD_Body ", new String[]{});
                if (body != null && body.getCount() > 0) {

                    while (body.moveToNext()) {

                        int i = Integer.parseInt(body.getString(0));
                        FEntryID = String.valueOf(i + 1);
                        FStockID = body.getString(1);
                        FStockID_FNumber = body.getString(2);
                        FStockID_FNAME = body.getString(3);
                        break;

                    }
                } else {

                }

                db.execSQL(zz_create.FHTZD_Body().getIn_D(), new String[]{strings[0], "0", strings[1], strings[3], strings[4], "10101"
                        , strings[5], "0", "0", FStockID, FStockID_FNumber, FStockID_FNAME, strings[5], "0", "0",
                        strings[6], "999999"});//15
            } catch (Exception e) {
                Log.i("sss", e.getMessage());
            }
        }


    }
    //插入发货通知单表体没有的物料----==========================
    public void insertFHTZDBodyBCZ_numberBIG(String[] strings, SQLiteDatabase db, String str) {
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), new String[]{ strings[0],strings[1]});
        if (cursor != null && cursor.getCount() > 0) {


        } else {
            //计算----------------
            String FEntryID = "10000";
            String FStockID = "";
            String FStockID_FNumber = "";
            String FStockID_FNAME = "";
            try {
                Cursor body = db.rawQuery("select  FEntity_FEntryID ,FStockID,FStockID_FNumber,FStockID_FNAME from FHTZD_Body ", new String[]{});
                if (body != null && body.getCount() > 0) {

                    while (body.moveToNext()) {

                        int i = Integer.parseInt(body.getString(0));
                        FEntryID = String.valueOf(i + 1);
                        FStockID = body.getString(1);
                        FStockID_FNumber = body.getString(2);
                        FStockID_FNAME = body.getString(3);
                        break;

                    }
                } else {

                }

                db.execSQL(zz_create.FHTZD_Body().getIn_D(), new String[]{strings[0], "0", strings[1], strings[3], strings[4], "10101"
                        , strings[5], "0", "0", FStockID, FStockID_FNumber, FStockID_FNAME, str, "0", "0",
                        strings[6], "999999"});//15
            } catch (Exception e) {
                Log.i("sss", e.getMessage());
            }
        }


    }
    //插入发货通知单表体箱号
    public void insertFHTZDXH(String[] strings, SQLiteDatabase db) {
        //查询
        if(strings.length==2){//2
            Cursor cursor = db.rawQuery(zz_create.FHTZD_XH_Body().getSe(new String[]{}, new String[]{"FID", "FXH"}), strings);
            if (cursor != null && cursor.getCount() > 0) {
            } else {
                db.execSQL(zz_create.FHTZD_XH_Body().getIn_D(), new String[]{strings[0],"", strings[1],"false"});
            }

        }else{//4
            Cursor cursor = db.rawQuery(zz_create.FHTZD_XH_Body().getSe(new String[]{}, new String[]{"FID", "FXH"}), new String[]{strings[0], strings[2]});
            if (cursor != null && cursor.getCount() > 0) {
            } else {
                db.execSQL(zz_create.FHTZD_XH_Body().getIn_D(), strings);
            }

        }
        //////


    }//deleteXH

    //插入发货通知单表体箱号
    public void insertZT(String[] strings, SQLiteDatabase db) {


        Cursor cursor = db.rawQuery(zz_create.ZT().getSe(new String[]{}, new String[]{"ZTIP", "ZTID"}), new String[]{strings[0], strings[1]});
        if (cursor != null && cursor.getCount() > 0) {
            db.execSQL("update ZT  set type='1' where ZTIP=? and ZTID=? ", new String[]{strings[0], strings[1]});
        } else {
            db.execSQL("update ZT  set type='0' ", new String[]{});
            db.execSQL(zz_create.ZT().getIn_D(), new String[]{strings[0], strings[1], "1"});
        }

    }//deleteXH

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////计算表体的行数、最后扫描后总共有几件
    public Cursor[] backHead(String[] strings, SQLiteDatabase db) {
        try {
            // 调用查找书库代码并返回数据源
            Cursor cursor1 = db.rawQuery("select count(*) from FHTZD_Body where FID=?", strings);
            //行数
            Cursor cursor2 = db.rawQuery("select sum(FPDAQTY) from FHTZD_Body where FID=?", strings);
            //件数

            Cursor[] cursors = new Cursor[]{cursor1, cursor2};
            return cursors;//返回一个Cursor[] 数组

        } catch (Exception e) {
            Log.i("ss", e.getMessage().toString());
        }

        return new Cursor[]{};


    }


    /////////////////////////////////////////////////////////////////////////////////////////////////

    //查询发货通知单,单据头，单据体，单据体箱号
    public Cursor[] selectFHIZD(String[] strings, SQLiteDatabase db) {
        //strings传入一个参数FID
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Head().getSe(new String[]{}, new String[]{"FID"}), strings);
        Cursor cursor2 = db.rawQuery(zz_create.FHTZD_Body().getSe_OrderBy(new String[]{}, new String[]{"FID"}, "  order by FMaterialID_FOrder ASC"), strings);
        Cursor cursor3 = db.rawQuery(zz_create.FHTZD_XH_Body().getSe(new String[]{}, new String[]{"FID"}), strings);
        Cursor cursor4 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID"}), strings);
        Cursor[] cursors = new Cursor[]{cursor, cursor2, cursor3,cursor4};
        return cursors;//返回一个Cursor[] 数组
    }

    public Cursor[] selectFHIZD_XHMX(String[] strings, SQLiteDatabase db) {
        //strings传入一个参数FID
//distinct
     // String dad=zz_create.F_pack_Entity().getSe(new String[]{"FPACKNUMBER3","sum(FQty3)"}, new String[]{"FID","FMaterialID3"});
       // String dad=zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID"});
        String dad=zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID","FMaterialID3"});
       // String dad=zz_create.F_pack_Entity().getSe_D();
        try{
           // Cursor cursor = db.rawQuery(dad, new String[]{strings[0]});
            Cursor cursor = db.rawQuery(dad, strings);
            Cursor cursor2 = db.rawQuery(zz_create.FHTZD_Body().getSe_OrderBy(new String[]{}, new String[]{"FID","FMaterialID"}, "  order by FMaterialID_FOrder ASC"), strings);
       /*  Cursor cursor3 = db.rawQuery(zz_create.FHTZD_XH_Body().getSe(new String[]{}, new String[]{"FID"}), strings);
        Cursor cursor4 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID"}), strings);*/
            Cursor[] cursors = new Cursor[]{cursor,cursor2};
            return cursors;//返回一个Cursor[] 数组

        }catch (Exception e){
            String msg=e.getMessage().toString();
        }

        return null;
    }

    //查询发货通知单,单据头，单据体，单据体箱号
    public Cursor[] selectFHIZD_WL(String[] strings, SQLiteDatabase db) {
        //strings传入一个参数FID//FID,、
        Cursor cursor2 = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), strings);
        Cursor[] cursors = new Cursor[]{cursor2};
        return cursors;//返回一个Cursor[] 数组
    }

    //查询发货通知单,单据头，单据体，单据体箱号
    public Cursor[] selectFHTZDBodyStrFEntryID(String[] strings, SQLiteDatabase db) {
        //strings传入一个参数FID
        Cursor cursor2 = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FEntity_FEntryID"}), strings);
        Cursor[] cursors = new Cursor[]{cursor2};
        return cursors;//返回一个Cursor[] 数组
    }

    //查询发货通知单,单据头，单据体，单据体箱号
    public Cursor[] selectFHIZDhead(SQLiteDatabase db) {
        //strings传入一个参数FID
        String[] strings = new String[]{};
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Head().getSe_D(), strings);
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    //删除发货通知单
    public void deleteFHIZD(String[] strings, SQLiteDatabase db) {
        //strings传入一个参数FID
        db.execSQL(zz_create.FHTZD_Head().getDe(new String[]{"FID"}), strings);
        db.execSQL(zz_create.FHTZD_Body().getDe(new String[]{"FID"}), strings);
        db.execSQL(zz_create.FHTZD_XH_Body().getDe(new String[]{"FID"}), strings);
        db.execSQL(zz_create.F_pack_Entity().getDe(new String[]{"FID"}), strings);
    }

    //更新发货通知单表体实发数量(普通)
    public boolean updateFHIZDHeadPT(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), strings);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String f1 = cursor.getString(1);
                String f2 = cursor.getString(2);
                String f3 = cursor.getString(3);
                String f4 = cursor.getString(4);
            }
        } else {
            return false;//不是单据体已有物料
        }
        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty+1", "FPDAQTY+1"}, new String[]{"FID", "FMaterialID"}), strings);
        return true;
    }

    //更新发货通知单表体实发数量(普通)
    public boolean updateFHIZDHeadPT_number(String[] strings, SQLiteDatabase db) {
        String[] stringsold = new String[2];
        stringsold[0] = strings[0];
        stringsold[1] = strings[1];
        String[] stringsnew = new String[4];
        stringsnew[0] = strings[2];
        stringsnew[1] = strings[2];
        stringsnew[2] = strings[0];
        stringsnew[3] = strings[1];
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), stringsold);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String f1 = cursor.getString(1);
                String f2 = cursor.getString(2);
                String f3 = cursor.getString(3);
                String f4 = cursor.getString(4);
            }
        } else {
            return false;//不是单据体已有物料
        }
        //strings传入三个参数 本次数量 ，FID ， 物料代码,+数量
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty+?", "FPDAQTY+?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
        return true;
    }
    //更新发货通知单表体实发数量(普通)
    public boolean updateFHIZDHeadPT_numberBIG(String[] strings, SQLiteDatabase db, String str) {
        String[] stringsold = new String[2];
        stringsold[0] = strings[0];
        stringsold[1] = strings[1];
        String[] stringsnew = new String[4];
        stringsnew[0] = strings[2];
        stringsnew[1] = str;
        stringsnew[2] = strings[0];
        stringsnew[3] = strings[1];
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), stringsold);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String f1 = cursor.getString(1);
                String f2 = cursor.getString(2);
                String f3 = cursor.getString(3);
                String f4 = cursor.getString(4);
            }
        } else {
            return false;//不是单据体已有物料
        }
        //strings传入三个参数 本次数量 ，FID ， 物料代码,+数量
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty+?", "FPDAQTY+?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
        return true;
    }


    //更新发货通知单表体实发数量(普通)
    public boolean updateFHIZDHeadPT_number_updata(String[] strings, SQLiteDatabase db) {
        String[] stringsold = new String[2];
        stringsold[0] = strings[0];
        stringsold[1] = strings[1];
        String[] stringsnew = new String[4];
        stringsnew[0] = strings[2];
        stringsnew[1] = strings[2];
        stringsnew[2] = strings[0];
        stringsnew[3] = strings[1];
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), stringsold);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String f1 = cursor.getString(1);
                String f2 = cursor.getString(2);
                String f3 = cursor.getString(3);
                String f4 = cursor.getString(4);
            }
        } else {
            return false;//不是单据体已有物料
        }
        //strings传入三个参数 本次数量 ，FID ， 物料代码,+数量
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty-1+?", "FPDAQTY-1+?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
        return true;
    }

    //更新发货通知单表体实发数量(普通)
    public boolean updateFHIZDHeadPTG(String[] strings, SQLiteDatabase db) {
        String[] strings2 = new String[2];
        strings2[0] = strings[1];
        strings2[1] = strings[2];
        // Double g=0D;

        Double FQty = 0D;
        Double newFQty = 0D;
        newFQty = Double.parseDouble(strings[0]);

        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), strings2);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // g = cursor.getDouble(7);
                FQty = cursor.getDouble(cursor.getColumnIndex("FQty"));

            }

        } else {
            return false;//不是单据体已有物料
        }

        strings[0] = String.valueOf(FQty + newFQty);
        //
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"?", "FPDAQTY+1"}, new String[]{"FID", "FMaterialID"}), strings);
        return true;
    }

    //更新发货通知单表体实发数量(普通)
    public boolean updateFHIZDHeadPTG_number(String[] strings, SQLiteDatabase db) {
        String[] strings2 = new String[2];
        strings2[0] = strings[1];
        strings2[1] = strings[2];
        // Double g=0D;

        Double FQty = 0D;
        Double newFQty = 0D;
        Double newFQty_number = 0D;
        newFQty = Double.parseDouble(strings[0]);

        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), strings2);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // g = cursor.getDouble(7);
                FQty = cursor.getDouble(cursor.getColumnIndex("FQty"));

            }

        } else {
            return false;//不是单据体已有物料
        }
        newFQty_number = newFQty * Double.parseDouble(strings[3]);//重量* 数量
        // strings[0]=String.valueOf(FQty+newFQty_number);
        //
        String[] stringsnew = new String[4];
        stringsnew[0] = String.valueOf(FQty + newFQty_number);
        stringsnew[1] = strings[3];
        stringsnew[2] = strings[1];
        stringsnew[3] = strings[2];
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"?", "FPDAQTY+?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
        return true;
    }

    //更新发货通知单表体实发数量(普通)
    public boolean updateFHIZDHeadPTG_number_sfsl_sfjs(String[] strings, SQLiteDatabase db) {

        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"?", "?"}, new String[]{"FID", "FMaterialID"}), strings);

       // db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty"}, new String[]{"?"}, new String[]{"FID", "FMaterialID"}), strings);
        return true;
    }

    //更新发货通知单表体实发数量(扣减)
    public int updateFHIZDHeadKJ_number(String[] strings, SQLiteDatabase db, String xh, String FCODE3, String FMaterialID3) {
        String[] stringsold = new String[2];
        stringsold[0] = strings[0];
        stringsold[1] = strings[1];
        String[] stringsnew = new String[4];
        stringsnew[0] = strings[2];
        stringsnew[1] = strings[2];
        stringsnew[2] = strings[0];
        stringsnew[3] = strings[1];
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), stringsold);


        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // int js=cursor.getInt(7);
                if (cursor.getInt(13) == 0) {//件数为0不能扣减
                    return 1;
                } else if (cursor.getInt(13) < Integer.valueOf(strings[2])) {//件数不能少于输入件数
                    return 1;
                } else {

                }
            }
        } else {
            return 2;//不是单据体已有物料
        }
        //判断该箱子下有没有足够扣减的数量
        String[] stringsold2 = new String[4];
        stringsold2[0] = strings[0];
        stringsold2[1] = FMaterialID3;
        stringsold2[2] = xh;
        stringsold2[3] = FCODE3;
        Cursor cursor2 = db.rawQuery(zz_create.F_pack_Entity().getSe(new String[]{}, new String[]{"FID", "FMaterialID3","FPACKNUMBER3","FCODE3"}), stringsold2);


        if (cursor2 != null && cursor2.getCount() > 0) {

        } else {
            return 4;//对应箱子没有该条码
        }


        ///

        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty-?", "FPDAQTY-?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
        return 0;
    }




    //更新发货通知单表体实发数量(扣减)
    public int updateFHIZDHeadKJ_numberBIG(String[] strings, SQLiteDatabase db, String str) {
        String[] stringsold = new String[2];
        stringsold[0] = strings[0];
        stringsold[1] = strings[1];

        String[] stringsnew = new String[4];
        stringsnew[0] = strings[2];
        stringsnew[1] = str;
        stringsnew[2] = strings[0];
        stringsnew[3] = strings[1];
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), stringsold);


        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // int js=cursor.getInt(7);
                if (cursor.getInt(13) == 0) {//件数为0不能扣减
                    return 1;
                } else if (cursor.getInt(13) < Integer.valueOf(str)) {//件数不能少于输入件数
                    return 1;
                } else {

                }
            }
        } else {
            return 2;//不是单据体已有物料
        }
        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty-?", "FPDAQTY-?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
        return 0;
    }


    //更新发货通知单表体实发数量(扣减)
    public int updateFHIZDHeadKJ_number_updata(String[] strings, SQLiteDatabase db) {
        String[] stringsold = new String[2];
        stringsold[0] = strings[0];
        stringsold[1] = strings[1];

        String[] stringsnew = new String[4];
        stringsnew[0] = strings[2];
        stringsnew[1] = strings[2];
        stringsnew[2] = strings[0];
        stringsnew[3] = strings[1];

        String[] stringsnewup = new String[2];
        stringsnewup[0] = strings[0];
        stringsnewup[1] = strings[1];
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), stringsold);
//判断该箱子下有没有

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // int js=cursor.getInt(7);
                if (cursor.getInt(13) == 0) {//件数为0不能扣减
                    db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"0", "0"}, new String[]{"FID", "FMaterialID"}), stringsnewup);
                    return 1;
                } else if (cursor.getInt(13) < Integer.valueOf(strings[2])) {//件数不能少于输入件数
                    db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"0", "0"}, new String[]{"FID", "FMaterialID"}), stringsnewup);
                    return 1;
                } else {
                    db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty-?", "FPDAQTY-?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
                    return 0;
                }
            }
        } else {
            return 2;//不是单据体已有物料
        }

        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        return 0;
    }


    //更新发货通知单表体实发数量(扣减)
    public int updateFHIZDHeadKJ_number_updataBIG(String[] strings, SQLiteDatabase db, String str) {
        String[] stringsold = new String[2];
        stringsold[0] = strings[0];
        stringsold[1] = strings[1];
        String[] stringsnew = new String[4];
        stringsnew[0] = strings[2];
        stringsnew[1] = str;
        stringsnew[2] = strings[0];
        stringsnew[3] = strings[1];

        String[] stringsnewup = new String[2];
        stringsnewup[0] = strings[0];
        stringsnewup[1] = strings[1];
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), stringsold);


        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // int js=cursor.getInt(7);
                if (cursor.getInt(13) == 0) {//件数为0不能扣减
                    db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"0", "0"}, new String[]{"FID", "FMaterialID"}), stringsnewup);
                    return 1;
                } else if (cursor.getInt(13) < Integer.valueOf(str)) {//件数不能少于输入件数
                    db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"0", "0"}, new String[]{"FID", "FMaterialID"}), stringsnewup);
                    return 1;
                } else {
                    db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty-?", "FPDAQTY-?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
                    return 0;
                }
            }
        } else {
            return 2;//不是单据体已有物料
        }
        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        return 0;
    }
    //更新发货通知单表体实发数量(扣减)
    public int updateFHIZDHeadKJ(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), strings);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // int js=cursor.getInt(7);
                if (cursor.getInt(13) == 0) {//件数为0不能扣减
                    return 1;
                } else {

                }
            }
        } else {
            return 2;//不是单据体已有物料
        }
        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"FQty-1", "FPDAQTY-1"}, new String[]{"FID", "FMaterialID"}), strings);
        return 0;
    }
    //更新发货通知单表体实发数量(扣减)
    public Cursor selectWLtiaoma(String[] strings, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FBARCODE"}), strings);
         return cursor;
    }

    //更新发货通知单表体实发数量(扣减)
    public int updateFHIZDHeadKJG(String[] strings, SQLiteDatabase db) {
        String[] strings2 = new String[2];
        strings2[0] = strings[1];
        strings2[1] = strings[2];

        Double FQty = 0D;
        Double newFQty = 0D;
        newFQty = Double.parseDouble(strings[0]);
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), strings2);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // int js=cursor.getInt(7);
                if (cursor.getInt(13) == 0) {//件数为0不能扣减
                    return 1;
                } else {
                    FQty = cursor.getDouble(cursor.getColumnIndex("FQty"));
                }
            }
        } else {
            return 2;//不是单据体已有物料
        }
        strings[0] = String.valueOf(FQty - newFQty);
        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"?", "FPDAQTY-1"}, new String[]{"FID", "FMaterialID"}), strings);
        return 0;
    }

    //更新发货通知单表体实发数量(扣减)
    public int updateFHIZDHeadKJG_number(String[] strings, SQLiteDatabase db) {
        String[] strings2 = new String[2];
        strings2[0] = strings[1];
        strings2[1] = strings[2];

        Double FQty = 0D;
        Double newFQty = 0D;
        Double newFQty_number = 0D;
        newFQty = Double.parseDouble(strings[0]);
        Cursor cursor = db.rawQuery(zz_create.FHTZD_Body().getSe(new String[]{}, new String[]{"FID", "FMaterialID"}), strings2);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // int js=cursor.getInt(7);
                if (cursor.getInt(13) == 0) {//件数为0不能扣减
                    return 1;
                } else if (cursor.getInt(13) < Integer.valueOf(strings[3])) {//件数不能少于输入件数
                    return 1;
                } else if (cursor.getDouble(cursor.getColumnIndex("FQty")) < newFQty * Double.parseDouble(strings[3])) {//重量不能少于输入重量* 数量
                    return 1;
                } else {
                    FQty = cursor.getDouble(cursor.getColumnIndex("FQty"));
                }
            }
        } else {
            return 2;//不是单据体已有物料
        }
        newFQty_number = newFQty * Double.parseDouble(strings[3]);//重量* 数量
      /*
        // strings[0]=String.valueOf(FQty+newFQty_number);
        //
        String[] stringsnew=new String[2];
        stringsnew[0]=String.valueOf(FQty+newFQty_number);
        stringsnew[1]=strings[3];*/
        String[] stringsnew = new String[4];
        stringsnew[0] = String.valueOf(FQty - newFQty_number);
        stringsnew[1] = strings[3];
        stringsnew[2] = strings[1];
        stringsnew[3] = strings[2];
        //  strings[0]=String.valueOf(FQty-newFQty);
        //strings传入三个参数 本次数量 ，FID ， 物料代码,
        db.execSQL(zz_create.FHTZD_Body().getUp(new String[]{"FQty", "FPDAQTY"}, new String[]{"?", "FPDAQTY-?"}, new String[]{"FID", "FMaterialID"}), stringsnew);
        return 0;
    }

    public Cursor[] selectSCANINFO(SQLiteDatabase db) {
        //strings传入一个参数FID
        String[] strings = new String[]{};
        Cursor cursor = db.rawQuery(zz_create.SCANINFO().getSe_D(), strings);
        Cursor[] cursors = new Cursor[]{cursor};
        return cursors;//返回一个Cursor[] 数组
    }

    public void insertSCANINFO(String[] strings, SQLiteDatabase db) {
        String SQL = zz_create.SCANINFO().getIn_D();
        db.execSQL(SQL, strings);
    }

    public void deleteSCANINFO(SQLiteDatabase db) {
        String[] strings = new String[]{};
        db.execSQL(zz_create.SCANINFO().getDe_D(), strings);
    }

}
