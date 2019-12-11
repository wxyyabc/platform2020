package cn.com.hsh.platform;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.com.hsh.platform.util.CItem;
import cn.com.hsh.platform.util.SQLDBHelper;
import cn.com.hsh.platform.zz.zz_OtherUrl;

public class MenuActivity extends AppCompatActivity {

    private Button JHBtn;
    private Button ZXBtn;
    private Button CXBtn;
    private Button CXZCBtn, WJHBtn;
    private String ZZ;
    private ProgressDialog myDialog;


    private Spinner xlmenu;
    private ArrayAdapter<CItem> arr_adapterxlmenu;
    private List<CItem> data_xlmenu;
    private boolean loo1 = false;
    private int indexxlmenu=0;

    private Spinner ckmenu;
    private ArrayAdapter<CItem> arr_adapterckmenu;
    private List<CItem> data_ckmenu;
    private boolean loo2 = false;
    private int indexckmenu=0;

    private SQLDBHelper sqldbHelper;//自定义数据库初始化类
    private SQLiteDatabase db;//数据库类

    private TextView tvShowDialog;
    private Calendar cal;
    private int year, month, day;
    private Intent intent;
    private Bundle bundle;
    private SimpleDateFormat myFmt;
    private Date now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void init() {


        myDialog=new ProgressDialog(this);
        //initViewAndClick();

        ZZ = zz_OtherUrl.ZZID;
        try{
            sqldbHelper = new SQLDBHelper(this);
            db = sqldbHelper.getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();

        }



        getDate();
        myFmt = new SimpleDateFormat("yyyy-MM-dd"); // yyyy年MM月dd日 HH时mm分ss秒2017-10-11T00:00:00T00:00:00
        now = new Date();


        tvShowDialog = (TextView) findViewById(R.id.tvShowDialog);
        tvShowDialog.setText(myFmt.format(now));
        if (zz_OtherUrl.date != "") {

            tvShowDialog.setText(zz_OtherUrl.date);
        }else{

            zz_OtherUrl.date=tvShowDialog.getText().toString();
        }

        tvShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        tvShowDialog.setText(year + "-" + (++month) + "-" + day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1

                        zz_OtherUrl.date = tvShowDialog.getText().toString();
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(MenuActivity.this, 0, listener, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date nowDate = null;
                try {
                    nowDate = df.parse(myFmt.format(now));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //dialog.getDatePicker().setMinDate( new Date(zz_Tool.getDateStr(myFmt.format(now),3)).getTime());时间段在这里设置
                dialog.getDatePicker().setMinDate((new Date(nowDate.getTime() - 3 * 24 * 60 * 60 * 1000)).getTime());
                dialog.getDatePicker().setMaxDate((new Date(nowDate.getTime() + 3 * 24 * 60 * 60 * 1000)).getTime());
                dialog.show();
            }
        });
        //ReturnLogin
        Button ReturnLogin = (Button) findViewById(R.id.ReturnLogin);
        ReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //setxl();
        //setck();

    }

    private void getDate() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = cal.get(Calendar.DAY_OF_MONTH);
    }
}
