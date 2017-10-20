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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Pos;

/**
 * 后台查询操作线程
 * 
 * @author BonizLee
 *
 */
public class QueryThread implements Runnable {
	private String dbDate;
	private String[] citycode;
	private QueryType type;
	private String filename;
	private Map<String, Object> words;

	private String taotaisql = "select fzjg,xzqh,hphm,hpzl,clxh,cllx,rlzl,hdzk,zzl,syxz,ccdjrq,zt "
			+ "from TAOTAI_{DBDate} where city='{City}'";

	private String shengyusql = "select vc.fzjg,vc.xzqh,vc.hphm,vc.clxh,vc.syxz,vc.cllx,vc.clsbdh,vc.ccdjrq,vc.rlzl,vc.qzbfqz,vc.zt "
			+ "from VEHICLE_HBT_{DBDate} vc , SY_HBC_2016_all v5 where "
			+ "vc.xh=v5.xh and instr(vc.zt,'E')=0 and instr(vc.zt,'P')=0 and vc.fzjg='{City}'";

	private String msql = "select fzjg,xzqh,hphm,hpzl,clxh,cllx,rlzl,hdzk,zzl,syxz,ccdjrq,zt "
			+ "from M_{DBDate} where fzjg='{City}'";

	private String dateTemplate = "to_date('{year}/{month}/{day}','yyyy/mm/dd')";

	private String logoutsql2017="select o.order_id,o.index_name,\r\n" + 
			"case when pf0.p0 is null then 0 else pf0.p0 end p0 ,\r\n" + 
			"case when pf1.p1 is null then 0 else pf1.p1 end p1 ,\r\n" + 
			"case when pf2.p2 is null then 0 else pf2.p2 end p2 ,\r\n" + 
			"case when pf3.p3 is null then 0 else pf3.p3 end p3 ,\r\n" + 
			"case when pf4.p4 is null then 0 else pf4.p4 end p4 ,\r\n" + 
			"case when pf5.p5 is null then 0 else pf5.p5 end p5 \r\n" + 
			"from index_order o\r\n" + 
			"left join (select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_logout_hbt_{DBDate} v \r\n" + 
			"where pf.xh=v.xh and pf.pf=0 and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' \r\n" + 
			"and v.zxrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' and pf.FZJG='{City}' group by index_name) pf0 on o.index_name=pf0.index_name\r\n" + 
			"left join (select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_logout_hbt_{DBDate} v \r\n" + 
			"where pf.xh=v.xh and pf.pf=1 and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' \r\n" + 
			"and v.zxrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' group by index_name) pf1 on o.index_name=pf1.index_name \r\n" + 
			"left join (select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_logout_hbt_{DBDate} v \r\n" + 
			"where pf.xh=v.xh and pf.pf=2 and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' \r\n" + 
			"and v.zxrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' group by index_name) pf2 on o.index_name=pf2.index_name \r\n" + 
			"left join (select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_logout_hbt_{DBDate} v \r\n" + 
			"where pf.xh=v.xh and pf.pf=3 and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' \r\n" + 
			"and v.zxrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' group by index_name) pf3 on o.index_name=pf3.index_name \r\n" + 
			"left join (select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_logout_hbt_{DBDate} v \r\n" + 
			"where pf.xh=v.xh and pf.pf=4 and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' \r\n" + 
			"and v.zxrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' group by index_name) pf4 on o.index_name=pf4.index_name \r\n" + 
			"left join (select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_logout_hbt_{DBDate} v \r\n" + 
			"where pf.xh=v.xh and pf.pf=5 and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' \r\n" + 
			"and v.zxrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' group by index_name) pf5 on o.index_name=pf5.index_name \r\n" + 
			"order by o.order_id";
	
	private String inpfsql2017 = "select o.order_id,o.index_name,\r\n"
			+ "case when pf0.p0 is null then 0 else pf0.p0 end p0 ,\r\n"
			+ "case when pf1.p1 is null then 0 else pf1.p1 end p1 ,\r\n"
			+ "case when pf2.p2 is null then 0 else pf2.p2 end p2 ,\r\n"
			+ "case when pf3.p3 is null then 0 else pf3.p3 end p3 ,\r\n"
			+ "case when pf4.p4 is null then 0 else pf4.p4 end p4 ,\r\n"
			+ "case when pf5.p5 is null then 0 else pf5.p5 end p5 \r\n" + "from index_order o\r\n"
			+ "left join (select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from pf0_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' group by index_name) pf0 on o.index_name=pf0.index_name\r\n"
			+ "left join (select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from pf1_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf1 on o.index_name=pf1.index_name \r\n"
			+ "left join (select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from pf2_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf2 on o.index_name=pf2.index_name \r\n"
			+ "left join (select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from pf3_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf3 on o.index_name=pf3.index_name \r\n"
			+ "left join (select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from pf4_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf4 on o.index_name=pf4.index_name \r\n"
			+ "left join (select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from pf5_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf5 on o.index_name=pf5.index_name \r\n"
			+ "order by o.order_id";

	private String outpfsql2017 = "select o.order_id,o.index_name,\r\n"
			+ "case when pf0.p0 is null then 0 else pf0.p0 end p0 , \r\n"
			+ "case when pf1.p1 is null then 0 else pf1.p1 end p1 ,\r\n"
			+ "case when pf2.p2 is null then 0 else pf2.p2 end p2 ,\r\n"
			+ "case when pf3.p3 is null then 0 else pf3.p3 end p3 ,\r\n"
			+ "case when pf4.p4 is null then 0 else pf4.p4 end p4 ,\r\n"
			+ "case when pf5.p5 is null then 0 else pf5.p5 end p5 \r\n" + "from index_order o\r\n"
			+ "left join (select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_out_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' and v.djrq >= to_date('2017/01/01','yyyy/mm/dd') \r\n"
			+ "and pf.pf=0 and pf.FZJG='{City}' group by index_name) pf0 on o.index_name=pf0.index_name\r\n"
			+ "left join (select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_out_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' and v.djrq >= to_date('2017/01/01','yyyy/mm/dd') \r\n"
			+ "and pf.pf=1 and pf.FZJG='{City}' group by index_name) pf1 on o.index_name=pf1.index_name\r\n"
			+ "left join (select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_out_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' and v.djrq >= to_date('2017/01/01','yyyy/mm/dd') \r\n"
			+ "and pf.pf=2 and pf.FZJG='{City}' group by index_name) pf2 on o.index_name=pf2.index_name\r\n"
			+ "left join (select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_out_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' and v.djrq >= to_date('2017/01/01','yyyy/mm/dd') \r\n"
			+ "and pf.pf=3 and pf.FZJG='{City}' group by index_name) pf3 on o.index_name=pf3.index_name\r\n"
			+ "left join (select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_out_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' and v.djrq >= to_date('2017/01/01','yyyy/mm/dd') \r\n"
			+ "and pf.pf=4 and pf.FZJG='{City}' group by index_name) pf4 on o.index_name=pf4.index_name\r\n"
			+ "left join (select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from vehicle_hbt_{DBDate} pf,veh_out_hbt_{DBDate} v \r\n"
			+ "where pf.xh=v.xh and pf.rlzl<>'C' and pf.rlzl<>'N' and pf.rlzl<>'P' and v.djrq >= to_date('2017/01/01','yyyy/mm/dd') \r\n"
			+ "and pf.pf=5 and pf.FZJG='{City}' group by index_name) pf5 on o.index_name=pf5.index_name\r\n"
			+ "order by o.order_id";

	private String citysql = "select o.order_id,o.index_name," + "case when pf0.p0 is null then 0 else pf0.p0 end p0 ,"
			+ "case when pf1.p1 is null then 0 else pf1.p1 end p1 ,"
			+ "case when pf2.p2 is null then 0 else pf2.p2 end p2 ,"
			+ "case when pf3.p3 is null then 0 else pf3.p3 end p3 ,"
			+ "case when pf4.p4 is null then 0 else pf4.p4 end p4 ,"
			+ "case when pf5.p5 is null then 0 else pf5.p5 end p5 " + "from index_order o " + "left join "
			+ "(select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from pf0_{DBDate} pf where pf.FZJG='{City}' "
			+ "group by index_name) pf0 on o.index_name=pf0.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from pf1_{DBDate} pf where pf.FZJG='{City}' "
			+ "group by index_name) pf1 on o.index_name=pf1.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from pf2_{DBDate} pf where pf.FZJG='{City}' "
			+ "group by index_name) pf2 on o.index_name=pf2.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from pf3_{DBDate} pf where pf.FZJG='{City}' "
			+ "group by index_name) pf3 on o.index_name=pf3.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from pf4_{DBDate} pf where pf.FZJG='{City}' "
			+ "group by index_name) pf4 on o.index_name=pf4.index_name " + "left join "
			+ "(select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from pf5_{DBDate} pf where pf.FZJG='{City}' "
			+ "group by index_name) pf5 on o.index_name=pf5.index_name " + "order by o.order_id";

	private String summarysql = "select o.order_id,o.index_name,"
			+ "case when pf0.p0 is null then 0 else pf0.p0 end p0 ,"
			+ "case when pf1.p1 is null then 0 else pf1.p1 end p1 ,"
			+ "case when pf2.p2 is null then 0 else pf2.p2 end p2 ,"
			+ "case when pf3.p3 is null then 0 else pf3.p3 end p3 ,"
			+ "case when pf4.p4 is null then 0 else pf4.p4 end p4 ,"
			+ "case when pf5.p5 is null then 0 else pf5.p5 end p5 " + "from index_order o "
			+ "left join (select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from pf0_{DBDate} pf group by index_name) pf0 on o.index_name=pf0.index_name "
			+ "left join (select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from pf1_{DBDate} pf group by index_name) pf1 on o.index_name=pf1.index_name "
			+ "left join (select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from pf2_{DBDate} pf group by index_name) pf2 on o.index_name=pf2.index_name "
			+ "left join (select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from pf3_{DBDate} pf group by index_name) pf3 on o.index_name=pf3.index_name "
			+ "left join (select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from pf4_{DBDate} pf group by index_name) pf4 on o.index_name=pf4.index_name "
			+ "left join (select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from pf5_{DBDate} pf group by index_name) pf5 on o.index_name=pf5.index_name "
			+ "order by o.order_id";

	/**
	 * 后台查询线程
	 * 
	 * @param type
	 *            查询类别，使用枚举表示
	 * @param dbDate
	 *            数据日期
	 * @param citycode
	 *            城市列表
	 * @param filename
	 *            保存文件名
	 */
	public QueryThread(QueryType type, String dbDate, String[] citycode, String filename) {
		super();
		this.type = type;
		this.dbDate = dbDate;
		this.citycode = citycode;
		this.words = new HashMap<String, Object>();

		if (filename == null || filename.isEmpty())
			this.filename = dbDate;
		else
			this.filename = filename;
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
				words.put("DBDate", dbDate);
				// String sql = String.format(summarysql, dbDate, dbDate, dbDate, dbDate,
				// dbDate,dbDate);
				String sql = PositionFormat.format(summarysql, words);
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
				System.out.println(dbDate + "_Summary is finished");

			} else {
				for (String city : citycode) {
					words.put("DBDate", dbDate);
					words.put("City", city);
					// String sql = String.format(citysql, dbDate, city, dbDate, city, dbDate, city,
					// dbDate, city, dbDate, city, dbDate, city);
					String sql="";
					switch (type) {
					case PF:
						sql = citysql;
						break;
					case IN:
						sql=inpfsql2017;
						break;
					case OUT:
						sql=outpfsql2017;
						break;
					case LOGOUT:
						sql=logoutsql2017;
						break;
					default:
						break;
					}
					sql = PositionFormat.format(sql, words);
					pre = con.prepareStatement(sql);// 实例化预编译语句
					pre.setQueryTimeout(0);

					result = pre.executeQuery();
					while (result.next()) {
						pfList.add(new PaiFang(result.getString("index_name"), result.getInt("p0"), result.getInt("p1"),
								result.getInt("p2"), result.getInt("p3"), result.getInt("p4"), result.getInt("p5")));
					}

					ExportExcel<PaiFang> expPaiFang = new ExportExcel<PaiFang>();
					String[] titleCityCode = new String[] { "发证机关", "p0", "p1", "p2", "p3", "p4", "p5" };
					OutputStream out = new FileOutputStream(System.getProperty("user.dir") + File.separator + filename
							+ "_" + CityCode.getCity(city) + ".xls");
					expPaiFang.exportExcel(titleCityCode, pfList, out);
					out.close();
					pfList.clear();
					System.out.println(filename + "_" + CityCode.getCity(city) + " is finished");
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
		List<TaoTai> ttList = new ArrayList<TaoTai>();
		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		try {
			con = getConn();
			System.out.println("连接成功！");
			String sql;
			for (String city : citycode) {
				words.put("DBDate", dbDate);
				words.put("City", city);
				if (type == QueryType.TT)
					sql = taotaisql;
				else
					sql = msql;
				sql = PositionFormat.format(sql, words);
				pre = con.prepareStatement(sql);// 实例化预编译语句
				pre.setQueryTimeout(0);

				result = pre.executeQuery();
				while (result.next()) {
					ttList.add(new TaoTai(result.getString("fzjg"), result.getString("xzqh"), result.getString("hphm"),
							result.getString("hpzl"), result.getString("clxh"), result.getString("cllx"),
							result.getString("rlzl"), result.getString("hdzk"), result.getString("zzl"),
							result.getString("syxz"), result.getDate("ccdjrq"), result.getString("zt")));
				}

				ExportExcel<TaoTai> expTaoTai = new ExportExcel<TaoTai>();
				String[] titleCityCode = new String[] { "发证机关", "行政区划", "号牌号码", "号牌种类", "车辆型号", "车辆类型", "燃料种类", "核定载客",
						"总质量", "使用性质", "初次登记日期", "状态" };
				OutputStream out = new FileOutputStream(System.getProperty("user.dir") + File.separator + filename + "_"
						+ CityCode.getCity(city) + ".xls");
				expTaoTai.exportExcel(titleCityCode, ttList, out);
				out.close();
				ttList.clear();
				System.out.println(filename + "_" + CityCode.getCity(city) + " is finished");
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

	protected void queryShengYu() {
		List<ShengYu> syList = new ArrayList<ShengYu>();
		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		try {
			con = getConn();
			System.out.println("连接成功！");
			for (String city : citycode) {
				words.put("DBDate", dbDate);
				words.put("City", city);
				String sql = PositionFormat.format(shengyusql, words);

				pre = con.prepareStatement(sql);// 实例化预编译语句
				pre.setQueryTimeout(0);

				result = pre.executeQuery();
				while (result.next()) {
					syList.add(new ShengYu(result.getString("fzjg"), result.getString("xzqh"), result.getString("hphm"),
							result.getString("clxh"), result.getString("syxz"), result.getString("cllx"),
							result.getString("clsbdh"), result.getDate("ccdjrq"), result.getString("rlzl"),
							result.getDate("qzbfqz"), result.getString("zt")));
				}

				ExportExcel<ShengYu> expShengYu = new ExportExcel<ShengYu>();
				String[] titleCityCode = new String[] { "发证机关", "行政区划", "号牌号码", "车辆型号", "使用性质", "车辆类型",
						"车辆识别代码（VIN号后6位）", "初次登记日期", "燃料种类", "强制报废期止", "状态" };
				OutputStream out = new FileOutputStream(System.getProperty("user.dir") + File.separator + filename + "_"
						+ CityCode.getCity(city) + ".xls");
				expShengYu.exportExcel(titleCityCode, syList, out);
				out.close();
				syList.clear();
				System.out.println(filename + "_" + CityCode.getCity(city) + " is finished");
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
		case IN:
		case OUT:
		case LOGOUT:
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
