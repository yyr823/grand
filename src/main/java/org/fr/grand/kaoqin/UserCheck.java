/**
 * 
 */
package org.fr.grand.kaoqin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author PE
 * @date 2019年7月25日 上午10:39:42
 * @explain
 */
@Getter
@Setter
@ToString
public class UserCheck extends BaseEntity {
	private String device_sn;
	private String time;
	private String startTime;
	private String endTime;



}
