package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.provider.MD5;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 2021/7/30 0030
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //进入登录页面
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(HttpServletRequest request){
         Cookie[] cookies=request.getCookies(); //获取浏览器上所有cookie
         String loginAct=null;
         String loginPwd=null;
         for(Cookie cookie:cookies){
            String name=cookie.getName();
            if("loginAct".equals(name)){
                loginAct=cookie.getValue();
                continue;
            }
            if("loginPwd".equals(name)){
                loginPwd=cookie.getValue();
            }
         }

         if(loginAct!=null&&loginPwd!=null){
            Map<String,Object> map=new HashMap<>();
            map.put("loginAct",loginAct);
            map.put("loginPwd", MD5Util.getMD5(loginPwd));
             //还要做数据库验证
            User user=userService.queryUserByLoginActAndPwd(map);
            request.getSession().setAttribute(contants.SESSION_USER,user);
            //成功
             return "redirect:/workbench/index.do";
        }else{
             return "settings/qx/user/login";
         }


    }

    //登录
    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody
    Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){

        Map<String,Object> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd", MD5Util.getMD5(loginPwd));

        User user=userService.queryUserByLoginActAndPwd(map);
        String ip=request.getRemoteAddr();
        System.out.println(ip);

        //统一返回结果类型
        ReturnObject returnObject=new ReturnObject();
        if(user==null){
            returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else{
            if(DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime())>0){
                returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已经过期");
            }else if("0".equals(user.getLockState())){
                returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已经被锁定");
            }/*else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                returnObject.setCode(contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限制");*/
            else{
                returnObject.setCode(contants.RETURN_OBJECT_CODE_SUCCESS);
                session.setAttribute(contants.SESSION_USER,user);

                //免登录
                if("true".equals(isRemPwd)){
                    //生成保存用户名cookie
                    Cookie c1=new Cookie("loginAct",loginAct);
                    c1.setMaxAge(10*24*60*60);//保存10天
                    response.addCookie(c1);

                    //生成保存密码okie
                    Cookie c2=new Cookie("loginPwd",loginPwd);
                    c2.setMaxAge(10*24*60*60);//保存10天
                    response.addCookie(c2);
                }
            }
        }

        return returnObject;
    }


    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookie,创建一个同名的cookie去覆盖原来的cookie
        Cookie c1=new Cookie("loginAct",null);
        c1.setMaxAge(0);
        response.addCookie(c1);

        Cookie c2=new Cookie("loginPwd",null);
        c2.setMaxAge(0);
        response.addCookie(c2);

        //销毁session
        session.invalidate();


        return "redirect:/"; //http://localhost:8080/crm/->login.jsp
    }
}
