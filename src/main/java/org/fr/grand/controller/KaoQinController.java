/**
 * 
 */
package org.fr.grand.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
import org.fr.grand.kaoqin.AttLog;
import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.Constants.DEV_FUNS;
import org.fr.grand.kaoqin.DeviceInfo;
import org.fr.grand.kaoqin.UserCheck;
import org.fr.grand.kaoqin.UserInfo;
import org.fr.grand.service.KaoQinService;
import org.fr.grand.util.BaseImgEncodeUtil;
import org.fr.grand.util.PushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author PE
 * @date 2019年8月4日 下午9:40:29
 * @explain
 */
@RestController
@RequestMapping({ "/kaoqin" })
public class KaoQinController extends BaseController {
	@Autowired
	private KaoQinService service;
	
	
	@RequestMapping({ "/logList" })
	public ModelMap logList(UserCheck ucCheck, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		String time = ucCheck.getTime();
		if (time != null && time != "") {
			String[] times = time.split("~");
			ucCheck.setStartTime(times[0]);
			ucCheck.setEndTime(times[1]);
		}
		List<AttLog> list = this.service.logList(ucCheck);
		System.out.println(list);
		Integer count = this.service.logListCount(ucCheck);
		return createModelMap_easyui1(count, list);
	}

	@RequestMapping({ "/deviceList" })
	public ModelMap deviceList(DeviceInfo deviceInfo, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		List<DeviceInfo> list = this.service.deviceList(deviceInfo);
		for (DeviceInfo deviceInfo2 : list) {
			setDeviceFuns(deviceInfo2);
		}
		Integer count = this.service.deviceListCount(deviceInfo);
		return createModelMap_easyui1(count, list);
	}

	private void setDeviceFuns(DeviceInfo deviceInfo) {
		String devFuns = deviceInfo.getDev_funs();
		if (null != devFuns && !devFuns.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			if (PushUtil.isDevFun(devFuns, DEV_FUNS.FP)) {
				sb.append("FP,");

			}
			if (PushUtil.isDevFun(devFuns, DEV_FUNS.FACE)) {
				sb.append("FACE,");
			}
			if (PushUtil.isDevFun(devFuns, DEV_FUNS.USERPIC)) {
				sb.append("USERPIC,");
			}
			if (PushUtil.isDevFun(devFuns, DEV_FUNS.BIOPHOTO)) {
				sb.append("BIOPHOTO,");
			}
			if (PushUtil.isDevFun(devFuns, DEV_FUNS.BIODATA)) {
				sb.append("BIODATA,");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			deviceInfo.setDev_funs(sb.toString());
		}
	}

	@RequestMapping(value = "combobox", method = RequestMethod.GET)
	public List<Combobox> search_combobox() {
		return this.service.search_combobox();
	}

	
	
}
