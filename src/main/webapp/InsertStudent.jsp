<%--
  Created by IntelliJ IDEA.
  User: Stoux
  Date: 07/10/2015
  Time: 03:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Insert Student</title>
</head>
<body>

<h2>Insert student</h2>

<form action="api/v1/Students/" method="POST">
    <input type="text" name="forename" /> <br />
    <input type="text" name="surname" /> <br />
    <input type="text" name="branch" /> <br />
    <input type="text" name="company_name" /> <br />
    <input type="number" name="offer_number" /> <br />
    <button type="submit">Submit</button>
</form> <br /> <br /> <br />


<h2>Link student</h2>

<form action="api/v1/Courses/ADCSB/2/Enrol" method="POST">
    <input type="number" name="student_id" /> <br />
    <button type="submit">Submit</button>
</form>

</body>
</html>
