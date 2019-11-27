/**
 * 
 */
package org.fr.grand.controller;

import java.util.List;

import org.fr.grand.conf.SystemLog;
import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.Company;
import org.fr.grand.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PE
 * @date 2019年11月21日 下午2:34:49
 * @explain 
 */
@RestController
@RequestMapping({ "/company" })
public class CompanyController extends BaseController{
	@Autowired
	private CompanyService companyService;
	
	@RequestMapping({ "/companyList" })
	public ModelMap logList(Company company) {
		List<Company> list = this.companyService.companyList(company);
		Integer count = this.companyService.companyCount(company);
		return createModelMap_easyui1(count, list);
	}
	
	
	@RequestMapping(value = "combobox", method = RequestMethod.GET)
	public List<Combobox> search_combobox() {
		return this.companyService.search_combobox();
	}

	
	@SystemLog(module = "Company management", methods = "modify company information")
	@RequestMapping({ "/company_modify" })
	public ModelMap modify(Company company) {
		int count =companyService.companyModify(company);
		return errorMsg(count, "修改成功");
	}

	@SystemLog(module = "Company management", methods = "add company information")
	@RequestMapping({ "/company_add" })
	public ModelMap add(Company company) {
		int count =companyService.companyAdd(company);
		return errorMsg(count, "添加成功");
	}
	@SystemLog(module = "Company management", methods = "delete company information")
	@RequestMapping({ "/delete" })
	public ModelMap delete(Integer id) {
		int count =companyService.delete(id);
		return errorMsg(count, "删除");
	}
	@SystemLog(module = "Company management", methods = "delete companys information")
	@RequestMapping({ "/deletes" })
	public ModelMap deletes(String ids) {
		int count =companyService.deletes(ids);
		return errorMsg(count, "批量删除");
	}

}
