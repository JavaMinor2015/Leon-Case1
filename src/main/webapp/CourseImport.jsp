<%@ page import="nl.stoux.minor.domain.Course" %>
<%--
  Created by IntelliJ IDEA.
  User: Stoux
  Date: 06/10/2015
  Time: 18:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Courses Import - Case 1 Demo</title>
</head>
<body>

<h2>Demo: Import Courses</h2>

<form enctype="multipart/form-data" action="/case1/api/v1<%=Course.URL%>/ImportCourses" method="POST">
    <input type="file" name="file" />
    <button type="submit">Import</button>
</form>

</body>
</html>
