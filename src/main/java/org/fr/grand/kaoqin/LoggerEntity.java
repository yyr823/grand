package org.fr.grand.kaoqin;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@Table(name = "logger_infos")
public class LoggerEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private  Integer id;
	private String clientip;
	private String clienturl;
	private String methodss;
	private String operatess;
	private String currtime;


	
}
