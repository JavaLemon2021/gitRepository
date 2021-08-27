package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 2021/8/14 0014
 */
@Controller
public class TranController {

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;


    @RequestMapping("/workbench/transaction/typeahead.do")
    public @ResponseBody Object typeahead(String query){
        List<Customer> customerList=new ArrayList<>();

        Customer customer=new Customer();
        customer.setId("001");
        customer.setName("动力节点");
        customerList.add(customer);

        customer=new Customer();
        customer.setId("002");
        customer.setName("字节跳动");
        customerList.add(customer);

        customer=new Customer();
        customer.setId("003");
        customer.setName("节节高");
        customerList.add(customer);

        return customerList;

    }

    @RequestMapping("/workbench/transaction/index.do")
    public String index(){
        return "workbench/transaction/index";
    }

    //跳到save页面
    @RequestMapping("/workbench/transaction/createTran.do")
    public String createTran(Model model){
        List<User> userList=userService.queryAllUsers();
        //阶段
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");

        model.addAttribute("userList",userList);
        model.addAttribute("stageList",stageList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        return "workbench/transaction/save";
    }

    //自动补全
    @RequestMapping("/workbench/transaction/queryCustomerByName.do")
    public @ResponseBody Object queryCustomerByName(String customerName){
        List<Customer> customerList=customerService.queryCustomerByName(customerName);
        return customerList;
    }

    //可能性与分值的对应
    @RequestMapping("/workbench/transaction/getPossibilityByStageValue.do")
    public @ResponseBody Object getPossibilityByStageValue(String stageValue){
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(stageValue);
        return possibility;
    }

    //保存
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(Tran tran, String customerName, HttpSession session){
        User user=(User)session.getAttribute(contants.SESSION_USER);
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));

        Map<String,Object> map=new HashMap<>();
        map.put("tran",tran);
        map.put("customerName",customerName);
        map.put("sessionUser",user);

        ReturnObject returnObject=new ReturnObject();

        //调业务层完成事务
        boolean flag=tranService.saveCreateTran(map);
        if(flag){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
        }


        return returnObject;
    }

    //交易详情
    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,Model model){
        //交易阶段
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        //交易详情
        Tran tran=tranService.queryTranForDetailById(id);
        //交易备注
        List<TranRemark> tranRemarkList=tranRemarkService.queryTranRemarkForDetailByTranId(id);
        //交易历史
        List<TranHistory> tranHistoryList=tranHistoryService.queryTranHistoryForDetailByTranId(id);

        //取可能性对应的分值
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possiblity=bundle.getString(tran.getStage());
        tran.setPossibility(possiblity);

        model.addAttribute("stageList",stageList);
        model.addAttribute("tran",tran);
        model.addAttribute("tranRemarkList",tranRemarkList);
        model.addAttribute("tranHistoryList",tranHistoryList);

        TranHistory tranHistory=tranHistoryList.get(tranHistoryList.size()-1);
        model.addAttribute("theOrderNo",tranHistory.getOrderNo());


        return "workbench/transaction/detail";
    }
}
