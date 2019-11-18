/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.fr.grand.kaoqin.AttLog;
import org.fr.grand.kaoqin.UserCheck;


/**
 * @author PE
 * @date 2019年7月31日 上午11:12:20
 * @explain
 */
public interface AttLogMapper {

	int save(AttLog attLog);
	List<AttLog> logList(UserCheck ucCheck);

	Integer logListCount(UserCheck attLog);

}
