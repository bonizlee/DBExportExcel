package app;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class DBExportExcelApp extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1063757897161436182L;

	public static void main(String[] args) {
		MainFrame mf=new MainFrame();
		/*
		String str="select o.order_id,o.index_name,\r\n" + 
				"case when pf0.p0 is null then 0 else pf0.p0 end p0 ,\r\n" + 
				"case when pf1.p1 is null then 0 else pf1.p1 end p1 ,\r\n" + 
				"case when pf2.p2 is null then 0 else pf2.p2 end p2 ,\r\n" + 
				"case when pf3.p3 is null then 0 else pf3.p3 end p3 ,\r\n" + 
				"case when pf4.p4 is null then 0 else pf4.p4 end p4 ,\r\n" + 
				"case when pf5.p5 is null then 0 else pf5.p5 end p5 \r\n" + 
				"from index_order o\r\n" + 
				"left join (select count(pf.INDEX_NAME) p0,pf.INDEX_NAME from pf0_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n" + 
				"where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}' group by index_name) pf0 on o.index_name=pf0.index_name\r\n" + 
				"left join (select count(pf.INDEX_NAME) p1,pf.INDEX_NAME from pf1_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n" + 
				"where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf1 on o.index_name=pf1.index_name \r\n" + 
				"left join (select count(pf.INDEX_NAME) p2,pf.INDEX_NAME from pf2_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n" + 
				"where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf2 on o.index_name=pf2.index_name \r\n" + 
				"left join (select count(pf.INDEX_NAME) p3,pf.INDEX_NAME from pf3_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n" + 
				"where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf3 on o.index_name=pf3.index_name \r\n" + 
				"left join (select count(pf.INDEX_NAME) p4,pf.INDEX_NAME from pf4_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n" + 
				"where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf4 on o.index_name=pf4.index_name \r\n" + 
				"left join (select count(pf.INDEX_NAME) p5,pf.INDEX_NAME from pf5_{DBDate} pf,veh_in_hbt_{DBDate} v \r\n" + 
				"where pf.xh=v.xh and v.zrrq >= to_date('2017/01/01','yyyy/mm/dd') and pf.FZJG='{City}'  group by index_name) pf5 on o.index_name=pf5.index_name \r\n" + 
				"order by o.order_id";
		Map<String,Object> words=new HashMap<String,Object>();
		words.put("DBDate", "20171001");
		words.put("City","粤C");
		//System.out.println(PositionFormat.format(str, words));
		System.out.println(CityCode.getCity("粤C"));
		*/
	}

}
