package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 2021/8/10 0010
 */
@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;



    @RequestMapping("/workbench/clue/index.do")
    public String index(Model model){
       //所有的用户
       List<User> userList=userService.queryAllUsers();

       //称呼
       List<DicValue> appellationList=dicValueService.queryDicValueByTypeCode("appellation");

       //线索状态
       List<DicValue> clueStateList=dicValueService.queryDicValueByTypeCode("clueState");

       //线索来源
       List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");

       model.addAttribute("userList",userList);
       model.addAttribute("appellationList",appellationList);
       model.addAttribute("clueStateList",clueStateList);
       model.addAttribute("sourceList",sourceList);

       return "workbench/clue/index";
   }

   @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
       User user=(User)session.getAttribute(contants.SESSION_USER);
       clue.setId(UUIDUtils.getUUID());
       clue.setCreateBy(user.getId());
       clue.setCreateTime(DateUtils.formatDateTime(new Date()));

       ReturnObject returnObject=new ReturnObject();
       int ret=clueService.saveCreateClue(clue);
       if(ret>0){
           returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
       }else{
           returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("插入失败");
       }

       return returnObject;
   }

    //处理详情页面
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,Model model){
        //clueId找到clue对象
        Clue clue=clueService.queryClueForDetailById(id);
        //线索评论
        List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);
        //与该线索相关的市场活动
        List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);

        model.addAttribute("clue",clue);
        model.addAttribute("remarkList",remarkList);
        model.addAttribute("activityList",activityList);

        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryNoBoundActivityById.do")
    public @ResponseBody Object queryNoBoundActivityById(String activityName,String clueId){
        HashMap<String,Object> map=new HashMap();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList=activityService.queryActivityNoBoundById(map);
        return activityList;
    }


    //关联未关联的市场活动
    @RequestMapping("/workbench/clue/saveBundActivity.do")
    public @ResponseBody Object saveBundActivity(String[] activityId,String clueId){
        ClueActivityRelation relation=null;
        //集合保存relation
        List<ClueActivityRelation> relationList=new ArrayList<>();

        for(String ai:activityId){
            relation=new ClueActivityRelation();
            relation.setId(UUIDUtils.getUUID());
            relation.setClueId(clueId);
            relation.setActivityId(ai);
            relationList.add(relation);
        }

        ReturnObject returnObject=new ReturnObject();
        int ret=clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);

        if(ret>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
            //查询这次关联的市场活动
            List<Activity> activityList=activityService.queryActivityForDetailByIds(activityId);
            returnObject.setRetData(activityList);
        }else{
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("插入失败");
        }

        return returnObject;

    }

    @RequestMapping("/workbench/clue/searchActivity.do")
    @ResponseBody
    public Object searchActivity(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.queryActivityNoBoundById(map);

        return activityList;
    }

    //解除关联
    @RequestMapping("/workbench/clue/saveUnbundActivity.do")
    public @ResponseBody Object saveUnbundActivity(ClueActivityRelation relation){

        ReturnObject returnObject=new ReturnObject();
        int ret=clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);

        if(ret>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("解除关联失败");
        }

        return returnObject;

    }

    @RequestMapping("/workbench/clue/convertClue.do")
    public String convertClue(String id,Model model){
        Clue clue=clueService.queryClueForDetailById(id);
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");

        model.addAttribute("clue",clue);
        model.addAttribute("stageList",stageList);

        return "workbench/clue/convert";
    }

    //转换
    @RequestMapping("/workbench/clue/saveConvertClue.do")
    public @ResponseBody Object saveConvertClue(String clueId,String isCreateTran,String amountOfMoney,String tradeName,String expectedClosingDate,String stage,String activityId,HttpSession session){
        //System.out.println("ok");
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("clueId",clueId);
        map.put("isCreateTran",isCreateTran);
        map.put("amountOfMoney",amountOfMoney);
        map.put("tradeName",tradeName);
        map.put("expectedClosingDate",expectedClosingDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("sessionUser",session.getAttribute(contants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
        try{
            //调用业务层
            clueService.saveConvert(map);
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
        }

        return returnObject;
    }
}
