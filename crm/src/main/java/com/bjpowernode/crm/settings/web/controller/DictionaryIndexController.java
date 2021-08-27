package com.bjpowernode.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 2021/8/2 0002
 */
@Controller
public class DictionaryIndexController {

    @RequestMapping("/settings/dictionary/index.do")
    public String index(){
        return "settings/dictionary/index";
    }
}
