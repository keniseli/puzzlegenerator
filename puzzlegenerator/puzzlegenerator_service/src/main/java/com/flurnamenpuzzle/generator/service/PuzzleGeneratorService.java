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
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.GeneralParameterValue;

import com.flurnamenpuzzle.generator.PuzzleGeneratorProgressStructure;
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
	private static final String PUZZLE_PIECE_FILE_NAME_PATTERN = "%s//shape-%d.%s";
	private final static String PUZZLE_PIECE_FILE_TYPE = "png";
	private static final int POLYGON_SHAPE_FEATURE_ATTRIBUTE_INDEX = 0;

	private final PuzzleGeneratorProgressStructure puzzleGeneratorProgressStructure;
	private final XMLService xmlService;
	ShapeService shapeService;

	public PuzzleGeneratorService(PuzzleGeneratorProgressStructure puzzleGeneratorProgressStructure) {
		this.puzzleGeneratorProgressStructure = puzzleGeneratorProgressStructure;
		xmlService = new XMLService();
	}

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
	 * @param destinationFilePath
	 *            the path to save the puzzle to.
	 * @return The created puzzle.
	 */
	public Puzzle generatePuzzle(String stateShapeFilePath, String stateName, String fieldShapeFilePath,
			String tifFilePath, String destinationFilePath) {
		Puzzle puzzle = new Puzzle();
		updateProgress(0);

		List<SimpleFeature> featuresInState = getFeaturesOfFieldsInState(stateShapeFilePath, stateName,
				fieldShapeFilePath);
		if (puzzleGeneratorProgressStructure.isAbortGeneration()) {
			return null;
		}

		GridCoverage2D coverage = getCoverageOfTifFile(tifFilePath);

		List<File> pieces = savePiecesImages(destinationFilePath, featuresInState, coverage);
		puzzle.setImages(pieces);
		if (puzzleGeneratorProgressStructure.isAbortGeneration()) {
			return null;
		}
		puzzleGeneratorProgressStructure.setPercentageGenerated(100);
		String pathToXmlFile = String.format("%s%s%s", destinationFilePath, File.separatorChar, "puzzle.xml");
		File temporaryXmlFile = new File(pathToXmlFile);
		xmlService.saveXML(pathToXmlFile);
		puzzle.setXmlFile(temporaryXmlFile);
		return puzzle;
	}

	/**
	 * Determines the shapes of the fields in a state with the given name.
	 * 
	 * @param stateShapeFilePath
	 *            the shapefile containing the state shapes.
	 * @param stateName
	 *            the name of the target state shape (inside the
	 *            stateShapeFile).
	 * @param fieldShapeFilePath
	 *            the shapefile containing the fields.
	 * @return the {@link List} of the {@link SimpleFeature field shapes} inside
	 *         the state.
	 */
	private List<SimpleFeature> getFeaturesOfFieldsInState(String stateShapeFilePath, String stateName,
			String fieldShapeFilePath) {
		// get feature of the state
		shapeService = new ShapeService();
		SimpleFeature featureOfState = shapeService.getFeatureOfShapeFileByName(stateShapeFilePath, stateName);
		if (puzzleGeneratorProgressStructure.isAbortGeneration()) {
			return null;
		}

		// get features of the fields
		File fieldShapeFile = new File(fieldShapeFilePath);
		SimpleFeatureCollection fieldFeaturesCollection = shapeService.getFeaturesOfShapeFile(fieldShapeFile);
		SimpleFeature[] featuresArray = (SimpleFeature[]) fieldFeaturesCollection.toArray();
		List<SimpleFeature> features = Arrays.asList(featuresArray);

		// get features of the fields that are located in the state
		List<SimpleFeature> featuresInState = shapeService.filterContainingFeaturesOfFeature(featureOfState, features);
		if (puzzleGeneratorProgressStructure.isAbortGeneration()) {
			return null;
		}

		if (featuresInState.size() == 0) {
			throw new ServiceException("No fields found that are located in the area of the state.");
		}
		return featuresInState;
	}

	/**
	 * Reads the {@link GridCoverage2D} of the given tifFilePath.
	 */
	private GridCoverage2D getCoverageOfTifFile(String tifFilePath) {
		File tifFile = new File(tifFilePath);
		AbstractGridFormat format = GridFormatFinder.findFormat(tifFile);
		if (format instanceof UnknownFormat) {
			throw new ServiceException("Could not find the GeoTIFF format.");
		}
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
		return coverage;
	}

	/**
	 * Generates the puzzle pieces and saves them to the destinationFilePath.
	 * 
	 * @param destinationFilePath
	 *            the path where the files should be saved to.
	 * @param featuresInState
	 *            the field shapes. Each and every one of them will be a puzzle
	 *            piece.
	 * @param mapCoverage
	 *            the coverage of the tif file that represents the map material.
	 * @return the created puzzle pieces as {@link List} of {@link File Files}.
	 */
	private List<File> savePiecesImages(String destinationFilePath, List<SimpleFeature> featuresInState,
			GridCoverage2D mapCoverage) {
		PlanarImage renderedMapImage = (PlanarImage) mapCoverage.getRenderedImage();
		BufferedImage mapImage = renderedMapImage.getAsBufferedImage();

		for (int i = 0; i < featuresInState.size(); i++) {
			xmlService.addPiece(i, shapeService.getNameOfFeature(featuresInState.get(i)), "", "", "", "", "");
		}

		List<Path2D> piecesShapes = getPathsFromShapes(featuresInState, mapCoverage);

		int numberOfShapes = piecesShapes.size();

		List<File> puzzlePieces = new ArrayList<>();
		for (int i = 0; i < numberOfShapes; i++) {
			Path2D path = piecesShapes.get(i);
			BufferedImage newImage = getPuzzlePieceImage(mapImage, path);

			File puzzlePiece = createPuzzlePiece(destinationFilePath, i, newImage);
			if (puzzlePiece != null) {
				puzzlePieces.add(puzzlePiece);
			}
			if (puzzleGeneratorProgressStructure.isAbortGeneration()) {
				return null;
			}
			double percentageValue = ((double) i / numberOfShapes) * 100;
			updateProgress(((int) percentageValue));
			if (puzzlePiece != null) {
				xmlService.updatePiece(i, null, puzzlePiece.getName(), "", "", "", "");
			}
		}
		return puzzlePieces;
	}

	/**
	 * Determines the {@link Path2D Paths} from the given features on the
	 * {@link GridCoverage2D}.
	 * 
	 * @param featuresInState
	 *            the features to get the path representations of.
	 * @param coverage
	 *            the {@link GridCoverage2D} to be referenced for determining
	 *            the coordinate {@link Point points}
	 * @return the determined coordinate {@link Point points} connected to a
	 *         {@link Path2D}
	 */
	private List<Path2D> getPathsFromShapes(List<SimpleFeature> featuresInState, GridCoverage2D coverage) {
		List<Path2D> pathsFromShapes = new ArrayList<Path2D>();
		for (SimpleFeature feature : featuresInState) {
			Path2D path = getPathFromShape(coverage, feature);
			if (path != null) {
				pathsFromShapes.add(path);
			}
			if (puzzleGeneratorProgressStructure.isAbortGeneration()) {
				return null;
			}
		}
		return pathsFromShapes;
	}

	/**
	 * Reads the given {@link SimpleFeature} and uses its points to build a
	 * {@link Path2D} on the coverage (of the tif file).
	 * 
	 * @param coverage
	 *            the {@link GridCoverage2D} of the tif file.
	 * @param feature
	 *            the feature to get the {@link Path2D} of.
	 * @return the {@link Path2D} representing the feaure on the tif file.
	 */
	private Path2D getPathFromShape(GridCoverage2D coverage, SimpleFeature feature) {
		Path2D path = new Path2D.Double();
		Object object = feature.getAttribute(POLYGON_SHAPE_FEATURE_ATTRIBUTE_INDEX);
		if (object instanceof MultiPolygon) {
			MultiPolygon polygon = (MultiPolygon) object;

			Coordinate[] coordinates = polygon.getCoordinates();

			Coordinate firstCoordinate = coordinates[0];
			Point firstPoint = getPointByCoordinates(coverage, firstCoordinate.x, firstCoordinate.y);
			path.moveTo(firstPoint.x, firstPoint.y);

			for (int i = 1; i < coordinates.length; i++) {
				Coordinate coordinate = coordinates[i];

				Point pixelPoint = new Point(-1, -1);
				pixelPoint = getPointByCoordinates(coverage, coordinate.x, coordinate.y);
				path.lineTo(pixelPoint.x, pixelPoint.y);
				if (puzzleGeneratorProgressStructure.isAbortGeneration()) {
					return null;
				}
			}
			path.closePath();
			return path;
		}
		return null;
	}

	/**
	 * Maps coordinates to the given {@link GridCoverage2D coverage} in order to
	 * create a {@link Point} representing the longitude and latitude on the
	 * coverage object.
	 * 
	 * @return the {@link Point} that represents the given coordinates on the
	 *         given coverage.
	 */
	private Point getPointByCoordinates(GridCoverage2D coverage, double longitude, double latitude) {
		// read width and height of tiff image
		int tifImageHeight = coverage.getRenderedImage().getHeight();
		int tifImageWidth = coverage.getRenderedImage().getWidth();

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
		double coordinatesPerPixelX = tifImageWidth / xAxisWidth;
		double coordinatesPerPixelY = tifImageHeight / yAxisHeight;

		// calculate point where pixels are
		int pixelX = (int) Math.round(longtidudeOffset * coordinatesPerPixelX);
		int pixelY = (int) Math.round(latitudeOffset * coordinatesPerPixelY);

		return new Point(pixelX, pixelY);
	}

	/**
	 * Writes a new {@link BufferedImage} with the given bufferedImage with
	 * modifications:<br />
	 * <ul>
	 * <li>transparent background</li>
	 * <li>clipped by the given path</li>
	 * <li>size cropped to the minimum</li>
	 * </ul>
	 * 
	 * @param bufferedImage
	 *            the image to be the source material of the new image.
	 * @param path
	 *            the template to the new image.
	 * @return the clipped image
	 */
	private BufferedImage getPuzzlePieceImage(BufferedImage bufferedImage, Path2D path) {
		BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D newGraphics = newImage.createGraphics();
		newGraphics.setColor(new Color(0, 0, 0, 0));
		newGraphics.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
		newGraphics.setClip(path);
		newGraphics.drawImage(bufferedImage, 0, 0, null);
		newGraphics.dispose();
		return newImage;
	}

	/**
	 * Creates a puzzle piece. That means it trims the given image to a minimum
	 * and writes it to the given folder.
	 * 
	 * @param destinationFilePath
	 *            the path to the folder where the image should be saved to.
	 * @param counter
	 *            by business rules the images should have a numerical naming.
	 *            This parameter is the number of the created piece.
	 * @param image
	 *            the {@link BufferedImage} containing the puzzle piece image.
	 * @return the newly created puzzle piece as {@link File}.
	 */
	private File createPuzzlePiece(String destinationFilePath, int counter, BufferedImage image) {
		File puzzlePiece = null;
		try {
			BufferedImage trimmedImage = trimToTheMinimum(image);
			if (trimmedImage != null) {
				String pathname = String.format(PUZZLE_PIECE_FILE_NAME_PATTERN, destinationFilePath, counter,
						PUZZLE_PIECE_FILE_TYPE);
				puzzlePiece = new File(pathname);
				ImageIO.write(trimmedImage, PUZZLE_PIECE_FILE_TYPE, puzzlePiece);
			}
		} catch (ServiceException | IOException e) {
			e.printStackTrace();
		}
		return puzzlePiece;
	}

	private BufferedImage trimToTheMinimum(BufferedImage image) {
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

	private void updateProgress(int percentage) {
		puzzleGeneratorProgressStructure.setPercentageGenerated(percentage);
	}

}
