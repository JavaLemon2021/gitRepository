package com.bjpowernode.crm.settings.web.interceptor;

import com.bjpowernode.crm.commons.contants.contants;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.security.pkcs11.wrapper.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>NAME: LoginInterceptor</p>
 * @author Administrator
 * @date 2020-05-16 17:27:52
 * @version 1.0
 */
public class LoginInterceptor  implements HandlerInterceptor{


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute(contants.SESSION_USER);
        String path=request.getContextPath();   
        if(user==null){
            //没有登录
            response.sendRedirect(request.getContextPath());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
