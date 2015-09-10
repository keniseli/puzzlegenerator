package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This service offers methods working on shapefiles (e.g. getting names of
 * shapes).
 */
public class ShapeService {
	private static final String NAME_ATTRIBUTE_NAME = "MapSheetNa";

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

		SimpleFeatureIterator featuresIterator = featuresCollection.features();
		while (featuresIterator.hasNext()) {
			SimpleFeature feature = featuresIterator.next();
			String shapeName = getNameOfFeature(feature);
			names.add(shapeName);
		}
		featuresIterator.close();
		return names;
	}

	public SimpleFeature getFeatureOfShapeFileByName(String shapeFilePath, String shapeName) {
		File shapeFile = new File(shapeFilePath);
		SimpleFeatureCollection featuresCollection = getFeaturesOfShapeFile(shapeFile);

		SimpleFeature feature = null;
		SimpleFeatureIterator featuresIterator = featuresCollection.features();
		while (featuresIterator.hasNext()) {
			SimpleFeature foundFeature = featuresIterator.next();
			String foundShapeName = getNameOfFeature(foundFeature);
			if (foundShapeName.equals(shapeName)) {
				feature = foundFeature;
			}
		}
		featuresIterator.close();
		return feature;
	}

	private String getNameOfFeature(SimpleFeature feature) {
		Object nameAttribute = feature.getAttribute(NAME_ATTRIBUTE_NAME);
		String foundShapeName = "";
		if (nameAttribute != null) {
			foundShapeName = nameAttribute.toString();
		}
		return foundShapeName;
	}

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
			// magical reasons it does not work. Go for java.lang.Exception
			// instead.
			dataStoreFlurnamen.dispose();
			throw new ServiceException(e, "There was an error reading the file to get features of.");
		}
		return flurnamenSimpleFeatureCollection;
	}

}
