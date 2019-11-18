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
 * Biometric template data entity
 * 
 * @author seiya
 *
 */
@Entity
@Table(name = "pers_bio_template")
@Getter
@Setter
@ToString
public class PersonBioTemplate implements Serializable {
	private static final long serialVersionUID = -1224885050967443958L;
	/** AUTO_INCREMENT NOT NULL PRIMARY KEY */
	/** User AUTO_INCREMENT ID */
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "USER_ID")
	private int userId;
	@Column(name = "USER_PIN")
	/** User business logic ID */
	private String userPin;
	/** Is valid template or not. 0-invalid, 1-valid. Default 1. */
	private int valid;
	/** Is duress template or not. 0-not duress, 1-duress. Default 0. */
	@Column(name = "IS_DURESS")
	private int isDuress;
	/** Type of biometric, like fingerprint, face, etc. */
	@Column(name = "BIO_TYPE")
	private int bioType;
	/** Biometric algorithm version */
	private String version;
	/** Format of biometric template */
	@Column(name = "DATA_FORMAT")
	private int dataFormat;
	/** Number of individual organisms */
	@Column(name = "TEMPLATE_NO")
	private int templateNo;
	/** Index of template */
	@Column(name = "TEMPLATE_NO_INDEX")
	private int templateNoIndex;
	/** template size */
	private int size;
	/** device serial number */
	@Column(name = "DEVICE_SN")
	private String deviceSn;
	/** template data */
	@Column(name = "TEMPLATE_DATA")
	private String templateData;
}
