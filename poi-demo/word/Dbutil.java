package poi.word;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Dbutil {
    private Connection cn = null;
    private List fieldTypes = null;
    private List executedTables = null;
    private String[] types = {"TABLE", "VIEW"}; //只生成表和视图的数据字典

    private String driver;
    private String url;
    private String uid;
    private String pwd;
    private String schema="wt_sys";

    public Dbutil() throws Exception {
        initData();
        String fileName = "D:\\code\\allinone\\src\\test\\resources\\config.properties";
        //initSqlServerDBParams();
        initDBParams(new FileInputStream(fileName));

        Class.forName(driver);
        this.cn = DriverManager.getConnection(url, uid, pwd);
    }

    private void initData() {
        //字符串类型
        fieldTypes = new ArrayList();
        fieldTypes.add("CHAR");
        fieldTypes.add("NCHAR");
        fieldTypes.add("VARCHAR");
        fieldTypes.add("NVARCHAR");
        fieldTypes.add("VARCHAR2");
        fieldTypes.add("NVARCHAR2");

        //排除以下表
        executedTables = new ArrayList();
        executedTables.add("dtproperties");
        executedTables.add("sysconstraints");
        executedTables.add("syssegments");
    }

    /**
     * mysql数据库连接参数
     *
     * @throws IOException
     */
    private void initDBParams(FileInputStream fs) throws IOException {

        Properties props = new Properties();
        props.load(fs);

        driver = props.getProperty("driverClass");
        url = props.getProperty("jdbc_url");
        uid = props.getProperty("jdbc_username");
        pwd = props.getProperty("jdbc_password");
    }

    /**
     * 生成数据字典
     */
    public List<Map> getColumnInfo() {
        List<Map> list = new ArrayList();
        try {
            Statement stmOut = cn.createStatement();
            ResultSet rs = stmOut.executeQuery("SHOW TABLE STATUS LIKE '%';");
            while (rs.next()) {
                try {
                    Map mapOut = new HashMap(2);
                    String tableName = rs.getString("Name");
                    String comment = rs.getString("Comment");
                    mapOut.put("tableName", tableName);
                    mapOut.put("tableComment", comment);
                    Statement stm = cn.createStatement();
                    String sql = "SELECT  column_name ,is_nullable,column_type,column_key,column_comment " +
                            "FROM information_schema.columns WHERE table_schema ='@schema@'  AND " +
                            "table_name = '@tableName@' ";
                    sql = sql.replaceAll("@tableName@", tableName);
                    sql = sql.replaceAll("@schema@", schema);
                    ResultSet rsIn = stm.executeQuery(sql);

                    List<Map> listColumn = new ArrayList();
                    while (rsIn.next()) {
                        Map map = new HashMap(5);
                        map.put("column_name", rsIn.getString("column_name"));
                        map.put("is_nullable", rsIn.getString("is_nullable"));
                        map.put("column_type", rsIn.getString("column_type"));
                        map.put("column_key", rsIn.getString("column_key"));
                        map.put("column_comment", rsIn.getString("column_comment"));
                        listColumn.add(map);
                    }
                    mapOut.put("column", listColumn);
                    list.add(mapOut);
                    rsIn.close();
                    stm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rs.close();
            stmOut.close();
            System.out.println("DONE");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            Dbutil dbutil = new Dbutil();
            dbutil.getColumnInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
