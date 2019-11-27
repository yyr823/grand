package org.fr.grand.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.fr.grand.util.CommonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;

public class BaseController extends CommonString {
	@Autowired
	protected MessageSource messageSource;
	public static final String MSG = "msg";
	public static final String STATE = "stat e";
	public static final String DATA = "data";
	public static final String TOTAl = "total";
	public static final String ROWS = "rows";
	public static final String FOOTER = "footer";
	public static final String ISERROR = "isError";
	public static final String TYPE = "type";
	public static final String TIME = "time";
	 protected void login(AuthenticationToken token) {
	        getSubject().login(token);
	    }
	    private Subject getSubject() {
	        return SecurityUtils.getSubject();
	    }

	public ModelMap createModelMap(boolean state, String msg, Object data) {
		ModelMap map = new ModelMap();
		map.put("state", Boolean.valueOf(state));
		map.put("msg", msg);
		map.put("data", data);
		return map;
	}

	public ModelMap jsonResult(Integer code, String msg) {
		ModelMap map = new ModelMap();
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	public ModelMap createModelMap_easyui(boolean state, int count, Object rows, Object footer) {
		ModelMap map = new ModelMap();
		map.put("state", Boolean.valueOf(state));
		map.put("total", Integer.valueOf(count));
		map.put("rows", rows);
		map.put("footer", footer);
		return map;
	}
	public ModelMap createModelMap_easyui1( int count, Object list) {
		ModelMap map = new ModelMap();
		map.put("code", 0);
		map.put("msg", "success");
		map.put("count", count);
		map.put("data",  list);
		return map;
	}

	
	public ModelMap errorMsg(int count, String msg) {
		ModelMap map = new ModelMap();
		if(count>0) {
			map.put("code", 200);
			map.put("msg", msg+"成功");	
		}else {
			map.put("code", 0);
			map.put("msg", msg+"失败,该用户暂未拥有此项数据");	
		}

		return map;
		
	}
	


}
