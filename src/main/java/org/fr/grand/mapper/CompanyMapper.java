/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.Company;

/**
 * @author PE
 * @date 2019年11月21日 下午2:41:03
 * @explain 
 */
public interface CompanyMapper {

	/**
	 * @param company
	 * @return
	 */
	List<Company> companyList(Company company);

	/**
	 * @param company
	 * @return
	 */
	Integer companyCount(Company company);

	/**
	 * @param company
	 * @return
	 */
	int companyModify(Company company);

	/**
	 * @param company
	 * @return
	 */
	int companyAdd(Company company);

	/**
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);

	/**
	 * @param cids
	 * @return
	 */
	int deleteByIds(List<Integer> cids);

	/**
	 * @return
	 */
	List<Combobox> search_combobox();

}
