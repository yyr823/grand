/**
 * 
 */
package org.fr.grand.kaoqin;

import javax.persistence.Transient;

/**
 * @author PE
 * @date 2019年10月28日 下午5:14:58
 * @explain 
 */
public class BaseEntity {

	private Integer page;

	private Integer limit;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = (page-1)*limit;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		
		this.limit = limit;
	}
	
}
