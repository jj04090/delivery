package com.delivery;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/listServlet")
public class ListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String setAddr = null;
		String dno = (String) this.getServletContext().getAttribute("DNO");

		List<Map<String, String>> dataRows = new ArrayList<Map<String, String>>();
		List<String> city = new ArrayList<String>();
		Map<String, String> row = null;
		Connection conn = null;
		Statement statement = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			InitialContext initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/mybatis");
			conn = ds.getConnection();
			
			String sql = "select * from call_info";
			String dql = "select dloc from delivery_info where dno=?";
			
			statement = conn.createStatement();
			pstmt = conn.prepareStatement(dql);			
			
			pstmt.setString(1, dno);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String[] c = rs.getString("dloc").split("[ ]");
				setAddr = c[1];
			}
			if(request.getParameter("LOC") !=null) {
				setAddr = request.getParameter("LOC");
			}
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				String[] citis = rs.getString("CADDRESS").split(" ");
				String t = citis[1];
				if(city.indexOf(t) ==-1) {
					city.add(t);
				}
				if (rs.getString("CADDRESS").contains(setAddr) &&rs.getString("CSTATUS").equals("X")) {
					row = new HashMap<String, String>();
					row.put("CNO", rs.getString("CNO"));
					row.put("CTIME", rs.getString("CTIME"));
					row.put("CSTORE", rs.getString("CSTORE"));
					row.put("CADDRESS", rs.getString("CADDRESS"));
					row.put("DESTINATION", rs.getString("DESTINATION"));
					dataRows.add(row);					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {rs.close();}
				if (statement != null) {statement.close();}
				if (conn != null) {conn.close();}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("dataRows", dataRows);
		request.setAttribute("citis", city);
		request.setAttribute("loc", setAddr);
		request.getRequestDispatcher("/WEB-INF/jsp/callList.jsp").forward(request, response);
	}
}