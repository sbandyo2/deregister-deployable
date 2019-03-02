package com.ibm.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utils {

	private static final String CREDENTIAL_FILE = "appconfig.properties";

	public static String geConfig(String key) {
		Properties properties = null;
		String value = "";
		properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = Utils.class.getClassLoader().getResourceAsStream(
					CREDENTIAL_FILE);

			if (inputStream != null)
				properties.load(inputStream);
			if (properties != null)
				value = properties.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	public static HostnameVerifier signHost() throws NoSuchAlgorithmException, KeyManagementException{
		// Create a trust manager that does not validate certificate chains
	 	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        // Install the all-trusting trust manager
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        
        return allHostsValid;
	}
}
