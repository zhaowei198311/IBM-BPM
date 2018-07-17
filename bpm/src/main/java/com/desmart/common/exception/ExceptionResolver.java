package com.desmart.common.exception;

import com.desmart.common.constant.ResponseCode;
import com.desmart.desmartbpm.service.impl.DhProcessDefinitionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOG.error("{} Exception", request.getRequestURI(), ex);
        ModelAndView modelAndView = new ModelAndView();
        if (ex instanceof PlatformException) {
            modelAndView.setViewName("desmartbpm/error");
            modelAndView.addObject("msg", ex.getMessage());
        } else {
            modelAndView.setViewName("desmartbpm/error");
        }
        return modelAndView;
    }




}