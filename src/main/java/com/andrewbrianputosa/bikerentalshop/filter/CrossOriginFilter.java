package com.andrewbrianputosa.bikerentalshop.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CrossOriginFilter implements Filter {
	
	@Value("${allowedClients}")
	private String allowedClients;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest oRequest = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", allowedClients);
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers"," Origin, X-Requested-With, Content-Type, Accept, X-AUTH-TOKEN, X-AUTH-USERID, X-AUTH-USERID, X-FORWARDED-FOR");
	    if (oRequest.getMethod().equals("OPTIONS")) {
	    	response.flushBuffer();
	    } else {
	    	chain.doFilter(req, response);
	    }
	}
	
	@Override
	public void init(FilterConfig filterConfig) {
		
	}
	
	@Override
	public void destroy() {
		
	}

}
