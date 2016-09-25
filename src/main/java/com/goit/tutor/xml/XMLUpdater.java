package com.goit.tutor.xml;

import com.goit.tutor.session.Card;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class XMLUpdater {
	
	private HashMap<Integer, Card> cardsToBeSentBackIntoXML;
	private String filePathway;

	private String statusToBeChanged;
	private String answeredToBeChanged;
	
	
	public XMLUpdater(HashMap<Integer, Card> cardsToBeSentBackIntoXML, String filePathway) {
		this.cardsToBeSentBackIntoXML = cardsToBeSentBackIntoXML;
		this.filePathway=filePathway;
	}

	public void update() {
		try {
			String filepath = filePathway;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			Element docEle = doc.getDocumentElement();
			NodeList nl = docEle.getChildNodes();
			if (nl != null && nl.getLength() > 0) {
				updateAttributes(nl);
			}
			updateXML(filepath, doc);
			System.out.println("XML was updated!");
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	/*Write the content into XML file*/
	private void updateXML(String filepath, Document doc) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-16");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
	}

	private void updateAttributes(NodeList nl) {
		for (int i = 0; i < nl.getLength(); i++) {
                        if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                                Element el = (Element) nl.item(i);
                                if (el.getNodeName().equals("card")) {
                                        String card = el.getElementsByTagName("word").item(0).getAttributes().item(0).getNodeValue();
                                        int cardId = Integer.parseInt(card);
                                        if (ifIdExistsInTheMap(cardId)) {
                                                // Need to change attributes of this Card
                                                Node myNode = el.getElementsByTagName("statistics").item(0);
                                                // Sets global fields
                                                retrieveParamsToBeChanged(cardId);
                                                ((Element) myNode).setAttribute("status", statusToBeChanged);
                                                ((Element) myNode).setAttribute("answered", answeredToBeChanged);
												// <statistics answered="1" status="3" />
												// <statistics status="2"/>
												// Check if answered param exists at all
												/*if (((Element) myNode).hasAttribute("answered")) {
												((Element) myNode).setAttribute("answered", answeredToBeChanged);
												} else {
												// If not - create such the param
												}*/
                                        }
                                }
                        }
                }
	}

	/* Checks if such a card (ID) exists in the HashMap*/
	private boolean ifIdExistsInTheMap(int nextId) {
		return cardsToBeSentBackIntoXML.containsKey(nextId);
		
	}

	private void retrieveParamsToBeChanged(int key) {
		Card aCard = cardsToBeSentBackIntoXML.get(key);
		statusToBeChanged = Integer.toString(aCard.status);
		answeredToBeChanged = Integer.toString(aCard.answered);
	}
}
