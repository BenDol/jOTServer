package org.jotserver.web.secure;


import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import net.sourceforge.stripes.util.StringUtil;

public class LoginSecurityFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        if (request.getSession().getAttribute("userAccount") != null) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/Login.action");
        }
    }
    
    public void destroy() {
        //
    }
}