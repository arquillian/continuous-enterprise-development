package org.cedj.geekseek.service.security.picketlink;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.picketlink.annotations.PicketLink;

@RequestScoped
public class HttpObjectHolder {

    private HttpServletResponse response;
    private HttpServletRequest request;

    public void setup(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Produces @PicketLink
    public HttpServletResponse getResponse() {
        return response;
    }

    @Produces @PicketLink
    public HttpServletRequest getRequest() {
        return request;
    }
}
