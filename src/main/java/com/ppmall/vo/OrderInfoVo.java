package com.ppmall.vo;

import java.math.BigDecimal;
import java.util.List;

import com.ppmall.pojo.Shipping;

public class OrderInfoVo {
	private long orderNo;
	private String createTime;
	private int status;
	private String statusDesc;
	private String paymentTypeDesc;
	private BigDecimal payment;
	private Shipping shippingVo;
	private List orderItemVoList;
	private String imageHost;

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getPaymentTypeDesc() {
		return paymentTypeDesc;
	}

	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Shipping getShippingVo() {
		return shippingVo;
	}

	public void setShippingVo(Shipping shippingVo) {
		this.shippingVo = shippingVo;
	}

	public List getOrderItemVoList() {
		return orderItemVoList;
	}

	public void setOrderItemVoList(List orderItemVoList) {
		this.orderItemVoList = orderItemVoList;
	}

	public String getImageHost() {
		return imageHost;
	}

	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}

}
