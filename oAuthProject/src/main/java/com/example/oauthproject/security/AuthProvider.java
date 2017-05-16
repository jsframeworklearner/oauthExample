package com.example.oauthproject.security;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.example.oauthproject.util.LoggerUtil;
@Component
public class AuthProvider implements AuthenticationProvider {
	PasswordEncoder encoder = new Md5PasswordEncoder();
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		try{
			return userAuthentication(authenticateFromREST(authentication), authentication);
		} catch( ResourceAccessException e){
			throw new AuthenticationCredentialsNotFoundException(e.getMessage());
		}
	}	
	private ResponseEntity<String> authenticateFromREST(
			Authentication authentication) throws ResourceAccessException{
		String username = authentication.getName();
		String encryptedPassword = encoder.encodePassword("****", null);
		String password = authentication.getCredentials().toString();
		LoggerUtil.logInfo("RestAuthentication : username -- " + username + " : password -- " + password);
		String status="Success";
		ResponseEntity<String> response = new ResponseEntity<String>("Failure", HttpStatus.UNAUTHORIZED);
		if(username.equals("New user")){
			response = new ResponseEntity<String>("Success", HttpStatus.OK);
		}else if(username.equals("ccdaprovider") && username.equals(encryptedPassword)){
			response = new ResponseEntity<String>("Success", HttpStatus.OK);
		}else{
			if(status.equals("Success")){
				response = new ResponseEntity<String>("Success", HttpStatus.OK);
			}else if(status.equals("Failure")){
				response = new ResponseEntity<String>("Failure", HttpStatus.UNAUTHORIZED);
			}else if(status.equals("Authentication Failed")){
				response = new ResponseEntity<String>("Authentication Failed", HttpStatus.BAD_REQUEST);
			}
		}
		return response;
	}
	private Authentication userAuthentication(ResponseEntity<String> response, Authentication authentication) {
		if(HttpStatus.OK == response.getStatusCode()){
			LoggerUtil.logInfo("RestAuthentication : Status" + response.getStatusCode());
			//userDetails.setUserName(authentication.getName());
			//userDetails.setStatus("Success");
			//userDetails.setPassword(authentication.getCredentials().toString());
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("USER"));					
			return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
		}else if(HttpStatus.BAD_REQUEST == response.getStatusCode()) {
			LoggerUtil.logInfo("RestAuthentication : Status" + response.getStatusCode());
			//userDetails.setStatus("Authentication Failed");
			//userDetails.setUserName(authentication.getName());
			//userDetails.setPassword(authentication.getCredentials().toString());
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("USER"));					
			return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
		}else if(HttpStatus.UNAUTHORIZED == response.getStatusCode()) {
			LoggerUtil.logInfo("RestAuthentication : Status" + response.getStatusCode());
			//userDetails.setStatus("Incorrect Username or Password");
			//userDetails.setUserName(authentication.getName());
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("USER"));
			return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
		//	throw new AuthenticationCredentialsNotFoundException(authentication.getName());
		} else {
			LoggerUtil.logInfo("RestAuthentication : Status" + response.getStatusCode());
			throw new AuthenticationCredentialsNotFoundException(authentication.getName());
		}
	}
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
