package org.fr.grand.kaoqin;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User info data entity
 * 
 * @author seiya
 *
 */
@Entity
@Table(name = "user_info")
@Getter
@Setter
@ToString
public class UserInfo extends BaseEntity implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695975455644400220L;
	/** AUTO_INCREMENT NOT NULL PRIMARY KEY */
	@Id
	@GeneratedValue
	private int user_id;
	/** User ID number */
	private String user_pin;
	/** User privilege */
	private int privilege;
	/** User name */

	private String name;
	/** User password */
	private String password;
	/** ID of face group, reserved */
	private int face_group_id;
	/** ID of access group */
	private int acc_group_id = 0;
	/** ID of department, reserved */
	private int dept_id;
	/** Use group timezone or not */
	private int is_group_tz;
	/**
	 * Biometric identification type, like fingerprint, face, password, vein, etc.
	 */
	@Transient
	private boolean isNewRecord;
	private int verify_type;
	/** Main card number */
	private String main_card;
	/** Vice card number, reserved */
	private String vice_card;
	/** User expires, reserved */
	private int expires;
	/** Device serial number */
	private String device_sn;
	/** User timezone */
	private String tz;
	/** Name of user photo */
	private String photo_id_name;
	/** The size of user photo data in Base64 format */
	private int photo_id_size;
	/** User photo data in Base64 format */
	private String photo_id_content;
	/** Fingerprint count of user */
	@Transient
	private int userFpCount;
	/** Face template count of user */
	@Transient
	private int userFaceCount;
	/** Meet code */
	private String meet_code;
	/** Category */
	private int category;

	
	private MultipartFile userPic; // myFile属性用来封装上传的文件
}
