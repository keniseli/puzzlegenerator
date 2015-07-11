package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

/**
 * This service offers methods working on shapefiles (e.g. getting names of
 * shapes).
 */
public class ShapeService {
	private static final int NAME_OF_SHAPE_ATTRIBUTE_INDEX = 3;

	/**
	 * This method reads the names of all shapes in a shape file.
	 * 
	 * @param shapeFile
	 *            The file representing the shape file.
	 * @return The names of the shapes.
	 */
	public List<String> getNamesOfShapeFile(File shapeFile) {
		List<String> names = new ArrayList<String>();

		SimpleFeatureCollection featuresCollection = getFeaturesCollectionOfShapeFile(shapeFile);

		SimpleFeatureIterator featuresIterator = featuresCollection.features();
		while (featuresIterator.hasNext()) {
			SimpleFeature feature = featuresIterator.next();
			int numberOfAttributes = feature.getAttributeCount();
			if (numberOfAttributes >= NAME_OF_SHAPE_ATTRIBUTE_INDEX) {
				Object nameAttribute = feature.getAttribute(NAME_OF_SHAPE_ATTRIBUTE_INDEX);
				String shapeName = nameAttribute.toString();
				names.add(shapeName);
			}
		}
		return names;
	}

	private SimpleFeatureCollection getFeaturesCollectionOfShapeFile(File shapeFile) {
		SimpleFeatureCollection featuresCollection = null;
		try {
			FileDataStore dataStore = FileDataStoreFinder.getDataStore(shapeFile);
			SimpleFeatureSource featureSource = dataStore.getFeatureSource();
			featuresCollection = featureSource.getFeatures();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e, "There was an IO error.");
		}
		return featuresCollection;
	}
}