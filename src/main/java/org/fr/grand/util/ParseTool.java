/**
 * 
 */
package org.fr.grand.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PE
 * @date 2019年10月30日 下午4:41:31
 * @explain 
 */
public class ParseTool {
	
	public static List<Integer> getIds(String userId) {
		List<Integer> uids = new ArrayList<>();
		String[] ids = userId.split(",");
		for (String id : ids) {
			uids.add(Integer.parseInt(id));
		}
		return uids;
	}

}
