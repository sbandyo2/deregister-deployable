package com.ibm.registryEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.http.HttpGet;
import com.ibm.parseXml.InstanceDTO;
import com.ibm.parseXml.XMlParser;
import com.ibm.util.Utils;

public class RegistryEntry {
	private static Logger logger = Logger.getLogger(RegistryEntry.class);

	private static final String[] INSTANCE_ARRAY = { "AUTH-SERVICE",
			"BACKEND-SERVICE", "CONFIG", "CSA-SERVICE", "CSA-SERVICEW2",
			"DISCOVERY", "GATEWAY", "SAPARIBA-SERVICE", "YOURPROCURE-SERVICE",
			"YOURPROCUREW2-SERVICE", "ON-SERVICE" };

	public static void main(String[] args) {
		//BasicConfigurator.configure();
		logger.info("Starting restart App checks");
		HttpGet httpGet = null;

		XMlParser xMlParser = null;
		List<InstanceDTO> instanceDTOs = null;
		List<String> shutDownApps = null;
		String eurekaEndPoint = null;

		xMlParser = new XMlParser();
		httpGet = new HttpGet();
		shutDownApps = new ArrayList<String>();

		eurekaEndPoint = Utils.geConfig("eurekaEnPoint");
		instanceDTOs = xMlParser.getRegisteredApplications(httpGet.getAllRegsiteredServices(eurekaEndPoint));

		// check if all apps required is registered if not go for recycling the
		// instance
		boolean matchFound = false;
		for (String app : INSTANCE_ARRAY) {
			matchFound = false;
			inner: for (InstanceDTO instanceDTO : instanceDTOs) {

				if (app.equalsIgnoreCase(instanceDTO.getEurelaApplicationName())) {
					matchFound = true;
					break inner;
				}
			}
			if (!matchFound) {
				shutDownApps.add(app);
			}
		}
		Process process = null;
		String commandVar = null;
		String containerName = null;
		try {
			for (String restartApp : shutDownApps) {
				commandVar = getCommandForApp(restartApp);
				containerName = getContainerName(restartApp);
				logger.info("Restarting App " + restartApp + " with paramter "
						+ commandVar + " with containetname " + containerName);
				String[] cmd = { "sudo", "/home/mesadmin/recycleApp.sh",
						commandVar, containerName };

				process = Runtime.getRuntime().exec(cmd);
				process.waitFor();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					logger.info("Output of script :::  " + line);
				}
			}
			logger.info("Finishing restart App checks");
		} catch (IOException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}

	}

	private static String getCommandForApp(String restartApp) {
		String commandVar = "";

		if ("ON-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "ordernow";
		} else if ("AUTH-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "auth-server";
		} else if ("BACKEND-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "backend-server";
		} else if ("CONFIG".equalsIgnoreCase(restartApp)) {
			commandVar = "config-server";
		} else if ("CSA-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "csa";
		} else if ("CSA-SERVICEW2".equalsIgnoreCase(restartApp)) {
			commandVar = "csaw2";
		} else if ("DISCOVERY".equalsIgnoreCase(restartApp)) {
			commandVar = "discovery";
		} else if ("SAPARIBA-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "ariba-server";
		} else if ("YOURPROCURE-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "yp";
		} else if ("YOURPROCUREW2-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "ypw2";
		} else if ("GATEWAY".equalsIgnoreCase(restartApp)) {
			commandVar = "gateway";
		}

		return commandVar;
	}

	private static String getContainerName(String restartApp) {
		String commandVar = "";

		if ("ON-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "on-primary";
		} else if ("AUTH-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "auth-server-primary";
		} else if ("BACKEND-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "backend-primary";
		} else if ("CONFIG".equalsIgnoreCase(restartApp)) {
			commandVar = "config-server-primary";
		} else if ("CSA-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "csa-primary";
		} else if ("CSA-SERVICEW2".equalsIgnoreCase(restartApp)) {
			commandVar = "csa-secondary";
		} else if ("DISCOVERY".equalsIgnoreCase(restartApp)) {
			commandVar = "discovery-primary";
		} else if ("SAPARIBA-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "ariba-primary";
		} else if ("YOURPROCURE-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "yp-primary";
		} else if ("YOURPROCUREW2-SERVICE".equalsIgnoreCase(restartApp)) {
			commandVar = "yp-secondary";
		} else if ("GATEWAY".equalsIgnoreCase(restartApp)) {
			commandVar = "gateway-primary";
		}

		return commandVar;
	}
}
