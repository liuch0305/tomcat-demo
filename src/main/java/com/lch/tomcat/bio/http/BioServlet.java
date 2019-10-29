package com.lch.tomcat.bio.http;

/**
 * @author: liuchenhui
 * @create: 2019-10-28 18:39
 **/
public abstract class BioServlet {

    void service(BioRequest request, BioResponse response) {
        if ("GET".equals(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    protected abstract void doPost(BioRequest request, BioResponse response);

    protected abstract void doGet(BioRequest request, BioResponse response);
}
