package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 2021/8/17 0017
 */
@Controller
public class ChartController {
    @Autowired
    TranService tranService;

    @RequestMapping("/workbench/chart/transaction/index.do")
    public String index(){
        return "workbench/chart/transaction/index";
    }

    @RequestMapping("/workench/chart/transaction/queryCountOfTranGroupByStage.do")
    public @ResponseBody Object queryCountOfTranGroupByStage(){
        List<FunnelVO> funnelVOList=tranService.queryCountOfTranGroupByStage();
        return funnelVOList;
    }
}
