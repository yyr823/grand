/**
 * 
 */
package org.fr.grand.controller;

import java.util.List;

import org.fr.grand.conf.SystemLog;
import org.fr.grand.kaoqin.Area;
import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.service.AreaService;
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
@RequestMapping({ "/area" })
public class AreaController extends BaseController{
	@Autowired
	private AreaService areaService;
	
	@RequestMapping({ "/areaList" })
	
	public ModelMap areaList(Area area) {
		List<Area> list = this.areaService.areaList(area);
		Integer count = this.areaService.areaCount(area);
		return createModelMap_easyui1(count, list);
	}
	
	@RequestMapping(value = "combobox", method = RequestMethod.GET)
	public List<Combobox> search_combobox() {
		return this.areaService.search_combobox();
	}
	@RequestMapping(value = "getInfo")
	public ModelMap getInfo(Integer aid) {
		ModelMap map = new ModelMap();
		String str=this.areaService.getInfo(aid);
		map.put("aid", str);
		return map;
	}
	
	@SystemLog(module = "Area management", methods = "modify area information")
	@RequestMapping({ "/area_modify" })
	public ModelMap modify(Area area) {
		int count =areaService.areaModify(area);
		return errorMsg(count, "修改成功");
	}
	@SystemLog(module = "Area management", methods = "add area information")
	@RequestMapping({ "/area_add" })
	public ModelMap add(Area area) {
		int count =areaService.areaAdd(area);
		return errorMsg(count, "添加成功");
	}
	@SystemLog(module = "Area management", methods = "delete area information")
	@RequestMapping({ "/delete" })
	public ModelMap delete(Integer id) {
		int count =areaService.delete(id);
		return errorMsg(count, "删除");
	}
	@SystemLog(module = "Area management", methods = "delete areas information")
	@RequestMapping({ "/deletes" })
	public ModelMap deletes(String ids) {
		int count =areaService.deletes(ids);
		return errorMsg(count, "批量删除");
	}

}
