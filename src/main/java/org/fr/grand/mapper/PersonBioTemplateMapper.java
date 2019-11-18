/**
 * 
 */
package org.fr.grand.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fr.grand.kaoqin.PersonBioTemplate;
import org.fr.grand.util.MyMapper;

/**
 * @author PE
 * @date 2019年7月31日 上午11:12:20
 * @explain
 */
public interface PersonBioTemplateMapper {

	List<PersonBioTemplate> fatchList(@Param("user_ids") List<Integer> ids);

	/**
	 * @param personBioTemplate
	 * @return
	 */
	int save(PersonBioTemplate personBioTemplate);

	/**
	 * @param ids
	 */
	int deleteBioTemplate(List<Integer> ids);

	/**
	 * @param ids
	 * @param bioTypeFace
	 * @return
	 */
	int deleteFromServerByType(@Param("ids")List<Integer> ids, @Param("bioType")int bioType);

	/**
	 * @param id
	 * @return
	 */
	int getById(int id);

	/**
	 * @param personBioTemplate
	 */
	int updateTemplate(PersonBioTemplate personBioTemplate);


	
}
