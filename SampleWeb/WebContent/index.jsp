<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cool Page</title>
</head>
<body>

	<% String userId = (String)session.getAttribute( "user_id" ); %>

	<h3>Welcome <%= userId %></h3>
	<h2>this is a cool page</h2>
	
	<br/><br/><a href="/SampleWeb/index.jsp">visit this page again</a>
	<br/><br/><a href="/SampleWeb/logout">logout</a>

</body>
</html>