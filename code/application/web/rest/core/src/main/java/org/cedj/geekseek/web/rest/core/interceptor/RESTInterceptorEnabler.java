package org.cedj.geekseek.web.rest.core.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.cedj.geekseek.web.rest.core.annotation.REST;

/**
 * Custom Interceptor chain for the REST layer.
 *
 * To avoid having to duplicate the beans.xml configuration of interceptors
 * for each module that would want to use them(*-rest-*), and open for
 * dynamic discovery of interceptors; we create one single interceptor
 * that is defined in the beans.xml of the rest modules and use CDI
 * as a discovery mechanism for out own Interceptors.
 *
 * We rely on a very simple Priority based integer to order the interceptor
 * chain.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 */
@REST
@Interceptor
public class RESTInterceptorEnabler {

    @Inject
    private Instance<RESTInterceptor> instances;

    @AroundInvoke
    public Object intercept(final InvocationContext context) throws Exception {
        final List<RESTInterceptor> interceptors = sort(instances);
        InvocationContext wrapped = new InvocationContext() {
            // we start by incrementing in the loop, so start with -1
            final AtomicInteger current = new AtomicInteger(-1);

            @Override
            public void setParameters(Object[] params) throws IllegalStateException, IllegalArgumentException {
                context.setParameters(params);
            }

            @Override
            public Object getTimer() {
                return context.getTimer();
            }

            @Override
            public Object getTarget() {
                return context.getTarget();
            }

            @Override
            public Object[] getParameters() throws IllegalStateException {
                return context.getParameters();
            }

            @Override
            public Method getMethod() {
                return context.getMethod();
            }

            @Override
            public Map<String, Object> getContextData() {
                return context.getContextData();
            }

            @Override
            public Object proceed() throws Exception {
                int pos = current.incrementAndGet();
                if(interceptors.size() == pos) {
                    return context.proceed();
                }
                RESTInterceptor interceptor = interceptors.get(pos);
                return interceptor.invoke(this);
            }

        };
        return wrapped.proceed();

    }

    private List<RESTInterceptor> sort(Instance<RESTInterceptor> rawInterceptors) {
        List<RESTInterceptor> interceptors = new ArrayList<RESTInterceptor>();
        for(RESTInterceptor interceptor : rawInterceptors) {
            interceptors.add(interceptor);
        }
        Collections.sort(interceptors, new Comparator<RESTInterceptor>(){

            @Override
            public int compare(RESTInterceptor o1, RESTInterceptor o2) {
                return new Integer(o2.getPriority()).compareTo(new Integer(o1.getPriority()));
            }
        });
        return interceptors;
    }

}
