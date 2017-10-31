/**
 * 
 */
package app;

import java.util.EnumSet;

/**
 * 查询类型  
 * @author BonizLee 
 */
public enum QueryType {
	/**
	 * 保有量按排放分类
	 */
	PF, 
	/**
	 * 已淘汰
	 */
	TT, 
	/**
	 * M状态
	 */
	M, 
	/**
	 * 剩余黄标车
	 */
	SY,
	/**
	 * 迁出
	 */
	OUT,
	/**
	 * 迁入
	 */
	IN,
	/**
	 * 注销
	 */
	LOGOUT,
	OUT2016,
	IN2016,
	LOGOUT2016
	;
	
	public static QueryType getType(String name) {
		EnumSet<QueryType> typeset=EnumSet.allOf(QueryType.class);
		QueryType q = null;
		
		for (QueryType type : typeset) {
			if(type.name().equals(name))
				q= type;
		}
		return q;
	}
}