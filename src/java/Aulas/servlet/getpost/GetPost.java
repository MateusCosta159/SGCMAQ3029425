package Aulas.servlet.getpost;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class GetPost extends HttpServlet {

    ArrayList<InformacaoFormulario> dados;

    @Override
    public void init() throws ServletException {
        super.init();
        dados = new ArrayList<>();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String campoA = request.getParameter("campoA");
        String opcaoA = request.getParameter("opcaoA");
        String opcaoB = request.getParameter("opcaoB");

        System.out.println(" campoA : " + campoA);
        System.out.println(" opcaoA : " + opcaoA);
        System.out.println(" opcaoB : " + opcaoB);

        if (campoA != null) {
            InformacaoFormulario info = new InformacaoFormulario();
            info.setCampoA(campoA);
            info.setOpcaoA(opcaoA);
            info.setOpcaoB(opcaoB);

            dados.add(info);

        }
        
        //Vai gerar uma poha de uma nova HTTP vinda doc cliente(novos cabeçalhos de requisição e resposta)  

//        response.sendRedirect("/SGCMAQ3029425/aulas/servlet/getpost/getpost_form.html");
        //??
       

        String html = "<!DOCTYPE html>";
        html += "<html>";
        html += "<head>";
        html += "<title>Dados do formulario</title>";
        html += "</head>";
        html += "<body>";
        html += "<h1>Dados do Fomulario</h1>";
        
        html += "<table>";
        html += "<tr>";
        html += "<th> CampoA </th>";
        html += "<th> Opção </th>";
        html += "<th> Opção </th>";
        html += "</tr>";
        
        for(InformacaoFormulario info : dados){
            html += "<tr>";
            html += "<td>" + info.getCampoA() + "</td>";
            html += "<td>" + info.getOpcaoA() + "</td>";
            html += "<td>" + info.getOpcaoB() + "</td>";
            
            html += "</tr>";
        }
        
        html += "</table></br></br>";
        
        html += "<a href=\"/SGCMAQ3029425/aulas/servlet/getpost/getpost_form.html\">Adicionar Informação </a>";
        
        html += "</body>";
        html += "</html>";
        
         response.setContentType("text/html;charset=UTF-8");

        PrintWriter pw = response.getWriter();
        pw.write(html);
        pw.close();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        dados.clear();
        dados = null;
    }

}
