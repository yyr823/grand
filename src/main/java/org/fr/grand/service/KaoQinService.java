/**
 * 
 */
package org.fr.grand.service;

import java.util.List;

import org.fr.grand.kaoqin.AttLog;
import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.Constants;
import org.fr.grand.kaoqin.Constants.DEV_FUNS;
import org.fr.grand.kaoqin.DeviceInfo;
import org.fr.grand.kaoqin.PersonBioTemplate;
import org.fr.grand.kaoqin.UserCheck;
import org.fr.grand.kaoqin.UserInfo;
import org.fr.grand.mapper.AttLogMapper;
import org.fr.grand.mapper.DeviceCommandMapper;
import org.fr.grand.mapper.DeviceInfoMapper;
import org.fr.grand.mapper.PersonBioTemplateMapper;
import org.fr.grand.mapper.UserInfoMapper;
import org.fr.grand.util.ParseTool;
import org.fr.grand.util.PushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author PE
 * @date 2019年8月4日 下午9:42:16
 * @explain
 */
@Service
@Transactional
public class KaoQinService {
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private PersonBioTemplateMapper personBioTemplateMapper;
	@Autowired
	private DeviceCommandMapper deviceCommandMapper;
	@Autowired
	private AttLogMapper attLogMapper;

	/**
	 * @param deviceInfo
	 * @return
	 */
	public List<DeviceInfo> deviceList(DeviceInfo deviceInfo) {
		return this.deviceInfoMapper.deviceList(deviceInfo);
	}

	/**
	 * @param deviceInfo
	 * @return
	 */
	public Integer deviceListCount(DeviceInfo deviceInfo) {
		return this.deviceInfoMapper.deviceListCount(deviceInfo);
	}

	/**
	 * @param userInfo
	 * @return
	 */
	public List<UserInfo> userList(UserInfo userInfo) {

		return this.userInfoMapper.userList(userInfo);
	}

	/**
	 * @param userInfo
	 * @return
	 */
	public Integer userListCount(UserInfo userInfo) {
		return this.userInfoMapper.userListCount(userInfo);
	}

	public List<Combobox> search_combobox() {
		return this.deviceInfoMapper.search_combobox();
	}

	/**
	 * @param ids
	 * @param object
	 */

	public int createUpdateUserInfosCommandByIds(String userIds, String deviceSn) {
		List<UserInfo> userInfos = null;
		List<PersonBioTemplate> bioTemplates = null;
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportFP = false;
		boolean isSupportFace = false;
		boolean isSupportUserPic = false;
		boolean isSupportBioPhoto = false;
		/** get the target device information from buffer */
		if (null != deviceSn && !deviceSn.isEmpty() && PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			System.out.println("判断1:" + haveDestSn);
			/** see what function the device is support */
			isSupportFP = PushUtil.isDevFun(deviceInfo.getDev_funs(), DEV_FUNS.FP);
			isSupportFace = PushUtil.isDevFun(deviceInfo.getDev_funs(), DEV_FUNS.FACE);
			isSupportUserPic = PushUtil.isDevFun(deviceInfo.getDev_funs(), DEV_FUNS.USERPIC);
			isSupportBioPhoto = PushUtil.isDevFun(deviceInfo.getDev_funs(), DEV_FUNS.BIOPHOTO);
		}

		List<Integer> ids = ParseTool.getIds(userIds);
		userInfos = userInfoMapper.findUserByIds(ids);

		if (null == userInfos) {
			return -1;
		}

		/** get the biometrics template list from database */
		if (!haveDestSn || (haveDestSn && (isSupportFP || isSupportFace))) {
			bioTemplates = personBioTemplateMapper.fatchList(ids);
		}
		System.out.println("判断2:" + haveDestSn);
		for (UserInfo userInfo : userInfos) {
			/** create update user information command */
			deviceCommandMapper.save(DevCmdUtil.getUpdateUserCommand(userInfo, deviceSn));
			System.out.println("判断3:" + haveDestSn);
			if (haveDestSn) { // specify the target device
				/** create update user photo command */
				if (isSupportUserPic && null != userInfo.getPhoto_id_name() && !userInfo.getPhoto_id_name().isEmpty()) {
					deviceCommandMapper.save(DevCmdUtil.getUpdateUserPicCommand(userInfo, deviceSn));
				}
				/** create update user bio photo command */
				if (isSupportBioPhoto && null != userInfo.getPhoto_id_name()
						&& !userInfo.getPhoto_id_name().isEmpty()) {
					deviceCommandMapper.save(DevCmdUtil.getUpdateBioPhotoCommand(userInfo, deviceSn));
				}
			} else {
				/**
				 * if target device is not specify, need to check if user photo function is
				 * support in this device where the user belonging
				 */
				String tempSn = userInfo.getDevice_sn();
				DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
				if (null != tempDev && PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.USERPIC)
						&& null != userInfo.getPhoto_id_name() && !"".equals(userInfo.getPhoto_id_name())) {
					/** create update user photo command */
					deviceCommandMapper.save(DevCmdUtil.getUpdateUserPicCommand(userInfo, deviceSn));

				}
				if (null != tempDev && PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.BIOPHOTO)
						&& null != userInfo.getPhoto_id_name() && !"".equals(userInfo.getPhoto_id_name())) {
					/** create update user biophoto command */
					deviceCommandMapper.save(DevCmdUtil.getUpdateBioPhotoCommand(userInfo, deviceSn));
				}
			}
		}
		System.out.println("判断4:" + haveDestSn);
		for (PersonBioTemplate personTemplate : bioTemplates) {

			if (haveDestSn) {

				if (isSupportFP && Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
					/** create update fingerprint template command */
					deviceCommandMapper.save(DevCmdUtil.getUpdateFPCommand(personTemplate, deviceSn));
					System.out.println("执行1");
				} else if (isSupportFace && Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
					/** create update face template command */
					deviceCommandMapper.save(DevCmdUtil.getUpdateFaceCommand(personTemplate, deviceSn));
					System.out.println("执行2");
				}
			} else {
				/**
				 * if target device is not specify, need to check the device where user
				 * belonging
				 */
				String tempSn = personTemplate.getDeviceSn();
				DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
				System.out.println("tempDev:" + tempDev);
				if (null == tempDev) {
					continue;
				}
				System.out.println("flag:" + PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.FP));

				if (PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.FP)
						&& Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
					/** create update fingerprint template command */
					deviceCommandMapper.save(DevCmdUtil.getUpdateFPCommand(personTemplate, deviceSn));
					System.out.println("执行3");
				} else if (PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.FACE)
						&& Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
					/** create update face template command */
					deviceCommandMapper.save(DevCmdUtil.getUpdateFaceCommand(personTemplate, deviceSn));
					System.out.println("执行4");
				}
			}
		}
		return 1;

	}

	/**
	 * @param userInfo
	 * @return
	 */
	public void userAdd(UserInfo userInfo) {
		userInfoMapper.addUserInfo(userInfo);
		UserInfo u = userInfoMapper.getMax();
		sendUserDev(u.getUser_id() + "", userInfo.getDevice_sn());
	}

	public int sendUserDev(String ids, String sn) {
		new Thread(new Runnable() {
			public void run() {
				createUpdateUserInfosCommandByIds(ids, sn);
			}
		}).start();
		return 1;
	}

	
	/**
	 * @param userId
	 * @param sn
	 * @return
	 */
	public int toNewDevice(String userId, String sn) {
		String[] destSns = sn.split(",");
		for (final String deviceSn : destSns) {
			new Thread(new Runnable() {
				public void run() {
					createUpdateUserInfosCommandByIds(userId, deviceSn);
				}
			}).start();	
		}
		return 1;
	}
	
	
	public void userUpdate(UserInfo userInfo) {
		userInfoMapper.updateUser(userInfo);
		sendUserDev(userInfo.getUser_id() + "", userInfo.getDevice_sn());

	}

	/*	*//**
			 * @param ucCheck
			 * @return
			 */
	public List<AttLog> logList(UserCheck ucCheck) {

		return this.attLogMapper.logList(ucCheck);
	}

	/**
	 * @param ucCheck
	 * @return
	 */
	public Integer logListCount(UserCheck ucCheck) {
		return this.attLogMapper.logListCount(ucCheck);
	}

	/**
	 * @param ids
	 */
	public int deleteUserServ(List<Integer> ids) {

		userInfoMapper.deleteUserInfo(ids);
		personBioTemplateMapper.deleteBioTemplate(ids);
		return ids.size();

	}

	/**
	 * @param ids
	 * @return
	 */

	public int createDeleteUserCommandByIds(List<Integer> ids, String sn) {
		List<UserInfo> list = userInfoMapper.findUserByIds(ids);
		if (list != null && list.size() > 0) {
			for (UserInfo userInfo : list) {
				/** create delete user command */
				deviceCommandMapper.save(DevCmdUtil.getDeleteUserCommand(userInfo, sn));
			}

		}
		return 1;
	}

	/**
	 * @param ids
	 * @param object
	 * @return
	 */
	public int createDeleteUserFpByIds(List<Integer> ids, String deviceSn) {
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportFP = false;
		/** get target device information from buffer */
		if (null != deviceSn && !deviceSn.isEmpty() && PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			/** see what function support in device */
			isSupportFP = PushUtil.isDevFun(deviceInfo.getDev_funs(), DEV_FUNS.FP);
		} /**
			 * if target device is not support finger function or user list is empty, then
			 * not send the command to device
			 */
		if ((haveDestSn && !isSupportFP)/* || null == userInfos || userInfos.size() <= 0 */) {
			return -1;
		}
		/** get biometrics template list from database */
		List<PersonBioTemplate> bioTemplates = personBioTemplateMapper.fatchList(ids);
		for (PersonBioTemplate personTemplate : bioTemplates) {
			if (!haveDestSn) {
				/**
				 * if target device not exist, then check the device SN from the fingerprint
				 * template
				 */
				String tempSn = personTemplate.getDeviceSn();
				DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
				if (null == tempDev) {
					continue;
				}
				if (PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.FP)
						&& Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
					/** create delete fingerprint command */
					deviceCommandMapper.save(DevCmdUtil.getDeleteFpCommand(personTemplate, deviceSn));
				}
			} else {
				if (Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
					/** create delete fingerprint command */
					deviceCommandMapper.save(DevCmdUtil.getDeleteFpCommand(personTemplate, deviceSn));
				}
			}
		}
		return 1;
	}

	/**
	 * @param ids
	 * @return
	 */
	public int deleteFaceFromServer(List<Integer> ids) {
		int count = personBioTemplateMapper.deleteFromServerByType(ids, Constants.BIO_TYPE_FACE);
		return count;
	}

	/**
	 * @param ids
	 * @return
	 */
	public int deleteFpFromServer(List<Integer> ids) {
		int count = personBioTemplateMapper.deleteFromServerByType(ids, Constants.BIO_TYPE_FP);
		return count;
	}

	/**
	 * @param ids
	 * @return
	 */
	public int deleteUserPicFromServer(List<Integer> ids) {
		int count = userInfoMapper.deleteUserPicFromServer(ids);
		return count;
	}

	/**
	 * Create delete user photo command for specific device
	 * 
	 * @param userIds
	 *            user ID
	 * @param deviceSn
	 *            device SN
	 * @return
	 */
	public int createDeleteUserPicByIds(List<Integer> userIds, String deviceSn) {
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportUserPic = false;
		/** get target device information from buffer */
		if (null != deviceSn && !deviceSn.isEmpty() && PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			/** see what function support */
			isSupportUserPic = PushUtil.isDevFun(deviceInfo.getDev_funs(), DEV_FUNS.USERPIC);
		}
		if (haveDestSn && !isSupportUserPic) {
			return -1;
		}
		/** get user basic information by condition */
		List<UserInfo> userInfos = userInfoMapper.findUserByIds(userIds);

		if (null == userInfos) {
			return -1;
		}
		for (UserInfo userInfo : userInfos) {
			if (!haveDestSn) {
				/**
				 * if target device is not exist, then check the device SN in user information
				 */
				String tempSn = userInfo.getDevice_sn();
				DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
				if (null == tempDev) {
					return -1;
				}
				if (PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.USERPIC)) {
					/** create delete user photo command */
					deviceCommandMapper.save(DevCmdUtil.getDeleteUserPicCommand(userInfo, deviceSn));
				}
			} else {
				/** create delete user photo command */
				deviceCommandMapper.save(DevCmdUtil.getDeleteUserPicCommand(userInfo, deviceSn));
			}
		}
		return 1;
	}

	/**
	 * Create delete user face template command for specific device
	 * 
	 * @param userIds
	 *            user ID
	 * @param deviceSn
	 *            device SN
	 * @return
	 */
	public int createDeleteFaceByIds(List<Integer> userIds, String deviceSn) {
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportFace = false;
		/** get target devcie information from buffer */
		if (null != deviceSn && !deviceSn.isEmpty() && PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			// haveDestSn = true;
			/** see what function support */
			isSupportFace = PushUtil.isDevFun(deviceInfo.getDev_funs(), DEV_FUNS.FACE);
		}

		/**
		 * if face function is not support bytarget device, then not send the command
		 */
		if (haveDestSn && !isSupportFace) {
			return -1;
		}

		/** get the biometrics template list from database */
		List<PersonBioTemplate> bioTemplates = personBioTemplateMapper.fatchList(userIds);

		for (PersonBioTemplate personTemplate : bioTemplates) {
			if (!haveDestSn) {
				/**
				 * if target device is not exist, then check the device SN from face template
				 */
				String tempSn = personTemplate.getDeviceSn();
				DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
				if (null == tempDev) {
					continue;
				}
				if (PushUtil.isDevFun(tempDev.getDev_funs(), DEV_FUNS.FACE)
						&& Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
					/** create delete face template command */
					deviceCommandMapper.save(DevCmdUtil.getDeleteFaceCommand(personTemplate, deviceSn));
				}
			} else {
				if (Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
					/** create delete face template command */
					deviceCommandMapper.save(DevCmdUtil.getDeleteFaceCommand(personTemplate, deviceSn));
				}
			}
		}

		return 1;
	}




}
