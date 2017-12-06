<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="/notebook/addNewPhone" method="post">
    <%
        HashMap<String, ArrayList<String>> nt = (HashMap<String, ArrayList<String>>) request.getAttribute("notes");
    %>
    <p><select name="nameUser">
        <%    for (Map.Entry<String, ArrayList<String>> entry :  nt.entrySet()) {
        %>
        <option value="<%= entry.getKey() %>"><%= entry.getKey()%> </option>
        <%
            }
        %>
    </select></p>
    <p>

        Your phone:
        <input type="text" size="50" name="newnumberPhones">
    </p>
    <input type="submit" value="Submit">
</form>
</body>
</html>
