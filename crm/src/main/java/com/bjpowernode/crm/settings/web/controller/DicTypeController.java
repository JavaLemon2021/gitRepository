package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 2021/8/2 0002
 */
@Controller
public class DicTypeController {

    @Autowired
    private DicTypeService dicTypeService;

    @RequestMapping("/settings/dictionary/type/index.do")
    public String index(Model model){
        //获取所有的字典类型
        List<DicType> dicTypeList=dicTypeService.queryAllDicTypes();
        model.addAttribute("dicTypeList",dicTypeList); //requests
        return "settings/dictionary/type/index";
    }

    @RequestMapping("/settings/dictionary/type/toSave.do")
    public String toSave(){
        return "settings/dictionary/type/save";
    }

    @RequestMapping("/settings/dictionary/type/checkCode.do")
    @ResponseBody
    public Object checkCode(String code){
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        ReturnObject returnObject = new ReturnObject();
        if(dicType==null){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("编码已经存在，请重新输入");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/saveCreateDicType.do")
    @ResponseBody
    public Object saveCreateDicType(DicType dicType){
        ReturnObject returnObject = new ReturnObject();
        int result = dicTypeService.saveCreateDicType(dicType);
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("插入信息失败");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/editDicType.do")
    public String editDicType(String code,Model model){
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        model.addAttribute("dicType",dicType);
        return "settings/dictionary/type/edit";
    }

    @RequestMapping("/settings/dictionary/type/saveEditDicType.do")
    @ResponseBody
    public Object saveEditDicType(DicType dicType){
        ReturnObject returnObject = new ReturnObject();
        int result = dicTypeService.saveEditDicType(dicType);
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/deleteDicTypeByCodes.do")
    @ResponseBody
    public Object deleteDicTypeByCodes(String[] code){
        ReturnObject returnObject = new ReturnObject();
        int result = dicTypeService.deleteDicTypeByCodes(code);
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }
}
