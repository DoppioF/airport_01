package airport_01Rest.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/rest/*")
public class FiltroAntiCORS implements Filter {

   public FiltroAntiCORS() {
	   
   }

   public void destroy() {
	   
   }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	 	HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;

	    res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
	    res.setHeader("Access-Control-Allow-Credentials", "true");
	    res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    res.setHeader("Access-Control-Max-Age", "36000");
	    res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

	    chain.doFilter(req, res);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
	}
}