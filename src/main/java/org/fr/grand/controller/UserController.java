/**
 * 
 */
package org.fr.grand.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.fr.grand.conf.SystemLog;
import org.fr.grand.kaoqin.UserInfo;
import org.fr.grand.mapper.UserInfoMapper;
import org.fr.grand.service.AreaService;
import org.fr.grand.service.KaoQinService;
import org.fr.grand.util.BaseImgEncodeUtil;
import org.fr.grand.util.MyDateUtil;
import org.fr.grand.util.MyPOIUtil;
import org.fr.grand.util.ParseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * @author PE
 * @date 2019年11月27日 下午1:14:43
 * @explain
 */
@RestController
@RequestMapping({ "/user" })
public class UserController extends BaseController {
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private KaoQinService service;
	@Autowired
	private AreaService areaService;

	@RequestMapping({ "/userList" })
	@SystemLog(module = "Employee management", methods = "show employee information")
	public ModelMap userList(UserInfo userInfo, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		List<UserInfo> list = this.service.userList(userInfo);
		Integer count = this.service.userListCount(userInfo);
		return createModelMap_easyui1(count, list);

	}

	@SystemLog(module = "Employee management", methods = "add employee information")
	@RequestMapping({ "/user_add" })
	public ModelMap userAdd(UserInfo userInfo) throws Exception {
		System.out.println(userInfo);
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
		return errorMsg(1, "添加");
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

	@SystemLog(module = "Employee management", methods = "add employees information")
	@RequestMapping(value = "/uploadFile")
	public ModelMap uploadFile(MultipartFile file, String platform, HttpServletRequest request) {
		Locale locale = RequestContextUtils.getLocale(request);
		if (file != null && !file.isEmpty()) {
			File tempFile = null;
			BufferedOutputStream stream = null;
			FileInputStream in = null;
			List<UserInfo> list = new ArrayList<UserInfo>();
			try {
				byte[] bytes = file.getBytes();
				String d = FILE_DIR;// filepath.getCanonicalPath()+"\\";//FILE_DIR 常量换为path变量
				tempFile = new File(d + MyDateUtil.getCurrentDate(MyDateUtil.dateFormatYMDHMS).replace("-", "")
						.replace(":", "").replace(" ", "") + ".xls");
				stream = new BufferedOutputStream(new FileOutputStream(tempFile));
				stream.write(bytes);
				stream.close();
				if (tempFile.exists() && tempFile.length() == file.getSize()) {

					in = new FileInputStream(tempFile);
					Workbook workbook = new HSSFWorkbook(in);

					Sheet sheet = workbook.getSheetAt(0);
					if (sheet == null) {
						return createModelMap(false, "No data in sheet", null);
					} else {

						for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
							Row row = sheet.getRow(j);
							UserInfo user = new UserInfo();
							Cell cell = row.getCell(0);
							user.setDevice_sn(MyPOIUtil.getCellValue(cell));
							Cell cell01 = row.getCell(1);
							user.setUser_pin(MyPOIUtil.getCellValue(cell01));
							Cell cell02 = row.getCell(2);
							user.setName(MyPOIUtil.getCellValue(cell02));
							Cell cell03 = row.getCell(3);
							user.setPassword(MyPOIUtil.getCellValue(cell03));
							// Area Name
							Cell cell04 = row.getCell(4);
							String area_id = "";
							String area_name = MyPOIUtil.getCellValue(cell04);
							if (area_name != null && area_name != "") {
								area_id = areaService.getArea_Id(area_name);
							}
							user.setArea_id(area_id);

							Cell cell05 = row.getCell(5);
							user.setMain_card(MyPOIUtil.getCellValue(cell05));
							// Privilege
							Cell cell06 = row.getCell(6);
							String privilege = MyPOIUtil.getCellValue(cell06);
							int pri = 0;
							switch (privilege) {
							case "registrar":
								pri = 2;
								break;
							case "administrator":
								pri = 6;
								break;
							case "custom":
								pri = 10;
								break;
							case "superadmin":
								pri = 14;
								break;
							default:
								pri = 0;
								break;
							}
							user.setPrivilege(pri);
							// Category
							Cell cell07 = row.getCell(7);
							String category = MyPOIUtil.getCellValue(cell07);
							int cate = 0;
							switch (category) {
							case "vip":
								cate = 1;
								break;
							case "blacklist":
								cate = 2;
								break;

							default:
								cate = 0;
								break;
							}

							user.setCategory(cate);
							list.add(user);
						}
						// 批量插入
						if (list.isEmpty()) {
							return createModelMap(false, "None has been imported, as no data in sheet", null);
						} else {
							int addSuccess = 0;
							int addFailed = 0;
							int addAlready = 0;
							for (UserInfo user : list) {
								try {
									UserInfo u = service.getByAuthenUserBySnAndId(user.getDevice_sn(),
											user.getUser_pin());
									if (null == u) {
										if (userInfoMapper.addUserInfo(user) == 1) {
											user = userInfoMapper.getMax();

											if (user != null) {
												// 推送
												service.userAddBySn(user);
												// service.sendUserDev(user.getUser_id() + "", user.getDevice_sn());
												Thread.sleep(1000);
												addSuccess++;
											}
										}
									} else {
										addFailed++;
									}

								} catch (Exception e) {
									e.printStackTrace();
									return createModelMap(false, "File upload failed", null);
								}
							}

							return createModelMap(true,
									"Total" + " " + list.size() + " " + "items, import" + "  " + addSuccess + " "
											+ "items，failed" + " " + addFailed + "  " + "items, ignore already exists"
											+ " " + addAlready + " " + "items",
									null);
						}
					}
				} else {
					return createModelMap(false, "File upload failed", null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return createModelMap(false, "File upload failed", null);
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}
					if (in != null) {
						in.close();
					}
					tempFile.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			return createModelMap(false, "File upload failed", null);
		}
	}

	@SystemLog(module = "Employee management", methods = "add employee to  new Device")
	@RequestMapping({ "/addUserToNew" })
	public ModelMap addUserToNew(String uids, String sn) {
		String[] sns = sn.split(",");
		for (String sn1 : sns) {
			service.sendUserDev(uids, sn1);
		}
		return errorMsg(0, "同步到其他数据");
	}

	@SystemLog(module = "Employee management", methods = "modify employee information")
	@RequestMapping({ "/user_modify" })
	public ModelMap modify(UserInfo userInfo, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println(userInfo);
		service.userUpdate(userInfo);
		return errorMsg(0, "修改成功");
	}

	@SystemLog(module = "Employee management", methods = "delete employee from server")
	@RequestMapping({ "/deleteUserServ" })
	public ModelMap deleteUserServ(String userId) {

		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.deleteUserServ(ids);
		return errorMsg(count, "删除(用户信息+指纹数据)");
	}

	@SystemLog(module = "Employee management", methods = "delete Face from server")
	@RequestMapping({ "/deleteUserFaceServ" })
	public ModelMap deleteUserFaceServ(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.deleteFaceFromServer(ids);
		return errorMsg(count, "删除(面部数据)");
	}

	@SystemLog(module = "Employee management", methods = "delete finger from server")
	@RequestMapping({ "/deleteUserFpServ" })
	public ModelMap deleteUserFpServ(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.deleteFpFromServer(ids);
		return errorMsg(count, "删除(指纹数据)");
	}

	@SystemLog(module = "Employee management", methods = "delete picture from server")
	@RequestMapping({ "/deleteUserPicServ" })
	public ModelMap deleteUserPicServ(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.deleteUserPicFromServer(ids);
		return errorMsg(count, "删除(图片)");
	}

	@SystemLog(module = "Employee management", methods = "synchronize employee information to device")
	@RequestMapping({ "/sendUserDev" })
	public ModelMap send(String userId) {
		int count = service.sendUserDev(userId, null);
		return errorMsg(count, "同步全部数据");
	}

	@SystemLog(module = "Employee management", methods = "synchronize employee information to other device")
	@RequestMapping({ "/toNewDevice" })
	public ModelMap toNewDevice(String userId, String sn) {
		int count = service.toNewDevice(userId, sn);
		return errorMsg(count, "同步数据到其他机器上");
	}

	@SystemLog(module = "Employee management", methods = "delete employee from device")
	@RequestMapping({ "/deleteUserDev" })
	public ModelMap deleteUserDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteUserCommandByIds(ids, null);
		return errorMsg(count, "删除机器全部用户数据");
	}

	@SystemLog(module = "Employee management", methods = "delete finger from device")

	@RequestMapping({ "/deleteUserFpDev" })
	public ModelMap deleteUserFpDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteUserFpByIds(ids, null);
		return errorMsg(count, "删除机器指纹");
	}

	@SystemLog(module = "Employee management", methods = "delete picture from device")

	@RequestMapping({ "/deleteUserPicDev" })
	public ModelMap deleteUserPicDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteUserPicByIds(ids, null);
		return errorMsg(count, "删除机器用户图片");
	}

	@SystemLog(module = "Employee management", methods = "delete face from device")
	@RequestMapping({ "/deleteUserFaceDev" })
	public ModelMap deleteUserFaceDev(String userId) {
		List<Integer> ids = ParseTool.getIds(userId);
		int count = service.createDeleteFaceByIds(ids, null);
		return errorMsg(count, "删除机器面部数据");
	}

}
