<%@page import="model.Exame"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exames</title>
    </head>
    <body>
        
        <%@ include file="/home/app/modulos.jsp" %>
        
        <% ArrayList<Exame> dados = new Exame().getAllTableEntities(); %>
        
        <h1>Exame</h1>
        
        <table>
            
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Valor R$</th>
                <th></th>
                <th></th>
            </tr>
            
            <% for( Exame cv : dados ) { %>
            <tr>
                <td><%= cv.getId() %></td>
                <td><%= cv.getNome() %></td>
                <td><%= cv.getValor() %></td>
                
                
                <td><a href="<%= request.getContextPath()  %>/home/app/atm/exames_form.jsp?action=update&id=<%= cv.getId()%>" >Alterar</a></td>
                
                <td><a href="<%= request.getContextPath() %>/home?action=delete&id=<%= cv.getId()%>&task=exames" onclick="return confirm('Deseja realmente excluir Exame <%= cv.getId()%> (<%= cv.getNome() %>) ?')">Excluir</a></td>
            </tr>
            <% } %>
            
        </table>
        
            <a href="<%= request.getContextPath()  %>/home/app/atm/exames_form.jsp?action=create" >Adicionar</a>
    </body>
</html>
