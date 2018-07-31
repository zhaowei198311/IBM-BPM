package com.desmart.common.excelForm;

import java.util.Date;

import com.desmart.common.annotation.excel.ExcelBoolean;
import com.desmart.common.annotation.excel.ExcelDate;
import com.desmart.common.annotation.excel.ExcelField;
import com.desmart.common.constant.ExcelEnum;
/**
 * 商品状态修改申请表
 * @author lys
 *
 */
public class GoodsStateModifyForm {
	@ExcelField(name="商品编码",sort=0,nullable=false)
	private String goodsNo;
	@ExcelField(name="商品名称",sort=1,nullable=false)
	private String goodsName;
	@ExcelField(name="门店编码",sort=2,nullable=false)
	private String shopNo;
	@ExcelField(name="门店名称",sort=3,nullable=false)
	private String shopName;
	@ExcelField(name="采购/配送状态",sort=4,nullable=false)
	private String procurementState;
	@ExcelField(name="采购/配送状态生效日期",sort=5,nullable=false)
	@ExcelDate(format="yyyy/MM/dd")
	private Date procurementEffectDate;
	@ExcelField(name="销售起始日期",sort=6,nullable=false)
	@ExcelDate(format="yyyy/MM/dd")
	private Date sellStartDate;
	@ExcelField(name="销售结束日期",sort=7,nullable=true)
	@ExcelDate(format="yyyy/MM/dd")
	private Date sellEndDate;
	@ExcelField(name = "是否允许门店退货", sort = 8, nullable = false)
	@ExcelBoolean(False = ExcelEnum.FALSE, True = ExcelEnum.TRUE)
	private Boolean isShopReturn;
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getShopNo() {
		return shopNo;
	}
	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getProcurementState() {
		return procurementState;
	}
	public void setProcurementState(String procurementState) {
		this.procurementState = procurementState;
	}
	public Date getProcurementEffectDate() {
		return procurementEffectDate;
	}
	public void setProcurementEffectDate(Date procurementEffectDate) {
		this.procurementEffectDate = procurementEffectDate;
	}
	public Date getSellStartDate() {
		return sellStartDate;
	}
	public void setSellStartDate(Date sellStartDate) {
		this.sellStartDate = sellStartDate;
	}
	public Date getSellEndDate() {
		return sellEndDate;
	}
	public void setSellEndDate(Date sellEndDate) {
		this.sellEndDate = sellEndDate;
	}
	public Boolean getIsShopReturn() {
		return isShopReturn;
	}
	public void setIsShopReturn(Boolean isShopReturn) {
		this.isShopReturn = isShopReturn;
	}
	@Override
	public String toString() {
		return "GoodsStateModifyForm [goodsNo=" + goodsNo + ", goodsName=" + goodsName + ", shopNo=" + shopNo
				+ ", shopName=" + shopName + ", procurementState=" + procurementState + ", procurementEffectDate="
				+ procurementEffectDate + ", sellStartDate=" + sellStartDate + ", sellEndDate=" + sellEndDate
				+ ", isShopReturn=" + isShopReturn + "]";
	}
		
}
