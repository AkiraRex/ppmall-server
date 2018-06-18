package com.ppmall.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dBThread")
@Scope("prototype") // spring 多例
public class DBThread implements Runnable {
	private String msg;
	private Logger log = LoggerFactory.getLogger(DBThread.class);

	@Override
	public void run() {
		// 模拟在数据库插入数据

		// systemLogService.insert(systemlog);
		log.info("insert->" + msg);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}