package cn.com.hsh.platform;
/**
 * 1、AsyncHttpClient 实现异步请求  20191211
 * 2、Retroift 实现异步网络请求 20191212
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.simple.spiderman.SpiderMan;

import java.util.List;

import cn.com.hsh.platform.fastjson.FastJsonConverterFactory;
import cn.com.hsh.platform.model.HrUser;
import cn.com.hsh.platform.util.DatabasePublic;
import cn.com.hsh.platform.util.FileUtil;
import cn.com.hsh.platform.util.RetrofitApi;
import cn.com.hsh.platform.util.SQLDBHelper;
import cn.com.hsh.platform.zz.zz_OtherUrl;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


import static cn.com.hsh.platform.util.SQLDBHelper.zz_create;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView4;
    private TextView mTextView2;
    private EditText mEditText;
    private EditText mEditText2;
    private Button mButton;
    private FloatingActionButton mFloatingActionButton;
    private ProgressDialog myDialog;
    private SQLDBHelper sqldbHelper;//自定义数据库初始化类
    private SQLiteDatabase db;//数据库类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //放在其他库初始化前,日志收集，使用SpiderMan.show(e)可以在线显示;
        SpiderMan.init(this);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTextView4 = (TextView) findViewById(R.id.textView4);
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText2 = (EditText) findViewById(R.id.editText2);
        mEditText.setText("03152");
        mEditText2.setText("4264282018");
        mButton = (Button) findViewById(R.id.button);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        mButton.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        sqldbHelper = new SQLDBHelper(this);
        db = sqldbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(zz_create.ZT().getSe(new String[]{}, new String[]{"TYPE"}), new String[]{"1"});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                zz_OtherUrl.ZTID = cursor.getString(1);
                zz_OtherUrl.ZTIP = "http://" + cursor.getString(2) + "/k3cloud/";//更改连接地址加上前缀后缀
                break;
            }
        }
        cursor = db.rawQuery(zz_create.ZZ().getSe(new String[]{}, new String[]{"TYPE"}), new String[]{"1"});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                zz_OtherUrl.ZZID = cursor.getString(1);
                zz_OtherUrl.zzname = cursor.getString(2);
                break;
            }
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                submit();
                break;
            case R.id.floatingActionButton:
                Toast.makeText(this, "进入设置界面", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SecretActivity.class));// 跳转设置界面
                break;
        }
    }

    private void submit() {
        // validate
        String editTextString = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(editTextString)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String editText2String = mEditText2.getText().toString().trim();
        if (TextUtils.isEmpty(editText2String)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        //第一次设置账套用户，密码，如果得到的值可以登录，那么保存到配置文件
        //设置用户和密码

        //显示对话框
        myDialog = ProgressDialog.show(MainActivity.this, "请稍后...", "正在加载数据", true);
        myDialog.setCancelable(true);

        //创建实例
        AsyncHttpClient client=new AsyncHttpClient();
        String usertStr = mEditText.getText().toString().trim();
        String pswStr = mEditText2.getText().toString().trim();
//        String url="https://main.hsh.com.cn/api/hrcontroller/checkuser"; // 中台hr登录接口
//        url = url + "?appkey=5A69586C78744F7977574A33784D3271442F30595942516C33747557643849393368576F5962374F7653554F544F367941477355672B50544B374C3479704C70&usercode="+usertStr+"&password="+pswStr;
//        //AsyncHttpClient 异步框架处理网络请示例
//        client.get(this, url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                myDialog.dismiss();
//                Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                try {
//                    JSONObject jsonObj = JSONObject.fromObject(responseString);
//                    if(jsonObj.getInt("returnCode")==-1){
//                        myDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
//                    }else {
//                        zz_OtherUrl.loginName=jsonObj.getJSONObject("data").getString("fnameL2");//登录成功后传值 用户名
//                        zz_OtherUrl.loginId=jsonObj.getJSONObject("data").getString("fnumber");//登录成功后传值 用户ID
//                        myDialog.dismiss();
//                        startActivity(new Intent(MainActivity.this, MenuActivity.class));// 跳转主界面
//                    }
//                }catch (Exception e){
//                    myDialog.dismiss();
//                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                    SpiderMan.show(e);
//                }
//            }
//        });

        //Retrofit发起请求方式示例
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(zz_OtherUrl.platformUrl) //基础url,其他部分在GetRequestInterface里
                .addConverterFactory(FastJsonConverterFactory.create()) //fastjson数据转换器
                .build();

        //创建网络请求接口实例
        RetrofitApi request = retrofit.create(RetrofitApi.class);
        String appkey= "5A69586C78744F7977574A33784D3271442F30595942516C33747557643849393368576F5962374F7653554F544F367941477355672B50544B374C3479704C70";
        Call<HrUser> call = request.hrLogin(appkey,usertStr,pswStr);

        //发送网络请求(异步)
        call.enqueue(new Callback<HrUser>() {
            @Override
            public void onResponse(Call<HrUser> call, Response<HrUser> response) {
                HrUser userList = response.body();
                zz_OtherUrl.loginName=userList.getData().getString("fnameL2");//登录成功后传值 用户名
                zz_OtherUrl.loginId=userList.getData().getString("fnumber");//登录成功后传值 用户ID
                myDialog.dismiss();
                startActivity(new Intent(MainActivity.this, MenuActivity.class));// 跳转主界面
            }

            @Override
            public void onFailure(Call<HrUser> call, Throwable t) {
                myDialog.dismiss();
                Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
            }
        });

////
        Retrofit retrofit2 = new  Retrofit.Builder()
                .baseUrl("http://192.168.1.88/")
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();

        RetrofitApi request2 = retrofit2.create(RetrofitApi.class);
        Call<JSONObject> call2 = request2.k3Login(zz_OtherUrl.ZTID,zz_OtherUrl.Admin,zz_OtherUrl.pwd,zz_OtherUrl.lang);
        call2.enqueue(new Callback<JSONObject>(){
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response){
                System.out.println("11"+response.body().toString());
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t){
                System.out.println("22"+t.getMessage());
            }
        });

    }
}
