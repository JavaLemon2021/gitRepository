package com.bjpowernode.crm.settings.mapper;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

public interface DicTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbggenerated Mon May 18 16:01:30 CST 2020
     */
    int deleteByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbggenerated Mon May 18 16:01:30 CST 2020
     */
    int insert(DicType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbggenerated Mon May 18 16:01:30 CST 2020
     */
    int insertSelective(DicType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbggenerated Mon May 18 16:01:30 CST 2020
     */
    DicType selectByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbggenerated Mon May 18 16:01:30 CST 2020
     */
    int updateByPrimaryKeySelective(DicType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_type
     *
     * @mbggenerated Mon May 18 16:01:30 CST 2020
     */
    int updateByPrimaryKey(DicType record);

    //显示所有字典类型的列表
    List<DicType> selectAllDicTypes();

    //创建
    int insertDicType(DicType dicType);

    //根据字典类型的编码找到字典类型
    DicType selectDicTypeCode(String code);

    //更新
    int updateDicType(DicType dicType);

    //删除
    int deleteDicTypeByCodes(String[] code);

}

