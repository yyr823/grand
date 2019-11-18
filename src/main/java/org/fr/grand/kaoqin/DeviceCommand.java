package org.fr.grand.kaoqin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Device Command data entity
 * 
 * @author seiya
 *
 */
@Entity
@Table(name = "device_command")
@Getter
@Setter
@ToString
public class DeviceCommand implements Serializable {

	private static final long serialVersionUID = 4897891380884898686L;
	/** AUTO_INCREMENT NOT NULL PRIMARY KEY */
	@Id
	@GeneratedValue
	@Column(name = "DEV_CMD_ID")
	private Integer devCmdId;
	/** Device serial number */
	@Column(name = "DEVICE_SN")
	private String deviceSn;
	/** Content of command */
	@Column(name = "CMD_CONTENT")
	private String cmdContent;
	@Column(name = "CMD_COMMIT_TIMES")
	/** Time of command committed on Server */
	private String cmdCommitTime;
	/** Time of command transfered to Device */
	@Column(name = "CMD_TRANS_TIMES")
	private String cmdTransTime;
	/** Time of Device reply to Sever after command executed */
	@Column(name = "CMD_OVER_TIME")
	private String cmdOverTime;
	/** The return value from Device after command executed */
	@Column(name = "CMD_RETURN")
	private String cmdReturn;
	@Column(name = "CMD_RETURN_INFO")
	/** The return info from Device after command executed */
	private String cmdReturnInfo;

}
