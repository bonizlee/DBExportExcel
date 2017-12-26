package app;

import java.util.Date;

public class ShengYu {
	String fzjg;// 发证机关
	String xzqh;// 行政区划
	String hphm;// 号牌号码
	String hpzl;// 号牌种类
	String clxh;// 车辆型号
	String syxz;// 使用性质
	String cllx;// 车辆类型
	String clsbdh;// 车辆识别代码（VIN号后6位）
	Date ccdjrq;// 初次登记日期
	String rlzl;// 燃料种类
	Date qzbfqz;// 强制报废期止
	String zt;// 状态

	public ShengYu() {
	}

	public ShengYu(String fzjg, String xzqh, String hphm, String hpzl, String clxh, String syxz, String cllx, String clsbdh,
			Date ccdjrq, String rlzl, Date qzbfqz, String zt) {
		super();
		this.fzjg = fzjg;
		this.xzqh = xzqh;
		this.hphm = hphm;
		this.hpzl = hpzl;
		this.clxh = clxh;
		this.syxz = syxz;
		this.cllx = cllx;
		this.clsbdh = clsbdh;
		this.ccdjrq = ccdjrq;
		this.rlzl = rlzl;
		this.qzbfqz = qzbfqz;
		this.zt = zt;
	}

	public String getFzjg() {
		return fzjg;
	}

	public void setFzjg(String fzjg) {
		this.fzjg = fzjg;
	}

	
	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	public String getSyxz() {
		return syxz;
	}

	public void setSyxz(String syxz) {
		this.syxz = syxz;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getClsbdh() {
		return clsbdh;
	}

	public void setClsbdh(String clsbdh) {
		this.clsbdh = clsbdh;
	}

	public Date getCcdjrq() {
		return ccdjrq;
	}

	public void setCcdjrq(Date ccdjrq) {
		this.ccdjrq = ccdjrq;
	}

	public String getRlzl() {
		return rlzl;
	}

	public void setRlzl(String rlzl) {
		this.rlzl = rlzl;
	}

	public Date getQzbfqz() {
		return qzbfqz;
	}

	public void setQzbfqz(Date qzbfqz) {
		this.qzbfqz = qzbfqz;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}
	
}
