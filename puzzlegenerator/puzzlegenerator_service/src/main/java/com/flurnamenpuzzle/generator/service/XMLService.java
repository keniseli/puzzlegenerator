package com.flurnamenpuzzle.generator.service;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This service offers methods working on XML files
 */
public class XMLService {

	private Document document;
	private Element rootElement;

	public XMLService() {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			document = docBuilder.newDocument();
			rootElement = document.createElement("puzzle");
			document.appendChild(rootElement);

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
	public void addPiece(Integer id, String name, String image, String xpos, String ypos, String text, String bild) {

		// staff elements
		Element piece = document.createElement("teil");
		rootElement.appendChild(piece);

		// set attribute to staff element
		Attr attr = document.createAttribute("id");
		attr.setValue(id.toString());
		piece.setAttributeNode(attr);

		// name element
		Element nameElement = document.createElement("name");
		nameElement.appendChild(document.createTextNode(name));
		piece.appendChild(nameElement);

		// image element
		Element imageElement = document.createElement("img");
		imageElement.appendChild(document.createTextNode(image));
		piece.appendChild(imageElement);

		// xpos element
		Element xposElement = document.createElement("xpos");
		xposElement.appendChild(document.createTextNode(xpos));
		piece.appendChild(xposElement);

		// ypos element
		Element yposElement = document.createElement("ypos");
		yposElement.appendChild(document.createTextNode(ypos));
		piece.appendChild(yposElement);

		// text element
		Element textElement = document.createElement("text");
		textElement.appendChild(document.createTextNode(text));
		piece.appendChild(textElement);

		// bild element
		Element bildElement = document.createElement("bild");
		bildElement.appendChild(document.createTextNode(bild));
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
	public void updatePiece(Integer id, String name, String image, String xpos, String ypos, String text, String bild) {

		NodeList pieces = rootElement.getChildNodes(); // all pieces

		for (int i = 0; i < pieces.getLength(); i++) {

			// get element object
			Element el = (Element) pieces.item(i);
			int elementID = Integer.parseInt(el.getAttribute("id"));

			if (elementID == id) // found element!
			{
				// update name
				if (name != null)
					el.getChildNodes().item(0).setTextContent(name);

				// update image
				if (image != null)
					el.getChildNodes().item(1).setTextContent(image);

				// update xpos
				if (xpos != null)
					el.getChildNodes().item(2).setTextContent(xpos);

				// update ypos
				if (ypos != null)
					el.getChildNodes().item(3).setTextContent(ypos);

				// update text
				if (text != null)
					el.getChildNodes().item(4).setTextContent(text);

				// update bild
				if (bild != null)
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
	public void saveXML(String path) {

		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(path));

			transformer.transform(source, result);
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

}
