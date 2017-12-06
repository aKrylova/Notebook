<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/notebook/addUser">Добавить нового пользователя</a>
<a href="/notebook/addPhone">Добавить номер телефона </a>
<br>
<%
    Map<String, ArrayList<String>> nt = (Map<String, ArrayList<String>>) request.getAttribute("notes");
    for (Map.Entry<String, ArrayList<String>> entry :  nt.entrySet()) {
%>
Name:  <%=  entry.getKey() %><br>
Phones: <%= entry.getValue().toString() %><br>
<%    }
%>
<a href="/notebook/sort">Отсортировать по имени</a>
</body>
</html>
