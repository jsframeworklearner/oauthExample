package com.example.oauthproject.service.common;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.oauthproject.util.LoggerUtil;
@Service
/**
 * NG REST Template Service class
 * @version 1.0 17 Oct 2016
 * @author ****
 */
public class NGRESTTemplateService {
	private static final Logger LOG = Logger.getLogger(NGRESTTemplateService.class);
	@Value("${nextgate.authentication.url}")
	private String nextgateAuthenticateURL;
	@Value("${nextgate.username}")
	private String userName;
	@Value("${nextgate.password}")
	private String password;
	RestTemplate restTemplate = new RestTemplate();
	public static Map<String, String> cookies = new HashMap<String, String>();
	public Map<String, String> getCookies() {
		return cookies;
	}
	public void clearAllCookies() {
		cookies.clear();
	}
	public void setCookie(String key, String value) {
		cookies.put(key, value);
	}
	public void removeCookie(String key) {
		cookies.remove(key);
	}
	protected void extractCookies(HttpHeaders headers) {
		List<String> cookiesList = headers.get("Set-Cookie");
		if (cookiesList != null && !cookiesList.isEmpty()) {
			String cookiesStr = cookiesList.get(0);
			if (LOG.isTraceEnabled()) {
				LOG.trace("Cookies read from response : " + cookiesStr);
			}
			String[] cookiesSplit = cookiesStr.split(";");
			cookies.put("Cookie", cookiesSplit[0]);
		}
	}
	protected String getCookiesString() {
	    StringBuilder sb = new StringBuilder();
	    if (!cookies.isEmpty()) {
	    	for(Map.Entry<String, String> entry : cookies.entrySet() ){
	    		sb.append(entry.getKey());
	 	        sb.append("=");
	 	        sb.append(entry.getValue());
	 	        sb.append(";");
		      }
	      sb.deleteCharAt(sb.length() - 1);
	    }
	    if (LOG.isTraceEnabled()) {
	      LOG.trace("Cookies added to request : " + sb.toString());
	    }
	    return sb.toString();
	}
	private HttpHeaders setHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}
	public void nextGateAuthentication() {
		MultiValueMap<String, String> nextgateInfo = new LinkedMultiValueMap<String, String>();
		nextgateInfo.add("username", userName);
		nextgateInfo.add("password", password);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(nextgateInfo, setHeaders());
		try {
			ResponseEntity<String> authResponse = restTemplate.exchange(nextgateAuthenticateURL,HttpMethod.POST,requestEntity,String.class);
			LoggerUtil.logInfo("Status code + " + authResponse.getStatusCode());
			LoggerUtil.logInfo("Get headers + " + authResponse.getHeaders());
			if (authResponse.getStatusCode() == HttpStatus.OK) {
				// getting cookies
				extractCookies(authResponse.getHeaders());
				LoggerUtil.logInfo("The Cookie is******** + " + getCookiesString());
			} else if (authResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
               LoggerUtil.logInfo(" UNAUTHORIZED AUTHENTICATION REQUEST");				
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.logInfo("Exception found : " + e.getMessage());
		}
	}
	public HttpHeaders setHeadersforAPICalls() {
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 headers.add("Cookie", cookies.get("Cookie"));
		 return headers;
	}
}
