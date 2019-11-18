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
import org.fr.grand.util.ParseTool;
import org.fr.grand.util.PushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author PE
 * @date 2019年8月4日 下午9:40:29
 * @explain
 */
@CrossOrigin
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

	@RequestMapping({ "/userList" })
	public ModelMap userList(UserInfo userInfo, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		List<UserInfo> list = this.service.userList(userInfo);
		Integer count = this.service.userListCount(userInfo);
		return createModelMap_easyui1(count, list);

	}



	@RequestMapping({ "/user_add" })
	public ModelMap userAdd(UserInfo userInfo) throws Exception {
		String img = null;
		// String realPhotoPath
		// =ServletActionContext.getServletContext().getRealPath(File.separator + "pers"
		// + File.separator + "photo");
		String realPhotoPath = "D:/photo/img/";
		long nowTime = System.currentTimeMillis();
		String fileName = nowTime + ".jpg";
		File userPicFile = new File(new File(realPhotoPath), fileName);
		File userPic = multipartFileToFile(userInfo.getUserPic());
		System.out.println("绝对路径:" + userPic.getCanonicalPath());
		if (userPic != null) {
			InputStream in = null;
			byte[] data = null;
			try {
				if (!userPicFile.getParentFile().exists()) {
					userPicFile.getParentFile().mkdirs();
				} else if (userPicFile.exists()) {
					userPicFile.delete();
				}

				System.out.println(userPicFile);
				BaseImgEncodeUtil.createZoomImage(userPic, userPicFile, 640, 1);
				userPic.delete();
				in = new FileInputStream(userPicFile);
				data = new byte[in.available()];
				in.read(data);
				in.close();
				img = new String(Base64.encodeBase64(data));
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (img != null) {
			img = img.replace("\r\n", "");
			img = img.replace("\t", "");
			img = img.replace(" ", "");
			userInfo.setPhoto_id_content(img);
			userInfo.setPhoto_id_name(userInfo.getUser_pin() + ".jpg");
			userInfo.setPhoto_id_size(img.length());
		}
		service.userAdd(userInfo);
		return errorMsg(0, "添加成功");
	}

	/**
	 * MultipartFile 转 File
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static File multipartFileToFile(MultipartFile file) throws Exception {

		File toFile = null;
		if (file.equals("") || file.getSize() <= 0) {
			file = null;
		} else {
			InputStream ins = null;
			ins = file.getInputStream();
			toFile = new File(file.getOriginalFilename());
			inputStreamToFile(ins, toFile);
			ins.close();
		}
		return toFile;
	}

	public static void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping({ "/user_modify" })
	public ModelMap modify(UserInfo userInfo, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println(userInfo);
		service.userUpdate(userInfo);
		return errorMsg(0, "修改成功");
	}

	@RequestMapping({ "/deleteUserServ" })
	public ModelMap deleteUserServ(String userId) {

		List<Integer> ids =  ParseTool.getIds(userId);
		int count = service.deleteUserServ(ids);
		return errorMsg(count, "删除(用户信息+指纹数据)");
	}
	
	@RequestMapping({ "/deleteUserFaceServ" })
	public ModelMap deleteUserFaceServ(String userId) {

		List<Integer> ids =  ParseTool.getIds(userId);
		int count = service.deleteFaceFromServer(ids);
		return errorMsg(count, "删除(面部数据)");
	}
	
	
	@RequestMapping({ "/deleteUserFpServ" })
	public ModelMap deleteUserFpServ(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.deleteFpFromServer(ids);
		return errorMsg(count, "删除(指纹数据)");
	}	
	
	@RequestMapping({ "/deleteUserPicServ" })
	public ModelMap deleteUserPicServ(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.deleteUserPicFromServer(ids);
		return errorMsg(count, "删除(图片)");
	}
	
	@RequestMapping({ "/sendUserDev" })
	public ModelMap send(String userId) {
		int count = service.sendUserDev(userId,null);
		return errorMsg(count, "同步全部数据");
	}
	
	@RequestMapping({ "/toNewDevice" })
	public ModelMap toNewDevice(String userId,String sn) {
		int count = service.toNewDevice(userId,sn);
		return errorMsg(count, "同步数据到其他机器上");
	}
	
	
	@RequestMapping({ "/deleteUserDev" })
	public ModelMap deleteUserDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteUserCommandByIds(ids,null);
		return errorMsg(count, "删除机器全部用户数据");
	}
	
	@RequestMapping({ "/deleteUserFpDev" })
	public ModelMap deleteUserFpDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteUserFpByIds(ids,null);
		return errorMsg(count, "删除机器指纹");
	}
	
	@RequestMapping({ "/deleteUserPicDev" })
	public ModelMap deleteUserPicDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteUserPicByIds(ids,null);
		return errorMsg(count, "删除机器用户图片");
	}
	
	@RequestMapping({ "/deleteUserFaceDev" })
	public ModelMap deleteUserFaceDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteFaceByIds(ids,null);
		return errorMsg(count, "删除机器面部数据");
	}
	

}
