package cn.com.hsh.platform;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.simple.spiderman.SpiderMan;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //放在其他库初始化前,日志收集，使用SpiderMan.show(e)可以在线显示;
        SpiderMan.init(this);
        setContentView(R.layout.activity_main);
    }
}
