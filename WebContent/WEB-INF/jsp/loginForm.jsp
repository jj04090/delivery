<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Login</h1>
	<form action="loginServlet" method="post">
		ID: <input type="text" name="id"> <br /> Password : <input
			type="password" name="pwd"> <br /> <input type="submit"
			value="로그인"> <input type="reset" value="지우기">
	</form>
</body>
</html>