package org.cedj.geekseek.web.rest.core.interceptor;

import javax.interceptor.InvocationContext;

public interface RESTInterceptor {

    int getPriority();

    Object invoke(InvocationContext context) throws Exception;
}
