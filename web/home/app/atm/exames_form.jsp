<%@page import="model.Exame"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exame</title>
    </head>
    <body>

        <%@ include file="/home/app/modulos.jsp" %>

        <%
            Exame ex = null;

            String action = request.getParameter("action");
            if (action == null) {
                action = "create";
            } else {
                if (action.equals("update")) {

                    int id = Integer.valueOf(request.getParameter("id"));

                    ex = new Exame();
                    ex.setId(id);
                    ex.load();

                }
            }

        %>
        <h1>Exame</h1>

        <form action="<%= request.getContextPath()%>/home?action=<%= action%>&task=exames" method="post">

            <label for="id">Id:</label>
            <input type="text" id="id" name ="id" pattern="\d+" title="apenas dígitos" value="<%= (ex != null) ? ex.getId() : ""%>" <%= (ex != null) ? "readonly" : ""%> required><br/>   

            <label for="nome">Nome:</label>
            <input type="text" id="nome" name ="nome" value="<%= ((ex != null) && (ex.getNome() != null)) ? ex.getNome() : ""%>" required><br/>
            
            <label for="descricao">Descrição:</label>
            <textarea id="descricao" name="descricao" rows="4" cols="50" required><%= ((ex != null) && (ex.getDescricao() != null)) ? ex.getDescricao() : ""%></textarea><br/>            

            <label for="valor">Valor:</label>
            <input type="number" step="any" id="valor" name ="valor" title="apenas dígitos" value="<%= (ex != null) ? ex.getValor(): ""%>"required><br/>
            
            <input type="submit" name="Salvar" value="Salvar"> 
        </form>

    </body>
</html>
