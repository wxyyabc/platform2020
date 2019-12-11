package cn.com.hsh.platform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.com.hsh.platform.zz.zz_OtherUrl;

public class SecretActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        init();
    }


    public void init(){
        //view层的控件和业务层的控件，靠id关联和映射  给btn1赋值，即设置布局文件中的Button按钮id进行关联
        Button btn1 = (Button) findViewById(R.id.button);
        //给btn1绑定监听事件
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.sec_editText);
                String str1 = editText1.getText().toString();

                //Toast.makeText(load_activt.this, key, Toast.LENGTH_LONG).show();
                if (str1.equals("888888"))//比较信息是否相等
                {
                    Toast.makeText(SecretActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SecretActivity.this, SZActivity.class);
                    //启动
                    startActivity(intent);
                } else if(str1.equals("777777")){
                    Toast.makeText(SecretActivity.this, "允许添加通知单外商品", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SecretActivity.this, SZActivity.class);
                    zz_OtherUrl.isOutAdd = true;//允许添加通知单外商品
                    //启动
                    startActivity(intent);
                }else if(str1.equals("999999")){
                    Toast.makeText(SecretActivity.this, "禁止添加通知单外商品", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SecretActivity.this, SZActivity.class);
                    zz_OtherUrl.isOutAdd = false;//禁止添加通知单外商品
                    //启动
                    startActivity(intent);
                }else if(str1.equals("666666")){
                    Toast.makeText(SecretActivity.this, "进入条码扫描界面", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SecretActivity.this, BarcodeScanActivity.class);
                    //启动
                    startActivity(intent);
                }else {
                    Toast.makeText(SecretActivity.this, "密码错误，重新输入", Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
