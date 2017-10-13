package app;

import java.util.EnumSet;

/**
 * 
 * @author BonizLee
 * 广东地级市与车牌代号
 *
 */
public enum CityCode{
	A("广州","A"),B("深圳","B"),C("珠海","C"),D("汕头","D"),E("佛山","E"),F("韶关","F"),G("湛江","G"),
	H("肇庆","H"),J("江门","J"),K("茂名","K"),L("惠州","L"),M("梅州","M"),N("汕尾","N"),P("河源","P"),
	Q("阳江","Q"),R("清远","R"),S("东莞","S"),T("中山","T"),U("潮州","U"),V("揭阳","V"),W("云浮","W"),
	X("顺德","X"),Y("南海","Y");
	
	private String city;
	private String key;
	
	private CityCode(String name,String key) {
		this.city=name;
		this.key=key;
	}
	
	/**
	 * 根据代码查询城市名称
	 * @param k:城市字母
	 * @return 城市名称
	 */
	public static String getCity(String k) {
		EnumSet<CityCode> cityset=EnumSet.allOf(CityCode.class);
		String city="";
		if(k.length()>1)
			k=k.substring(1, 2);
		for (CityCode cityCode : cityset) {
			if(cityCode.key.equals(k))
				city=cityCode.city;
		}
		return city;
	}
	
	/**
	 * 返回城市名称
	 * @return 城市名称
	 */
	public String getValue() {
		return this.city;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return String.format("%s_%s", city,key);
	}
	
	/**
	 * 获取枚举的车牌前缀
	 * @return 粤某
	 */
	public String toPlate() {
		return String.format("粤%s",key);
	}
	
	/**
	 * 根据条件查询某地市的车牌前缀
	 * @param name 可以是字母，也可以是城市名称
	 * @return
	 */	 
	public static String getPlate(String name) {
		EnumSet<CityCode> cityset=EnumSet.allOf(CityCode.class);
		String key="";
		for (CityCode cityCode : cityset) {
			if(cityCode.key.equals(name)||cityCode.city.equals(name))
				key=cityCode.key;
		}		
		return String.format("粤%s",key);
	}
	
	/**
	 * 获取城市字母数组
	 * @return 
	 */
	public static String[] getKeys() {
		String[] keys=new String[23];
		EnumSet<CityCode> cityset=EnumSet.allOf(CityCode.class);
		int i=0;
		for (CityCode cityCode : cityset) {
			keys[i]=cityCode.key;
			i++;
		}
		return keys;
	}
	
	/**
	 * 获取城市名称数组
	 * @return
	 */
	public static String[] getCities() {
		String[] cities=new String[23];
		EnumSet<CityCode> cityset=EnumSet.allOf(CityCode.class);
		int i=0;
		for (CityCode cityCode : cityset) {
			cities[i]=cityCode.city;
			i++;
		}
		return cities;
	}
	
	/* 
	public static String[] toArray() {
		String[] cities=new String[23];
		EnumSet<CityCode> cityset=EnumSet.allOf(CityCode.class);
		int i=0;
		for (CityCode cityCode : cityset) {
			cities[i]=cityCode.toString();
			i++;
		}
		return cities;
	}
	*/
}
