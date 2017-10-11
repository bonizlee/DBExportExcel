package app;

import java.util.Date;

public class TaoTai {
	// 发证机关
	String fzjg;
	//
	String xzqh;
	// 号牌号码
	String hphm;
	// 号牌种类
	String hpzl;
	// 车辆型号
	String clxh;
	// 车辆类型
	String cllx;
	// 燃料种类
	String rlzl;
	// 核定载客
	String hdzk;
	// 总质量
	String zzl;
	// 使用性质
	String syxz;
	// 初次登记日期
	Date ccdjrq;
	// 状态
	String zt;

	public TaoTai() {
	}

	public TaoTai(String fzjg, String xzqh, String hphm, String hpzl, String clxh, String cllx, String rlzl,
			String hdzk, String zzl, String syxz, Date ccdjrq, String zt) {
		super();
		this.fzjg = fzjg;
		this.xzqh = xzqh;
		this.hphm = hphm;
		this.hpzl = hpzl;
		this.clxh = clxh;
		this.cllx = cllx;
		this.rlzl = rlzl;
		this.hdzk = hdzk;
		this.zzl = zzl;
		this.syxz = syxz;
		this.ccdjrq = ccdjrq;
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

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getRlzl() {
		return rlzl;
	}

	public void setRlzl(String rlzl) {
		this.rlzl = rlzl;
	}

	public String getHdzk() {
		return hdzk;
	}

	public void setHdzk(String hdzk) {
		this.hdzk = hdzk;
	}

	public String getZzl() {
		return zzl;
	}

	public void setZzl(String zzl) {
		this.zzl = zzl;
	}

	public String getSyxz() {
		return syxz;
	}

	public void setSyxz(String syxz) {
		this.syxz = syxz;
	}

	public Date getCcdjrq() {
		return ccdjrq;
	}

	public void setCcdjrq(Date ccdjrq) {
		this.ccdjrq = ccdjrq;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

}
