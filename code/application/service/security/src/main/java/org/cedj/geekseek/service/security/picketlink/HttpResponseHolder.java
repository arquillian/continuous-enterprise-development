package org.cedj.geekseek.service.security.picketlink;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletResponse;

@RequestScoped
public class HttpResponseHolder {

    private HttpServletResponse response;

    public void setup(HttpServletResponse response) {
        this.response = response;
    }

    @Produces
    public HttpServletResponse getResponse() {
        return response;
    }
}
