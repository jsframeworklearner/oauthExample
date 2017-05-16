/**
 * 
 */
package com.example.oauthproject.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *@author Rachael Joseph
 *@version 1.0 17 Oct 2016
 *This is logger util class
 */
public class LoggerUtil {
	private static Logger log = LoggerFactory.getLogger("SYNAPSE");
    public static void logInfo(String message){
    	log.info(message);
    }
}
