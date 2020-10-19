package com.demo.controller;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.springframework.stereotype.Service;

/**
 * @author mifei
 * @create 2020-07-30 16:25
 **/
@Service
public class UserService {

	public void aaa() throws InterruptedException {

		Transaction transaction = Cat.newTransaction("UserService", "Serviceaaa");
		try {
			Thread.sleep(500);
			transaction.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			transaction.setStatus(e); // catch 到异常，设置状态，代表此请求失败
			Cat.logError(e); // 将异常上报到cat上
			// 也可以选择向上抛出： throw e;
		} finally {
			transaction.complete();
		}
	}
}
