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

public class HttpGet {

	public String getAllRegsiteredServices(String eurekaEndPoint) {
		StringBuffer discoveryApplications = null;
		try {
			
			HttpsURLConnection.setDefaultHostnameVerifier(Utils.signHost());

			URL url = new URL(eurekaEndPoint);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			// conn.setDoOutput(true);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output = null;
			
			
			discoveryApplications = new StringBuffer();
			while ((output = br.readLine()) != null) {
				discoveryApplications.append(output);
			}

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

		return discoveryApplications.toString();
	}

}
