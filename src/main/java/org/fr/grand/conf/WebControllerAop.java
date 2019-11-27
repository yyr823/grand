package org.fr.grand.conf;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.fr.grand.kaoqin.LoggerEntity;
import org.fr.grand.service.LoggerEntityService;
import org.fr.grand.util.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebControllerAop {
	private static Logger logger = Logger.getLogger(WebControllerAop.class);
	@Autowired
	HttpServletRequest request;
	@Autowired
	private LoggerEntityService loggerService;
	// Controller层切点
	@Pointcut("execution (* org.fr.grand.controller..*.*(..))")
	public void controllerAspect() {
	}

	@Around("controllerAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		try {
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes sra = (ServletRequestAttributes) ra;
			request = sra.getRequest();
			LoggerEntity log = new LoggerEntity();
			String uri = request.getRequestURI();
			String ip = request.getLocalAddr();
			log.setClientip(ip);
			log.setClienturl(uri);
			MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
			Method methodObject = methodSignature.getMethod();
			String operationType = "";
			String operationName = "";
			operationType = methodObject.getAnnotation(SystemLog.class).module();
			operationName = methodObject.getAnnotation(SystemLog.class).methods();
			log.setMethodss(operationType);
			log.setOperatess(operationName);
			String timeMillis = MyDateUtil.getStringByFormat(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss");
			log.setCurrtime(timeMillis);
			loggerService.addOne(log);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		// result的值就是被拦截方法的返回值
		Object result = pjp.proceed();
		//Gson gson = new Gson();
		//logger.info("Response: " + gson.toJson(result));
		return result;
	}
}
