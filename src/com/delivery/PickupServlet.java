package com.delivery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/pickupServlet")
public class PickupServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String[] checks = request.getParameterValues("checks");
		String dno = (String) this.getServletContext().getAttribute("DNO");

		System.out.println("qqqqq"+checks[0]);
		System.out.println("wwwwww"+dno);
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			if (checks != null) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Context initContext = new InitialContext();
				DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/mybatis");
				conn = ds.getConnection();
				String sql = "INSERT INTO MY_INFO VALUES(?,?)";
				String sql2 = "UPDATE CALL_INFO SET CSTATUS='I' WHERE cno=?";
				pstmt = conn.prepareStatement(sql2);
				for (String c : checks) {
					pstmt.setString(1, c);
					pstmt.executeUpdate();
				}
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dno);
				for (String c : checks) {
					pstmt.setString(2, c);
					pstmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			response.sendRedirect(request.getContextPath() + "/listServlet?DNO="+dno);
		}
	}
}