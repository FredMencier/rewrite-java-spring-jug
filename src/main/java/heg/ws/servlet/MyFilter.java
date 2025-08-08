package heg.ws.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//@WebFilter("/*")
public class MyFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        String secret = req.getParameter("secret");
        if ("123".equalsIgnoreCase(secret)) {
            filterChain.doFilter(req, resp);
        } else {
            ((HttpServletResponse) resp).setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
