package org.jeecgframework.codegenerate.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.codegenerate.pojo.Columnt;
import org.jeecgframework.codegenerate.pojo.TableConvert;
import org.jeecgframework.codegenerate.util.CodeResourceUtil;
import org.jeecgframework.codegenerate.util.CodeStringUtils;

public class JeecgReadTable {
	
	private Connection jdField_a_of_type_JavaSqlConnection;
	private Statement jdField_a_of_type_JavaSqlStatement;
	private String jdField_a_of_type_JavaLangString;
	private ResultSet jdField_a_of_type_JavaSqlResultSet;

	@SuppressWarnings("rawtypes")
	public static void main(String[] paramArrayOfString) {
		try {
			List localList = new JeecgReadTable().readTableColumn("person");
			Iterator localIterator = localList.iterator();
			while (localIterator.hasNext()) {
				Columnt localColumnt = (Columnt) localIterator.next();
				System.out.println(localColumnt.getFieldName());
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List readTableColumn(String paramString) throws Exception {
		ArrayList localArrayList1 = new ArrayList();
		Columnt localColumnt1;
		try {
			Class.forName(CodeResourceUtil.DIVER_NAME);
			this.jdField_a_of_type_JavaSqlConnection = DriverManager
					.getConnection(CodeResourceUtil.URL,
							CodeResourceUtil.USERNAME,
							CodeResourceUtil.PASSWORD);
			this.jdField_a_of_type_JavaSqlStatement = this.jdField_a_of_type_JavaSqlConnection
					.createStatement(1005, 1007);
			if (CodeResourceUtil.DATABASE_TYPE.equals("mysql"))
				this.jdField_a_of_type_JavaLangString = MessageFormat
						.format("select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1}",
								new Object[] {
										TableConvert.getV(paramString
												.toUpperCase()),
										TableConvert
												.getV(CodeResourceUtil.DATABASE_NAME) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("oracle"))
				this.jdField_a_of_type_JavaLangString = MessageFormat
						.format(" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}",
								new Object[] { TableConvert.getV(paramString
										.toUpperCase()) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("postgresql"))
				this.jdField_a_of_type_JavaLangString = MessageFormat
						.format("SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ",
								new Object[] { TableConvert.getV(paramString
										.toLowerCase()) });
			this.jdField_a_of_type_JavaSqlResultSet = this.jdField_a_of_type_JavaSqlStatement
					.executeQuery(this.jdField_a_of_type_JavaLangString);
			this.jdField_a_of_type_JavaSqlResultSet.last();
			int i = this.jdField_a_of_type_JavaSqlResultSet.getRow();

			// FIXME CWZ j = i;
			int j = i;

			if (j > 0) {
				localColumnt1 = new Columnt();
				if (CodeResourceUtil.JEECG_FILED_CONVERT)
					localColumnt1
							.setFieldName(formatField(this.jdField_a_of_type_JavaSqlResultSet
									.getString(1).toLowerCase()));
				else
					localColumnt1
							.setFieldName(this.jdField_a_of_type_JavaSqlResultSet
									.getString(1).toLowerCase());
				localColumnt1
						.setFieldDbName(this.jdField_a_of_type_JavaSqlResultSet
								.getString(1).toUpperCase());
				localColumnt1
						.setFieldType(formatField(this.jdField_a_of_type_JavaSqlResultSet
								.getString(2).toLowerCase()));
				localColumnt1
						.setPrecision(this.jdField_a_of_type_JavaSqlResultSet
								.getString(4));
				localColumnt1.setScale(this.jdField_a_of_type_JavaSqlResultSet
						.getString(5));
				localColumnt1
						.setCharmaxLength(this.jdField_a_of_type_JavaSqlResultSet
								.getString(6));
				localColumnt1.setNullable(TableConvert
						.getNullAble(this.jdField_a_of_type_JavaSqlResultSet
								.getString(7)));
				a(localColumnt1);
				localColumnt1.setFiledComment(StringUtils
						.isBlank(this.jdField_a_of_type_JavaSqlResultSet
								.getString(3)) ? localColumnt1.getFieldName()
						: this.jdField_a_of_type_JavaSqlResultSet.getString(3));
				String[] arrayOfString = new String[0];
				if (CodeResourceUtil.JEECG_GENERATE_UI_FILTER_FIELDS != null)
					arrayOfString = CodeResourceUtil.JEECG_GENERATE_UI_FILTER_FIELDS
							.toLowerCase().split(",");
				if ((!CodeResourceUtil.JEECG_GENERATE_TABLE_ID
						.equals(localColumnt1.getFieldName()))
						&& (!CodeStringUtils.isIn(localColumnt1
								.getFieldDbName().toLowerCase(), arrayOfString)))
					localArrayList1.add(localColumnt1);
				while (this.jdField_a_of_type_JavaSqlResultSet.previous()) {
					Columnt localColumnt2 = new Columnt();
					if (CodeResourceUtil.JEECG_FILED_CONVERT)
						localColumnt2
								.setFieldName(formatField(this.jdField_a_of_type_JavaSqlResultSet
										.getString(1).toLowerCase()));
					else
						localColumnt2
								.setFieldName(this.jdField_a_of_type_JavaSqlResultSet
										.getString(1).toLowerCase());
					localColumnt2
							.setFieldDbName(this.jdField_a_of_type_JavaSqlResultSet
									.getString(1).toUpperCase());
					if ((CodeResourceUtil.JEECG_GENERATE_TABLE_ID
							.equals(localColumnt2.getFieldName()))
							|| (CodeStringUtils.isIn(localColumnt2
									.getFieldDbName().toLowerCase(),
									arrayOfString)))
						continue;
					localColumnt2
							.setFieldType(formatField(this.jdField_a_of_type_JavaSqlResultSet
									.getString(2).toLowerCase()));
					localColumnt2
							.setPrecision(this.jdField_a_of_type_JavaSqlResultSet
									.getString(4));
					localColumnt2
							.setScale(this.jdField_a_of_type_JavaSqlResultSet
									.getString(5));
					localColumnt2
							.setCharmaxLength(this.jdField_a_of_type_JavaSqlResultSet
									.getString(6));
					localColumnt2
							.setNullable(TableConvert
									.getNullAble(this.jdField_a_of_type_JavaSqlResultSet
											.getString(7)));
					a(localColumnt2);
					localColumnt2.setFiledComment(StringUtils
							.isBlank(this.jdField_a_of_type_JavaSqlResultSet
									.getString(3)) ? localColumnt2
							.getFieldName()
							: this.jdField_a_of_type_JavaSqlResultSet
									.getString(3));
					localArrayList1.add(localColumnt2);
				}
			} else {
				throw new RuntimeException("该表不存在或者表中没有字段");
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw localClassNotFoundException;
		} catch (SQLException localSQLException1) {
			throw localSQLException1;
		} finally {
			try {
				if (this.jdField_a_of_type_JavaSqlStatement != null) {
					this.jdField_a_of_type_JavaSqlStatement.close();
					this.jdField_a_of_type_JavaSqlStatement = null;
					System.gc();
				}
				if (this.jdField_a_of_type_JavaSqlConnection != null) {
					this.jdField_a_of_type_JavaSqlConnection.close();
					this.jdField_a_of_type_JavaSqlConnection = null;
					System.gc();
				}
			} catch (SQLException localSQLException2) {
				throw localSQLException2;
			}
		}
		ArrayList localArrayList2 = new ArrayList();
		for (int j = localArrayList1.size() - 1; j >= 0; j--) {
			localColumnt1 = (Columnt) localArrayList1.get(j);
			localArrayList2.add(localColumnt1);
		}
		return localArrayList2;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List readOriginalTableColumn(String paramString) throws Exception {
		ArrayList localArrayList1 = new ArrayList();
		Columnt localColumnt1;
		try {
			Class.forName(CodeResourceUtil.DIVER_NAME);
			this.jdField_a_of_type_JavaSqlConnection = DriverManager
					.getConnection(CodeResourceUtil.URL,
							CodeResourceUtil.USERNAME,
							CodeResourceUtil.PASSWORD);
			this.jdField_a_of_type_JavaSqlStatement = this.jdField_a_of_type_JavaSqlConnection
					.createStatement(1005, 1007);
			if (CodeResourceUtil.DATABASE_TYPE.equals("mysql"))
				this.jdField_a_of_type_JavaLangString = MessageFormat
						.format("select column_name,data_type,column_comment,numeric_precision,numeric_scale,character_maximum_length,is_nullable nullable from information_schema.columns where table_name = {0} and table_schema = {1}",
								new Object[] {
										TableConvert.getV(paramString
												.toUpperCase()),
										TableConvert
												.getV(CodeResourceUtil.DATABASE_NAME) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("oracle"))
				this.jdField_a_of_type_JavaLangString = MessageFormat
						.format(" select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment, colstable.Data_Precision column_precision, colstable.Data_Scale column_scale,colstable.Char_Length,colstable.nullable from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = {0}",
								new Object[] { TableConvert.getV(paramString
										.toUpperCase()) });
			if (CodeResourceUtil.DATABASE_TYPE.equals("postgresql"))
				this.jdField_a_of_type_JavaLangString = MessageFormat
						.format("SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ",
								new Object[] { TableConvert.getV(paramString
										.toLowerCase()) });
			this.jdField_a_of_type_JavaSqlResultSet = this.jdField_a_of_type_JavaSqlStatement
					.executeQuery(this.jdField_a_of_type_JavaLangString);
			this.jdField_a_of_type_JavaSqlResultSet.last();
			int i = this.jdField_a_of_type_JavaSqlResultSet.getRow();

			// FIXME CWZ j = i;
			int j = i;

			if (j > 0) {
				localColumnt1 = new Columnt();
				if (CodeResourceUtil.JEECG_FILED_CONVERT)
					localColumnt1
							.setFieldName(formatField(this.jdField_a_of_type_JavaSqlResultSet
									.getString(1).toLowerCase()));
				else
					localColumnt1
							.setFieldName(this.jdField_a_of_type_JavaSqlResultSet
									.getString(1).toLowerCase());
				localColumnt1
						.setFieldDbName(this.jdField_a_of_type_JavaSqlResultSet
								.getString(1).toUpperCase());
				localColumnt1.setPrecision(TableConvert
						.getNullString(this.jdField_a_of_type_JavaSqlResultSet
								.getString(4)));
				localColumnt1.setScale(TableConvert
						.getNullString(this.jdField_a_of_type_JavaSqlResultSet
								.getString(5)));
				localColumnt1.setCharmaxLength(TableConvert
						.getNullString(this.jdField_a_of_type_JavaSqlResultSet
								.getString(6)));
				localColumnt1.setNullable(TableConvert
						.getNullAble(this.jdField_a_of_type_JavaSqlResultSet
								.getString(7)));
				localColumnt1.setFieldType(a(
						this.jdField_a_of_type_JavaSqlResultSet.getString(2)
								.toLowerCase(), localColumnt1.getPrecision(),
						localColumnt1.getScale()));
				a(localColumnt1);
				localColumnt1.setFiledComment(StringUtils
						.isBlank(this.jdField_a_of_type_JavaSqlResultSet
								.getString(3)) ? localColumnt1.getFieldName()
						: this.jdField_a_of_type_JavaSqlResultSet.getString(3));
				localArrayList1.add(localColumnt1);
				while (this.jdField_a_of_type_JavaSqlResultSet.previous()) {
					Columnt localColumnt2 = new Columnt();
					if (CodeResourceUtil.JEECG_FILED_CONVERT)
						localColumnt2
								.setFieldName(formatField(this.jdField_a_of_type_JavaSqlResultSet
										.getString(1).toLowerCase()));
					else
						localColumnt2
								.setFieldName(this.jdField_a_of_type_JavaSqlResultSet
										.getString(1).toLowerCase());
					localColumnt2
							.setFieldDbName(this.jdField_a_of_type_JavaSqlResultSet
									.getString(1).toUpperCase());
					localColumnt2
							.setPrecision(TableConvert
									.getNullString(this.jdField_a_of_type_JavaSqlResultSet
											.getString(4)));
					localColumnt2
							.setScale(TableConvert
									.getNullString(this.jdField_a_of_type_JavaSqlResultSet
											.getString(5)));
					localColumnt2
							.setCharmaxLength(TableConvert
									.getNullString(this.jdField_a_of_type_JavaSqlResultSet
											.getString(6)));
					localColumnt2
							.setNullable(TableConvert
									.getNullAble(this.jdField_a_of_type_JavaSqlResultSet
											.getString(7)));
					localColumnt2.setFieldType(a(
							this.jdField_a_of_type_JavaSqlResultSet
									.getString(2).toLowerCase(), localColumnt2
									.getPrecision(), localColumnt2.getScale()));
					a(localColumnt2);
					localColumnt2.setFiledComment(StringUtils
							.isBlank(this.jdField_a_of_type_JavaSqlResultSet
									.getString(3)) ? localColumnt2
							.getFieldName()
							: this.jdField_a_of_type_JavaSqlResultSet
									.getString(3));
					localArrayList1.add(localColumnt2);
				}
			} else {
				throw new RuntimeException("该表不存在或者表中没有字段");
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw localClassNotFoundException;
		} catch (SQLException localSQLException1) {
			throw localSQLException1;
		} finally {
			try {
				if (this.jdField_a_of_type_JavaSqlStatement != null) {
					this.jdField_a_of_type_JavaSqlStatement.close();
					this.jdField_a_of_type_JavaSqlStatement = null;
					System.gc();
				}
				if (this.jdField_a_of_type_JavaSqlConnection != null) {
					this.jdField_a_of_type_JavaSqlConnection.close();
					this.jdField_a_of_type_JavaSqlConnection = null;
					System.gc();
				}
			} catch (SQLException localSQLException2) {
				throw localSQLException2;
			}
		}
		ArrayList localArrayList2 = new ArrayList();
		for (int j = localArrayList1.size() - 1; j >= 0; j--) {
			localColumnt1 = (Columnt) localArrayList1.get(j);
			localArrayList2.add(localColumnt1);
		}
		return localArrayList2;
	}

	public static String formatField(String paramString) {
		String[] arrayOfString = paramString.split("_");
		paramString = "";
		int i = 0;
		int j = arrayOfString.length;
		while (i < j) {
			if (i > 0) {
				String str = arrayOfString[i].toLowerCase();
				str = str.substring(0, 1).toUpperCase()
						+ str.substring(1, str.length());
				paramString = paramString + str;
			} else {
				paramString = paramString + arrayOfString[i].toLowerCase();
			}
			i++;
		}
		return paramString;
	}

	public static String formatFieldCapital(String paramString) {
		String[] arrayOfString = paramString.split("_");
		paramString = "";
		int i = 0;
		int j = arrayOfString.length;
		while (i < j) {
			if (i > 0) {
				String str = arrayOfString[i].toLowerCase();
				str = str.substring(0, 1).toUpperCase()
						+ str.substring(1, str.length());
				paramString = paramString + str;
			} else {
				paramString = paramString + arrayOfString[i].toLowerCase();
			}
			i++;
		}
		paramString = paramString.substring(0, 1).toUpperCase()
				+ paramString.substring(1);
		return paramString;
	}

	public boolean checkTableExist(String paramString) {
		try {
			System.out.println("数据库驱动: " + CodeResourceUtil.DIVER_NAME);
			Class.forName(CodeResourceUtil.DIVER_NAME);
			this.jdField_a_of_type_JavaSqlConnection = DriverManager
					.getConnection(CodeResourceUtil.URL,
							CodeResourceUtil.USERNAME,
							CodeResourceUtil.PASSWORD);
			this.jdField_a_of_type_JavaSqlStatement = this.jdField_a_of_type_JavaSqlConnection
					.createStatement(1005, 1007);
			if (CodeResourceUtil.DATABASE_TYPE.equals("mysql"))
				this.jdField_a_of_type_JavaLangString = ("select column_name,data_type,column_comment,0,0 from information_schema.columns where table_name = '"
						+ paramString.toUpperCase()
						+ "'"
						+ " and table_schema = '"
						+ CodeResourceUtil.DATABASE_NAME + "'");
			if (CodeResourceUtil.DATABASE_TYPE.equals("oracle"))
				this.jdField_a_of_type_JavaLangString = ("select colstable.column_name column_name, colstable.data_type data_type, commentstable.comments column_comment from user_tab_cols colstable  inner join user_col_comments commentstable  on colstable.column_name = commentstable.column_name  where colstable.table_name = commentstable.table_name  and colstable.table_name = '"
						+ paramString.toUpperCase() + "'");
			if (CodeResourceUtil.DATABASE_TYPE.equals("postgresql"))
				this.jdField_a_of_type_JavaLangString = MessageFormat
						.format("SELECT a.attname AS  field,t.typname AS type,col_description(a.attrelid,a.attnum) as comment,null as column_precision,null as column_scale,null as Char_Length,a.attnotnull  FROM pg_class c,pg_attribute  a,pg_type t  WHERE c.relname = {0} and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum ",
								new Object[] { TableConvert.getV(paramString
										.toLowerCase()) });
			this.jdField_a_of_type_JavaSqlResultSet = this.jdField_a_of_type_JavaSqlStatement
					.executeQuery(this.jdField_a_of_type_JavaLangString);
			this.jdField_a_of_type_JavaSqlResultSet.last();
			int i = this.jdField_a_of_type_JavaSqlResultSet.getRow();
			if (i > 0)
				return true;
		} catch (Exception localException) {
			localException.printStackTrace();
			return false;
		}
		return false;
	}

	private void a(Columnt paramColumnt) {
		String str1 = paramColumnt.getFieldType();
		String str2 = paramColumnt.getScale();
		paramColumnt.setClassType("inputxt");
		if ("N".equals(paramColumnt.getNullable()))
			paramColumnt.setOptionType("*");
		if (("datetime".equals(str1)) || (str1.contains("time")))
			paramColumnt.setClassType("easyui-datetimebox");
		else if ("date".equals(str1))
			paramColumnt.setClassType("easyui-datebox");
		else if (str1.contains("int"))
			paramColumnt.setOptionType("n");
		else if ("number".equals(str1)) {
			if ((StringUtils.isNotBlank(str2)) && (Integer.parseInt(str2) > 0))
				paramColumnt.setOptionType("d");
		} else if (("float".equals(str1)) || ("double".equals(str1))
				|| ("decimal".equals(str1)))
			paramColumnt.setOptionType("d");
		else if ("numeric".equals(str1))
			paramColumnt.setOptionType("d");
	}

	private String a(String paramString1, String paramString2,
			String paramString3) {
		if (paramString1.contains("char"))
			paramString1 = "java.lang.String";
		else if (paramString1.contains("int"))
			paramString1 = "java.lang.Integer";
		else if (paramString1.contains("float"))
			paramString1 = "java.lang.Float";
		else if (paramString1.contains("double"))
			paramString1 = "java.lang.Double";
		else if (paramString1.contains("number")) {
			if ((StringUtils.isNotBlank(paramString3))
					&& (Integer.parseInt(paramString3) > 0))
				paramString1 = "java.math.BigDecimal";
			else if ((StringUtils.isNotBlank(paramString2))
					&& (Integer.parseInt(paramString2) > 10))
				paramString1 = "java.lang.Long";
			else
				paramString1 = "java.lang.Integer";
		} else if (paramString1.contains("decimal"))
			paramString1 = "BigDecimal";
		else if (paramString1.contains("date"))
			paramString1 = "java.util.Date";
		else if (paramString1.contains("time"))
			paramString1 = "java.util.Date";
		else if (paramString1.contains("blob"))
			paramString1 = "byte[]";
		else if (paramString1.contains("clob"))
			paramString1 = "java.sql.Clob";
		else if (paramString1.contains("numeric"))
			paramString1 = "BigDecimal";
		else
			paramString1 = "java.lang.Object";
		return paramString1;
	}
}

/*
 * Location:
 * E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB
 * -INF\lib\org.jeecgframework.codegenerate.jar Qualified Name:
 * org.jeecgframework.codegenerate.database.JeecgReadTable JD-Core Version:
 * 0.6.0
 */