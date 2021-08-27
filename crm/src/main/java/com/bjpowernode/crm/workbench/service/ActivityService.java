package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * 2021/8/6 0006
 */
public interface ActivityService {
    //保存市场活动
    int saveCreateActivity(Activity activity);
    //分页列表（多条件）查询
    List<Activity> queryActivityForPageByCondition(Map<String,Object> map);
    //记录数
    long queryCountOfActivityByCondition(Map<String,Object> map);
    //按id去查询市场活动对象
    Activity queryActivityById(String id);
    //更新
    int saveEditActivity(Activity activity);
    //删除
    int deleteActivityByIds(String[] ids);
    //批量导出
    List<Activity> queryAllActivityForDetail();
    //选择导出
    List<Activity> queryActivityForDetailByIds(String[] ids);
    //批量导入
    int saveCreateActivityByList(List<Activity> activityList);
    //市场活动详情
    Activity queryActivityForDetailById(String id);
    //根据name模糊查询
    List<Activity> queryActivityForDetailByName(String name);
    //根据clueId找到相关的市场活动
    List<Activity> queryActivityForDetailByClueId(String clueId);
    //根据clueId找不相关的市场活动
    List<Activity> queryActivityNoBoundById(Map<String,Object> map);
}
