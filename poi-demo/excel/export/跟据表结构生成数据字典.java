package poi.excel.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class 跟据表结构生成数据字典 {
	private Connection con = null;
	private List fieldTypes = null;
	private List executedTables = null;
	private String[] types = {"TABLE", "VIEW"}; //只生成表和视图的数据字典
	private String dbname = "kxe";

	private String driver;
	private String url;
	private String uid;
	private String pwd;
	private String catalog;
	private String schema;

	public 跟据表结构生成数据字典()throws Exception{
		initData();
		String fileName = "D:\\code\\allinone\\src\\test\\resources\\config.properties";
		//initSqlServerDBParams();
		initDBParams(new FileInputStream(fileName));

		Class.forName(driver);
		this.con = DriverManager.getConnection(url, uid, pwd);
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
		catalog = "kxe"; //数据库名
		schema = "test";

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

			PreparedStatement ps = con.prepareStatement("SELECT table_name,table_comment FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = 'kxe' ");
			ResultSet rs = ps.executeQuery();
			int col = rs.getMetaData().getColumnCount();

			int rowIndex = 0;
			int tableCount = 0;
			while(rs.next()){
				try{
					String tableName = rs.getString("table_name");
					String table_comment = rs.getString("table_comment");

					//排除表
					if(executedTables.contains(tableName.toLowerCase())) continue;

					tableCount++;
					System.out.println(tableCount + "、" + tableName + " doing...");

					//表名
					sheet.mergeCells(0, rowIndex, 6, rowIndex);  //合并单元格，5数字要与表头的cell个数一致
					sheet.addCell(new Label(0, rowIndex, tableCount + "、" + tableName+table_comment));
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
					String strPrimaryKeys = "";//getTablePrimaryKeys(tableName);

					Statement stm = con.createStatement();
					ResultSet rsColumn = stm.executeQuery(String.format("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema ='%s' AND table_name = '%s'; ",dbname,tableName));

					int recordIndex = 1;

					while(rsColumn.next()){
						sheet.addCell(new Label(0,rowIndex,String.valueOf(recordIndex)));//序号
						sheet.addCell(new Label(1,rowIndex,rsColumn.getString(4)));//显示字段英文名
						sheet.addCell(new Label(2,rowIndex,rsColumn.getString(20)));//显示字段中文注释名
						//字段类型
						String fieldType = rsColumn.getString(8);
//						if(fieldTypes.contains(fieldType.toUpperCase())){//如果是字符型
//							fieldType += "(" + rsColumn.getString(9) + ")";
//						}else{
//								//精度
//							fieldType += "(" + rsColumn.getString(11) + "," + rsColumn.getString(12)  + ")";//数字型
//						}
						sheet.addCell(new Label(3,rowIndex,rsColumn.getString(16)));	//显示字段类型和长度
						sheet.addCell(new Label(4,rowIndex,"YES".equals(rsColumn.getString(7))?"是":"否"));//是否可为null
						sheet.addCell(new Label(5,rowIndex,"PRI".equals(rsColumn.getString("column_key"))?"是":""));//是否是主键字段
						sheet.addCell(new Label(6,rowIndex,""));//字段说明
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
				if(con != null) con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try{
			跟据表结构生成数据字典 md = new 跟据表结构生成数据字典();
			md.createTableMetadata("d:\\md.xls");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
