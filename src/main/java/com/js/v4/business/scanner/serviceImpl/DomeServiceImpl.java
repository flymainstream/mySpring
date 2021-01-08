package com.js.v4.business.scanner.serviceImpl;


import com.js.v4.business.service.DomeService;
import com.js.v4.framework.annotation.MyService;

/**
 * @author 刘锦涛
 * @title: DomeService
 * @projectName spring
 * @date 2021/1/8
 * @dateTime 19:26
 * @description: TODO
 */
@MyService
public class DomeServiceImpl implements DomeService {

    @Override
    public String handler(String str) {
        return "true ? "+str +"false ?";
    }
}
