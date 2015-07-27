package com.flurnamenpuzzle.generator.service;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

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
	public List<SimpleFeature> filterContainingFeaturesOfFeature(SimpleFeature parentFeature, List<SimpleFeature> childFeatures) {
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
	 * Calculate pixel coordinates on tiff image of given lontitude and latitude in LV03
	 * 
	 * @param coverage
	 * @param longitude
	 * @param latitude
	 * @return Point(xAxis, yAxis) of representive pixel
	 * @throws Exception
	 */
	private static Point getPointByCoordinates(GridCoverage2D coverage, double longitude, double latitude) throws Exception{ 
		
		// read width and height of tiff image
		int tHeight	= coverage.getRenderedImage().getHeight();
		int tWidth	= coverage.getRenderedImage().getWidth();
		
		// read boundaries of tiff file
		double yAxisOrigin	= coverage.getEnvelope2D().getBounds2D().getY();
		double xAxisOrigin	= coverage.getEnvelope2D().getBounds2D().getX();
		double yAxisHeight	= coverage.getEnvelope2D().getHeight();
		double xAxisWidth	= coverage.getEnvelope2D().getWidth();
		double yAxisMaxY	= coverage.getEnvelope2D().getMaxY();
		double xAxisMaxX	= coverage.getEnvelope2D().getMaxX();
		
		// check if provided coordinates are available
		if(!(longitude >= xAxisOrigin && longitude <= xAxisMaxX && latitude >= yAxisOrigin && latitude <= yAxisMaxY))
			throw new Exception("Provided coordinates not found within tiff file");
		
		// calculate coordinate offset from origin 
		double longtidudeOffset = longitude - xAxisOrigin;
		double latitudeOffset = yAxisMaxY - latitude;
		
		// calculate pixel/coordinate relation
		double coordinatesPerPixelX = tWidth / xAxisWidth;
		double coordinatesPerPixelY = tHeight / yAxisHeight;
		
		// calculate point where pixels are
		int pixelX = (int)Math.round(longtidudeOffset * coordinatesPerPixelX);
		int pixelY = (int)Math.round(latitudeOffset * coordinatesPerPixelY);
		
		return new Point(pixelX, pixelY);
	}
	
	/**
	 * To describe...
	 * 
	 * @param shapeFile
	 * @param coverage
	 * @return ArrayList<Path2D>
	 */
	private static ArrayList<Path2D> getPathFromShapeFile(File shapeFile, GridCoverage2D coverage) {
		FileDataStore dataStore;
		try {
			shapeFile.setReadOnly();
			dataStore = FileDataStoreFinder.getDataStore(shapeFile);
			SimpleFeatureSource shapeFileSource = dataStore.getFeatureSource();
			
			SimpleFeatureCollection featuresCollection = shapeFileSource.getFeatures();
			SimpleFeatureIterator featuresIterator = featuresCollection.features();
			
			ArrayList<Path2D> resultSet = new ArrayList<Path2D>();
			
			while(featuresIterator.hasNext()){
				SimpleFeature feature = featuresIterator.next();
				
				Object object = feature.getAttribute(0);
				if (object instanceof MultiPolygon) {
					MultiPolygon polygon = (MultiPolygon) object;
					
					Coordinate[] coordinates = polygon.getCoordinates();
					Path2D path = new Path2D.Double();
					Coordinate firstCoordinate = coordinates[0];
					
					// get pixel position
					Point firstPoint = new Point(-1,-1);
					try {
						firstPoint = getPointByCoordinates(coverage, firstCoordinate.x, firstCoordinate.y);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					path.moveTo(firstPoint.x, firstPoint.y);
					for (int i = 1; i < coordinates.length; i++) {
						Coordinate coordinate = coordinates[i];
						
						Point pixelPoint = new Point(-1, -1);
						try {
							pixelPoint = getPointByCoordinates(coverage, coordinate.x, coordinate.y);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						path.lineTo(pixelPoint.x, pixelPoint.y);
					}
					path.closePath();
					
					resultSet.add(path);
				}
			}
			
			featuresIterator.close();
			dataStore.dispose();
			
			return resultSet;
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
