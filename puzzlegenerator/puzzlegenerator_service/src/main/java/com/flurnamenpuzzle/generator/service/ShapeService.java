package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This service offers methods working on shapefiles (e.g. getting names of
 * shapes or filter the shapes of each other).
 */
public class ShapeService {
	/**
	 * The team specified possible attribute keys of a shapeFile which may
	 * represent the name of the shapes (features) inside. This key is one of
	 * them.
	 */
	private static final String MAP_SHEET_NAME_ATTRIBUTE_KEY = "mapSheetNa";
	
	/**
	 * The team specified possible attribute keys of a shapeFile which may
	 * represent the name of the shapes (features) inside. This key is one of
	 * them.
	 */
	private static final String NAME_ATTRIBUTE_KEY = "Name";

	/**
	 * Returns a {@link List}<{@link SimpleFeature}> where all SimpleFeatures
	 * are in the area of the parentFeature.
	 *
	 * @param parentFeature
	 *            SimpleFeature that covers the area of all wanted
	 *            SimpleFeatures
	 * @param childFeatures
	 *            List of SimpleFeatures to be filtered
	 * @return Filtered {@code List<SimpleFeature>}
	 */
	public List<SimpleFeature> filterContainingFeaturesOfFeature(SimpleFeature parentFeature,
			List<SimpleFeature> childFeatures) {
		List<SimpleFeature> filteredFeatures = new ArrayList<SimpleFeature>();
		Geometry parentGeometry = (Geometry) parentFeature.getDefaultGeometry();
		for (SimpleFeature childFeature : childFeatures) {
			Geometry childGeometry = (Geometry) childFeature.getDefaultGeometry();
			if (parentGeometry.contains(childGeometry.getCentroid())) {
				filteredFeatures.add(childFeature);
			}
		}
		return filteredFeatures;
	}

	/**
	 * Finds the {@link SimpleFeatureCollection} of the given file.<br />
	 * <b>This must be an existing shapefile otherwise a
	 * {@link ServiceException} is thrown</b>.
	 * 
	 * @param flurnamenShapeFile
	 *            the file to find the features of.
	 * @return the {@link SimpleFeatureCollection} of the given file.
	 */
	public SimpleFeatureCollection getFeaturesOfShapeFile(File flurnamenShapeFile) {
		SimpleFeatureCollection flurnamenSimpleFeatureCollection = null;
		FileDataStore dataStoreFlurnamen = null;
		try {
			dataStoreFlurnamen = FileDataStoreFinder.getDataStore(flurnamenShapeFile);
			SimpleFeatureSource shapeFileSourceFlurnamen = dataStoreFlurnamen.getFeatureSource();
			dataStoreFlurnamen.dispose();
			flurnamenSimpleFeatureCollection = shapeFileSourceFlurnamen.getFeatures();
		} catch (IOException | RuntimeException e) {
			// FIXME: Actually this should be a java.io.IOException but for
			// magical reasons it throws a java.lang.RuntimeException. Thanks.
			dataStoreFlurnamen.dispose();
			throw new ServiceException(e, "There was an error reading the file to get features of.");
		}
		return flurnamenSimpleFeatureCollection;
	}

	/**
	 * This method reads the names of all shapes in a shape file.
	 * 
	 * @param shapeFile
	 *            The file representing the shape file.
	 * @return The names of the shapes.
	 */
	public List<String> getNamesOfShapeFile(File shapeFile) {
		List<String> names = new ArrayList<String>();

		SimpleFeatureCollection featuresCollection = getFeaturesOfShapeFile(shapeFile);
		String attributeNameKey = determineNameAttributeKeyOfFeatures(featuresCollection);
		SimpleFeatureIterator featuresIterator = featuresCollection.features();
		while (featuresIterator.hasNext()) {
			SimpleFeature feature = featuresIterator.next();
			String shapeName = getAttributeValueOfFeature(feature, attributeNameKey);
			names.add(shapeName);
		}
		featuresIterator.close();
		return names;
	}

	/**
	 * Finds the {@link SimpleFeature} with the specified name in the given
	 * shapefile.
	 * 
	 * @param shapeFilePath
	 *            The path to the shapeFile to be read.
	 * @param shapeName
	 *            The name of the shape to be found.
	 * @return The found {@link SimpleFeature}, null otherwise.
	 */
	public SimpleFeature getFeatureOfShapeFileByName(String shapeFilePath, String shapeName) {
		File shapeFile = new File(shapeFilePath);
		SimpleFeatureCollection featuresCollection = getFeaturesOfShapeFile(shapeFile);

		SimpleFeature feature = null;
		SimpleFeatureIterator featuresIterator = featuresCollection.features();
		String attributeNameKey = determineNameAttributeKeyOfFeatures(featuresCollection);
		while (featuresIterator.hasNext()) {
			SimpleFeature foundFeature = featuresIterator.next();
			String foundShapeName = getAttributeValueOfFeature(foundFeature, attributeNameKey);
			if (foundShapeName.equals(shapeName)) {
				feature = foundFeature;
			}
		}
		featuresIterator.close();
		return feature;
	}

	/**
	 * Determines the name attribute key of the features given. Calls
	 * {@link #determineNameAttributeKeyOfFeature(SimpleFeature)} with the first
	 * element in the given {@link SimpleFeatureCollection}
	 * 
	 * @param featuresCollection
	 *            The collection to find the name attribute key of.
	 * @return The found name
	 */
	private String determineNameAttributeKeyOfFeatures(SimpleFeatureCollection featuresCollection) {
		SimpleFeatureIterator featuresIterator = featuresCollection.features();
		String nameAttributeKey = "";
		if (featuresIterator.hasNext()) {
			SimpleFeature feature = featuresIterator.next();
			nameAttributeKey = determineNameAttributeKeyOfFeature(feature);
		}
		featuresIterator.close();
		return nameAttributeKey;
	}

	/**
	 * Iterates over the {@link Property properties} of the given
	 * {@link SimpleFeature} and checks for the keys. If it is either
	 * {@value #MAP_SHEET_NAME_ATTRIBUTE_KEY} or {@value #NAME_ATTRIBUTE_KEY}
	 * then that key is returned. Otherwise the first key is returned.
	 * 
	 * @param feature
	 *            the {@link SimpleFeature} to find the name attribute of.
	 * @return the found name attribute key. May be something completely
	 *         different!
	 */
	private String determineNameAttributeKeyOfFeature(SimpleFeature feature) {
		String nameAttributeKey = null;
		Collection<Property> properties = feature.getProperties();
		Property[] propertiesArray = properties.toArray(new Property[0]);
		for (int i = 0; i < propertiesArray.length; i++) {
			Property property = propertiesArray[i];
			Name propertyName = property.getName();
			String localPartOfPropertyName = propertyName.getLocalPart();
			if (localPartOfPropertyName.equalsIgnoreCase(MAP_SHEET_NAME_ATTRIBUTE_KEY)
					|| localPartOfPropertyName.equalsIgnoreCase(NAME_ATTRIBUTE_KEY)) {
				nameAttributeKey = localPartOfPropertyName;
				break;
			}
		}
		if (nameAttributeKey == null && !properties.isEmpty()) {
			Property firstProperty = propertiesArray[0];
			nameAttributeKey = firstProperty.getName().getLocalPart().toString();
		}
		return nameAttributeKey;
	}

	/**
	 * Reads the attribute with the given name of the given
	 * {@link SimpleFeature} and returns the value.
	 * 
	 * @param feature
	 *            The {@link SimpleFeature} to find the attribute value of.
	 * @param attributeNameKey
	 *            The attribute key of the attribute.
	 * @return the value of the attribute.
	 */
	private String getAttributeValueOfFeature(SimpleFeature feature, String attributeNameKey) {
		Object nameAttribute = feature.getAttribute(attributeNameKey);
		String foundShapeName = "";
		if (nameAttribute != null) {
			foundShapeName = nameAttribute.toString();
		}
		return foundShapeName;
	}

}
