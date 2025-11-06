/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.TipoUsuario;

/**
 * Filtro responsável por controlar o acesso aos módulos
 * Administrativo, Agendamento e Atendimento.
 * 
 * Baseado no filtro de login já existente no sistema.
 */
public class AutorizadorModulo implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nenhuma inicialização necessária
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession sessao = req.getSession(false);

        // Verifica se existe usuário logado
        if (sessao == null || sessao.getAttribute("tipo_usuario") == null) {
            req.setAttribute("msg", "É necessário fazer login para acessar o sistema.");
            req.getRequestDispatcher("/home/login.jsp").forward(request, response);
            return;
        }

        TipoUsuario tipo = (TipoUsuario) sessao.getAttribute("tipo_usuario");
        String uri = req.getRequestURI();
        String context = req.getContextPath();
        String path = uri.substring(context.length());

        // --- Verificação de módulo ADMINISTRATIVO ---
        if (path.startsWith("/home/app/adm/tipousuario") || path.startsWith("/home/app/adm/usuarios")) {
            if (!"S".equals(tipo.getModuloAdministrativo())) {
                req.setAttribute("msg", "Acesso negado ao módulo Administrativo.");
                req.getRequestDispatcher("/home/app/modulos.jsp").forward(request, response);
                return;
            }
        }

        // --- Verificação de módulo AGENDAMENTO ---
        if (path.contains("/agendamento") || path.contains("/agenda")) {
            if (!"S".equals(tipo.getModuloAgendamento())) {
                req.setAttribute("msg", "Acesso negado ao módulo de Agendamento.");
                req.getRequestDispatcher("/home/app/adm/modulos.jsp").forward(request, response);
                return;
            }
        }

        // --- Verificação de módulo ATENDIMENTO ---
        if (path.contains("/atendimento") || path.contains("/atend")) {
            if (!"S".equals(tipo.getModuloAtendimento())) {
                req.setAttribute("msg", "Acesso negado ao módulo de Atendimento.");
                req.getRequestDispatcher("/home/app/adm/modulos.jsp").forward(request, response);
                return;
            }
        }

        // Se todas as verificações forem aprovadas, continua a requisição
        chain.doFilter(request, response);
    }

   
}
