package com.ibm.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import com.ibm.util.Utils;

public class HttpDelete {

	public void unRegisterService(String eurekaEndPoint,String appId,String instanceId) {
		StringBuffer discoveryApplications = null;
		try {
			
			HttpsURLConnection.setDefaultHostnameVerifier(Utils.signHost());

			URL url = new URL(eurekaEndPoint+"/"+appId+"/"+instanceId);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			
			conn.setRequestMethod("DELETE");
			conn.connect();
		

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			System.out.println("Service unregistered for appId "+ appId);

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
