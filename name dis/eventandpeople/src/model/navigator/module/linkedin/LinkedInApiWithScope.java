/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.module.linkedin;

import org.scribe.builder.api.LinkedInApi;


/**
 * Modifies the default request token endpoint to include some permissions.
 */
public class LinkedInApiWithScope extends LinkedInApi {

	@Override
	public String getRequestTokenEndpoint() {
		return "https://api.linkedin.com/uas/oauth/requestToken?scope=r_fullprofile r_basicprofile r_emailaddress";
	}

}