package org.fr.grand.kaoqin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.fr.grand.util.PushUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "att_log")
@Getter
@Setter
@ToString
public class AttLog extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2276220903759258580L;
	@Id
	@GeneratedValue
	/** AUTO_INCREMENT NOT NULL PRIMARY KEY */
	private Integer att_log_id;
	/** User ID number */
	private String user_pin;
	/** Biometric identification type, like fingerprint, face, vein, etc. */
	private int verify_type;
	/** The string name of verifyType */
	@Transient
	private String verifyTypeStr;
	/** The time of the attendance log created */
	private String verify_time;
	/** Attendance status, like check-in, check-out, break-in, break-out, etc. */
	private int status;
	/** The string name of status */
	@Transient
	private String statusStr;
	/** WorkCode number, defined in the Device */
	private int work_code;
	/** Reserved, sensor number */
	private int sensor_no;
	/** Reserved, use this flag to indicate the exception attendance log */
	private int att_flag;
	/** Device serial number */
	private String device_sn;
	/** reserved field */
	private int reserved1;
	/** reserved field */
	private int reserved2;
	/** user name , use to view in web page */
	@Transient
	private String user_name;

	public String getStatusStr() {
		if (null == statusStr || statusStr.isEmpty()) {
			statusStr = PushUtil.ATT_STATUS.get(String.valueOf(status));
		}
		return statusStr;
	}
	public String getVerifyTypeStr() {
		if (null == verifyTypeStr || verifyTypeStr.isEmpty()) {
			verifyTypeStr = PushUtil.ATT_VERIFY.get(String.valueOf(verify_type));
		}
		return verifyTypeStr;
	}

}