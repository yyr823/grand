/**
 * 
 */
package org.fr.grand.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.fr.grand.exception.FebsException;
import org.fr.grand.kaoqin.UserInfo;
import org.fr.grand.mapper.UserInfoMapper;
import org.fr.grand.util.CaptchaUtil;
import org.fr.grand.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wf.captcha.base.Captcha;


/**
 * @author PE
 * @date 2019年10月23日 上午11:23:01
 * @explain
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController{
	@Autowired
	private UserInfoMapper userInfoMapper;
	 @GetMapping("images/captcha")
	    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
	        CaptchaUtil.outPng(110, 34, 4, Captcha.TYPE_ONLY_NUMBER, request, response);
	    }
	 
	    @PostMapping("user_login")
	    public ModelMap login( String username, String password, String verifyCode,
	            boolean rememberMe, HttpServletRequest request) throws FebsException {
	        if (!CaptchaUtil.verify(verifyCode, request)) {
	        	return jsonResult(400,"验证码错误！");
	        } 
	        password = MD5Util.encrypt(username.toLowerCase(), password);
	        UserInfo u=userInfoMapper.getUserByNameAndPassward(username,password);
	       if(u!=null) {
	    	    UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
		        super.login(token);
		    	return jsonResult(200,"登录！");
	       }else {
	    	   return jsonResult(400,"登录失败！");
	       }
	       
	    
	        
	    }
}
