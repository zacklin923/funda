package com.qchery.funda.modules.sys.controller;

import com.qchery.funda.Result;
import com.qchery.funda.enums.ResultCode;
import com.qchery.funda.modules.sys.entity.User;
import com.qchery.funda.modules.sys.model.UserModel;
import com.qchery.funda.props.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    public static final ThreadLocal<Object> MODEL_HOLDER = new ThreadLocal<>();

    @Autowired
    private SystemProperties systemProperties;

    @RequestMapping("list")
    public Result list() {
        return new Result(ResultCode.SUCCESS, systemProperties.getUsers());
    }

    @RequestMapping("login")
    public Result login(@RequestBody @Valid UserModel userModel) {
        User existUser = null;
        for (User user : systemProperties.getUsers()) {
            if (user.getUsername().equals(userModel.getUsername())) {
                existUser = user;
            }
        }

        if (existUser == null || !existUser.getPassword().equals(userModel.getPassword())) {
            return new Result(ResultCode.PASSWORD_ERROR);
        }

        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        return new Result(ResultCode.SUCCESS, user);
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        MODEL_HOLDER.set(webDataBinder.getTarget());
    }

}
