/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fr.grand.kaoqin.DeviceCommand;
import org.fr.grand.util.MyMapper;

/**
 * @author PE
 * @date 2019年7月31日 上午11:12:20
 * @explain
 */
public interface DeviceCommandMapper {

	List<DeviceCommand> getDeviceCommandListToDevice(@Param("devSn") String devSn);

	DeviceCommand getDeviceCommandById(int devCmdId);

	/**
	 * @param deviceCommand
	 */
	int update(DeviceCommand deviceCommand);

	/**
	 * @param cat
	 */
	int save(DeviceCommand deviceCommand);

}
