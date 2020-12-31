package com.js.v3.business;

import com.js.v3.framework.annotation.MyController;
import com.js.v3.framework.annotation.MyRequestMapping;
import com.js.v3.framework.annotation.MyRequestParam;
import com.js.v3.framework.web.mvc.servlet.MyModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        echoMap.put("Jimmy", "Jimmy");
        echoMap.put("Sophie", "Sophie");
        return new MyModelAndView("index", echoMap);
    }

    @MyRequestMapping("/other.love")
    public MyModelAndView makeSimple(
            @MyRequestParam("name") String name
            , HttpServletRequest request
            , HttpServletResponse response
    ) {

        HashMap<String, String> echoMap = new HashMap<>();

        echoMap.put("name", name);
        echoMap.put("Jimmy", "Jimmy");
        echoMap.put("Sophie", "Sophie");
        return new MyModelAndView("more", echoMap);
    }
}
