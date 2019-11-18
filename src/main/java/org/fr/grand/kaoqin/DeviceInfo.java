package org.fr.grand.kaoqin;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.fr.grand.kaoqin.Constants.DEV_FUNS;
import org.fr.grand.util.PushUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Device info entity
 * 
 * @author seiya
 *
 */
@Entity
@Table(name = "device_info")
@Getter
@Setter
@ToString
public class DeviceInfo extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -713219808104772168L;
	/** Device ID, AUTO_INCREMENT NOT NULL PRIMARY KEY */
	@Id
	@GeneratedValue
	private Integer device_id;
	/** Device serial number */
	private String device_sn;
	/** Device name */
	private String device_name;
	/** Device alias name */
	private String alias_name;
	/** Department ID */
	private int dept_id;
	/** Device status */
	private String state;
	/** Device last online activity time */
	private String last_activity;
	/**
	 * This parameter will be sent to device when connecting. The transTimes is
	 * several time combination separated by semicolon that the device will go to
	 * check and upload any data updates on time. It must be 24 hour format HH:MM,
	 * like transTimes=00:00;14:00. It can support up to 10 time combination.
	 */
	private String trans_times;
	/**
	 * This parameter will be sent to device when connecting. transInterval in
	 * minutes is the interval that the device goes to check and upload any data
	 * updates. transInterval = 0 means no check.
	 */
	private int trans_interval;
	/**
	 * This parameter will be sent to device when connecting. Attendance transaction
	 * timestamp The device will upload attendance transactions created after the
	 * timestamp.
	 */
	private String log_stamp;
	/**
	 * This parameter will be sent to device when connecting. Oplog timestamp The
	 * device will upload oplogs created after the timestamp.
	 */
	private String op_log_stamp;
	/**
	 * This parameter will be sent to device when connecting. Attendance photo
	 * timestamp. The device will upload attendance photos created after the
	 * timestamp.
	 */

	private String photo_stamp;
	/** Push firmware version of the device */
	private String fw_version;
	/** User count of the device */
	private int user_count;
	/** User count in the server */
	@Transient
	private int act_user_count;
	/** Finggerprint count of the device */
	private int fp_count;
	/** Finggerprint count in the server */
	@Transient
	private int act_fp_count;
	/** Attendance transaction count of the device */
	private int trans_count;
	/** Attendance transaction count in the server */
	@Transient
	private int act_trans_count;
	/** Fingerprint algorithm version of the device */
	private String fp_alg_ver;
	/** Push protocal version of the device */
	private String push_version;
	/** Device type */
	private String device_type;
	/** Device IP address */
	private String ipaddress;
	/** Device displayed language */
	private String dev_language;
	/** Communication password */
	private String push_comm_key;
	/** Face count in the device */
	private int face_count;
	/** Face count in the server */
	@Transient
	private int act_face_count;
	/** Face algorithm version of the device */
	private String face_alg_ver;
	/** Template count of one face */
	private int reg_face_count;
	/** Functions supported in device */
	private String dev_funs;
	@Transient
	private List<DeviceAttrs> attrList = null;
	private int error_delay;
	private int delay;
	private int real_time;
	private String trans_flag;
	private int encrypt;	

	
}
