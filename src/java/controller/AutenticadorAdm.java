
package controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;


public class AutenticadorAdm {
    private static final boolean debug = true;

    private FilterConfig filterConfig = null;

    public AutenticadorAdm() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        
        HttpSession sessao = httpServletRequest.getSession(false);
        
     
        
        if((sessao == null) || (sessao.getAttribute("usuario") == null ) || (sessao.getAttribute("tipo_usuario") == null) ) {
            httpServletRequest.setAttribute("msg", "faça o login");
            httpServletRequest.getRequestDispatcher("/home/login.jsp").forward(request, response);
        }else{
            //se logado, continue
           chain.doFilter(request, response);
        }

        

    }
}
