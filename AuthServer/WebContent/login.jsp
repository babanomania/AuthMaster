<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login To AuthMaster</title>
</head>
<body>

	<h1>Welcome to AuthMaster</h1>
	<h3>login to continue to your site</h3>

	<form action="login" method="post">
	
		<input type="text" name="userid" />
		<input type="password" name="passwd" />
		<input type="hidden" name="clientId" value="<%= request.getParameter( "client_id" ) %>" />
		<br/><br/>
		<input type="submit" name="login" />
	
	</form>

</body>
</html>