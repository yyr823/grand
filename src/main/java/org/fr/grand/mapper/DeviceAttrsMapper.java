/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.fr.grand.kaoqin.DeviceAttrs;

/**
 * @author PE
 * @date 2019年7月31日 上午11:23:20
 * @explain
 */
public interface DeviceAttrsMapper {

	/**
	 * @param deviceSn
	 * @return
	 */
	List<DeviceAttrs> getDeviceAttrsBySn(String deviceSn);

}
