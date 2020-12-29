package com.js.v3.business;

import com.js.v3.framework.annotation.MyController;
import com.js.v3.framework.annotation.MyRequestMapping;
import com.js.v3.framework.web.mvc.servlet.MyModelAndView;

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

        return null;
    }
}
