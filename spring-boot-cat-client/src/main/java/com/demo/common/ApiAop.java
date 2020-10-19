package com.demo.common;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Configuration
public class ApiAop {

	private static final Logger logger = LoggerFactory.getLogger(ApiAop.class);

	@Pointcut("@annotation(com.demo.common.CatTransaction)")
	public void excuteService() {
	}

	@Around("excuteService()")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		String queryString = request.getQueryString();
//        if (!isInclude(uri)) {
//            logger.info("请求开始, 各个参数, url: " + url + ", method: " + method + ", uri:" + uri + ", params:" + queryString);
//        }
		String simpleName = point.getTarget().getClass().getSimpleName();
		String methodName = point.getSignature().getName();

		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		String[] parameterNames = methodSignature.getParameterNames();
		Object[] param = point.getArgs();

		Method method = methodSignature.getMethod();
		CatTransaction catTransaction = method.getAnnotation(CatTransaction.class);
		String transName = point.getSignature().getDeclaringType().getSimpleName() + "." + point.getSignature().getName();
		if (StringUtils.isNotBlank(catTransaction.name())) {
			transName = catTransaction.name();
		}
		Transaction t = Cat.newTransaction(catTransaction.type(), transName);

		// 开始处理方法
		try {
			Object result = point.proceed();
			t.setStatus(Transaction.SUCCESS);
			return result;
		} catch (Throwable e) {
			t.setStatus(e);
			throw e;
		} finally {
			t.complete();
		}
	}

	/**
	 * 是否需要过滤
	 *
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url) {
//        for (String pattern : Const.AOP_URI) {
//            if (url.indexOf(pattern) != -1) {
//                return true;
//            }
//        }
		return false;
	}
}