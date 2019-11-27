/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fr.grand.kaoqin.UserInfo;
import org.fr.grand.util.MyMapper;

/**
 * @author PE
 * @date 2019年7月31日 上午11:12:20
 * @explain
 */
public interface UserInfoMapper {
	int addUserInfo(UserInfo user);

	UserInfo getUserInfoByPinAndSn(@Param("userPin") String userPin, @Param("deviceSn") String deviceSn);

	String getUserIdByPinAndSn(@Param("userPin") String userPin, @Param("deviceSn") String deviceSn);

	int updateUser(UserInfo user);

	List<UserInfo> userList(UserInfo userInfo);

	Integer userListCount(UserInfo userInfo);

	List<UserInfo> findUserByIds(@Param("user_ids") List<Integer> ids);

	UserInfo getMax();

	/**
	 * @param ids
	 */
	int deleteUserInfo(List<Integer> ids);

	/**
	 * @param ids
	 * @return
	 */
	int deleteUserPicFromServer(List<Integer> ids);

	/**
	 * @param list
	 * @return
	 */
	int updateUserPic(List<UserInfo> list);

	/**
	 * @return
	 */
	List<String> getDevice_SnByArea(Integer uid);

	/**
	 * @param device_sn
	 * @param user_pin
	 * @return
	 */
	UserInfo getByAuthenUserBySnAndId(@Param("sn")String device_sn,@Param("pin")String user_pin);

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	UserInfo getUserByNameAndPassward(@Param("username")String username,
			@Param("password")String password);



}
