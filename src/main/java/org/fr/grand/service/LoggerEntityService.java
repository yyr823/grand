/**
 * 
 */
package org.fr.grand.service;

import java.util.List;

import org.fr.grand.kaoqin.LoggerEntity;
import org.fr.grand.kaoqin.UserCheck;
import org.fr.grand.mapper.LoggerEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PE
 * @date 2019年11月27日 下午1:57:23
 * @explain 
 */
@Service
public class LoggerEntityService {
@Autowired
private LoggerEntityMapper mapper;
	/**
	 * @param log
	 */
	public int  addOne(LoggerEntity log) {
		return mapper.addOne(log);
		
	}
	/**
	 * @param ucCheck
	 * @return
	 */
	public List<LoggerEntity> logList(UserCheck ucCheck) {
		return mapper.logList(ucCheck);
	}
	/**
	 * @param ucCheck
	 * @return
	 */
	public Integer logListCount(UserCheck ucCheck) {
		return mapper.logListCount(ucCheck);
	}

}
