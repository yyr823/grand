/**
 * 
 */
package org.fr.grand.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author PE
 * @date 2019年8月19日 下午5:19:05
 * @explain
 */
@Component
@ConfigurationProperties(prefix = "config")
public class ConfigUrl {
	private String gt168;
	private String kaoqin;

	public String getGt168() {
		return gt168;
	}

	public void setGt168(String gt168) {
		this.gt168 = gt168;
	}

	public String getKaoqin() {
		return kaoqin;
	}

	public void setKaoqin(String kaoqin) {
		this.kaoqin = kaoqin;
	}

}
