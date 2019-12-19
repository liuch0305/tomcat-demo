package com.lch.tomcat.index;

import com.alibaba.fastjson.JSON;
import com.lch.tomcat.HttpServelt;
import com.lch.tomcat.exception.ServletException;
import com.lch.tomcat.http.HttpServletRequest;
import com.lch.tomcat.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: liuchenhui
 * @create: 2019-12-16 15:56
 **/
@Slf4j
public class IndexServelt extends HttpServelt {
    @Override
    protected void init() {
        log.info("init indexServelt");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap == null || parameterMap.size() == 0) {
            resp.write("no params");
            return;
        }
        resp.write(JSON.toJSON(parameterMap));
    }
}
