package org.fr.grand.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fr.grand.kaoqin.AttLog;
import org.fr.grand.kaoqin.Constants;
import org.fr.grand.kaoqin.Constants.DEV_FUNS;
import org.fr.grand.kaoqin.DeviceCommand;
import org.fr.grand.kaoqin.DeviceInfo;
import org.fr.grand.kaoqin.PersonBioTemplate;
import org.fr.grand.kaoqin.UserInfo;
import org.fr.grand.mapper.AttLogMapper;
import org.fr.grand.mapper.DeviceCommandMapper;
import org.fr.grand.mapper.DeviceInfoMapper;
import org.fr.grand.mapper.PersonBioTemplateMapper;
import org.fr.grand.mapper.UserInfoMapper;
import org.fr.grand.service.KaoQinService;
import org.fr.grand.util.ConfigUrl;
import org.fr.grand.util.FileTool;
import org.fr.grand.util.PushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PE
 * @date 2019年7月31日 上午11:00:17
 * @explain
 */
@RestController
public class CdataController {
	public static Map<String, DeviceInfo> devMaps = new HashMap<String, DeviceInfo>();
	@Autowired
	private ConfigUrl configUrl;
	@Autowired
	private UserInfoMapper mapper;
	@Autowired
	private AttLogMapper logMapper;
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;
	@Autowired
	private DeviceCommandMapper deviceCommandMapper;
	@Autowired
	private PersonBioTemplateMapper personBioTemplateMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@RequestMapping(value = "/getrequest", method = RequestMethod.GET)
	public String getRequest(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String info = request.getParameter("INFO");
		String devSn = request.getParameter("SN");
		try {
		
			/** return when device serial number exception */
			if (null == devSn || devSn.isEmpty()) {
				response.getWriter().write("error");
				return null;
			}
			/** Define the character encoding type by the current language */
			if (Constants.DEV_LANG_ZH_CN.equals(getDeviceLangBySn(devSn))) {
				 response.setCharacterEncoding("GB2312");
			} else {
				response.setCharacterEncoding("UTF-8");
			}
			System.out.println("get reqeust and begin get cmd list." + request.getRemoteAddr() + ";" + request.getRequestURL()
					+ "?" + request.getQueryString());
			/** Gets the command list by device SN */
			List<DeviceCommand> list = deviceCommandMapper.getDeviceCommandListToDevice(devSn);
			System.out.println("get cmd list over and begin contact:" + list.size());
			/** save the logs of commands */
			List<DeviceCommand> tempList = new ArrayList<DeviceCommand>();
			if (list.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (DeviceCommand command : list) {
					/** Gets the command content */
					StringBuilder content = new StringBuilder();
					content.append(Constants.DEV_CMD_TITLE);
					content.append(command.getDevCmdId()).append(":");
					content.append(command.getCmdContent()).append("\n");
					/** the command should be less than setting, default 64K */
					System.out.println("SN:" + devSn + "cmdSize:");// getCmdSizeBySn(devSn)
					sb.append(content);
					/** Sets the transmit time */
					command.setCmdTransTime(dateFormat.format(new Date()));
					tempList.add(command);

				}
				/** Sets the command */
				response.setCharacterEncoding("GB2312");
				response.getWriter().write(sb.toString());
				System.out.println("contact cmd and send list:" + tempList.size());
				System.out.println("cmd info:" + sb.toString());
				/** Update the command list */
				for (DeviceCommand deviceCommand : tempList) {
					deviceCommandMapper.update(deviceCommand);
				}
			} else {
				response.getWriter().write("OK");
			}
			/** Update device INFO */
			if (null != info) {
				System.out.println("INFO:" + info);
				updateDeviceInfo(info, devSn);
			} else {
				deviceInfoMapper.updateDeviceState(devSn, "connecting", dateFormat.format(new Date()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int updateDeviceInfo(String info, String devSn) {
		String[] values = info.split(",");
		/** Gets the device info by SN */
		DeviceInfo deviceInfo = deviceInfoMapper.getDeviceInfoBySn(devSn);
		if (null == deviceInfo) {
			return -1;
		}
	
		deviceInfo.setFw_version(values[0]); // firmware version
		try {
			int userCount = Integer.valueOf(values[1]);// user count
			deviceInfo.setUser_count(userCount);
		} catch (Exception e) {
			return -1;
		}
		try {
			int fpCount = Integer.valueOf(values[2]);// FP count
			deviceInfo.setFp_count(fpCount);
		} catch (Exception e) {
		}
		try {
			int attCount = Integer.valueOf(values[3]);// the count of time attendance logs
			deviceInfo.setTrans_count(attCount);
		} catch (Exception e) {
		}
		deviceInfo.setIpaddress(values[4]);// IP Address
		deviceInfo.setFp_alg_ver(values[5]); // FP algorithm
		deviceInfo.setFace_alg_ver(values[6]);
		try {
			int regFaceCount = Integer.valueOf(values[7]);
			deviceInfo.setReg_face_count(regFaceCount);
		} catch (Exception e) {
		}
		try {
			int faceCount = Integer.valueOf(values[8]);
			deviceInfo.setFace_count(faceCount);
		} catch (Exception e) {
			// TODO: handle exception
		}
		deviceInfo.setDev_funs(values[9]);// the feature which device can support
		deviceInfo.setState("Online");
		deviceInfo.setLast_activity(dateFormat.format(new Date()));
		deviceInfoMapper.updateDevice(deviceInfo);
		return 0;
	}

	@RequestMapping(value = "/devicecmd")
	public String devicecmd(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String deviceSn = request.getParameter("SN");
		System.out.println("get reqeust and begin process cmd return." + request.getRemoteAddr() + ";"
				+ request.getRequestURL() + "?" + request.getQueryString());
		System.out.println("content-length:" + request.getContentLength());
		try {
			int iReadLength = 0;
			byte[] buffer = new byte[1024];
			StringBuffer bufferData = new StringBuffer();
			InputStream postData;
			postData = request.getInputStream();
			/** Gets the path of command GetFile */
			String getFilePathStr = request.getSession().getServletContext().getRealPath("\\getfile");
			FileOutputStream fileOS = null;
			while ((iReadLength = postData.read(buffer)) != -1) {
				String inString = new String(buffer, 0, iReadLength);
				/** process the return of command GetFile */
				if (inString.contains("CMD=GetFile")) {
					File path = processGetFileReturn(inString, getFilePathStr);
					if (null == path) {
						response.getWriter().write("OK");
						return null;
					}
					String filePath = path.toString();
					fileOS = new FileOutputStream(filePath);
					inString = inString.substring(0, inString.indexOf("Content=") + "Content=".length());
					fileOS.write(buffer, inString.length(), iReadLength - inString.length());
				} else {
					if (null != fileOS) {
						fileOS.write(buffer, 0, iReadLength);
					} else {
						/** Process the return of normal command */
						if (Constants.DEV_LANG_ZH_CN.equals(getDeviceLangBySn(deviceSn))) {
							bufferData.append(new String(buffer, 0, iReadLength, "GB2312"));
						} else {
							bufferData.append(new String(buffer, 0, iReadLength, "UTF-8"));
						}
					}
				}

			}
			String data = bufferData.toString();
			System.out.println("DEV CMD RETURN:\n" + data);
			System.out.println("update cmd return begin");
			int ret = -1;
			if (data.contains("CMD=Shell")) {
				ret = processShellReturn(data, response);
			} else {
				ret = updateDeviceCommand(data, response);
			}
			System.out.println("update cmd return end");
			if (0 == ret) {
				response.getWriter().write("OK");
			} else {
				response.getWriter().write("Error");
			}
			deviceInfoMapper.updateDeviceState(deviceSn, "connecting", dateFormat.format(new Date()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private File processGetFileReturn(String data, String filePath) {
		if (null == data || data.isEmpty() || null == filePath || filePath.isEmpty()) {
			return null;
		}
		File file = null;
		String[] lines = data.split("\n");
		int devCmdId = 0;
		String cmdReturn = "";
		String cmd = "";
		String deviceSn = "";
		String fileName = "";
		/***/
		for (String string : lines) {
			/** split by character & */
			if (string.contains("ID=") && string.contains("Return=") && string.contains("CMD=")) {
				String[] cmdReturns = string.split("&");
				for (String field : cmdReturns) {
					if (field.startsWith("ID")) {
						try {
							devCmdId = Integer.valueOf(field.substring(field.indexOf("ID=") + "ID=".length()));
						} catch (Exception e) {
							return null;
						}
					} else if (field.startsWith("Return")) {
						cmdReturn = field.substring(field.indexOf("Return=") + "Return=".length());
					} else if (field.startsWith("CMD")) {
						cmd = field.substring(field.indexOf("CMD=") + "CMD=".length());
						System.out.println(cmd);
					}
				}
			} else {// try with character \n
				if (string.startsWith("ID")) {
					try {
						devCmdId = Integer.valueOf(string.substring(string.indexOf("ID=") + "ID=".length()));
					} catch (Exception e) {
						return null;
					}
				} else if (string.startsWith("Return")) {
					cmdReturn = string.substring(string.indexOf("Return=") + "Return=".length());
				} else if (string.startsWith("CMD")) {
					cmd = string.substring(string.indexOf("CMD=") + "CMD=".length());
				} else if (string.startsWith("SN")) {
					deviceSn = string.substring(string.indexOf("SN=") + "SN=".length());
				} else if (string.startsWith("FILENAME")) {
					fileName = string.substring(string.indexOf("FILENAME=") + "FILENAME=".length());
				}
			}
		}

		/** update the device command info */
		DeviceCommand command = deviceCommandMapper.getDeviceCommandById(devCmdId);
		if (null != command) {
			command.setCmdOverTime(dateFormat.format(new Date()));
			command.setCmdReturn(cmdReturn);
			command.setCmdReturnInfo(data);
			System.out.println("command:" + command);
			deviceCommandMapper.update(command);
		}

		/** create the file */
		try {
			File fileSn = new File(filePath + "\\" + deviceSn);
			if (!fileSn.exists() && !fileSn.isDirectory()) {
				fileSn.mkdir();
			}
			if (fileName.contains("/")) {
				String[] dirs = fileName.split("/");
				StringBuilder temp = new StringBuilder();
				for (int i = 0; i < dirs.length - 1; i++) {
					if (dirs[i].isEmpty()) {
						continue;
					}
					temp.append(dirs[i]).append("\\");
				}
				System.out.println();
				String tempFile = fileSn + "\\" + temp.toString();
				fileSn = new File(tempFile);
				//FileUtils.forceMkdir(fileSn);
				fileName = fileName.substring(fileName.lastIndexOf("/"));
			}
			file = new File(fileSn + "\\" + fileName);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}

	private int processShellReturn(String data, HttpServletResponse response) {
		if (null == data || data.isEmpty()) {
			return -1;
		}
		String[] lines = data.split("\n");
		int devCmdId = 0;
		String cmdReturn = "";
		String cmd = "";
		String deviceSn = "";
		/** Gets the field values of command shell */
		for (String string : lines) {
			if (string.startsWith("ID")) {
				try {
					devCmdId = Integer.valueOf(string.substring(string.indexOf("ID=") + "ID=".length()));
				} catch (Exception e) {
					return -1;
				}
			} else if (string.startsWith("Return")) {
				cmdReturn = string.substring(string.indexOf("Return=") + "Return=".length());
			} else if (string.startsWith("CMD")) {
				cmd = string.substring(string.indexOf("CMD=") + "CMD=".length());
			} else if (string.startsWith("SN")) {
				deviceSn = string.substring(string.indexOf("SN=") + "SN=".length());
			}
		}

		DeviceCommand command = deviceCommandMapper.getDeviceCommandById(devCmdId);
		if (null != command) {
			command.setCmdOverTime(dateFormat.format(new Date()));
			command.setCmdReturn(cmdReturn);
			command.setCmdReturnInfo(data);
			deviceCommandMapper.update(command);
		}
		return 0;
	}

	private int updateDeviceCommand(String data, HttpServletResponse response) {
		if (null == data || "".equals(data)) {
			return -1;
		}
		int devCmdId = 0;
		String cmdReturn = "";
		String cmd = "";
		String deviceSn = "";
		String[] lines = data.split("\n");
		List<DeviceCommand> returnList = new ArrayList<DeviceCommand>();
		/** processing the return string */
		for (String string : lines) {
			System.out.println("***" + string);
			/** split by "&" */
			if (string.contains("ID=") && string.contains("Return=") && string.contains("CMD=")) {
				String[] cmdReturns = string.split("&");
				for (String field : cmdReturns) {
					if (field.startsWith("ID")) {
						try {
							devCmdId = Integer.valueOf(field.substring(field.indexOf("ID=") + "ID=".length()));
						} catch (Exception e) {
							return -1;
						}
					} else if (field.startsWith("Return")) {
						cmdReturn = field.substring(field.indexOf("Return=") + "Return=".length());
					} else if (field.startsWith("CMD")) {
						cmd = field.substring(field.indexOf("CMD=") + "CMD=".length());
					}
				}
				System.out.println("cmdReturn:" + cmdReturn);
				DeviceCommand command = deviceCommandMapper.getDeviceCommandById(devCmdId);

				if (null != command) {
					deviceSn = command.getDeviceSn();
					command.setCmdOverTime(dateFormat.format(new Date()));
					command.setCmdReturn(cmdReturn);
					command.setCmdReturnInfo(data);
					System.out.println("command" + command);
					returnList.add(command);
				}
			} else if (Constants.DEV_CMD_INFO.equals(cmd)) {
				/** command INFO */
				updateDeviceInfo(lines, deviceSn);
				break;
			}
		}
		for (DeviceCommand deviceCommand : returnList) {
			System.out.println("开始执行");
			deviceCommandMapper.update(deviceCommand);
		}
		return 0;
	}

	public int updateDeviceInfo(String[] infoDatas, String deviceSn) {
		DeviceInfo deviceInfo = deviceInfoMapper.getDeviceInfoBySn(deviceSn);
		for (String string : infoDatas) {
			if (string.startsWith("UserCount")) {
				try {
					int userCount = Integer
							.valueOf(string.substring(string.indexOf("UserCount=") + "UserCount=".length()));
					deviceInfo.setUser_count(userCount);
				} catch (Exception e) {

				}
			} else if (string.startsWith("FPCount")) {
				try {
					int fpCount = Integer.valueOf(string.substring(string.indexOf("FPCount=") + "FPCount=".length()));
					deviceInfo.setFp_count(fpCount);
				} catch (Exception e) {

				}
			} else if (string.startsWith("FWVersion")) {
				try {
					String fwVersion = string.substring(string.indexOf("FWVersion=") + "FWVersion=".length());
					deviceInfo.setFw_version(fwVersion);
				} catch (Exception e) {

				}
			} else if (string.startsWith("FaceCount")) {
				try {
					int faceCount = Integer
							.valueOf(string.substring(string.indexOf("FaceCount=") + "FaceCount=".length()));
					deviceInfo.setFace_count(faceCount);
				} catch (Exception e) {
				}
			}
		}

		deviceInfo.setLast_activity(dateFormat.format(new Date()));
		deviceInfoMapper.updateDeviceInfo(deviceInfo);
		return 0;
	}

	@RequestMapping(value = "/cdata", method = RequestMethod.GET)
	public String getCdata(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String deviceSn = request.getParameter("SN");
		String options = request.getParameter("options");
		System.out.println("get request and make the device options :" + request.getRemoteAddr() + ";"
				+ request.getRequestURL() + "?" + request.getQueryString());
		try {
			/** there is no Device SN */
			if (null == deviceSn || deviceSn.isEmpty()) {
				response.getWriter().write("error");
				System.out.println("device is null or empty");
				return null;
			}
			/** Define the character encoding type by the current language */
			String lang = getDeviceLangBySn(deviceSn);
			if (Constants.DEV_LANG_ZH_CN.equals(lang)) {
				 response.setCharacterEncoding("GB2312");
			} else {
				 response.setCharacterEncoding("UTF-8");
			}
			/**
			 * <li>if the parameter options equal "all", it is initialization connection,
			 * <li>if the parameter table equal "RemoteAtt", it is remote attendance
			 */
			System.out.println("options:" + options);
			if (null != options && "all".equals(options)) {
				DeviceInfo devInfo = getDeviceInfo(deviceSn, request);
				if (null != devInfo) {
					String deviceOptions = getDeviceOptions(devInfo);
					response.getWriter().write(deviceOptions);
					System.out.println(deviceOptions);
				} else {
					response.getWriter().write("error");
					System.out.println("option=all device is null");
				}
			}
		} catch (IOException e) {
			//logger.error(e);
		}
		return null;
	}

	/**
	 * Gets the device info by Device SN<br>
	 * <li>Query the device info from Server. if it is existed, return it.
	 * <li>if not, create a new device info.
	 * 
	 * @param deviceSn
	 * @param request
	 * @return device info<code>DeviceInfo</code>
	 */
	private DeviceInfo getDeviceInfo(String deviceSn, HttpServletRequest request) {
		DeviceInfo devInfo = deviceInfoMapper.getDeviceInfoBySn(deviceSn);
		System.out.println("初始化成功");
		/** push version */
		String pushver = request.getParameter("pushver");
		System.out.println(pushver);
		/** Current language */
		String language = request.getParameter("language");
		/** the communication key between Server and device */
		String pushcommkey = request.getParameter("pushcommkey");
		/** Device IP Address */
		String ipAddress = request.getRemoteAddr();
		if (null == devInfo) {
			System.out.println("为空");
			/** if the device is not existed. Set the device info as default value. */
			devInfo = new DeviceInfo();
			devInfo.setIpaddress(ipAddress);
			devInfo.setDevice_name(deviceSn + "(" + ipAddress + ")");
			devInfo.setAlias_name(ipAddress);
			devInfo.setTrans_interval(1);
			devInfo.setDevice_sn(deviceSn);
			devInfo.setState("Online");
			devInfo.setLog_stamp("0");
			devInfo.setOp_log_stamp("0");
			devInfo.setPhoto_stamp("0");
			devInfo.setDev_language(language);
			if (null == pushver) {
				devInfo.setPush_version("1.0.0");
			} else {
				devInfo.setPush_version(pushver);
			}
			devInfo.setTrans_times("00:00;14:05");
			devInfo.setPush_comm_key(pushcommkey);
			devInfo.setLast_activity(dateFormat.format(new Date()));
			devInfo = setDeviceFuns(devInfo);
			int count = deviceInfoMapper.save(devInfo);
			System.out.println("添加条数:" + count);
			/** update the device info cache */
			// updateDevMap(devInfo);
			// new device add a INFO command
			deviceCommandMapper.save(getINFOCommand(devInfo.getDevice_sn()));

		} else {
			devInfo.setIpaddress(ipAddress);
			devInfo.setState("Online");// Device Status
			devInfo.setLast_activity(dateFormat.format(new Date()));
			devInfo = setDeviceFuns(devInfo);
			deviceInfoMapper.updateDevice(devInfo);

		}

		return devInfo;
	}

	private DeviceInfo setDeviceFuns(DeviceInfo deviceInfo) {
		String devFuns = deviceInfo.getDev_funs();
		System.out.println(null != devFuns && !devFuns.isEmpty());
		if (null != devFuns && !devFuns.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			if (isDevFun(devFuns, DEV_FUNS.FP)) {
				sb.append("FP,");
			}
			if (isDevFun(devFuns, DEV_FUNS.FACE)) {
				sb.append("FACE,");
			}
			if (isDevFun(devFuns, DEV_FUNS.USERPIC)) {
				sb.append("USERPIC,");
			}
			if (isDevFun(devFuns, DEV_FUNS.BIOPHOTO)) {
				sb.append("BIOPHOTO,");
			}
			if (isDevFun(devFuns, DEV_FUNS.BIODATA)) {
				sb.append("BIODATA,");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			deviceInfo.setDev_funs(sb.toString());
		}
		return deviceInfo;
	}

	public DeviceCommand getINFOCommand(String deviceSn) {
		/** Gets INFO Command objects */
		return getNewCommand(deviceSn, Constants.DEV_CMD_INFO);
	}

	private DeviceCommand getNewCommand(String deviceSn, String content) {
		DeviceCommand command = new DeviceCommand();
		command.setDeviceSn(deviceSn);
		command.setCmdContent(content);
		command.setCmdCommitTime(dateFormat.format(new Date()));
		command.setCmdTransTime("");
		command.setCmdOverTime("");
		command.setCmdReturn("");
		return command;
	}

	private int updateDeviceStamp(String deviceSn, HttpServletRequest request) {
		String stamp = request.getParameter("Stamp");
		String opStamp = request.getParameter("OpStamp");
		String photoStamp = request.getParameter("PhotoStamp");
		DeviceInfo deviceInfo = deviceInfoMapper.getDeviceInfoBySn(deviceSn);

		if (null == deviceInfo) {
			return -1;
		}
		if (null != stamp) {
			deviceInfo.setLog_stamp(stamp);
		}
		if (null != opStamp) {
			deviceInfo.setOp_log_stamp(opStamp);
		}
		if (null != photoStamp) {
			deviceInfo.setPhoto_stamp(photoStamp);
		}

		deviceInfo.setState("connection");
		deviceInfo.setLast_activity(dateFormat.format(new Date()));
		deviceInfoMapper.updateDevice(deviceInfo);
		return 0;
	}

	@RequestMapping(value = "/cdata", method = RequestMethod.POST)
	public String postCdata(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String deviceSn = request.getParameter("SN");
		/** Data table from device */
		String table = request.getParameter("table");
		System.out.println("table为**********************" + table);
		/** Current Language of device */
		String lang = getDeviceLangBySn(deviceSn);

		System.out.println("device language:" + lang + ",get request and begin update device stamp:"
				+ request.getRemoteAddr() + ";" + request.getRequestURL() + "?" + request.getQueryString());
		try {
			/** update the device stamp for current table. */
			int re = updateDeviceStamp(deviceSn, request);
			if (0 != re) {
				response.getWriter().write("error:device not exist");
				return null;
			}
			System.out.println("update device stamp end and get stream.");
			int iReadLength = 0;
			byte[] buffer = new byte[1024];
			StringBuffer bufferData = new StringBuffer();
			InputStream postData = request.getInputStream();
			/** Gets the path of attendance photo. */
			String pathStr = request.getSession().getServletContext().getRealPath("\\AttPhoto");
			FileOutputStream fileOS = null;
			while ((iReadLength = postData.read(buffer)) != -1) {
				String inString = new String(buffer, 0, iReadLength);
				/** check if it is attendance. */
				if ((inString.contains("CMD=realupload") || inString.contains("CMD=uploadphoto"))
						&& "ATTPHOTO".equals(table)) {
					/** Create attendance photo */
					File path = createAttPhotoFile(inString, pathStr);
					if (path == null) {
						response.getWriter().write("OK");
						return null;
					}
					String filePath = path.toString();
					fileOS = new FileOutputStream(filePath);
					String cmdUpload = "";
					if (inString.contains("CMD=realupload")) {
						cmdUpload = "CMD=realupload";
					} else {
						cmdUpload = "CMD=uploadphoto";
					}
					/** Gets the data stream, put it into the file */
					inString = inString.substring(0, inString.indexOf(cmdUpload) + cmdUpload.length());
					fileOS.write(buffer, inString.length() + 1, iReadLength - (inString.length() + 1));
				} else {
					/** if it is photo. continue. */
					if (fileOS != null) {
						fileOS.write(buffer, 0, iReadLength);
					} else {
						/** Define the character encoding type by the current language */
						if (Constants.DEV_LANG_ZH_CN.equals(lang)) {
							bufferData.append(new String(buffer, 0, iReadLength, "GB2312"));
						} else {
							bufferData.append(new String(buffer, 0, iReadLength, "UTF-8"));
						}
					}
				}
			}
			if (fileOS != null) {
				fileOS.close();
			}
			String data = bufferData.toString();
			System.out.println("data:" + data);
			System.out.println("end get stream and process data");
			/** Data processing */
			int result = processDatas(table, data, deviceSn);
			System.out.println(result + "end process data and return msg to device");
			/** return the result. */

			if (0 == result) {
				response.getWriter().write("OK");
				System.out.println("return msg to device and request over OK");
			} else {
				response.getWriter().write("error");
				System.out.println("return msg to device and request over error");
			}

		} catch (UnsupportedEncodingException e) {
			//logger.error(e);
		} catch (IOException e) {
			//logger.error(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private File createAttPhotoFile(String data, String filePath) {
		File file = null;
		try {
			String photoArr[] = data.split("\n");
			/** data fields */
			String fileName = photoArr[0].split("=")[1];
			String sn = photoArr[1].split("=")[1];
			String size = photoArr[2].split("=")[1];
			/** creates sub-directory by device SN */
			File fileSn = new File(filePath + "\\" + sn);
			if (!fileSn.exists() && !fileSn.isDirectory()) {
				fileSn.mkdir();
			}
			/** Gets the attendance photo path */
			file = new File(fileSn + "\\" + fileName);

			/** file non-existed */
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * Processes the data by table structure.
	 * 
	 * @param table
	 *            data table<br>
	 *            <li>OPERLOG including operation logs:OPLOG，user info: USER，finger
	 *            print data: FP，user photo: USERPIC，face template: FACE
	 *            <li>ATTLOG attendance record
	 * @param data
	 *            post data
	 * @param deviceSn
	 *            Device SN
	 * @return
	 */
	public int processDatas(String table, String data, String deviceSn) {
		System.out.println("是否含有:" + data.startsWith("USER "));
		if ("OPERLOG".equals(table)) {
			if (data.startsWith("USER ")) {
				System.out.println("begin parse op user");
				parseUserData(data, deviceSn);
				System.out.println("end parse op user");
				return 0;

			} else if (data.startsWith("FP ")) {
				System.out.println("begin parse op fp");
				parseFingerPrint(data, deviceSn);
				System.out.println("end parse op fp");
				return 0;
			}
		} else if ("ATTLOG".equals(table)) {
			System.out.println("begin parse op attlog");
			parseAttlog(data, deviceSn);
			System.out.println("end parse op attlog");
			return 0;
		}else if (data.startsWith("FACE ")) {
			System.out.println("begin parse op face");
			parseFace(data, deviceSn);
			System.out.println("end parse op face");
			return 0;
			}
		else if(data.startsWith("USERPIC ")) {
			System.out.println("begin parse op user pic");
			parseUserPic(data, deviceSn);
			System.out.println("end parse op user pic");
			return 0;
		}

		

		return 0;
	}
	

	
	/**
	 * @param data
	 * @param deviceSn
	 */
	private int parseUserPic(String data, String deviceSn) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (null != data && !data.isEmpty()) {
			String[] userInfos = data.split("\n");
			for (String string : userInfos) {
				String fieldsStr = string.substring(string.indexOf("USERPIC ") + "USERPIC ".length());
				String[] fields = fieldsStr.split("\t");
				UserInfo info = parseUserPic(fields, deviceSn);
				
				if (null == info) {
					return -1;
				}
				list.add(info);
			}
		}
		/**save it into database*/
		int ret = userInfoMapper.updateUserPic(list);
System.out.println("userpic size:" + list.size());
		return ret;
		
	}
	
	private  UserInfo parseUserPic(String[] fields, String deviceSn) {
		UserInfo info = new UserInfo();
		/**Gets all the field value*/
		for (String string : fields) {
			if (string.startsWith("PIN")) {
				info.setUser_pin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
			} else if (string.startsWith("FileName")) {
				info.setPhoto_id_name(string.substring(string.indexOf("FileName=") + "FileName=".length()));
			} else if (string.startsWith("Size")) {
				try {
					int size = Integer.valueOf(string.substring(string.indexOf("Size=") + "Size=".length()));
					info.setPhoto_id_size(size);
				} catch (Exception e) {
					return null;
				}
			} else if (string.startsWith("Content")) {
				info.setPhoto_id_content(string.substring(string.indexOf("Content=") + "Content=".length()));
			} 
		}
		/**gets the user info*/
		UserInfo userInfo = userInfoMapper.getUserInfoByPinAndSn(info.getUser_pin(), deviceSn);
		if (null == userInfo) {
			return null;
		}
		/**update the user info*/
		userInfo.setPhoto_id_name(info.getPhoto_id_name());
		userInfo.setPhoto_id_size(info.getPhoto_id_size());
		userInfo.setPhoto_id_content(info.getPhoto_id_content());
		return userInfo;
	}
	

	/**
	 * @param data
	 * @param deviceSn
	 */
	public  int parseFace(String data, String deviceSn) {
		if (null == data || data.isEmpty()
				|| null == deviceSn || deviceSn.isEmpty()) {
			return -1;
		}
		System.out.println("face data:\n"+data);
		List<PersonBioTemplate> list = new ArrayList<PersonBioTemplate>();
		String[] faces = data.split("\n");
		for (String string : faces) {
			String fieldsStr = string.substring(string.indexOf("FACE ") + "FACE ".length());
			String[] fields = fieldsStr.split("\t");
			PersonBioTemplate face = parseFace(fields, deviceSn);
			
			if (null == face) {
				return -1;
			}
			list.add(face);

		}
		
		for (PersonBioTemplate personBioTemplate : list) {
			try {
				personBioTemplateMapper.save(personBioTemplate);
			}catch (Exception e) {
				personBioTemplateMapper.updateTemplate(personBioTemplate);
			}
		
		}
		System.out.println("face size:" + list.size());
		return 1;
	}
	
	/**
	 * Gets face PersonBioTemplate from data.
	 * 
	 * @param fields
	 * @param deviceSn
	 * @return
	 */
	public PersonBioTemplate parseFace(String[] fields, String deviceSn) {
		PersonBioTemplate template = new PersonBioTemplate();
		/**Gets all the field value*/
		for (String string : fields) {
			if (string.startsWith("PIN")) {
				template.setUserPin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
			} else if (string.startsWith("FID")) {
				try {
					int templateNo = Integer.valueOf(string.substring(string.indexOf("FID=") + "FID=".length()));
					template.setTemplateNo(templateNo);
				} catch (Exception e) {
					template.setTemplateNo(0);
				}
			} else if (string.startsWith("SIZE")) {
				try {
					int size = Integer.valueOf(string.substring(string.indexOf("SIZE=") + "SIZE=".length()));
					template.setSize(size);
				} catch (Exception e) {
					template.setSize(0);
				}
			} else if (string.startsWith("VALID")) {
				try {
					int valid = Integer.valueOf(string.substring(string.indexOf("VALID=") + "VALID".length()));
					template.setValid(valid);
				} catch (Exception e) {
					template.setValid(1);
				}
			} else if (string.startsWith("TMP")) {
				template.setTemplateData(string.substring(string.indexOf("TMP=") + "TMP=".length()));
			}
		}
		
		template.setIsDuress(0);
		template.setBioType(Constants.BIO_TYPE_FACE);//Biometrics type: face
		template.setDataFormat(Constants.BIO_DATA_FMT_ZK);//Data format: zk type
		template.setVersion(Constants.BIO_VERSION_FACE_7);//Face algorithm version
		template.setTemplateNoIndex(0);
		template.setDeviceSn(deviceSn);
		
		/**Gets the user info by user ID and device SN*/
		UserInfo userInfo = userInfoMapper.getUserInfoByPinAndSn(template.getUserPin(), template.getDeviceSn());
		
		if (null == userInfo) {
			return null;
		}
		
		template.setUserId(userInfo.getUser_id());
		return template;
	}
	
	public int parseAttlog(String data, String deviceSn) {
		List<AttLog> list = new ArrayList<AttLog>();
		if (null != data && !"".equals(data)) {
			String[] attLogs = data.split("\n");
			for (String string : attLogs) {
				String[] attValues = string.split("\t");
				AttLog log = new AttLog();
				log.setDevice_sn(deviceSn);
				log.setUser_pin(attValues[0]);
				log.setVerify_time(attValues[1]);
				StringBuilder sb = new StringBuilder();
				try {
					sb.append(PushUtil.ATT_STATUS.get(attValues[2]));
					sb.append(":");
					int status = Integer.valueOf(attValues[2]);
					log.setStatus(status);
				} catch (Exception e) {
					log.setStatus(0);
				}
				try {
					sb.append(PushUtil.ATT_VERIFY.get(attValues[3]));
					int verifyType = Integer.valueOf(attValues[3]);
					log.setVerify_type(verifyType);
				} catch (Exception e) {
					log.setVerify_type(1);
				}
				try {
					int workcode = Integer.valueOf(attValues[4]);
					log.setWork_code(workcode);
				} catch (Exception e) {
					log.setWork_code(0);
				}
				try {
					int reserved1 = Integer.valueOf(attValues[5]);
					log.setReserved1(reserved1);
				} catch (Exception e) {
					log.setReserved1(0);
				}
				try {
					int reserved2 = Integer.valueOf(attValues[6]);
					log.setReserved2(reserved2);
				} catch (Exception e) {
					log.setReserved2(0);
				}
				list.add(log);
			}
		}
		for (AttLog attLog : list) {
			StringBuffer sb = new StringBuffer();
			String time = attLog.getVerify_time();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = (Date) sdf1.parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");

			sb.append(31).append(sdf2.format(date)).append(attLog.getWork_code())
					.append(String.format("%0" + 2 + "d", attLog.getStatus())).append(attLog.getUser_pin()).append("00")
					.append(attLog.getDevice_sn()).append("\r\n");
			//FileTool.testFile(sb, configUrl.getGt168(), "dakintai2.txt");

			logMapper.save(attLog);

		}
		return 0;
	}

	public int parseFingerPrint(String data, String deviceSn) {
		System.out.println("finger data:\n" + data);
		List<PersonBioTemplate> list = new ArrayList<PersonBioTemplate>();
		if (null != data && !"".equals(data)) {
			String[] fps = data.split("\n");
			for (String string : fps) {
				String fieldsStr = string.substring(string.indexOf("FP ") + "FP ".length());
				String[] fields = fieldsStr.split("\t");
				PersonBioTemplate fp = parseFingerPrint(fields, deviceSn);

				if (null == fp) {
					return -1;
				}
				list.add(fp);
			}
		}
		int count=0;
		for (PersonBioTemplate personBioTemplate : list) {
		 personBioTemplateMapper.save(personBioTemplate);
			count++;
		}
		System.out.println("fp size:" + count);

		return 0;
	}

	private PersonBioTemplate parseFingerPrint(String[] fields, String deviceSn) {
		PersonBioTemplate template = new PersonBioTemplate();

		for (String string : fields) {
			if (string.startsWith("PIN")) {
				template.setUserPin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
			} else if (string.startsWith("FID")) {
				try {
					int templateNo = Integer.valueOf(string.substring(string.indexOf("FID=") + "FID=".length()));
					template.setTemplateNo(templateNo);
				} catch (Exception e) {
					template.setTemplateNo(0);
				}
			} else if (string.startsWith("Size")) {
				try {
					int size = Integer.valueOf(string.substring(string.indexOf("Size=") + "Size=".length()));
					template.setSize(size);
				} catch (Exception e) {
					template.setSize(0);
				}
			} else if (string.startsWith("Valid")) {
				try {
					int valid = Integer.valueOf(string.substring(string.indexOf("Valid=") + "Valid".length()));
					template.setValid(valid);
				} catch (Exception e) {
					template.setValid(1);
				}
			} else if (string.startsWith("TMP")) {
				template.setTemplateData(string.substring(string.indexOf("TMP=") + "TMP=".length()));
			}
		}
		template.setIsDuress(0);
		template.setBioType(Constants.BIO_TYPE_FP);
		template.setDataFormat(Constants.BIO_DATA_FMT_ZK);
		template.setVersion(Constants.BIO_VERSION_FP_10);
		template.setTemplateNoIndex(0);
		template.setDeviceSn(deviceSn);
		UserInfo userInfo = mapper.getUserInfoByPinAndSn(template.getUserPin(), template.getDeviceSn());

		if (null == userInfo) {
			return null;
		}

		template.setUserId(userInfo.getUser_id());
		return template;
	}

	public int parseUserData(String data, String deviceSn) {
		System.out.println("user data:\n" + data);
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (null != data && !"".equals(data)) {
			String[] userInfos = data.split("\n");
			for (String string : userInfos) {
				String fieldsStr = string.substring(string.indexOf("USER ") + "USER ".length());
				String[] fields = fieldsStr.split("\t");
				UserInfo info = parseUser(fields);
				if(info.getUser_id()>0||info.getUser_pin()!=null) {
					info.setDevice_sn(deviceSn);
					list.add(info);
				}
			}
		}
		for (UserInfo userInfo : list) {
	
			// 修改失败
			String pin = mapper.getUserIdByPinAndSn(userInfo.getUser_pin(), deviceSn);
			if (pin != "" && pin != null) {
				UserInfo user = mapper.getUserInfoByPinAndSn(userInfo.getUser_pin(), deviceSn);
					if(user!=null) {
						userInfo.setUser_id(user.getUser_id());
						mapper.updateUser(userInfo);
					}
			} else {
				
				mapper.addUserInfo(userInfo);
				//sendUserDev(userInfo.getUser_id()+"","4843182260054");
			}
		}
		return 0;
	}
	
	
/*	public int sendUserDev(String ids, String sn) {
		new Thread(new Runnable() {
			public void run() {
				System.out.println("*******1************1*******1*******");
				kaoQinService.createUpdateUserInfosCommandByIds(ids, sn);
			}
		}).start();
		return 1;
	}*/
	public static boolean isDevFun(String devFuns, DEV_FUNS devFun) {
		boolean isSupport = false;
		if (null == devFuns || devFuns.isEmpty()) {
			return isSupport;
		}
		switch (devFun) {
		case FP:// the first mark indicate finger print feature.
			isSupport = devFuns.substring(0, 1).equals("1") ? true : false;
			break;
		case FACE:// the second indicate face feature.
			isSupport = devFuns.substring(1, 2).equals("1") ? true : false;
			;
			break;
		case USERPIC:// the third indicate user photo.
			isSupport = devFuns.substring(2, 3).equals("1") ? true : false;
			;
			break;
		case BIOPHOTO:// the fourth indicate user photo.
			if (devFuns.length() > 3) {
				isSupport = devFuns.substring(3, 4).equals("1") ? true : false;
				;
			}
			break;
		case BIODATA:// the fourth indicate user photo.
			if (devFuns.length() > 4) {
				isSupport = devFuns.substring(4, 5).equals("1") ? true : false;
				;
			}
			break;
		default:
			break;
		}

		return isSupport;
	}

	/**
	 * Parse every user info by key=value then return <code>UserInfo</code> entity
	 * 
	 * @param fields
	 * @return
	 */
	private static UserInfo parseUser(String[] fields) {
		UserInfo info = new UserInfo();
		for (String string : fields) {
			if (string.startsWith("PIN")) {
				info.setUser_pin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
			} else if (string.startsWith("Name")) {
				info.setName(string.substring(string.indexOf("Name=") + "Name=".length()));
			} else if (string.startsWith("Pri")) {
				try {
					int pri = Integer.valueOf(string.substring(string.indexOf("Pri=") + "Pri=".length()));
					info.setPrivilege(pri);
				} catch (Exception e) {
					info.setPrivilege(0);
				}
			} else if (string.startsWith("Passwd")) {
				info.setPassword(string.substring(string.indexOf("Passwd=") + "Passwd=".length()));
			} else if (string.startsWith("Card")) {
				info.setMain_card(string.substring(string.indexOf("Card=") + "Card=".length()));
			} else if (string.startsWith("Grp")) {
				try {
					int accGroupId = Integer.valueOf(string.substring(string.indexOf("Grp=") + "Grp".length()));
					info.setAcc_group_id(accGroupId);
				} catch (Exception e) {
					info.setAcc_group_id(1);
				}
			} else if (string.startsWith("TZ")) {
				info.setTz(string.substring(string.indexOf("TZ=") + "TZ=".length()));
			}
		}
		return info;
	}

	/**
	 * Protocol have deprecated, use "cdata" instead
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/fdata")
	public String fdata(HttpServletRequest request, HttpServletResponse response) {
		return postCdata(request, response);
	}

	/**
	 * Gets device info by the information in the initialization
	 * 
	 * @param devInfo
	 * @return
	 */
	private String getDeviceOptions(DeviceInfo devInfo) {
		StringBuilder sb = new StringBuilder();

		sb.append("GET OPTION FROM: ").append(devInfo.getDevice_sn()).append("\n");
		String ver = Constants.OPTION_VER;
		/** processes Stamp and TransFlag by the old style */
		if (null != ver && "1".equals(ver)) {
			sb.append("Stamp=").append(devInfo.getLog_stamp()).append("\n");
			sb.append("OpStamp=").append(devInfo.getOp_log_stamp()).append("\n");
			sb.append("PhotoStamp=").append(devInfo.getPhoto_stamp()).append("\n");
			sb.append(
					"TransFlag=TransData AttLog\tOpLog\tAttPhoto\tEnrollUser\tChgUser\tEnrollFP\tChgFP\tFPImag\tFACE\tUserPic\n");
		} else {
			/** Processes Stamp and TransFlag */
			int verComp = -1;
			try {
				verComp = compareVersion(devInfo.getPush_version(), "2.0.0");
			} catch (Exception e) {
				e.printStackTrace();
			}
			/**
			 * if the push is high then 2.0.0, it will do like {table}Stamp. otherwise, old
			 * style
			 */
			if (verComp >= 0) {
				sb.append("ATTLOGStamp=").append(devInfo.getLog_stamp()).append("\n");
				sb.append("OPERLOGStamp=").append(devInfo.getOp_log_stamp()).append("\n");
				sb.append("ATTPHOTOStamp=").append(devInfo.getPhoto_stamp()).append("\n");
				sb.append(
						"TransFlag=TransData AttLog\tOpLog\tAttPhoto\tEnrollUser\tChgUser\tEnrollFP\tChgFP\tFPImag\tFACE\tUserPic\tBioPhoto\n");
			} else {
				sb.append("Stamp=").append(devInfo.getLog_stamp()).append("\n");
				sb.append("OpStamp=").append(devInfo.getOp_log_stamp()).append("\n");
				sb.append("PhotoStamp=").append(devInfo.getPhoto_stamp()).append("\n");
				sb.append("TransFlag=111111111111\n");
			}
		}
		/** other information */
		sb.append("ErrorDelay=60\n");
		sb.append("Delay=30\n");
		sb.append("transTimes=").append(devInfo.getTrans_times()).append("\n");
		sb.append("TransInterval=").append(devInfo.getTrans_interval()).append("\n");
		sb.append("Realtime=1\n");
		sb.append("Encrypt=None\n");
		sb.append("ServerVer=2.2.14\n");

		return sb.toString();
	}

	public final static String MD5(String s) {
		try {
			byte[] Input = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(Input);
			byte[] md = mdInst.digest();
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = (md[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return new String(hexValue.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Data is read in the data stream
	 * 
	 * @param request
	 * @return String data
	 * @throws IOException
	 */
	public String getStreamData(HttpServletRequest request) throws IOException {
		StringBuilder bufferData = new StringBuilder();
		BufferedReader br = null;
		try {
			request.setCharacterEncoding("UTF-8");
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String inline = "";
			while ((inline = br.readLine()) != null) {
				bufferData.append(inline + "\r\n");
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return bufferData.toString().trim();
	}

	/**
	 * Compares the version.
	 * 
	 * @param version1
	 * @param version2
	 * @return version1>version2 positive. version1<version2 negative, equal 0.
	 */
	public static int compareVersion(String version1, String version2) throws Exception {
		if (version1 == null || version2 == null) {
			throw new Exception("compareVersion error:illegal params.");
		}
		String[] versionArray1 = version1.split("\\.");
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);
		int diff = 0;
		while (idx < minLength && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
			++idx;
		}
		// if it is not 0, return the value. otherwise, compare the length.
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}

	/**
	 * Gets the device language by the SN(serial number)
	 * 
	 * @param deviceSn
	 *            device SN
	 * @return
	 */
	public static String getDeviceLangBySn(String deviceSn) {
		if (null == deviceSn || deviceSn.isEmpty()) {
			return "";
		}
		DeviceInfo info = devMaps.get(deviceSn);
		if (null != info) {
			return info.getDev_language();
		}
		return "";
	}

	public static String encodingByDeviceLang(String src, String lang) {
		String dest = src;
		try {
			if (lang.equals(Constants.DEV_LANG_ZH_CN)) {
				dest = new String(src.getBytes(), "GB2312");
			} else {
				dest = new String(src.getBytes(), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dest;
	}
}
