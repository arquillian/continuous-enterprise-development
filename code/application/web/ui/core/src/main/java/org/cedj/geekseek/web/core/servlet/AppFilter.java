package org.cedj.geekseek.web.core.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(
    urlPatterns = {"/app/*"},
    initParams = {
        @WebInitParam(name = "pattern", value = ".*((styles|scripts|images)\\/.*)$")
    })
public class AppFilter implements Filter {

    private static String APP = "/app/";
    private static String APP_INDEX = APP + "index.jsp";

    private static Pattern letThroughExpression;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String patternExp = filterConfig.getInitParameter("pattern");
        letThroughExpression = Pattern.compile(patternExp);
        System.out.println("AppFitlerPattern: " + letThroughExpression.pattern());
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        request.setAttribute("BASE_ROOT", request.getContextPath() + APP_INDEX);

        String requestPath = getResourceRequestPath(request);

        System.out.println(requestPath);
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        String[] paths = requestPath.split("/");
        URL resourceURL = cl.getResource(paths[paths.length-1]);

        Matcher matcher = letThroughExpression.matcher(requestPath);
        if(matcher.matches()) {
            String redirected = "/" + matcher.group(1);
            //System.out.println("Forwarding to: " + redirected);
            request.getRequestDispatcher(redirected).forward(request, response);
        } else if(resourceURL != null && !requestPath.isEmpty()) {
            //System.out.println("Loaded from: " + resourceURL);
            InputStream input = resourceURL.openStream();
            OutputStream output = response.getOutputStream();
            int r;
            while( (r = input.read()) != -1) {
                output.write(r);
            }
            output.close();
            input.close();
        } else {
            //System.out.println("Forwarding to: " + APP_INDEX);
            request.getRequestDispatcher(APP_INDEX).forward(request, response);
        }
    }

    //  /context-path/app-root/resource
    private String getResourceRequestPath(HttpServletRequest request) {
        String contextPath = request.getServletContext().getContextPath();
        String path = request.getRequestURI().replace(contextPath + APP, "");
        return path;
    }
}
