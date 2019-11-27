/**
 * 
 */
package org.fr.grand.exception;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author PE
 * @date 2019年11月27日 下午3:32:28
 * @explain 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends Exception{

	private static final long serialVersionUID = 1L;
	     private int code;   //返回码 非0即失败  
	    private String msg; //消息提示  
	    private Map<String, Object> data; //返回的数据  
	    public static String success() {  
	        return success(new HashMap<>(0));  
	    }  
	    public static String success(Map<String, Object> data) {  
	    	Gson gson=new Gson();
	    	return	gson.toJson(new MyException(0, "解析成功", data));
	    }  

	    public static String failed() {  
	        return failed("解析失败");  
	    }  
	    public static String failed(String msg) {  
	        return failed(-1, msg);  
	    }  
	    public static String failed(int code, String msg) { 
	    	Gson gson=new Gson();
	    	return gson.toJson(new MyException(code, msg, new HashMap<>(0)));  
	    }

}
