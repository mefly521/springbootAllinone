package com.demo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.demo.entity.DataBean;
import com.demo.entity.Notice;
import com.demo.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class HelloController {

	@Autowired
	private UserServiceImpl userService;

	@RequestMapping("/hello")
	public String index() {
		String resourceName = "testSentinel";
		Entry entry = null;
		String retVal;
		try{
			entry = SphU.entry(resourceName, EntryType.IN);
			retVal = "passed";
		}catch(BlockException e){
			retVal = "blocked";
		}finally {
			if(entry!=null){
				entry.exit();
			}
		}
		return retVal;
	}

	@RequestMapping("/notice/list/{num}/{id}")
	public Object test(@PathVariable(value = "num") Long num, @PathVariable(value = "id") Long id) throws InterruptedException {
		Notice notice = new Notice();
		notice.setMsg("test");
		DataBean dataBean = new DataBean();
		dataBean.setNoticeId(1);
		dataBean.setNoticeContent("一个demo");
		List list = new ArrayList<>();
		list.add(dataBean);
		notice.setData(list);
		R<Notice> r = new R();
		r.setData(notice);
		return r;
	}

	private static void initFlowRules() {
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule = new FlowRule();
		rule.setResource("HelloWorld");
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		// Set limit QPS to 20.
		rule.setCount(20);
		rules.add(rule);
		FlowRuleManager.loadRules(rules);
	}

	public static void main(String[] args) {
		// 配置规则.
		initFlowRules();

		while (true) {
			// 1.5.0 版本开始可以直接利用 try-with-resources 特性，自动 exit entry
			try (Entry entry = SphU.entry("HelloWorld")) {
				// 被保护的逻辑
				System.out.println("hello world");
			} catch (BlockException ex) {
				// 处理被流控的逻辑
				System.out.println("blocked!");
			}
		}
	}
}