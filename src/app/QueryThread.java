package app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

enum QueryType{
	PF,//排放
	TT,//淘汰
	M,//M状态
	SY;//剩余黄标车	
}
public class QueryThread implements Runnable {
	private String tablename;
	private String[] citycode;
	private QueryType type;
	private String filename;

	private String taotaisql= "select fzjg,xzqh,hphm,hpzl,clxh,cllx,rlzl,hdzk,zzl,syxz,ccdjrq,zt from TAOTAI_%s where fzjg='%s'";
	
	private String shengyusql="select fzjg,xzqh,hphm,clxh,syxz,cllx,clsbdh,ccdjrq,rlzl,qzbfqz,zt from sy_hbc_2016_qiang2 where xh not in(select xh from taotai_%s) and fzjg='%s'";
	
	private String msql="select fzjg,xzqh,hphm,hpzl,clxh,cllx,rlzl,hdzk,zzl,syxz,ccdjrq,zt from M_%s where fzjg='%s'";
	
	private String citysql = "select o.order_id,o.index_name," 
			+ "case when pf0.p0 is null then 0 else pf0.p0 end p0 ,"
			+ "case when pf1.p1 is null then 0 else pf1.p1 end p1 ,"
			+ "case when pf2.p2 is null then 0 else pf2.p2 end p2 ,"
			+ "case when pf3.p3 is null then 0 else pf3.p3 end p3 ,"
			+ "case when pf4.p4 is null then 0 else pf4.p4 end p4 ,"
			+ "case when pf5.p5 is null then 0 else pf5.p5 end p5 " + "from index_order o " + "left join "
			+ "(select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from pf0_%s pf where pf.FZJG='%s' "
			+ "group by index_name) pf0 on o.index_name=pf0.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from pf1_%s pf where pf.FZJG='%s' "
			+ "group by index_name) pf1 on o.index_name=pf1.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from pf2_%s pf where pf.FZJG='%s' "
			+ "group by index_name) pf2 on o.index_name=pf2.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from pf3_%s pf where pf.FZJG='%s' "
			+ "group by index_name) pf3 on o.index_name=pf3.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from pf4_%s pf where pf.FZJG='%s' "
			+ "group by index_name) pf4 on o.index_name=pf4.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from pf5_%s pf where pf.FZJG='%s' "
			+ "group by index_name) pf5 on o.index_name=pf5.index_name " + "order by o.order_id";

	private String summarysql = "select o.order_id,o.index_name,"
			+ "case when pf0.p0 is null then 0 else pf0.p0 end p0 ,"
			+ "case when pf1.p1 is null then 0 else pf1.p1 end p1 ,"
			+ "case when pf2.p2 is null then 0 else pf2.p2 end p2 ,"
			+ "case when pf3.p3 is null then 0 else pf3.p3 end p3 ,"
			+ "case when pf4.p4 is null then 0 else pf4.p4 end p4 ,"
			+ "case when pf5.p5 is null then 0 else pf5.p5 end p5 " + "from index_order o "
			+ "left join (select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from pf0_%s pf group by index_name) pf0 on o.index_name=pf0.index_name "
			+ "left join (select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from pf1_%s pf group by index_name) pf1 on o.index_name=pf1.index_name "
			+ "left join (select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from pf2_%s pf group by index_name) pf2 on o.index_name=pf2.index_name "
			+ "left join (select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from pf3_%s pf group by index_name) pf3 on o.index_name=pf3.index_name "
			+ "left join (select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from pf4_%s pf group by index_name) pf4 on o.index_name=pf4.index_name "
			+ "left join (select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from pf5_%s pf group by index_name) pf5 on o.index_name=pf5.index_name "
			+ "order by o.order_id";

	public QueryThread(QueryType type,String tablename, String[] citycode,String filename) {
		super();
		this.type=type;
		this.tablename = tablename;
		this.citycode = citycode;
		if(filename==null || filename.isEmpty()) this.filename=tablename;
		else this.filename=filename;
	}

	protected void queryPaiFang() {
		List<PaiFang> pfList = new ArrayList<PaiFang>();

		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		try {
			con = getConn();
			System.out.println("连接成功！");
			if (citycode[0].equals("Summary")) {
				String sql = String.format(summarysql, tablename, tablename, tablename, tablename, tablename,
						tablename);
				pre = con.prepareStatement(sql);// 实例化预编译语句
				pre.setQueryTimeout(0);

				result = pre.executeQuery();
				while (result.next()) {
					pfList.add(new PaiFang(result.getString("index_name"), result.getInt("p0"), result.getInt("p1"),
							result.getInt("p2"), result.getInt("p3"), result.getInt("p4"), result.getInt("p5")));
				}

				ExportExcel<PaiFang> expPaiFang = new ExportExcel<PaiFang>();
				String[] titleCityCode = new String[] { "发证机关", "p0", "p1", "p2", "p3", "p4", "p5" };
				OutputStream out = new FileOutputStream(
						System.getProperty("user.dir") + File.separator + filename + "_Summary.xls");
				expPaiFang.exportExcel(titleCityCode, pfList, out);
				out.close();
				System.out.println(tablename + "_Summary is finished");

			} else {
				for (String city : citycode) {
					String sql = String.format(citysql, tablename, city, tablename, city, tablename, city, tablename,
							city, tablename, city, tablename, city);

					pre = con.prepareStatement(sql);// 实例化预编译语句
					pre.setQueryTimeout(0);

					result = pre.executeQuery();
					while (result.next()) {
						pfList.add(new PaiFang(result.getString("index_name"), result.getInt("p0"), result.getInt("p1"),
								result.getInt("p2"), result.getInt("p3"), result.getInt("p4"), result.getInt("p5")));
					}

					ExportExcel<PaiFang> expPaiFang = new ExportExcel<PaiFang>();
					String[] titleCityCode = new String[] { "发证机关", "p0", "p1", "p2", "p3", "p4", "p5" };
					OutputStream out = new FileOutputStream(
							System.getProperty("user.dir") + File.separator + filename + "_" + city + ".xls");
					expPaiFang.exportExcel(titleCityCode, pfList, out);
					out.close();
					pfList.clear();
					System.out.println(filename + "_" + city + " is finished");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				// 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
				// 注意关闭的顺序，最后使用的最先关闭
				if (result != null)
					result.close();
				if (pre != null)
					pre.close();
				if (con != null)
					con.close();
				System.out.println("数据库连接已关闭！");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected void queryTaoTai() {
		List<TaoTai> ttList=new ArrayList<TaoTai>();
		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		try {
			con = getConn();
			System.out.println("连接成功！");
			String sql;
			for (String city : citycode) {
				if(type==QueryType.TT)
					 sql= String.format(taotaisql,tablename,city);
				else
					 sql = String.format(msql,tablename,city);
				pre = con.prepareStatement(sql);// 实例化预编译语句
				pre.setQueryTimeout(0);

				result = pre.executeQuery();
				while (result.next()) {
					ttList.add(new TaoTai(result.getString("fzjg"),result.getString("xzqh"),result.getString("hphm"),result.getString("hpzl"),
							result.getString("clxh"),result.getString("cllx"),result.getString("rlzl"),result.getString("hdzk"),
							result.getString("zzl"),result.getString("syxz"),result.getDate("ccdjrq"),result.getString("zt") ));
				}

				ExportExcel<TaoTai> expTaoTai = new ExportExcel<TaoTai>();
				String[] titleCityCode = new String[] { "发证机关", "行政区划","号牌号码", "号牌种类", "车辆型号","车辆类型", "燃料种类","核定载客", 
					"总质量", "使用性质","初次登记日期","状态" };
				OutputStream out = new FileOutputStream(
						System.getProperty("user.dir") + File.separator + filename + "_" + city + ".xls");
				expTaoTai.exportExcel(titleCityCode, ttList, out);
				out.close();
				ttList.clear();
				System.out.println(filename + "_" + city + " is finished");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				// 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
				// 注意关闭的顺序，最后使用的最先关闭
				if (result != null)
					result.close();
				if (pre != null)
					pre.close();
				if (con != null)
					con.close();
				System.out.println("数据库连接已关闭！");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}		
	}
	
	protected void queryShengYu() {
		List<ShengYu> syList=new ArrayList<ShengYu>();
		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		try {
			con = getConn();
			System.out.println("连接成功！");
			for (String city : citycode) {
				String sql = String.format(shengyusql,tablename,city);

				pre = con.prepareStatement(sql);// 实例化预编译语句
				pre.setQueryTimeout(0);

				result = pre.executeQuery();
				while (result.next()) {
					syList.add(new ShengYu(result.getString("fzjg"),result.getString("xzqh"),result.getString("hphm"),result.getString("clxh"),
							result.getString("syxz"),result.getString("cllx"),result.getString("clsbdh"),result.getDate("ccdjrq"),
							result.getString("rlzl"),result.getDate("qzbfqz"),result.getString("zt") ));
				}

				ExportExcel<ShengYu> expShengYu = new ExportExcel<ShengYu>();
				String[] titleCityCode = new String[] { "发证机关","行政区划", "号牌号码", "车辆型号","使用性质","车辆类型",
						"车辆识别代码（VIN号后6位）", "初次登记日期","燃料种类","强制报废期止", "状态" };
				OutputStream out = new FileOutputStream(
						System.getProperty("user.dir") + File.separator + filename + "_" + city + ".xls");
				expShengYu.exportExcel(titleCityCode, syList, out);
				out.close();
				syList.clear();
				System.out.println(filename + "_" + city + " is finished");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				// 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
				// 注意关闭的顺序，最后使用的最先关闭
				if (result != null)
					result.close();
				if (pre != null)
					pre.close();
				if (con != null)
					con.close();
				System.out.println("数据库连接已关闭！");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}

	private Connection getConn() throws ClassNotFoundException, SQLException {
		Connection con;
		Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
		System.out.println("开始尝试连接数据库！");
		String url = "jdbc:oracle:" + "thin:@127.0.0.1:1521:HBTDB";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
		String user = "flowerszhong";// 用户名,系统默认的账户名
		String password = "3pang";// 你安装时选设置的密码
		con = DriverManager.getConnection(url, user, password);// 获取连接
		return con;
	}

	@Override
	public void run() {
		switch (type) {
		case PF:
			queryPaiFang();
			break;
		case TT:
			queryTaoTai();
			break;			
		case M:
			queryTaoTai();
			break;
		case SY:
			queryShengYu();
			break;
		default:
			break;
		}
		
	}

}
