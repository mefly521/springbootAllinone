package poi.excel.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class 跟据表结构生成数据字典2 {
	private Connection cn = null;
	private List fieldTypes = null;
	private List executedTables = null;
	private String[] types = {"TABLE", "VIEW"}; //只生成表和视图的数据字典

	private String driver;
	private String url;
	private String uid;
	private String pwd;
	private String catalog;
	private String schema;

	public 跟据表结构生成数据字典2()throws Exception{
		initData();
		String fileName = "D:\\code\\allinone\\src\\test\\resources\\config.properties";
		//initSqlServerDBParams();
		initDBParams(new FileInputStream(fileName));

		Class.forName(driver);
		this.cn = DriverManager.getConnection(url, uid, pwd);
	}

	private void initData(){
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

//	/**
//	 * SqlServer数据库连接参数
//	 */
//	private void initSqlServerDBParams(){
//		catalog = "test"; //SqlServer的数据库名
//		schema = null;
//
//		driver = "net.sourceforge.jtds.jdbc.Driver";
//   		url = "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=test";
//   		uid = "test";
//   		pwd = "test";
//	}
//
//	/**
//	 * Oracle数据库连接参数
//	 */
//	private void initOracleDBParams(){
//		catalog = null;
//		schema = "QYCD"; //Oracle的用户名
//
//		driver = "oracle.jdbc.driver.OracleDriver";
//   		url = "jdbc:oracle:thin:@localhost:1521:ORCL";
//   		uid = "qycd";
//   		pwd = "qycd";
//	}
	/**
	 * mysql数据库连接参数
	 * @throws IOException
	 */
	private void initDBParams(FileInputStream fs) throws IOException{
		catalog = "wt_sys"; //数据库名
		schema = "test";

		Properties props = new Properties();
		props.load(fs);

		driver = props.getProperty("driverClass");
		url = props.getProperty("jdbc_url");
		uid = props.getProperty("jdbc_username");
		pwd = props.getProperty("jdbc_password");
	}

	/**
	 * 取得一个表的所有主键字段
	 */
	private String getTablePrimaryKeys(String tableName){
		try{
			DatabaseMetaData dbmd = cn.getMetaData();
			ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, tableName);
			StringBuffer sb = new StringBuffer(",");
			while(rs.next()){
				sb.append(rs.getString("COLUMN_NAME") + ",");
			}
			rs.close();

			return sb.toString();
		}catch(Exception ex){
			return "";
		}
	}

	/**
	 * 生成数据字典
	 */
	public void createTableMetadata(String fileName){
		try{
			if(fileName == null || fileName.length() == 0){
				throw new IllegalArgumentException("fileName is null");
			}

			File file = new File(fileName);

			//delete old file
			if(file.exists() && file.isFile()) file.delete();

			//create sheet
			WritableWorkbook book = Workbook.createWorkbook(new FileOutputStream(file));
			WritableSheet sheet = book.createSheet("数据字典",0);

			DatabaseMetaData dbmd = cn.getMetaData();
			ResultSet rs = dbmd.getTables(catalog ,schema, null, types);
			int rowIndex = 0;
			int tableCount = 0;
			while(rs.next()){
				try{
					String tableName = rs.getString("TABLE_NAME");

					//排除表
					if(executedTables.contains(tableName.toLowerCase())) continue;

					tableCount++;
					System.out.println(tableCount + "、" + tableName + " doing...");

					//表名
					sheet.mergeCells(0, rowIndex, 6, rowIndex);  //合并单元格，5数字要与表头的cell个数一致
					sheet.addCell(new Label(0, rowIndex, tableCount + "、" + tableName));
					rowIndex++;

					//表头
					sheet.addCell(new Label(0,rowIndex,"序号"));
					sheet.addCell(new Label(1,rowIndex,"字段名"));
					sheet.addCell(new Label(2,rowIndex,"中文名称"));
					sheet.addCell(new Label(3,rowIndex,"字段类型"));
					sheet.addCell(new Label(4,rowIndex,"是否可空"));
					sheet.addCell(new Label(5,rowIndex,"主键"));
					sheet.addCell(new Label(6,rowIndex,"字段说明"));
					rowIndex++;

					//主键
					String strPrimaryKeys = getTablePrimaryKeys(tableName);

					Statement stm = cn.createStatement();
					stm.setMaxRows(1);
					ResultSet rsColumn = stm.executeQuery("select * from " + tableName);
					ResultSetMetaData rsmd = rsColumn.getMetaData();
					int recordIndex = 1;
					for(int i=1;i<=rsmd.getColumnCount();i++){
						sheet.addCell(new Label(0,rowIndex,String.valueOf(recordIndex)));
						sheet.addCell(new Label(1,rowIndex,rsmd.getColumnName(i)));
						sheet.addCell(new Label(2,rowIndex,""));

						//字段类型
						String fieldType = rsmd.getColumnTypeName(i);
						if(fieldTypes.contains(fieldType.toUpperCase())){
							fieldType += "(" + rsmd.getColumnDisplaySize(i) + ")";
						}else{
							if(rsmd.getPrecision(i) > 0){
								//精度
								fieldType += "(" + rsmd.getPrecision(i) + "," + rsmd.getScale(i) + ")";
							}
						}
						sheet.addCell(new Label(3,rowIndex,fieldType));

						//是否可为null
						sheet.addCell(new Label(4,rowIndex,(rsmd.isNullable(i)==1)?"":"N"));

						//是否是主键字段
						if(strPrimaryKeys.indexOf("," + rsmd.getColumnName(i) + ",") != -1){
							sheet.addCell(new Label(5,rowIndex,"Y"));
						}else{
							sheet.addCell(new Label(5,rowIndex,""));
						}

						//字段说明
						sheet.addCell(new Label(6,rowIndex,""));

						rowIndex++;
						recordIndex++;
					}
					rowIndex += 2;

					rsColumn.close();
					stm.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			rs.close();

			System.out.println("DONE");

			book.write();
			book.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(cn != null) cn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try{
			跟据表结构生成数据字典2 md = new 跟据表结构生成数据字典2();
			md.createTableMetadata("d:\\md.xls");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
