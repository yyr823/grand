package org.fr.grand.kaoqin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "device_attrs")
@Getter
@Setter
@ToString
public class DeviceAttrs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1754691984901806692L;
	@Id
	@GeneratedValue
	private int id;
	private String deviceSn;

	private String attrName;

	private String attrValue;

}
