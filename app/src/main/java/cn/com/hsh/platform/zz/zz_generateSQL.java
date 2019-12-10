package cn.com.hsh.platform.zz;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13 0013.
 */
public class zz_generateSQL {
    /*
     * 返回数据库需要的语句
     */

    private String Table_Name;
    private String[] fields;//字段名
    private String[] type;//字段名
    private LinkedHashMap<String, String> Table;
    /*
     * 构造方法：
     * Table_Name 表名
     * fields 字段名数组
     */

    public zz_generateSQL(LinkedHashMap<String, String> Table, String Table_Name ){
        this.Table=Table;
        this.Table_Name=Table_Name;
        fields=new String[Table.size()];
        type=new String[Table.size()];
        Iterator iter = Table.entrySet().iterator();
        int index=0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            fields[index] = entry.getKey().toString();
            type[index]  = entry.getValue().toString();
            index++;
        }
    }

    /*
     * 返回创建表语句
     */
    public String Cr() {
        StringBuffer sql = new StringBuffer();
        sql.append(" create table ");
        sql.append(Table_Name);
        sql.append("( _id int auto_increment,");
        Iterator iter = Table.entrySet().iterator();
        int index=0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            sql.append(entry.getKey().toString() + " "+entry.getValue().toString()+" ,");
            index++;
        }
        sql.append("primary key('_id')) ;");
        return sql.toString();
    }

    /*
     * 返回插入语句
     */
    public String In_D() {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into ");
        sql.append(Table_Name);
        sql.append("( ");
        for (String field : fields) {
            sql.append(field);
            if (field != fields[fields.length - 1]) {
                sql.append(" ,");
            }

        }
        sql.append(" )values( ");
        for (String field : fields) {
            sql.append(" ? ");
            if (field != fields[fields.length - 1]) {
                sql.append(" , ");
            }

        }
        sql.append(");");
        return sql.toString();
    }

    /*
     * 返回更新语句
     * fields 字段名数组
     * values 更新值数组
     * wheres 条件数组
     * 字段名数组长度与更新值数组长度不一致 values默认为"?"
     */
    public String Up(String[] fields, String[] values, String[] wheres) {
        if(values.length!=fields.length){
            values=new String[fields.length];
            for(int i=0;i<values.length;i++){
                values[i]="?";
            }
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" update  ");
        sql.append(Table_Name);
        sql.append(" set ");
        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]);
            sql.append("=");
            sql.append(values[i]);
            if (i != fields.length - 1) {
                sql.append(",");
            }
        }
        if (wheres.length > 0) {
            sql.append(" where ");
            for (String where : wheres) {
                sql.append(where);
                sql.append("=?");
                if (where != wheres[wheres.length - 1]) {
                    sql.append(" and  ");
                }

            }
        }
        sql.append(";");
        return sql.toString();
    }

    /*
     * 返回查询语句
     * fields 字段名数组
     * wheres 条件数组
     */
    public String Se(String[] fields, String[] wheres) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  ");
        if (fields.length == 0) {
            sql.append(" *  ");
        } else {
            for (String field : fields) {
                sql.append(field);
                if (field != fields[fields.length - 1]) {
                    sql.append(" , ");
                }
            }
        }
        sql.append(" from ");
        sql.append(Table_Name);
        if (wheres.length > 0) {
            sql.append(" where ");
            for (String where : wheres) {
                sql.append(where);
                sql.append("=?");
                if (where != wheres[wheres.length - 1]) {
                    sql.append(" and  ");
                }

            }
        }
        sql.append(";");
        return sql.toString();
    }

    public String Se_OrderBy(String[] fields, String[] wheres, String orderby) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  ");
        if (fields.length == 0) {
            sql.append(" *  ");
        } else {
            for (String field : fields) {
                sql.append(field);
                if (field != fields[fields.length - 1]) {
                    sql.append(" , ");
                }
            }
        }
        sql.append(" from ");
        sql.append(Table_Name);
        if (wheres.length > 0) {
            sql.append(" where ");
            for (String where : wheres) {
                sql.append(where);
                sql.append("=?");
                if (where != wheres[wheres.length - 1]) {
                    sql.append(" and  ");
                }

            }
        }
        sql.append(" "+orderby+" ");
        sql.append(";");
        return sql.toString();
    }
    /*
     * 返回默认查询语句
     * 查询全部字段
     */
    public String Se_D() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  ");
        sql.append(" *  ");
        sql.append(" from ");
        sql.append(Table_Name);
        sql.append(";");
        return sql.toString();
    }
    /*
     * 返回删除语句
     * wheres 条件数组
     */
    public String De(String[] wheres) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from  ");
        sql.append(Table_Name);

        if (wheres.length > 0) {
            sql.append(" where ");
            for (String where : wheres) {
                sql.append(where);
                sql.append("=?");
                if (where != wheres[wheres.length - 1]) {
                    sql.append(" and  ");
                }

            }
        }
        sql.append(";");
        return sql.toString();
    }
    /*
     * 返回默认删除语句
     * 删除表所有数据
     */
    public String De_D() {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from  ");
        sql.append(Table_Name);
        sql.append(";");
        return sql.toString();
    }

}

