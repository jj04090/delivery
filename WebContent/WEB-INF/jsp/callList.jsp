<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table border="1">
		<tr>
			<form action="${pageContext.request.contextPath}/listServlet"
				method="get">
			<td colspan=5><select name="LOC">
					<c:forEach items="${citis}" var="city">

						<c:if test="${loc eq city}">
							<option value="${city}" selected>${city}</option>
						</c:if>
						<c:if test="${loc ne city}">
							<option value="${city}">${city}</option>
						</c:if>
					</c:forEach>
			</select> <input type="submit" value="조회"></td>
			</form>
			<form action="${pageContext.request.contextPath}/pickupServlet"
				method="get">
			<td><input type="submit" value="Pick up"></td>
		<tr>
		<tr>
			<td>${"배달CALL 번호"}</td>
			<td>${"주문시간"}</td>
			<td>${"가게명"}</td>
			<td>${"가게주소"}</td>
			<td>${"배달목적지"}</td>
			<td>${"선택"}</td>
		</tr>
		<c:forEach items="${dataRows}" var="row" varStatus="status">
			<tr>

				<td>${row.CNO}</td>
				<td>${row.CTIME}</td>
				<td>${row.CSTORE}</td>
				<td>${row.CADDRESS}</td>
				<td>${row.DESTINATION}</td>
				<td><input type="checkbox" name="checks" value="${row.CNO}"></td>
			</tr>
		</c:forEach>
	</table>
	<input type="hidden" name="dno" value="${dno}">
	</form>
	<form action="${pageContext.request.contextPath}/pickupListServlet"
		method="get">
		<input type="submit" value="내 목록">
	</form>
	<form action="logoutServlet" method="get">
		<input type="submit" value="로그아웃">
	</form>
</body>
</html>