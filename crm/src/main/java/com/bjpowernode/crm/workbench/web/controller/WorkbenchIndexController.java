package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 2021/8/1 0001
 */
@Controller
public class WorkbenchIndexController {

    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }
}
