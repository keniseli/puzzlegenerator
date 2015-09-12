package com.flurnamenpuzzle.generator.service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * This service offers methods working on XML files
 */
public class XMLService {

	private Document _document;
	private Element _rootElement;
	
	
	public XMLService(){
		
		// init base document, root element
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root element
			_document = docBuilder.newDocument();
			_rootElement = _document.createElement("puzzle");
			_document.appendChild(_rootElement);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method creates a new piece/node within the xml document
	 * 
	 * @param id
	 *            The unique identifier
	 * @param name
	 *            The name of the shape
	 * @param image
	 *            The image representing the shape
	 * @param xpos
	 *            The x-axis position   
	 * @param ypos
	 *            The y-axis position 
	 * @param text
	 *            The custom text for this shape
	 * @param bild
	 *            The custom image/photo for this shape 
	 */
	public void addPiece(Integer id, String name, String image, String xpos, String ypos, String text, String bild){
			
		// staff elements
		Element piece = _document.createElement("teil");
		_rootElement.appendChild(piece);
			
		// set attribute to staff element
		Attr attr = _document.createAttribute("id");
		attr.setValue(id.toString());
		piece.setAttributeNode(attr);
			
		// name element
		Element nameElement = _document.createElement("name");
		nameElement.appendChild(_document.createTextNode(name));
		piece.appendChild(nameElement);
						
		// image element
		Element imageElement = _document.createElement("img");
		imageElement.appendChild(_document.createTextNode(image));
		piece.appendChild(imageElement);
						
		// xpos element
		Element xposElement = _document.createElement("xpos");
		xposElement.appendChild(_document.createTextNode(xpos));
		piece.appendChild(xposElement);
						
		// ypos element
		Element yposElement = _document.createElement("ypos");
		yposElement.appendChild(_document.createTextNode(ypos));
		piece.appendChild(yposElement);
						
		// text element
		Element textElement = _document.createElement("text");
		textElement.appendChild(_document.createTextNode(text));
		piece.appendChild(textElement);
									
		// bild element
		Element bildElement = _document.createElement("bild");
		bildElement.appendChild(_document.createTextNode(bild));
		piece.appendChild(bildElement);		
			
	}	
	
	/**
	 * This method updates a existing piece/node within the xml document
	 * 
	 * @param id
	 *            The unique identifier
	 * @param name
	 *            The name of the shape
	 * @param image
	 *            The image representing the shape
	 * @param xpos
	 *            The x-axis position   
	 * @param ypos
	 *            The y-axis position 
	 * @param text
	 *            The custom text for this shape
	 * @param bild
	 *            The custom image/photo for this shape 
	 */
	public void updatePiece(Integer id, String name, String image, String xpos, String ypos, String text, String bild){
		
		NodeList pieces = _rootElement.getChildNodes(); // all pieces
				
		for(int i = 0; i < pieces.getLength(); i++){
			
			// get element object
			Element el = (Element)pieces.item(i);
			int elementID = Integer.parseInt(el.getAttribute("id"));
			
			if(elementID == id) // found element!
			{
				// update name
				if(name != null)
					el.getChildNodes().item(0).setTextContent(name);
				
				// update image
				if(image != null)
					el.getChildNodes().item(1).setTextContent(image);
				
				// update xpos
				if(xpos != null)
					el.getChildNodes().item(2).setTextContent(xpos);
				
				// update ypos
				if(ypos != null)
					el.getChildNodes().item(3).setTextContent(ypos);
				
				// update text
				if(text != null)
					el.getChildNodes().item(4).setTextContent(text);
				
				// update bild
				if(bild != null)
					el.getChildNodes().item(5).setTextContent(bild);
				
			}
			
		}
	}
	
	/**
	 * This method saves the xml to the given path
	 * 
	 * @param path
	 *            The path where to save the xml data
	 */
	public void saveXML(String path){
		
		try {
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(_document);
			StreamResult result = new StreamResult(new File(path));
			
			transformer.transform(source, result);
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		
	}
	
}
