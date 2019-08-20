package com.ibm.parseXml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMlParser {

	public InstanceDTO getApplicationSpecificInfo(String discoveryData,String appId) {
		Document xmlDoc = null;
		InstanceDTO instanceDTO = null;
		Map<String, String> discoveryMap = null;
		
		try {
		
			// prepare the key value pair by parsing the xml
			xmlDoc = convertStringToDocument(discoveryData);
			discoveryMap = new HashMap<String, String>();
			Element e = xmlDoc.getDocumentElement();
			NodeList children = null;
			Node childNode = null;
			children = e.getChildNodes();
			String elemVal = null;
			String elementname = null;
			instanceDTO = new InstanceDTO();
			
			
			
			for (int i = 0; i < children.getLength(); i++) {
				childNode = children.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {

					// get the element value
					elemVal = childNode.getTextContent();
					elementname = childNode.getNodeName();
					
					if(elementname.equalsIgnoreCase("application")) {
						NodeList applicationChildren = null;
						applicationChildren = childNode.getChildNodes();
						Node applicationChildnode = null;
						String applicationName = "";
						for (int j = 0; j < applicationChildren.getLength(); j++) {
							applicationChildnode = applicationChildren.item(j);
							if (applicationChildnode.getNodeType() == Node.ELEMENT_NODE) {
								// get the element value
								elemVal = applicationChildnode.getTextContent();
								elementname = applicationChildnode.getNodeName();
								
								if(elementname.equalsIgnoreCase("name")) {
									applicationName = elemVal.trim();
								}
								
								
								if(elementname.equalsIgnoreCase("instance")) {
									NodeList instanceChildren = null;
									instanceChildren = applicationChildnode.getChildNodes();
									Node instanceChildnode = null;
									for (int k = 0; k < instanceChildren.getLength(); k++) {
										instanceChildnode = instanceChildren.item(k);
										if (instanceChildnode.getNodeType() == Node.ELEMENT_NODE) {
											
											// get the element value
											elemVal = instanceChildnode.getTextContent();
											elementname = instanceChildnode.getNodeName();

											
											if(elementname.equalsIgnoreCase("instanceId")) {
												discoveryMap.put(applicationName, elemVal);	
											}	
										}
									}
								}
							}
						}
					}
					
				}
			}
			
		
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (Exception e) {
		} 
		
		if(discoveryMap != null && !discoveryMap.isEmpty()) {
			instanceDTO.setEurelaApplicationName(appId);
			instanceDTO.setEurekaInstanceID(discoveryMap.get(appId));
		}
		return instanceDTO;
	}
	
	
	public List<InstanceDTO> getRegisteredApplications(String discoveryData) {
		Document xmlDoc = null;
		InstanceDTO instanceDTO = null;
		List<InstanceDTO> instanceDTOs = null;
		Map<String, String> discoveryMap = null;
		
		try {
		
			// prepare the key value pair by parsing the xml
			xmlDoc = convertStringToDocument(discoveryData);
			discoveryMap = new HashMap<String, String>();
			Element e = xmlDoc.getDocumentElement();
			NodeList children = null;
			Node childNode = null;
			children = e.getChildNodes();
			String elemVal = null;
			String elementname = null;
			
			
			instanceDTOs = new ArrayList<InstanceDTO>();
			
			
			for (int i = 0; i < children.getLength(); i++) {
				childNode = children.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {

					// get the element value
					elemVal = childNode.getTextContent();
					elementname = childNode.getNodeName();
					
					if(elementname.equalsIgnoreCase("application")) {
						NodeList applicationChildren = null;
						applicationChildren = childNode.getChildNodes();
						Node applicationChildnode = null;
						String applicationName = "";
						for (int j = 0; j < applicationChildren.getLength(); j++) {
							applicationChildnode = applicationChildren.item(j);
							if (applicationChildnode.getNodeType() == Node.ELEMENT_NODE) {
								// get the element value
								elemVal = applicationChildnode.getTextContent();
								elementname = applicationChildnode.getNodeName();
								
								if(elementname.equalsIgnoreCase("name")) {
									applicationName = elemVal.trim();
								}
								
								
								if(elementname.equalsIgnoreCase("instance")) {
									NodeList instanceChildren = null;
									instanceChildren = applicationChildnode.getChildNodes();
									Node instanceChildnode = null;
									for (int k = 0; k < instanceChildren.getLength(); k++) {
										instanceChildnode = instanceChildren.item(k);
										if (instanceChildnode.getNodeType() == Node.ELEMENT_NODE) {
											
											// get the element value
											elemVal = instanceChildnode.getTextContent();
											elementname = instanceChildnode.getNodeName();

											
											if(elementname.equalsIgnoreCase("instanceId")) {
												discoveryMap.put(applicationName, elemVal);	
											}	
										}
									}
								}
							}
						}
					}
					
				}
			}
			
		
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (Exception e) {
		} 
		
		if(discoveryMap != null && !discoveryMap.isEmpty()) {
			Set<String> set = discoveryMap.keySet();
			Iterator<String> it = set.iterator();
			
			while(it.hasNext()) {
				String key = it.next();
				instanceDTO = new InstanceDTO();
				instanceDTO.setEurelaApplicationName(key);
				instanceDTO.setEurekaInstanceID(discoveryMap.get(key));
				
				instanceDTOs.add(instanceDTO);
			}
		}
		return instanceDTOs;
	}
	
	
	/**
	 * Converts the return Xml to Document
	 * @param xmlStr
	 * @return
	 * @throws GDException
	 */
	private static Document convertStringToDocument(String responseXml)  {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = null;  
        Document doc = null;
        
        try  
        {  
            builder = factory.newDocumentBuilder();  
            doc = builder.parse( new InputSource( new StringReader( responseXml ) ) ); 
            
        } catch (Exception e) {  
        	 
        } 
        return doc;
    }
}
