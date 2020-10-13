package com.delivery.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "excluciveUrl", value = "/loginServlet,/logoutServlet"))
public class SessionFilter implements Filter {
	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);

		HttpSession session = httpRequest.getSession();
		boolean isAuth = Boolean.valueOf(String.valueOf(session.getAttribute("isAuth")));
		if (isAuth) {
			chain.doFilter(request, response);
		} else {
			String requestUrl = httpRequest.getRequestURI();
			String[] excluciveUrls = filterConfig.getInitParameter("excluciveUrl").split("[,]");

			if (isExcluciveUrl(requestUrl, excluciveUrls)) {
				chain.doFilter(request, response);
			} else {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginServlet");
			}
		}
	}

	private boolean isExcluciveUrl(String requestUrl, String[] excluciveUrls) {
		for (String excluciveUrl : excluciveUrls) {
			if (requestUrl.indexOf(excluciveUrl) > 0) {
				return true;
			}
		}

		return false;
	}
}
