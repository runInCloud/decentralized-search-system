<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Registration page</title>

    <spring:url value="/resources" var="resourcesUrl"/>
    <link href="${resourcesUrl}/bootstrap.min.css" rel="stylesheet">
    <link href="${resourcesUrl}/signin.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="${resourcesUrl}/ie8-responsive-file-warning.js"></script><![endif]-->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div class="container">
    <form:form method="POST" modelAttribute="account" cssClass="form-signin">
        <h2 class="form-signin-heading">Registration form</h2>
        <form:input path="name" id="name" cssClass="form-control" placeholder="User name"/>
        <form:input path="email" id="email" type="email" cssClass="form-control" placeholder="Email address"/>
        <form:input path="password" id="password" type="password" cssClass="form-control" placeholder="Password"/>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Register</button>
        <ul>
            <li><form:errors path="name"/></li>
            <li><form:errors path="email"/></li>
            <li><form:errors path="password"/></li>
        </ul>
    </form:form>
</div>
<!-- /container -->
</body>
</html>