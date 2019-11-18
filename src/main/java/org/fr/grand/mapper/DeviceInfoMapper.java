/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.DeviceInfo;
import org.fr.grand.util.MyMapper;

/**
 * @author PE
 * @date 2019年7月31日 上午11:12:20
 * @explain
 */
public interface DeviceInfoMapper {

	List<DeviceInfo> getAllDeviceInfoList();

	DeviceInfo getDeviceInfoBySn(@Param("deviceSn") String deviceSn);

	int updateDeviceState(@Param("deviceSn") String deviceSn, @Param("state") String state,
			@Param("format") String format);

	/**
	 * @param deviceInfo
	 * @return
	 */
	List<DeviceInfo> deviceList(DeviceInfo deviceInfo);

	/**
	 * @param deviceInfo
	 * @return
	 */
	Integer deviceListCount(DeviceInfo deviceInfo);

	int updateDeviceInfo(DeviceInfo deviceInfo);

	int updateDevice(DeviceInfo deviceInfo);

	String getLang(String deviceSn);

	List<Combobox> search_combobox();

	/**
	 * @param devInfo
	 * @return
	 */
	int save(DeviceInfo devInfo);

}
