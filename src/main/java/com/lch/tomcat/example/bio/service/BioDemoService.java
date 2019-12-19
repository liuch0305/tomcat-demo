package com.lch.tomcat.example.bio.service;

import com.lch.tomcat.example.bio.http.BioRequest;
import com.lch.tomcat.example.bio.http.BioResponse;
import com.lch.tomcat.example.bio.http.BioServlet;

/**
 * @author: liuchenhui
 * @create: 2019-10-29 23:55
 **/
public class BioDemoService extends BioServlet {

    @Override
    protected void doPost(BioRequest request, BioResponse response) {
        response.write("helloWorld");
    }

    @Override
    protected void doGet(BioRequest request, BioResponse response) {
        doPost(request, response);
    }
}
