/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.fr.grand.kaoqin.LoggerEntity;
import org.fr.grand.kaoqin.UserCheck;

/**
 * @author PE
 * @date 2019年11月27日 下午2:48:07
 * @explain 
 */
public interface LoggerEntityMapper {
	int addOne(LoggerEntity log);

	/**
	 * @param ucCheck
	 * @return
	 */
	List<LoggerEntity> logList(UserCheck ucCheck);

	/**
	 * @param ucCheck
	 * @return
	 */
	Integer logListCount(UserCheck ucCheck);

}
