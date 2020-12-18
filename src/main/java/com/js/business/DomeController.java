package com.js.v1.business;

import com.js.v1.MyAutoWired;
import com.js.v1.MyController;
import com.js.v1.MyRequestMapping;
import com.js.v1.business.service.DomeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 刘锦涛
 * @title: DomeController
 * @projectName spring
 * @date 2020/12/18
 * @dateTime 16:43
 * @description: TODO
 */
@MyController("")
public class DomeController {

    @MyAutoWired
    private DomeService domeService;

    @MyRequestMapping("/dome")
    public void bole(
            HttpServletRequest request,
            HttpServletResponse response,
            String name) throws IOException {

        String s = domeService.boleAdd(name);

        response.getWriter().write(s);
    }

}
