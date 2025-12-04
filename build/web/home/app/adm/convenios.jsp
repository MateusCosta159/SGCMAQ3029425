<%@page import="model.Convenio"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Convenio</title>
    </head>
    <body>
        
        <%@ include file="/home/app/modulos.jsp" %>
        
        <% ArrayList<Convenio> dados = new Convenio().getAllTableEntities(); %>
        
        <h1>Convenio</h1>
        
        <table>
            
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>CNPJ</th>
                <th>Telefone</th>
                <th>Valor R$</th>
                <th></th>
                <th></th>
            </tr>
            
            <% for( Convenio cv : dados ) { %>
            <tr>
                <td><%= cv.getId() %></td>
                <td><%= cv.getNome() %></td>
                <td><%= cv.getCnpj() != null ? cv.getCnpj() : "-"  %></td>
                <td><%= cv.getTelefone() != null ? cv.getTelefone() : "-"  %></td>
                <td><%= cv.getValor() %></td>
                
                
                <td><a href="<%= request.getContextPath()  %>/home/app/adm/convenios_form.jsp?action=update&id=<%= cv.getId()%>" >Alterar</a></td>
                
                <td><a href="<%= request.getContextPath() %>/home?action=delete&id=<%= cv.getId()%>&task=convenios" onclick="return confirm('Deseja realmente excluir Convenio <%= cv.getId()%> (<%= cv.getNome() %>) ?')">Excluir</a></td>
            </tr>
            <% } %>
            
        </table>
        
            <a href="<%= request.getContextPath()  %>/home/app/adm/convenios_form.jsp?action=create" >Adicionar</a>
    </body>
</html>
