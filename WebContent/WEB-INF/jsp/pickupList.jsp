<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 배달목록</title>
</head>
<body>
	<h3>내 배달목록</h3>
	<form action="${pageContext.request.contextPath}/pickupListServlet"
		method="post">
		<table border="1">
			<input type="submit" value="배달완료">
			<tr>
				<td>${"배달CALL 번호"}</td>
				<td>${"주문시간"}</td>
				<td>${"가게명"}</td>
				<td>${"가게주소"}</td>
				<td>${"배달목적지"}</td>
				<td>${"선택"}</td>
			</tr>
			<c:forEach items="${rows}" var="item">
				<tr>
					<td><c:out value="${item.cno}" /></td>
					<td><c:out value="${item.ctime}" /></td>
					<td><c:out value="${item.cstore}" /></td>
					<td><c:out value="${item.caddress}" /></td>
					<td><c:out value="${item.destination}" /></td>
					<td><input type="checkbox" name="checks" value="${item.cno}"></td>
				</tr>
			</c:forEach>
		</table>


	</form>
	<form action="${pageContext.request.contextPath}/listServlet"
		method="get">
		<input type="submit" value="전체목록 돌아가기">
	</form>
</body>
</html>