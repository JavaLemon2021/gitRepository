package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 2021/8/6 0006
 */
@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(Model model){
        List<User> userList=userService.queryAllUsers();
        model.addAttribute("userList",userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){
        User user=(User)session.getAttribute(contants.SESSION_USER);
        //补充activity其它参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject=new ReturnObject();

        try{
            int ret=activityService.saveCreateActivity(activity);
            if(ret>0){
                returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("插入市场活动失败");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("插入市场活动失败");
        }

        return returnObject;
    }

    //多条件分页查询列表
    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
    public @ResponseBody Object queryActivityForPageByCondition(int pageNo,int pageSize,String name,String owner, String startDate,String endDate){
        Map<String,Object> map=new HashMap<>();
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        List<Activity> activityList=activityService.queryActivityForPageByCondition(map);
        long totalRows=activityService.queryCountOfActivityByCondition(map);

        //封装返回的数据
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);

        return retMap;
    }

    //跳到编辑页面
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody Object editActivity(String id){
        Activity activity=activityService.queryActivityById(id);
        return activity;
    }

    //更新
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity,HttpSession session){
        User user=(User)session.getAttribute(contants.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        ReturnObject returnObject=new ReturnObject();
        int ret=activityService.saveEditActivity(activity);
        if(ret>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }

        return returnObject;
    }

    //更新
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody Object deleteActivityByIds(String[] id){

        ReturnObject returnObject=new ReturnObject();
        int ret=activityService.deleteActivityByIds(id);
        if(ret>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,Model model){
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        if(activity.getEditBy()==null){
            activity.setEditBy("无");
        }
        model.addAttribute("remarkList", remarkList);
        model.addAttribute("activity", activity);

        return "workbench/activity/detail";
    }

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark activityRemark,HttpSession session){
        /*
        *
        *   private String editTime;
        *   private String editBy;
        *
        *           noteContent:noteContent,
                    activityId:activityId
        * */
        User user =(User)session.getAttribute(contants.SESSION_USER);
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag(contants.RETURN_OBJECT_CODE_FAIL);

        int result = activityRemarkService.saveCreateActivityRemark(activityRemark);
        ReturnObject returnObject = new ReturnObject();
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("插入失败");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        int result = activityRemarkService.deleteActivityRemarkById(id);
        ReturnObject returnObject = new ReturnObject();
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(String id,String noteContent,HttpSession session){
        ActivityRemark activityRemark = activityRemarkService.queryActivityRemarkById(id);
        User user = (User)session.getAttribute(contants.SESSION_USER);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        int result = activityRemarkService.saveEditActivityRemark(activityRemark);
        ReturnObject returnObject = new ReturnObject();
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }

    //导出（批量）
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletRequest request, HttpServletResponse response){
        List<Activity> activityList=activityService.queryAllActivityForDetail();
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("市场活动列表");
        //第一行，标题
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("名称");

        cell=row.createCell(1);
        cell.setCellValue("开始时间");

        cell=row.createCell(2);
        cell.setCellValue("结束时间");

        cell=row.createCell(3);
        cell.setCellValue("成本");

        cell=row.createCell(4);
        cell.setCellValue("描述");

        //样式
        HSSFCellStyle style=wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        if(activityList!=null){
            Activity activity=null;
            //循环市场活动对象集合，取出每个对象放到excel中每一行
            for(int i=0;i<activityList.size();i++){
                activity=activityList.get(i);
                //跳过标题，从第二行开始
                row=sheet.createRow(i+1);
                //每行的1列
                cell=row.createCell(0);
                cell.setCellValue(activity.getName());

                //每行的2列
                cell=row.createCell(1);
                cell.setCellValue(activity.getStartDate());

                //每行的3列
                cell=row.createCell(2);
                cell.setCellValue(activity.getEndDate());

                //每行的4列
                cell=row.createCell(3);
                cell.setCellValue(activity.getCost());

                //每行的5列
                cell=row.createCell(4);
                cell.setCellValue(activity.getDescription());
            }
        }
        OutputStream os=null;
        //通过浏览器下载excel img/jpeg 验证码 图片，服务器
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            //下载窗口以附件的形式来下载excel
            String flleName= URLEncoder.encode("市场活动表", "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="+flleName+".xls");
            os=response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //处理上传
    @RequestMapping("/workbench/activity/fileUpload.do")
    public @ResponseBody Object fileUpload(String username, MultipartFile myfile) throws Exception{
        //获取文件名
        String fileName=myfile.getOriginalFilename();
        File file=new File("d:\\testDir",fileName);
        //将文件输出到指定的目录
        myfile.transferTo(file);

        ReturnObject returnObject=new ReturnObject();
        returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);

        return returnObject;
    }

    //批量导入         workbench/activity/importActivity.do
    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile,String username,HttpSession session) throws Exception{
        User user=(User)session.getAttribute(contants.SESSION_USER);
        Map<String,Object> retMap=new HashMap<>();

        //创建一个集合来存放市场活动对象
        List<Activity> activityList=new ArrayList<>();
        InputStream is=activityFile.getInputStream();
        HSSFWorkbook wb=new HSSFWorkbook(is); //wb就是上传的excel
        HSSFSheet sheet=wb.getSheetAt(0);
        HSSFRow row=null;
        HSSFCell cell=null;
        Activity activity=null;

        //外层对行循环
        for(int i=1;i<=sheet.getLastRowNum();i++){
            row=sheet.getRow(i);
            activity=new Activity();
            activity.setId(UUIDUtils.getUUID());
            activity.setOwner(user.getId());
            activity.setCreateBy(user.getId());
            activity.setCreateTime(DateUtils.formatDateTime(new Date()));

            //内层对列循环
            for(int j=0;j<row.getLastCellNum();j++){
                cell=row.getCell(j);
                //获取单元格中的值
                String cellValue=getCellValue(cell);
                if(j==0){
                    activity.setName(cellValue);
                }else if(j==1){
                    activity.setStartDate(cellValue);
                }else if(j==2){
                    activity.setEndDate(cellValue);
                }else if(j==3){
                    activity.setCost(cellValue);
                }else if(j==4){
                    activity.setDescription(cellValue);
                }

            }
            activityList.add(activity);

        }

        int ret=activityService.saveCreateActivityByList(activityList);
        retMap.put("code",contants.RETURN_OBJECT_CODE_SUCCESS);
        retMap.put("count",ret);


        return retMap;
    }


    //根据单元格内容的类型，使用合适的get方法来获取单元格的值
    public static String getCellValue(HSSFCell cell){
        String ret="";
        switch(cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret=cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret=cell.getBooleanCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret=cell.getNumericCellValue()+"";
                break;
            default:
                ret="";

        }

        return ret;
    }
}
