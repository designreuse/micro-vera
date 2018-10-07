package com.vera.acl.controller;

import com.vera.shared.model.Rest;
import com.vera.shared.model.RestCode;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局的异常捕获
 * @author ychost
 * @date 2018/10/6
 */
@ControllerAdvice
public class ExpHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Rest<Exception> exceptionHandler(HttpServletRequest request,  Exception e){
        var rest = Rest.<Exception>create();
        rest.with(RestCode.exeception,"未处理异常",e);
        return rest;
    }
}
