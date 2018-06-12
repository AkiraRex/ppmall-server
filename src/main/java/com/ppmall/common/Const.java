package com.ppmall.common;

public class Const {
	public static final String CURRENT_USER = "currentUser";
	public static final String FORGET_TOKEN = "forgetToken";
	
	public interface Cache{
		/**
		 * 所有品类
		 */
		String ALL_CATEGORY = "allCategory";
	}

	public interface Role {
		/**
		 * 管理员
		 */
		int ROLE_ADMIN = 1;
		/**
		 * 普通用户
		 */
		int ROLE_CUSTOMER = 0;
	}
	
	public interface AliPayReponse{
		String SUCCESS = "success";
		String ERROR = "error";
	}

	/**
	 * 订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
	 */
	public enum OrderStatus {
		CANCEL(0, "已取消"), 
		UNPAY(10, "未付款"), 
		PAID(20, "已付款"), 
		PREPARE(30, "已备货"), 
		SHIPED(40, "已发货"), 
		SUCCESS(50, "交易成功"), 
		CLOSE(60, "交易关闭");

		OrderStatus(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		private int code;
		private String desc;

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public static OrderStatus codeOf(int code) {
			for (OrderStatus orderStatusEnum : values()) {
				if (orderStatusEnum.getCode() == code) {
					return orderStatusEnum;
				}
			}
			throw new RuntimeException("无对应的枚举");
		}

	}

	public enum PayType {
		WECHAT(0, "微信支付"), 
		ALIPAY(1, "支付宝");

		PayType(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		private int code;
		private String desc;

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public static PayType codeOf(int code) {
			for (PayType payType : values()) {
				if (payType.getCode() == code) {
					return payType;
				}
			}
			throw new RuntimeException("无对应的枚举");
		}

	}

	public enum AliPayStatus {
		WAIT_BUYER_PAY("WAIT_BUYER_PAY", "交易创建，等待买家付款"),
		TRADE_CLOSED("TRADE_CLOSED", "未付款交易超时关闭，或支付完成后全额退款"),
		TRADE_SUCCESS("TRADE_SUCCESS", "交易支付成功"),
		TRADE_FINISHED("TRADE_FINISHED", "交易结束，不可退款");

		private String status;
		private String desc;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		AliPayStatus(String status, String desc) {
			this.desc = desc;
			this.status = status;
		}
	}
}
