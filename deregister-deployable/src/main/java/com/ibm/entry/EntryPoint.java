package com.ibm.entry;

import com.ibm.http.HttpDelete;
import com.ibm.http.HttpGet;
import com.ibm.parseXml.InstanceDTO;
import com.ibm.parseXml.XMlParser;
import com.ibm.util.Utils;

public class EntryPoint {

	public static void main(String[] args) {
		HttpGet httpGet = null;
		HttpDelete httpDelete = null;
		XMlParser xMlParser = null;
		InstanceDTO dto = null;
		String appId =  null;
		String eurekaEndPoint = null;
		if(args.length > 0 ){
			appId = args[0].toUpperCase();
			xMlParser = new XMlParser();
			httpGet = new HttpGet();
			httpDelete = new HttpDelete();
			
			eurekaEndPoint = Utils.geConfig("eurekaEnPoint");
			dto = xMlParser.getApplicationSpecificInfo(httpGet.getAllRegsiteredServices(eurekaEndPoint), appId);
			
			System.out.println("Instance Id  for application "+ dto.getEurelaApplicationName() + " is "+ dto.getEurekaInstanceID());
			
			httpDelete.unRegisterService(eurekaEndPoint, dto.getEurelaApplicationName(), dto.getEurekaInstanceID());
		}
	}
}
