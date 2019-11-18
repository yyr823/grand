/**
 * 
 */
package org.fr.grand.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author PE
 * @date 2019年10月23日 上午11:23:01
 * @explain
 */
@Controller
public class LoginController {

	@RequestMapping("/index2")
	public String cat() {
		return "index";
	}

	@RequestMapping("/index3")
	@ResponseBody
	public String cat2() {
		return "index";
	}
}
