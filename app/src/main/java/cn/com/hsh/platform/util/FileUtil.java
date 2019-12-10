package cn.com.hsh.platform.util;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 *
 * 文件工具类，生成日志保存及日志删除功能，后来把日志传中台接口存储
 */

public class FileUtil {

    /**
     * 字符串保存到手机内存设备中
     *
     * @param str
     */
    public static void saveFile(String str, String fileName) {
        // 创建String对象保存文件名路径

        makeRootDirectory(Environment.getExternalStorageDirectory()+"/APDAlog/");
        try {


            // 创建指定路径的文件
            File file = new File(Environment.getExternalStorageDirectory()+"/APDAlog/"+fileName);
            // 如果文件不存在
            if (file.exists()) {
                // 创建新的空文件
                file.delete();
            }
            file.createNewFile();
            // 获取文件的输出流对象
            FileOutputStream outStream = new FileOutputStream(file);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(str.getBytes());
            // 最后关闭文件输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }
    /**
     * 读取文件里面的内容
     *
     * @return
     */
    public static String getFile(String fileName) {
        try {
            // 创建文件
            File file = new File(Environment.getExternalStorageDirectory(),fileName);
            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建字节数组 每次缓冲1M
            byte[] b = new byte[1024];
            int len = 0;// 一次读取1024字节大小，没有数据后返回-1.
            // 创建ByteArrayOutputStream对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 一次读取1024个字节，然后往字符输出流中写读取的字节数
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            // 将读取的字节总数生成字节数组
            byte[] data = baos.toByteArray();
            // 关闭字节输出流
            baos.close();
            // 关闭文件输入流
            fis.close();
            // 返回字符串对象
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //移除文件，获取文件时间与当前时间对比，我这时间定位5天，删除五天前的文件
    public static void removeFileByTime(String dirPath) {
        //获取目录下所有文件
        List<File> allFile = getDirAllFile(new File(dirPath));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //获取当前时间
        Date end = new Date(System.currentTimeMillis());
        try {
            end = dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis())));
        } catch (Exception e){
            Log.d(TAG, "dataformat exeption e " + e.toString());
        }
        Log.d(TAG, "getNeedRemoveFile  dirPath = "  +dirPath);
        for (File file : allFile) {//ComDef
            try {
                //文件时间减去当前时间
//                Date start = dateFormat.parse(dateFormat.format(new Date(file.lastModified())));
//                long diff = end.getTime() - start.getTime();//这样得到的差值是微秒级别
//                long days = diff / (1000 * 60 * 60 * 24);
//                if(5 <= days){
                    deleteFile(file);
//                }

            } catch (Exception e){
                Log.d(TAG, "dataformat exeption e " + e.toString());
            }
        }
    }


    //同步前一天日志文件到中台
    public static void syncFileToRedisByTime(String dirPath) {
        //获取目录下所有文件
        List<File> allFile = getDirAllFile(new File(dirPath));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //获取当前时间
        Date end = new Date(System.currentTimeMillis());
        try {
            end = dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis())));
        } catch (Exception e){
            Log.d(TAG, "dataformat exeption e " + e.toString());
        }
        Log.d(TAG, "getNeedRemoveFile  dirPath = "  +dirPath);
        for (File file : allFile) {//ComDef
            try {
                //文件时间减去当前时间
                Date start = dateFormat.parse(dateFormat.format(new Date(file.lastModified())));
                long diff = end.getTime() - start.getTime();//这样得到的差值是微秒级别
                long days = diff / (1000 * 60 * 60 * 24);
                if(1 <= days){
                    String fileText=getFile(file.getName());
                    Map<String, String> pdaMap2 = new HashMap<String, String>();
                    pdaMap2.put("key", "PDALog");
                    pdaMap2.put("message", file.getName()+fileText);
                    // 远程调用发送pdaMap存redis
                    DatabasePublic.sendPost("http://main.hsh.com.cn/api/RedisController/redisToQueue", pdaMap2);
                }

            } catch (Exception e){
                Log.d(TAG, "dataformat exeption e " + e.toString());
            }
        }
    }

    //删除文件夹及文件夹下所有文件
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }

    //获取指定目录下一级文件
    public static List<File> getDirAllFile(File file) {
        List<File> fileList = new ArrayList<>();
        File[] fileArray = file.listFiles();
        if(fileArray == null)
            return fileList;
        for (File f : fileArray) {
            fileList.add(f);
        }
        fileSortByTime(fileList);
        return fileList;
    }

    //对文件进行时间排序
    public static void fileSortByTime(List<File> fileList) {
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File p1, File p2) {
                if (p1.lastModified() < p2.lastModified()) {
                    return -1;
                }
                return 1;
            }
        });
    }

}