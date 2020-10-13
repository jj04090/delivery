package com.delivery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.servlet.http.HttpSession;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/jsp/loginForm.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String check = "false";
		String dno = null;
		String getDid = "";
		String getDpwd = "";

		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpSession session = httpRequest.getSession();

		System.out.println(id + " : " + pwd);

		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			InitialContext initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/mybatis");
			conn = ds.getConnection();
			statement = conn.createStatement();
			String sql = "select DNO, DID, DPWD from delivery_info";
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("DID").equals(id) && rs.getString("DPWD").equals(pwd)) {
					getDid = rs.getString("DID");
					getDpwd = rs.getString("DPWD");
					dno = rs.getString("DNO");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (getDid.equals(id) && getDpwd.equals(pwd)) {
			check = "ture";
			session.setAttribute("isAuth", "true");
			this.getServletContext().setAttribute("DNO", dno);
			response.sendRedirect(request.getContextPath() + "/listServlet");
		} else {
			check = "false";
			session.setAttribute("isAuth", "false");
			request.getRequestDispatcher("/WEB-INF/jsp/loginForm.jsp").forward(request, response);
		}
	}
}
