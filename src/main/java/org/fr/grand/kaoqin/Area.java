/**
 * 
 */
package org.fr.grand.kaoqin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author PE
 * @date 2019年11月21日 下午2:34:05
 * @explain 
 */
@Entity
@Table(name = "area")
@Getter
@Setter
@ToString
public class Area extends BaseEntity {
	@Id
	@GeneratedValue
private Integer id;
private Integer cid;
private String area_name;
private String company_name;
@Transient
private String act;
@Transient
private Integer employee_count;
@Transient
private Integer device_count;
}
