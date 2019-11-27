/**
 * 
 */
package org.fr.grand.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fr.grand.kaoqin.AttLog;
import org.fr.grand.kaoqin.LoggerEntity;
import org.fr.grand.kaoqin.UserCheck;
import org.fr.grand.service.LoggerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PE
 * @date 2019年11月27日 下午3:13:13
 * @explain 
 */
@RestController
@RequestMapping({ "/log" })
public class LoggerController extends BaseController{
	@Autowired
	private LoggerEntityService loggerService;
	@RequestMapping({ "/logList" })
	public ModelMap logList(UserCheck ucCheck, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		String time = ucCheck.getTime();
		if (time != null && time != "") {
			String[] times = time.split("~");
			ucCheck.setStartTime(times[0]);
			ucCheck.setEndTime(times[1]);
		}
		List<LoggerEntity> list = this.loggerService.logList(ucCheck);
		System.out.println(list);
		Integer count = this.loggerService.logListCount(ucCheck);
		return createModelMap_easyui1(count, list);
	}
}
