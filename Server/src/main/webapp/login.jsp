<%--
  Created by IntelliJ IDEA.
  User: Corrado
  Date: 23/03/18
  Time: 15:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
    <title>Login Page</title>
</head>

<body>
<form action="/login" method="post">


    <%-- TODO: put parameters into Json Object!!! --%>
    Please enter your username
    <input type="text" name="user"/><br>

    Please enter your password
    <input type="text" name="pwd"/>

    <input type="submit" value="submit">

</form>
</body>
</html>