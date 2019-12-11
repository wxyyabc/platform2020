package cn.com.hsh.platform;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simple.spiderman.SpiderMan;

import cn.com.hsh.platform.util.DatabasePublic;
import cn.com.hsh.platform.util.FileUtil;
import cn.com.hsh.platform.zz.zz_OtherUrl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView4;
    private TextView mTextView2;
    private EditText mEditText;
    private EditText mEditText2;
    private Button mButton;
    private FloatingActionButton mFloatingActionButton;
    private ProgressDialog myDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    myDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));// 跳转主界面
                    new Thread() {//删除5天以前的日志
                        public void run() {
                            FileUtil.removeFileByTime(Environment.getExternalStorageDirectory() + "/APDAlog/");
                        }}.start();
                    break;
                case 1:
                    myDialog.dismiss();
                    Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    myDialog.dismiss();
                    Toast.makeText(MainActivity.this, "未选择默认线路，请到设置页面，设置默认线路", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    myDialog.dismiss();
                    Toast.makeText(MainActivity.this, "未选择默认组织，请到设置页面，设置默认组织", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    myDialog.dismiss();
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    myDialog.dismiss();
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

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
        new Thread() {
            public void run() {
                try {

                    String usertStr = mEditText.getText().toString().trim();
                    String pswStr = mEditText2.getText().toString().trim();
                    int lang = 2052;
                    try {
                        DatabasePublic.POST_K3CloudURL = zz_OtherUrl.ZTIP;
                        //if (DatabasePublic.Login(zz_OtherUrl.ZTID, usertStr, pswStr, lang)) {
                        if (DatabasePublic.loginHr(usertStr, pswStr)) {
                            //设置组织
                            //设置线路,缓存线路集合信息减少前端请求次数20191129
//                            String xlInfo = DatabasePublic.lsq_shazd_order_queryxl_zz(zz_OtherUrl.ZZID);
//                            zz_OtherUrl.xlInfo = xlInfo;
                            //设置账户
                            //zz_OtherUrl.Admin = usertStr;
                            //zz_OtherUrl.pwd = pswStr;
//                            if (zz_OtherUrl.ZZID.equals("")) {
//                                mHandler.sendEmptyMessage(3);
//
//                                return;
//                            }
//                            if (zz_OtherUrl.XL.equals("")) {
//                                mHandler.sendEmptyMessage(2);
//
//                                return;
//                            }

                        } else {
                            // Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                            myDialog.dismiss();
                            mHandler.sendEmptyMessage(4);
                            return;
                        }
                    } catch (Exception e) {

                        Log.i("", e.getMessage().toString());
                        myDialog.dismiss();
                        mHandler.sendEmptyMessage(5);
                        return;
                    }


                    mHandler.sendEmptyMessage(0);

                } catch (Exception e) {

                    mHandler.sendEmptyMessage(1);
                    SpiderMan.show(e);
                    e.printStackTrace();
                }
            }
        }.start();//开始运行线程

    }
}
