package com.ppmall.vo.alipayVo;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL )
public class BizContentVo {
	String out_trade_no;
	// 付款金额，必填
	String total_amount;
	// 订单名称，必填
	String subject;
	// 商品描述，可空
	String body;
	
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
        try {
            String returnString =  mapper.writeValueAsString(this);
            return returnString;
        } catch (IOException e) {
            e.printStackTrace();
        }
		return super.toString();
	}
}
