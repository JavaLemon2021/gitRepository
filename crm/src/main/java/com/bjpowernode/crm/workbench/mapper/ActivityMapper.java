package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat May 23 15:59:14 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat May 23 15:59:14 CST 2020
     */
    int insert(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat May 23 15:59:14 CST 2020
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat May 23 15:59:14 CST 2020
     */
    Activity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat May 23 15:59:14 CST 2020
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Sat May 23 15:59:14 CST 2020
     */
    int updateByPrimaryKey(Activity record);

    //保存市场活动
    int insertActivity(Activity activity);

    //根据多条件和分页做查询
    List<Activity> selectActivityForPageByCondition(Map<String,Object> map);

    //按条件查询市场活动总数
    long selectCountOfActivityByCondition(Map<String,Object> map);

    //根据id查询市场活动
    Activity selectActivityById(String id);

    //更新
    int updateActivity(Activity activity);

    //根据id做批量删除
    int deleteActivityByIds(String[] ids);

    //批量保存市场活动对象(导入）
    int insertActivityByList(List<Activity> activityList);

    //根据查询市场活动（全部导出）
    List<Activity> selectAllActivityForDetail();

    //根据ids查询市场活动（选择导出）
    List<Activity> selectActivityForDetailByIds(String[] ids);

    //根据id查询市场活动的明细
    Activity selectActivityForDetailById(String id);

    //根据name模糊查询所有的市场活动
    List<Activity>  selectActivityForDetailByName(String name);

    //根据clueId查询与该clueId相关的市场活动
    List<Activity> selectActivityForDetailByClueId(String clueId);

    //与当前clueId不关联的市场活动
    List<Activity> selectActivityNoBoundById(Map<String,Object> map);

}
























