/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.fr.grand.kaoqin.Area;
import org.fr.grand.kaoqin.Combobox;

/**
 * @author PE
 * @date 2019年11月21日 下午2:41:03
 * @explain 
 */
public interface AreaMapper {
	/**
	 * @param area
	 * @return
	 */
	List<Area> areaList(Area area);

	/**
	 * @param area
	 * @return
	 */
	Integer areaCount(Area area);

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
	 * @param area
	 * @return
	 */
	int areaModify(Area area);

	/**
	 * @param area
	 * @return
	 */
	int areaAdd(Area area);

	/**
	 * @return
	 */
	List<Combobox> search_combobox();

	/**
	 * @param aid
	 * @return
	 */
	String getInfo(Integer aid);

	/**
	 * @param area_name
	 * @return
	 */
	String getArea_Id(String area_name);

}
