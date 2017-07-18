package com.testorg.ecommerce.web.security;
 
import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
/**
 * @author Kshitiz Garg
 */ 
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
 
    private static final String BASIC_REALM = "Basic realm=";
	private static final String WWW_AUTHENTICATE = "WWW-Authenticate";

	@Override
    public void commence(final HttpServletRequest request, 
            final HttpServletResponse response, 
            final AuthenticationException authException) throws IOException, ServletException {
         
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader(WWW_AUTHENTICATE, BASIC_REALM + getRealmName() + "");
         
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 : " + authException.getMessage());
    }
     
    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(CustomWebSecurityConfigurerAdapter.ECOMMERCE_REALM);
        super.afterPropertiesSet();
    }
 
}