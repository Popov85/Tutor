package com.goit.tutor.xml;

import com.goit.tutor.session.Card;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Андрей on 31.07.2016.
 */
public class XMLParser {

        /* XML-parser. Returns a well-formed array of CardObj from XMl*/
        public ArrayList<Card> getCardsFromXML(String filePathway) {
                long startTime;
                long stopTime;
                SAXParserFactory parserF= SAXParserFactory.newInstance();
                XMLHandler handler=new XMLHandler();
                SAXParser saxparser;
                try {
                        saxparser = parserF.newSAXParser();
                        // Let's trace the runtime
                        startTime = System.currentTimeMillis();
                        saxparser.parse(new File(filePathway), handler);
                        stopTime = System.currentTimeMillis();
                        //Display deserialization time in msec is:
                        System.out.println("SAX parser took: "+(stopTime-startTime)+" ms.");
                } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                } catch (SAXException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        System.out.println("Ooops, cannot find your file!");
                }
                return handler.getData();
        }
}
