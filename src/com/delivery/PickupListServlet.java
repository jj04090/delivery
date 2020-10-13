package com.delivery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/pickupListServlet")
public class PickupListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String dno = (String) this.getServletContext().getAttribute("DNO");
		List<String> cnoArr = new ArrayList<String>();
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		Map<String, String> row = null;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/mybatis");
			conn = ds.getConnection();
			String dql = "select cno from MY_INFO where dno=?";
			String sql = "select cno, ctime, cstore, cstatus, caddress, destination from CALL_INFO where cno=?";
			pstmt = conn.prepareStatement(dql);
			pstmt.setString(1, dno);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cnoArr.add(rs.getString("cno"));
			}

			pstmt = conn.prepareStatement(sql);
			for (String c : cnoArr) {
				pstmt.setString(1, c);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					row = new HashMap<String, String>();
					if (rs.getString("cstatus").equals("I")) {
						row.put("cno", rs.getString("cno"));
						row.put("ctime", rs.getString("ctime"));
						row.put("cstore", rs.getString("cstore"));
						row.put("caddress", rs.getString("caddress"));
						row.put("destination", rs.getString("destination"));
						rows.add(row);
					}
				}
			}
			request.setAttribute("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/pickupList.jsp").forward(request, response);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] checks = req.getParameterValues("checks");
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/mybatis");
			conn = ds.getConnection();

			String sql = "update call_info set cstatus = 'O' where cno = ?";

			pstmt = conn.prepareStatement(sql);
			for (String c : checks) {
				pstmt.setString(1, c);
				pstmt.executeUpdate();
			}

			pstmt.executeUpdate();
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
			} catch (Exception e) {

			}
			resp.sendRedirect(req.getContextPath() + "/listServlet");
		}
	}
}