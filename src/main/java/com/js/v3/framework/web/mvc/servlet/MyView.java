package com.js.v3.framework.web.mvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author name
 * @date 2020/12/30
 * @dateTime 21:29
 * @description:
 */
public class MyView {

    private File viewFile;

    public MyView(File viewFile) {
        this.viewFile = viewFile;
    }

    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        StringBuilder sb = new StringBuilder();

        RandomAccessFile ra = new RandomAccessFile(viewFile, "r");

        String line = "";
        while (null != (line = ra.readLine())) {
            String str = new String(
                    line.getBytes("ISO-8859-1"), "UTF-8");

            Pattern pattern = Pattern.compile("\\^\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);

            while (matcher.find()) {
                String group = matcher.group();
                String paraName = group.replaceAll("\\^\\{|\\}", "");
                Object paramVlue = model.get(paraName);
                line = paramVlue.toString();
                matcher = pattern.matcher(line);
            }
            sb.append(line);

        }
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(sb.toString());
    }
}
