package cn.com.hsh.platform.zz;

import java.util.LinkedHashMap;

/**
 * 查表的SQL语句生成工具类
 */

public class zz_Table {
    private String name;
    private LinkedHashMap<String, String> Table;
    public zz_Table(String name){
        this.name=name;
        Table=new LinkedHashMap<String, String>();
    }

    public LinkedHashMap<String, String> getTable() {
        return Table;
    }

    public void add(String field, String type){
        Table.put(field,type);
    }
    public String getCr(){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.Cr();
    }
    public String getSe(String[] fields, String[] wheres){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.Se(fields, wheres);
    }
    public String getSe_OrderBy(String[] fields, String[] wheres, String orderby){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.Se_OrderBy(fields, wheres, orderby);
    }
    public String getUp(String[] fields, String[] values, String[] wheres){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.Up( fields, values, wheres);
    }
    public String getSe_D(){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.Se_D();
    }
    public String getDe(String[] wheres){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.De(wheres);
    }
    public String getDe_D(){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.De_D();
    }
    public String getIn_D(){
        zz_generateSQL zz_generateSQL=new zz_generateSQL(Table,name);
        return  zz_generateSQL.In_D();
    }
}
