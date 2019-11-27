/**
 * 
 */
package org.fr.grand.service;

import java.util.List;

import org.fr.grand.kaoqin.Area;
import org.fr.grand.kaoqin.Combobox;
import org.fr.grand.kaoqin.Company;
import org.fr.grand.mapper.AreaMapper;
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
public class AreaService {
@Autowired
private AreaMapper areaMapper;
/**
 * @param area
 * @return
 */
public List<Area> areaList(Area area) {
	return	areaMapper.areaList(area);
}

/**
 * @param area
 * @return
 */
public Integer areaCount(Area area) {
	return	areaMapper.areaCount(area);
}

	/**
	 * @param id
	 */
	public int delete(Integer id) {
		return	areaMapper.deleteById(id);
		
	}
	/**
	 * @param ids
	 */
	public int deletes(String ids) {
		List<Integer> cids = ParseTool.getIds(ids);
		return areaMapper.deleteByIds(cids);
	}

	/**
	 * @param area
	 * @return
	 */
	public int areaModify(Area area) {
		return areaMapper.areaModify(area);
	}

	/**
	 * @param area
	 * @return
	 */
	public int areaAdd(Area area) {
		return areaMapper.areaAdd(area);
	}

	/**
	 * @return
	 */
	public List<Combobox> search_combobox() {
		return areaMapper.search_combobox();
	}

	/**
	 * @param aid
	 * @return
	 */
	public String getInfo(Integer aid) {
		return areaMapper.getInfo(aid);
	}

	/**
	 * @param area_name
	 * @return
	 */
	public String getArea_Id(String area_name) {
		return areaMapper.getArea_Id(area_name);
	}



}
