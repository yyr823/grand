/**
 * 
 */
package org.fr.grand.controller;

import java.util.List;

import org.fr.grand.conf.SystemLog;
import org.fr.grand.kaoqin.Area;
import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.Company;
import org.fr.grand.kaoqin.DeviceInfo;
import org.fr.grand.service.AreaService;
import org.fr.grand.service.CompanyService;
import org.fr.grand.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PE
 * @date 2019年11月21日 下午2:34:49
 * @explain 
 */
@RestController
@RequestMapping({ "/device" })
public class DeviceController extends BaseController{
	@Autowired
	private DeviceService deviceService;
	

	@SystemLog(module = "Device management", methods = "modify device information")
	@RequestMapping({ "/device_modify" })
	public ModelMap modify(DeviceInfo deviceInfo) {
		int count =deviceService.deviceModify(deviceInfo);
		return errorMsg(count, "修改成功");
	}
	
	@SystemLog(module = "Device management", methods = "add device information")
	@RequestMapping({ "/device_add" })
	public ModelMap add(DeviceInfo deviceInfo) {
		int count =deviceService.deviceAdd(deviceInfo);
		return errorMsg(count, "添加成功");
	}
	@SystemLog(module = "Device management", methods = "delete device information")
	@RequestMapping({ "/delete" })
	public ModelMap delete(Integer id) {
		int count =deviceService.delete(id);
		return errorMsg(count, "删除");
	}
	@SystemLog(module = "Device management", methods = "delete devices information")
	@RequestMapping({ "/deletes" })
	public ModelMap deletes(String ids) {
		int count =deviceService.deletes(ids);
		return errorMsg(count, "批量删除");
	}

}
