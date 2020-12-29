package com.js.v3.business;

import com.js.v3.framework.annotation.MyController;
import com.js.v3.framework.annotation.MyRequestMapping;
import com.js.v3.framework.web.mvc.servlet.MyModelAndView;

import java.util.HashMap;

/**
 * @author name
 * @date 2020/12/29
 * @dateTime 21:46
 * @description:
 */
@MyController()
public class DomeController {


    @MyRequestMapping("/dome*")
    public MyModelAndView dome() {

        HashMap<String, String> echoMap = new HashMap<>();
        echoMap.put("Sophie","Jimmy");
        return new MyModelAndView("index",echoMap);
    }
}
