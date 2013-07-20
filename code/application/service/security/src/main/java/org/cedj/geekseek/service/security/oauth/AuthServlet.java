package org.cedj.geekseek.service.security.oauth;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cedj.geekseek.service.security.picketlink.HttpResponseHolder;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.authentication.AuthenticationException;

@WebServlet(urlPatterns={"/auth"})
public class AuthServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String SESSION_REDIRECT = "auth_redirect";
    private static final String REFERER = "Referer";
    private static final String LOCATION = "Location";

    @Inject // need to produce a Response so it can be used by the Authenticator
    private HttpResponseHolder holder;

    @Inject
    private Identity identity;

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws IOException,
        ServletException {

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        HttpSession session = request.getSession();
        holder.setup(response);

        if(!identity.isLoggedIn()) {
            if(session.getAttribute(SESSION_REDIRECT) == null) {
                session.setAttribute(SESSION_REDIRECT, request.getHeader(REFERER));
            }

            try {
                AuthenticationResult status = identity.login();
                if(status == AuthenticationResult.FAILED) {
                    if(response.getStatus() == 302) { // Authenticator is requesting a redirect
                        return;
                    }
                    response.setStatus(400);
                    response.getWriter().append("FAILED");
                } else {
                    String url = String.valueOf(request.getSession().getAttribute(SESSION_REDIRECT));
                    response.setStatus(302);
                    response.setHeader(LOCATION, url);
                    request.getSession().removeAttribute(SESSION_REDIRECT);
                }
            } catch(AuthenticationException e) {
                response.setStatus(400);
                response.getWriter().append(e.getMessage());
            }
        }
        else {
            response.setStatus(302);
            response.setHeader("Location", request.getHeader("Referer"));
            response.getWriter().append("ALREADY_LOGGED_IN");
        }
    }
}
