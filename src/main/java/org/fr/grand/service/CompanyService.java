/**
 * 
 */
package org.fr.grand.service;

import java.util.List;

import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.Company;
import org.fr.grand.mapper.CompanyMapper;
import org.fr.grand.util.ParseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author PE
 * @date 2019年11月21日 下午2:39:05
 * @explain 
 */
@Service
@Transactional
public class CompanyService {
@Autowired
private CompanyMapper companyMapper;
	/**
	 * @param company
	 * @return
	 */
	public List<Company> companyList(Company company) {
		return	companyMapper.companyList(company);
	}
	/**
	 * @param company
	 * @return
	 */
	public Integer companyCount(Company company) {
		return	companyMapper.companyCount(company);
	}
	/**
	 * @param company
	 */
	public int companyModify(Company company) {
		return	companyMapper.companyModify(company);
		
	}
	/**
	 * @param company
	 */
	public int companyAdd(Company company) {
		return	companyMapper.companyAdd(company);
		
	}
	/**
	 * @param id
	 */
	public int delete(Integer id) {
		return	companyMapper.deleteById(id);
		
	}
	/**
	 * @param ids
	 */
	public int deletes(String ids) {
		List<Integer> cids = ParseTool.getIds(ids);
		return companyMapper.deleteByIds(cids);
	}
	/**
	 * @return
	 */
	public List<Combobox> search_combobox() {
		return companyMapper.search_combobox();
	}

}
