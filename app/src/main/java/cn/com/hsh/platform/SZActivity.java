package cn.com.hsh.platform;
/**
 * 设置页面 校验密码为888888，后新增是否可以添加明细外商品
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import cn.com.hsh.platform.util.CItem;
import cn.com.hsh.platform.util.DatabasePublic;
import cn.com.hsh.platform.util.SQLDBHelper;
import cn.com.hsh.platform.zz.zz_OtherUrl;

import static cn.com.hsh.platform.util.SQLDBHelper.zz_create;


public class SZActivity extends Activity {
    //按钮监听
    Button button5;
    EditText editText3;
    private SQLDBHelper sqldbHelper;//自定义数据库初始化类
    private SQLiteDatabase db;//数据库类
    private Spinner zz, xl;
    private ArrayAdapter<CItem> arr_adapterzz, arr_adapterxl;
    List<CItem> data_listzz, data_listxl;
    EditText ztid, ztip;
    boolean loo1 = false, loo2 = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    Toast.makeText(SZActivity.this, "账套正确", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(SZActivity.this, "账套不正确", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(SZActivity.this, "异常", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(SZActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(SZActivity.this, "获取成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:

                    xl.setAdapter(arr_adapterxl);
                    Toast.makeText(SZActivity.this, "线路加载成功", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sz);
        init();
    }

    public void init() {
        data_listzz = new ArrayList<CItem>();
        data_listxl = new ArrayList<CItem>();
        sqldbHelper = new SQLDBHelper(this);
        db = sqldbHelper.getWritableDatabase();
        CItem ctzz = new CItem("0", "选择组织");
        data_listzz.add(ctzz);
        CItem ctxl = new CItem("0", "选择线路");
        data_listxl.add(ctxl);


        zz = (Spinner) findViewById(R.id.zzid);
        xl = (Spinner) findViewById(R.id.xl);
        //xl
        //适配器
        arr_adapterzz = new ArrayAdapter<CItem>(this, android.R.layout.simple_spinner_item, data_listzz);
        //设置样式
        arr_adapterzz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        zz.setAdapter(arr_adapterzz);
        zz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View viewss, int position, long id) {
                //设置组织id
                if (loo2) {
                    if (zz.getSelectedItem() == null) {

                        return;
                    }
                    if (zz.getSelectedItem() == "选择组织") {

                        return;
                    }
                    db.execSQL(zz_create.ZZ().getDe_D(), new String[]{});
                    db.execSQL(zz_create.ZZ().getIn_D(), new String[]{((CItem) zz.getSelectedItem()).GetID(), zz.getSelectedItem().toString(), "1"});
                    Toast.makeText(SZActivity.this, "设置组织为(" + zz.getSelectedItem().toString() + ")成功", Toast.LENGTH_SHORT).show();
                    zz_OtherUrl.ZZID = ((CItem) zz.getSelectedItem()).GetID();
                    zz_OtherUrl.zzname = zz.getSelectedItem().toString();
///////////////////////
                  try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String ss1 = DatabasePublic.lsq_shazd_order_queryxl_zz(zz_OtherUrl.ZZID);

                               // String ss1  = DatabasePublic.lsq_shazd_order_queryxl();
                                Gson gson = new Gson();
                                JsonParser parser = new JsonParser();
                                JsonArray Jarray = parser.parse(ss1).getAsJsonArray();
                                ArrayList<JsonObject> lcs = new ArrayList<JsonObject>();
                                for (JsonElement obj : Jarray) {
                                    JsonObject cse = gson.fromJson(obj, JsonObject.class);
                                    lcs.add(cse);
                                }
                                data_listxl.clear();
                                CItem ct2 = new CItem("0", "选择线路");
                                data_listxl.add(ct2);
                                for (JsonObject obj : lcs) {

                                    String FNUMBER = obj.getAsJsonObject().get("FNUMBER").getAsString();
                                    String FNAME = obj.getAsJsonObject().get("FNAME").getAsString();
                                    CItem ct = new CItem(FNUMBER, FNAME);
                                    data_listxl.add(ct);

                                }
                                mHandler.sendEmptyMessage(5);
                            } catch (Exception e) {

                                mHandler.sendEmptyMessage(2);
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //////////////////////
                    } catch (Exception e) {

                        mHandler.sendEmptyMessage(2);
                        e.printStackTrace();
                    }


                }
                loo2 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //适配器
        arr_adapterxl = new ArrayAdapter<CItem>(this, android.R.layout.simple_spinner_item, data_listxl);
        //设置样式
        arr_adapterxl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        xl.setAdapter(arr_adapterxl);
        xl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View viewss, int position, long id) {
                //换选项时触发，同一个不触发
                if (loo1) {
                    if (xl.getSelectedItem() == null) {

                        return;
                    }
                    if (xl.getSelectedItem() == "选择线路") {

                        return;
                    }
                    db.execSQL(zz_create.XL().getDe_D(), new String[]{});
                    db.execSQL(zz_create.XL().getIn_D(), new String[]{xl.getSelectedItem().toString(), ((CItem) xl.getSelectedItem()).GetID()});
                    Toast.makeText(SZActivity.this, "设置线路为(" + xl.getSelectedItem().toString() + ")成功", Toast.LENGTH_SHORT).show();
                    zz_OtherUrl.XL = ((CItem) xl.getSelectedItem()).GetID();
                    zz_OtherUrl.xlname = xl.getSelectedItem().toString();
                }
                loo1 = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ztid = (EditText) findViewById(R.id.ztid);
        ztip = (EditText) findViewById(R.id.ztip);
        Cursor cursor2 = sqldbHelper.selectZT(db);
        if (cursor2 != null && cursor2.getCount() > 0) {
            while (cursor2.moveToNext()) {
                ztid.setText(cursor2.getString(1));
                ztip.setText(cursor2.getString(2));
            }
        }
        initButton();


    }

    public void initButton() {
        //每个接口调用时new一个


        //
        Button button = (Button) findViewById(R.id.retuenMain);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SZActivity.this, MainActivity.class));// 跳转配置
            }
        });
        Button csBtn = (Button) findViewById(R.id.csBtn);//测试
        csBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DatabasePublic.POST_K3CloudURL = "http://" + ztip.getText().toString() + "/k3cloud/";
                            String dbId = ztid.getText().toString();

                            String uid = zz_OtherUrl.Admin;
                            String pwd = zz_OtherUrl.pwd;
                            int lang = 2052;
                            if (DatabasePublic.Login(dbId, uid, pwd, lang)) {

                                mHandler.sendEmptyMessage(0);
                            } else {

                                mHandler.sendEmptyMessage(1);
                            }

                        } catch (Exception e) {

                            mHandler.sendEmptyMessage(2);
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        Button bcBtn = (Button) findViewById(R.id.bcBtn);//保存
        bcBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DatabasePublic.POST_K3CloudURL = "http://" + ztip.getText().toString() + "/k3cloud/";
                            String dbId = ztid.getText().toString();

                            String uid = zz_OtherUrl.Admin;
                            String pwd = zz_OtherUrl.pwd;
                            int lang = 2052;
                            if (DatabasePublic.Login(dbId, uid, pwd, lang)) {
                                sqldbHelper.insertZT(new String[]{ztid.getText().toString(), ztip.getText().toString()}, db);
                                Cursor cursor = db.rawQuery(zz_create.ZT().getSe(new String[]{}, new String[]{"TYPE"}), new String[]{"1"});
                                if (cursor != null && cursor.getCount() > 0) {
                                    while (cursor.moveToNext()) {
                                        zz_OtherUrl.ZTID = cursor.getString(1);
                                        zz_OtherUrl.ZTIP = "http://" + cursor.getString(2) + "/k3cloud/";
                                        break;
                                    }
                                }
                                ////////////////////////////////////////////////////////////////////////////////////////////////////

                                mHandler.sendEmptyMessage(3);
                            } else {

                                mHandler.sendEmptyMessage(1);
                            }

                        } catch (Exception e) {

                            mHandler.sendEmptyMessage(2);
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        Button hqzz = (Button) findViewById(R.id.hqzz);
        hqzz.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //取到当前账套下组织
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //20191129把组织获取使用固定值，只开放现在的6个基地就行，其他不显不，显示了也没用，减少前端请求
                            String ss1="[{\"FORGID\":\"100003\",\"FNUMBER\":\"101\",\"FNAME\":\"江西煌上煌集团食品股份有限公司\"},{\"FORGID\":\"100005\",\"FNUMBER\":\"102\",\"FNAME\":\"江西煌上煌集团食品股份有限公司河南分公司\"},{\"FORGID\":\"100006\",\"FNUMBER\":\"103\",\"FNAME\":\"广东煌上煌食品有限公司\"},{\"FORGID\":\"100007\",\"FNUMBER\":\"104\",\"FNAME\":\"辽宁煌上煌食品有限公司\"},{\"FORGID\":\"102391\",\"FNUMBER\":\"109\",\"FNAME\":\"福建煌上煌食品有限公司\"},{\"FORGID\":\"537513\",\"FNUMBER\":\"113\",\"FNAME\":\"广西煌上煌食品有限公司\"}]";
                           // ss1 = DatabasePublic.lsq_shazd_order_queryzz();
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray Jarray = parser.parse(ss1).getAsJsonArray();
                            ArrayList<JsonObject> lcs = new ArrayList<JsonObject>();
                            for (JsonElement obj : Jarray) {
                                JsonObject cse = gson.fromJson(obj, JsonObject.class);
                                lcs.add(cse);
                            }
                            data_listzz.clear();
                            CItem ct2 = new CItem("0", "选择组织");
                            data_listzz.add(ct2);
                            for (JsonObject obj : lcs) {

                                String FNUMBER = obj.getAsJsonObject().get("FNUMBER").getAsString();
                                String FNAME = obj.getAsJsonObject().get("FNAME").getAsString();
                                CItem ct = new CItem(FNUMBER, FNAME);
                                data_listzz.add(ct);
                            }


                            ss1 = DatabasePublic.lsq_shazd_order_queryxl();
                            gson = new Gson();
                            parser = new JsonParser();
                            Jarray = parser.parse(ss1).getAsJsonArray();
                            lcs = new ArrayList<JsonObject>();
                            for (JsonElement obj : Jarray) {
                                JsonObject cse = gson.fromJson(obj, JsonObject.class);
                                lcs.add(cse);
                            }
                            data_listxl.clear();
                            ct2 = new CItem("0", "选择线路");
                            data_listxl.add(ct2);
                            for (JsonObject obj : lcs) {

                                String FNUMBER = obj.getAsJsonObject().get("FNUMBER").getAsString();
                                String FNAME = obj.getAsJsonObject().get("FNAME").getAsString();
                                CItem ct = new CItem(FNUMBER, FNAME);
                                data_listxl.add(ct);
                            }

                            mHandler.sendEmptyMessage(4);


                        } catch (Exception e) {

                            mHandler.sendEmptyMessage(2);
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            // zz = (Spinner) findViewById(R.id.zzid);
            //  xl = (Spinner) findViewById(R.id.xl);
            // zz.set


        });

    }

    //屏蔽返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
