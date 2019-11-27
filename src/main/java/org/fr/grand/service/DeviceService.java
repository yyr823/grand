/**
 * 
 */
package org.fr.grand.service;

import java.util.List;

import org.fr.grand.kaoqin.DeviceInfo;
import org.fr.grand.mapper.DeviceInfoMapper;
import org.fr.grand.util.ParseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author PE
 * @date 2019年11月22日 下午5:30:32
 * @explain 
 */
@Service
@Transactional
public class DeviceService {
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;

	/**
	 * @param deviceInfo
	 * @return
	 */
	public int deviceModify(DeviceInfo deviceInfo) {
		// TODO Auto-generated method stub
		return deviceInfoMapper.deviceModify(deviceInfo);
	}

	/**
	 * @param deviceInfo
	 * @return
	 */
	public int deviceAdd(DeviceInfo deviceInfo) {
		// TODO Auto-generated method stub
		return deviceInfoMapper.deviceAdd(deviceInfo);
	}

	/**
	 * @param ids
	 * @return
	 */
	public int deletes(String ids) {
		List<Integer> dids = ParseTool.getIds(ids);
		return deviceInfoMapper.deleteByIds(dids);
	}

	/**
	 * @param id
	 * @return
	 */
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return deviceInfoMapper.deleteById(id);
	}

	

}
