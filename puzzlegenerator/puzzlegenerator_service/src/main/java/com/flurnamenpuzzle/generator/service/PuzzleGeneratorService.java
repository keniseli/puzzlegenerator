package com.flurnamenpuzzle.generator.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.GeneralParameterValue;

import com.flurnamenpuzzle.generator.domain.Puzzle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * This class is responsible for generating the puzzle. For this purpose it
 * controls the {@link ShapeService} to do its job.
 *
 */
public class PuzzleGeneratorService {

	private static final int MINIMUM_SIZE_OF_PUZZLE_PIECE = 5;

	/**
	 * Generates the {@link Puzzle}. The shapes are identified and then used to
	 * crop the tif file. <br />
	 * All found shapes from the field shapefile inside the boundaries of the
	 * state shapefile are used to crop the tif file. Therefore the shapefiles
	 * have to be consistent (from the geo-information-perspective) with each
	 * other <b>and</b> with the tif file.
	 * 
	 * @param stateShapeFilePath
	 *            The shapefile containing the states. This file has to contain
	 *            a shape with the given stateName.
	 * @param stateName
	 *            The name of the shape that represents the border of the
	 *            puzzle.
	 * @param fieldShapeFilePath
	 *            The shapefile path of the shapefile containing the puzzle
	 *            pieces.
	 * @param tifFilePath
	 *            The geotif file path that contains the card material necessary
	 *            to get an actual image.
	 * @return The created puzzle. Its images are saved as Files in a temp
	 *         Folder and have to be moved!
	 */
	public Puzzle generatePuzzle(String stateShapeFilePath, String stateName, String fieldShapeFilePath,
			String tifFilePath, String destinationFilePath) {
		Puzzle puzzle = new Puzzle();

		List<File> images = getPieces(stateShapeFilePath, stateName, fieldShapeFilePath, tifFilePath,
				destinationFilePath);
		puzzle.setImages(images);
		return puzzle;
	}

	/**
	 * Crops the necessary images for the puzzle and saves the files into a
	 * temporary folder.
	 * 
	 * @param stateShapeFilePath
	 *            Path to the shapefile containing the states, respectively the
	 *            possible puzzle borders.
	 * @param fieldShapeFilePath
	 *            Path to the shapefile containing the fields, respectively the
	 *            puzzle pieces.
	 * @param tifFilePath
	 *            Path to the tif file containing the card material.
	 * @return A {@link List} of png {@link File Files}. Each of them represents
	 *         a puzzle piece.
	 */
	private List<File> getPieces(String stateShapeFilePath, String stateName, String fieldShapeFilePath,
			String tifFilePath, String destinationFilePath) {

		ShapeService shapeService = new ShapeService();
		SimpleFeature featureOfState = shapeService.getFeatureOfShapeFileByName(stateShapeFilePath, stateName);

		File fieldShapeFile = new File(fieldShapeFilePath);
		SimpleFeatureCollection fieldFeaturesCollection = shapeService.getFeaturesOfShapeFile(fieldShapeFile);
		SimpleFeature[] featuresArray = (SimpleFeature[]) fieldFeaturesCollection.toArray();
		List<SimpleFeature> features = Arrays.asList(featuresArray);
		List<SimpleFeature> featuresInState = shapeService.filterContainingFeaturesOfFeature(featureOfState, features);

		File tifFile = new File(tifFilePath);
		AbstractGridFormat format = GridFormatFinder.findFormat(tifFile);
		AbstractGridCoverage2DReader reader = format.getReader(tifFile);

		String coverageName = reader.getGridCoverageNames()[0];
		GeneralParameterValue[] params = {};
		GridCoverage2D coverage = null;
		try {
			coverage = reader.read(coverageName, params);
		} catch (IllegalArgumentException | IOException e) {
			throw new ServiceException(e, "There was an error reading the coverage of the tif file.");
		}
		reader.dispose();

		PlanarImage renderedImage = (PlanarImage) coverage.getRenderedImage();
		BufferedImage bufferedImage = renderedImage.getAsBufferedImage();

		List<Path2D> res = getPathFromShapes(featuresInState, coverage);

		int count = 1;
		List<File> pieces = new ArrayList<>();
		for (Path2D path : res) {
			BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D newGraphics = newImage.createGraphics();
			newGraphics.setColor(new Color(0, 0, 0, 0));
			newGraphics.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
			newGraphics.setClip(path);
			newGraphics.drawImage(bufferedImage, 0, 0, null);

			newGraphics.dispose();

			try {
				BufferedImage trimmedImage = trim(newImage);
				if (trimmedImage != null) {
					String pathname = String.format("%s//shape-%d.png", destinationFilePath, count);
					File puzzlePiece = new File(pathname);
					ImageIO.write(trimmedImage, "png", puzzlePiece);
					pieces.add(puzzlePiece);
				}
			} catch (ServiceException | IOException e) {
				e.printStackTrace();
			}
			count++;
		}
		return pieces;
	}

	private List<Path2D> getPathFromShapes(List<SimpleFeature> featuresInState, GridCoverage2D coverage) {
		ArrayList<Path2D> resultSet = new ArrayList<Path2D>();

		for (SimpleFeature feature : featuresInState) {
			Object object = feature.getAttribute(0);
			if (object instanceof MultiPolygon) {
				MultiPolygon polygon = (MultiPolygon) object;

				Coordinate[] coordinates = polygon.getCoordinates();
				Path2D path = new Path2D.Double();
				Coordinate firstCoordinate = coordinates[0];

				// get pixel position
				Point firstPoint = new Point(-1, -1);
				try {
					firstPoint = getPointByCoordinates(coverage, firstCoordinate.x, firstCoordinate.y);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
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

		return resultSet;
	}

	private Point getPointByCoordinates(GridCoverage2D coverage, double longitude, double latitude) throws Exception {
		// read width and height of tiff image
		int tHeight = coverage.getRenderedImage().getHeight();
		int tWidth = coverage.getRenderedImage().getWidth();

		// read boundaries of tiff file
		double yAxisOrigin = coverage.getEnvelope2D().getBounds2D().getY();
		double xAxisOrigin = coverage.getEnvelope2D().getBounds2D().getX();
		double yAxisHeight = coverage.getEnvelope2D().getHeight();
		double xAxisWidth = coverage.getEnvelope2D().getWidth();
		double yAxisMaxY = coverage.getEnvelope2D().getMaxY();
		double xAxisMaxX = coverage.getEnvelope2D().getMaxX();

		// check if provided coordinates are available
		if (!(longitude >= xAxisOrigin && longitude <= xAxisMaxX && latitude >= yAxisOrigin && latitude <= yAxisMaxY)) {
			throw new ServiceException("Provided coordinates not found within tiff file");
		}

		// calculate coordinate offset from origin
		double longtidudeOffset = longitude - xAxisOrigin;
		double latitudeOffset = yAxisMaxY - latitude;

		// calculate pixel/coordinate relation
		double coordinatesPerPixelX = tWidth / xAxisWidth;
		double coordinatesPerPixelY = tHeight / yAxisHeight;

		// calculate point where pixels are
		int pixelX = (int) Math.round(longtidudeOffset * coordinatesPerPixelX);
		int pixelY = (int) Math.round(latitudeOffset * coordinatesPerPixelY);

		return new Point(pixelX, pixelY);
	}

	private BufferedImage trim(BufferedImage image) {
		try {
			int x1 = Integer.MAX_VALUE;
			int y1 = Integer.MAX_VALUE;
			int x2 = 0;
			int y2 = 0;
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			for (int x = 0; x < imageWidth; x++) {
				for (int y = 0; y < imageHeight; y++) {
					int argb = image.getRGB(x, y);
					if (argb != 0) {
						x1 = Math.min(x1, x);
						y1 = Math.min(y1, y);
						x2 = Math.max(x2, x);
						y2 = Math.max(y2, y);
					}
				}
			}
			WritableRaster raster = image.getRaster();
			ColorModel colorModel = image.getColorModel();
			int newWidth = x2 - x1;
			int newHeight = y2 - y1;
			if (newWidth >= MINIMUM_SIZE_OF_PUZZLE_PIECE && newHeight >= MINIMUM_SIZE_OF_PUZZLE_PIECE) {
				raster = raster.createWritableChild(x1, y1, newWidth, newHeight, 0, 0, null);
				return new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
			}
		} catch (Exception e) {
			throw new ServiceException("Could not crop image.");
		}
		return null;
	}

}
