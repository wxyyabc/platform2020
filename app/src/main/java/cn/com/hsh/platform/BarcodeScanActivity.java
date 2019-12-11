package cn.com.hsh.platform;
/**
 * 条码扫描记录扫描的所有信息，这是一个学习的示例。了解到PDA扫描的整个从硬件读取过程
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;修改为 import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.TriggerStateChangeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.hsh.platform.util.FileUtil;

public class BarcodeScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener{

    private Button exitBtn;
    private Button saveBtn;
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    private String barcodeData;
    private String barcodeStr;
    private AlertDialog.Builder builder;
    private AlertDialog adialog;
    private TextView t1;
    private String recodeMSG="";
    private boolean isSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        init();
    }

    public  void init(){
        t1 = (TextView) findViewById(R.id.scanInfo);
        exitBtn = (Button) findViewById(R.id.exitScan);

        exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSave) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                        Date date = new Date(System.currentTimeMillis());
                        FileUtil.saveFile(recodeMSG, "条码扫描记录" + "_" + simpleDateFormat.format(date) + ".txt");
                    }
                    Intent intent = new Intent(BarcodeScanActivity.this, MainActivity.class);
                    //启动
                    startActivity(intent);
                }
             });
        saveBtn = (Button) findViewById(R.id.saveSacnLog);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                FileUtil.saveFile(recodeMSG, "条码扫描记录"+"_"+simpleDateFormat.format(date)+".txt");
                isSave = true;
            }
        });

        NEWPDA();
    }

    /**
     * 整个PDA的扫描实现功能在这里定义 implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener  在那个界面就实现这个方法可以快速扫描
     */
    public void NEWPDA(){

        //第一步：创建Aidc管理器和BarcodeReader对象
        AidcManager.create(this, new AidcManager.CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;							//获得Aidc管理器对象
                barcodeReader = manager.createBarcodeReader();	//创建BarcodeReader对象

                //第二步：设置扫描属性
                try {
                    //CheckDigit
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
                   /* barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_13_ADDENDA_REQUIRED_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_13_TWO_CHAR_ADDENDA_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_UPC_A_TRANSLATE_EAN13, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_13_FIVE_CHAR_ADDENDA_ENABLED, true);*/
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_13_CHECK_DIGIT_TRANSMIT_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_QR_CODE_ENABLED, false);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                            BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL);

                    barcodeReader.claim();			//打开扫描功能
                } catch(Exception e){
                    Toast.makeText(BarcodeScanActivity.this, "修改属性失败", Toast.LENGTH_SHORT).show();
                }

                //第三步：注册Trigger监听器和Barcode监听器  implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener 实现这几个类里的方法就行
                barcodeReader.addTriggerListener(BarcodeScanActivity.this);
                barcodeReader.addBarcodeListener(BarcodeScanActivity.this);
            }
        });
    }
    //第四步：实现触发键事件和条码事件的处理 最关键的业务在这里开始对接和实现
    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        barcodeData = barcodeReadEvent.getBarcodeData();		//获取扫描数据
        //在UI线程中进行数据显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barcodeStr = barcodeData;
                ConvertTo();
            }
        });
    }
    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {
        try {
            barcodeReader.light(triggerStateChangeEvent.getState());	//开关补光
            barcodeReader.aim(triggerStateChangeEvent.getState());		//开关瞄准线
            barcodeReader.decode(triggerStateChangeEvent.getState());	//开关解码功能
        } catch(Exception e){
            Toast.makeText(this, "开关扫描功能失败", Toast.LENGTH_SHORT).show();
        }
    }

    //第五步：扫描功能的关闭与恢复
    @Override
    protected void onResume() {
        super.onResume();
        if(barcodeReader != null){
            try {
                barcodeReader.claim();
            } catch (Exception e){
                Toast.makeText(this, "开启扫描失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(barcodeReader != null){
            barcodeReader.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(barcodeReader != null){
            barcodeReader.removeTriggerListener(this);
            barcodeReader.removeBarcodeListener(this);
            barcodeReader.close();
        }

        if(manager != null){
            manager.close();
        }
    }

    /**
     * 转换信息处理,接收的信息后续处理过程在这里实现的扩展
     */
    public void ConvertTo() {
        builder = new AlertDialog.Builder(BarcodeScanActivity.this);
        //设置对话框的图标
        builder.setIcon(android.R.drawable.alert_light_frame);
        //设置对话框的标题
        builder.setTitle("扫描信息确认");
        //设置文本
        builder.setMessage("扫描信息：" +barcodeStr);
        //设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                t1.setText(barcodeStr);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                recodeMSG += barcodeStr + "|扫描于|" + simpleDateFormat.format(date) + "\n";
            }
        });

        adialog = builder.create();
        //使用创建器生成一个对话框对象

        adialog.show();

        return;
    }
}
