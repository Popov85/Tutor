package com.goit.tutor.xml;

import com.goit.tutor.session.Card;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class XMLHandler extends DefaultHandler {

	// The result list of selected cards
	private ArrayList<Card> arrayOfCardObj= new ArrayList<Card>();
	
	private int id;
	//Transcription
	private String trsp;
	private String engWord;
	private String rusWord;
	private String examp;
	private int status;
	private int answered;
	
	private boolean ifId = false;
	private boolean ifExamp = false;
	private boolean ifTranslation = false;

	private boolean ifCard = false;
	
	//Variable lists of variants of translations and examples
	private ArrayList<String> translations = new ArrayList<String>();
	private ArrayList<String> examples = new ArrayList<String>();
	
	//Total number of cards in an XML-file
	private int n = 0;
	//Quantity of selected cards (status 2 or 3)
	private int nn = 0;
	// Variable object link of Card object
	private Card aCard;
	
	@Override
	public void startDocument() throws SAXException {
		System.out.println("start parsing...");
	}
	
	@Override
	public void endDocument() throws SAXException {
		System.out.println("Total number of cards = "+n);
		System.out.println("Selected cards = "+nn);
	}
   
	@Override
	public void startElement (String namespace, String localName, String qName, Attributes attr) {

		if(qName.equals("card")) {
			n++;
			ifCard = true;
	        }
		if(qName.equals("word")) {
			String ifAttr = attr.getValue("wordId");
			if (ifAttr != null) {
				id = Integer.parseInt(attr.getValue("wordId"));
				ifId=true;
			}
			else {
				// Translation Array goes ...
				ifTranslation = true;
			}
	        }

		if(qName.equals("meaning")) {
			trsp = attr.getValue("transcription");
		}

                if(ifCard && qName.equals("statistics")) {

                        status = Integer.parseInt(attr.getValue(0));
                        String ifSecondParameterExists = attr.getValue(1);
                        if (ifSecondParameterExists != null) {
                                answered = Integer.parseInt(attr.getValue(0));
                                status = Integer.parseInt(attr.getValue(1));
                        } else {
                                answered = 0;
                        }
                }

                if(qName.equals("example")) {
			//Examples Array goes...
			ifExamp = true;
		}
	}

	@Override
	public void endElement (String namespace, String localName, String qName) {
		if(qName.equals("card")) { // Add the current card to the ArrayList
			if ((status == 2)||(status == 3)) {
				aCard = new Card();
				aCard.wordId = id;
				aCard.trsp = trsp;
				aCard.status = status;
				aCard.answered = answered;
				aCard.engWord = engWord;
				aCard.translations.addAll(translations);
				aCard.examples.addAll(examples);
				arrayOfCardObj.add(aCard);
				//Clear the temporary list of rusWords, the same for examples array
				translations.clear();
				examples.clear();
				nn++;
				// Just in case nullify status;
				status = 0;
			} else {
				//System.out.println("Unwanted word is (probably 'mapping'): "+engWord);
				translations.clear();
			}
			ifCard = false;
		}
	}
	
	@Override
	public void characters (char []ch, int start, int end) {
		if (ifId) {// Goes a new wordId tag here
			engWord=new String(ch, start, end);
			ifId=false;
		}

		if (ifTranslation) {
			rusWord=new String(ch, start, end);
			translations.add(rusWord);
			ifTranslation =false;
		}
		if (ifExamp) {
			examp=new String(ch, start, end);
			examples.add(examp);
			ifExamp=false;
		}
	}
	
	public ArrayList<Card> getData() {
		return arrayOfCardObj;
	}
}
