package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * 2021/8/3
 */
@Controller
public class DicValueController {

    @Autowired
    DicValueService dicValueService;

    @Autowired
    DicTypeService dicTypeService;

    @RequestMapping("/settings/dictionary/value/index.do")
    public String toValueIndex(Model model){
        List<DicValue> dicValues = dicValueService.queryAllValue();
        model.addAttribute("dicValueList", dicValues);
        return "settings/dictionary/value/index";
    }

    @RequestMapping("/settings/dictionary/value/toSave.do")
    public String toSave(Model model){
        List<DicType> dicTypes = dicTypeService.queryAllDicTypes();
        model.addAttribute("dicTypeList",dicTypes);
        return "settings/dictionary/value/save";
    }

    @RequestMapping("/settings/dictionary/value/saveCreateDicValue.do")
    @ResponseBody
    public Object saveCreateDicValue(DicValue dicValue){
        ReturnObject returnObject = new ReturnObject();
        UUID uuid = UUID.randomUUID();
        dicValue.setId(uuid.toString().replace("-", "").substring(0, 32));
        int result = dicValueService.saveCreatValue(dicValue);
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("添加失败");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/value/editDicValue.do")
    public String editDicValue(String id,Model model){
        DicValue dicValue = dicValueService.queryValueById(id);
        model.addAttribute("dicValue", dicValue);
        return "settings/dictionary/value/edit";
    }

    @RequestMapping("/settings/dictionary/value/saveEditDicValue.do")
    @ResponseBody
    public Object saveEditDicValue(DicValue dicValue){
        ReturnObject returnObject = new ReturnObject();
        int result = dicValueService.saveEditValue(dicValue);
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/value/deleteDicValueByIds.do")
    @ResponseBody
    public Object deleteDicValueByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        int result = dicValueService.deleteValueById(id);
        if(result>0){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }
}
