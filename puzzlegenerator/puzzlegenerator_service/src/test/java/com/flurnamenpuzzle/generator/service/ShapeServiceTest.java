package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class ShapeServiceTest {
	private ShapeService shapeService;
	
	@Before
	public void setUp() {
		shapeService = new ShapeService();
	}

	@Test
	public void testFilterContainingFeaturesOfFeature() throws IOException {
		// arrange
		List<Integer> expectedIndices = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7,
				8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
				24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
				40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55,
				56, 57, 58, 59, 60, 61, 62, 63, 64, 83, 84, 85, 86, 87, 88, 89,
				90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103,
				104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115,
				116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127,
				128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
				140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151,
				152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163,
				164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175,
				176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187,
				188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199,
				200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
				212, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294,
				295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306,
				307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318,
				319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330,
				331, 332, 333, 334, 335, 336, 337, 469, 470, 471, 472, 473,
				474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485,
				486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497,
				498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509,
				510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521,
				522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533,
				534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545,
				546, 547, 548, 549, 550);

		URL urlFlurnamen = ShapeServiceTest.class.getResource("/No_Flurname_A.shp");
		String pathToFlurnamenShapeFile = urlFlurnamen.getFile();
		File flurnamenShapeFile = new File(pathToFlurnamenShapeFile);
		flurnamenShapeFile.setReadOnly();
		FileDataStore dataStoreFlurnamen = FileDataStoreFinder.getDataStore(flurnamenShapeFile);
		SimpleFeatureSource shapeFileSourceFlurnamen = dataStoreFlurnamen.getFeatureSource();
		dataStoreFlurnamen.dispose();
		SimpleFeatureCollection flurnamenSimpleFeatureCollection = shapeFileSourceFlurnamen.getFeatures();
		List<SimpleFeature> flurnamenFeatures = Arrays.asList((SimpleFeature[]) (flurnamenSimpleFeatureCollection.toArray()));

		URL urlGemeinde = ShapeServiceTest.class.getResource("/ExportPerimeter.shp");
		String pathToGemeindeShapeFile = urlGemeinde.getFile();
		File gemeindeShapeFile = new File(pathToGemeindeShapeFile);
		gemeindeShapeFile.setReadOnly();
		FileDataStore dataStoreGemeinde = FileDataStoreFinder.getDataStore(gemeindeShapeFile);
		SimpleFeatureSource shapeFileSourceGemeinde = dataStoreGemeinde.getFeatureSource();
		dataStoreGemeinde.dispose();
		SimpleFeatureCollection gemeindeSimpleFeatureCollection = shapeFileSourceGemeinde.getFeatures();
		List<SimpleFeature> gemeindeFeatures = Arrays.asList((SimpleFeature[]) (gemeindeSimpleFeatureCollection.toArray()));
		SimpleFeature gemeindeFeature = gemeindeFeatures.get(0);

		// act
		List<SimpleFeature> filteredFeatures = shapeService.filterContainingFeaturesOfFeature(gemeindeFeature, flurnamenFeatures);

		// assert
		Assert.assertTrue(filteredFeatures.size() == expectedIndices.size());
		for (int i : expectedIndices) {
			SimpleFeature currentFeature = flurnamenFeatures.get(i);
			Assert.assertTrue(filteredFeatures.contains(currentFeature));
		}
	}

	@Test
	public void testGetNamesOfShapes() {
		List<String> expectedNames = Arrays.asList("Im Bansteibode", "Galgenacher", "Steigrube", "Talweid",
				"Schlossacher", "Holde", "Bahnholz", "Rebhübel", "Hornbergli", "Weiermatt", "Schuelmatte", "Wüeri",
				"Widenmatt", "Dorf", "Rausen", "Neuhof", "Duttli", "Sackmatt", "Bünten", "Im Chilchgässli",
				"Buttenberg", "Flüeli", "Gassacher", "Liederweg", "Lauch", "Churzstück", "Ebnet", "Ischlag", "Rüti",
				"Bapur", "Dorfmatt", "Buckterflue", "Birch", "Bummelen", "Lör", "Ischlag", "Lättacher", "Rüti",
				"Schlossmatt", "Homberg", "Hombergchöpfli", "Rütiflue", "Hochrüti", "Schwizer", "In der Weid",
				"Neumatt", "Wannenacher", "Wannenweid", "Wannenmatt", "Reben", "Loch", "Birchacher", "Ischlag",
				"Schützenmatt", "Rüti", "Hürstenrain", "Brünnliacher", "Im langen Hag", "Hornacher", "Horn",
				"Luftmatt", "Eselweg", "Chatzerugge", "Ei", "Eirain", "Burenweid", "Löhr", "Gotthard", "Hopsen",
				"Rehhag", "Eiholden", "Glanzmatt", "Gritt", "Steinholden", "In der Wältschi", "Anstaltsmatte", "Mutti",
				"Rebholde", "Horn", "Zwei", "Summerau", "Schiffländi", "Schöftleteholde", "In den Matten", "Dorf",
				"Ramsach", "Bäbiloch", "Zehnten", "Grütsch", "Heirichen", "Weidli", "Gstad", "Bannsteinboden",
				"Schibli", "Anger", "Händschenmatt", "Horn", "Schneelismatt", "Fetzenberg", "Büelen", "Ischlag",
				"Wegacher", "Zwidenacher", "Schuelland", "Zägelen", "Zmüsli", "Bächleten", "Isental", "Stumpli",
				"Holden", "Cheibacher", "In den Reben", "Weid", "Zündli", "Homberg", "Büntenacher", "Obermatt",
				"Chirsgarten", "Mühlacher", "Fotzli", "Brunnig", "Hufligacher", "Reben", "Chessler", "In der Weid",
				"Himmelsgrund", "Rütli", "Lören", "Chapf", "Ruedispitz", "Eienmatt", "Im Eich", "Mühleten", "Gforli",
				"Aspenlon", "Tannenacher", "Schnidermatt", "Zmatten", "Steinig", "Rütihof", "Zlinden", "Buebenloch",
				"In der Matte", "Rüti", "Grundweid", "Herrüti", "Stolten", "Chrintal", "Bergmatten", "Lind",
				"Zehntenhübel", "Ischlag", "Geisshorn", "Steinrüti", "Hasmatt", "Mittlerer Wisenberg", "Wisenberg",
				"Hombergchöpfli", "Teufleten", "Häberlingen", "Flueacher", "Unter Ramsach", "Grosse Buechen",
				"Winterholden", "Meieracker", "Im Rütteli", "Im Hau", "Emdacker", "Kaisterstein", "Tauschacker",
				"Riedmatt", "Bauflen", "Erlibrunn", "Bummelen", "In der Ei", "Eiloch", "Felli", "Wittematt",
				"Witteacker", "Strassacker", "Leuwieden", "Kleinacker", "Eichbühl", "Kürze", "Hürlig", "Grüebliacker",
				"Langacker", "Breitacker", "Ringgacher", "Neuacker", "Bodenacher", "Breitenweg", "Buesgenacker",
				"Hard", "Buesgen", "Sonnenberg", "Vordermatt", "Dorf", "Ischlag", "Ägerten", "Dörlimatt",
				"Stapflenhof", "Stapflenacher", "Gassacher", "Neumatt", "Hinrauft", "Sackacher", "Sackmatten",
				"Ebenmatt", "Joggismatt", "Herrenrain", "Winkel", "Lör", "Brämenländ", "Fohrenweid", "Dietschi",
				"Fraumatt", "Cholholz", "Chliacher", "Baders Weid", "Bachtelen", "Länzmatt", "Linggrütt", "Hündler",
				"Chatzenruggen", "Ebenländ", "Riederweid", "Zelgli", "Rappenflue", "Ischlag", "Hasengatter",
				"Grossdietisberg", "Hard", "Chellermatt", "Rappenflueholz", "Höll", "Hagmatt", "Leber",
				"Vorderer Wisenberg", "Choltbrunnen", "Müllersweid", "Brodchorb", "Leimacher", "Leimgrueben", "Höchi",
				"Eggflue", "Eggwald", "Neuhusmatt", "Schlossholz", "Vorderi Eiholden", "Langmatt", "Weid", "Tellen",
				"Hinderi Eiholden", "Räckholder", "Rotacher", "Chremerrüti", "Schleipfimatt", "Winterholden",
				"Rotacherforen", "Eiholden", "Schloss", "Schlossholden", "Chalberweid", "Chatzbach", "Teichmatt",
				"Neuhus", "Hirzenfeld", "Husmatt", "Ramsachacher", "Tränenbänkli", "Dünkelweg", "Homberg",
				"Langenweid", "Hombergchöpfli", "Schlossholz", "Schlossmatt", "Papur", "Hagacher", "Falkensteinacher",
				"Sevogelacher", "Chillenmatt", "Mühli", "Chrätziger", "Eimatt", "Rebenrain", "Risiholden",
				"Eselholden", "Schöfftlen", "Hornberg", "Chrindel", "Grundweid", "Aspenlon", "Stegacher", "Eichhölzli",
				"Alti Rüti", "Moosmatt", "Chalchofen", "Gelterkinderberg", "Vrenenweid", "Im Ärgeli", "Breiti",
				"Pilgerruh", "Summeraumatte", "Weid", "Schürenrain", "Im Winkel", "Chrindeldamm", "Schmittli",
				"Eselflue", "Chrummi Rüti", "Hinterrüti", "Höchi", "Wälschmätteli", "Nebikerhof", "Trülle",
				"Mettenberg", "Rappenflue", "Holdenacher", "Chrindelholden", "Bodenrüti", "Hohwacht", "Schürenacher",
				"Allmend", "Sennmatt", "Lättacher", "Eich", "Hasenloch", "Eichbüel", "Langmatt", "Hohrain", "Weier",
				"Pfarrmatt", "Rümlingen", "Ringlichen", "Grüenmatt", "Chamber", "Mühlacher", "Hasel", "Mättenacher",
				"Oltechbrunn", "Bann", "Föhrlen", "Baderhölzli", "Egg", "Schibler", "Sommerholden", "Nebenhag",
				"Vorvortel", "Rämetslei", "Langenrüti", "Heubaum", "Gatterrüti", "Steingrueben", "Chrümbler", "Gröt",
				"Vorstein", "Geren", "Steinholz", "Chätzlisgraben", "Chrindelholden", "Riemetstuden", "Rauft",
				"Eichli", "Riemetboden", "Riemetmatt", "Pfäffli", "Ried", "Eselholden", "Rauftholden", "Horn",
				"Schöffleten", "Untergrüt", "Untergrieden", "Chrindel", "Stierengraben", "Stolten", "Beerholden",
				"Weiermatt", "Halbetsmatt", "Dubel", "Dietschi", "Cholholz", "Chürzi", "Schöni", "Dürrenhübel",
				"Hofstetten", "Weid", "Lingental", "Lören", "Chapf", "Hofmatt", "Rütihof", "Länz", "Berg",
				"Teufenboden", "Weidli", "Gigerhübel", "Fraumatt", "Ostergäu", "Löli", "Giessen", "Oltschloss",
				"Hollengraben", "Weid", "Grüt", "Obergrüt", "Schlatt", "Dürrenmatt", "Hellmatt", "Unterdorfmatten",
				"Unterdorf", "Grund", "Linggrüt", "Olthus", "Hundsbrunn", "Pfandacher", "Allmend", "Stockenmatt",
				"Breiten", "Grueben", "Oberdorf", "Schulgasse", "Mitteldorf", "Rosen", "Linden", "Rätzmatt", "Leier",
				"Zil", "Voreimatt", "Station", "Rüsselmatt", "Geissmatte", "Geissgrabe", "Cholholz", "Talacherweid",
				"Talacher", "Leisimatt", "Altbergweid", "Rintelweid", "Rintel", "Brunnenhof", "Weid", "Bälweid",
				"Grafenrüti", "Uebechs", "Aechleten", "Uf Bäl", "Langholden", "Heuberg", "Altberg", "Buechmatt",
				"Riedlisholdenrain", "Stutz", "Unter-Gisiberg", "Gümschen", "Frauenacher", "Schönegg", "Wasserbaum",
				"Holden", "Brand", "Gisiberg", "Breiten", "Bergacher", "Unteri Weid", "Weidli", "Oberg", "Fluerain",
				"Burenweid", "Hinterholz", "Sunneberg", "Eichholde", "Holche", "Altweg", "Langmatte", "Talbode",
				"Stälzenacher", "Stöckliacher", "Breiteweg", "Heidacher", "Oberdorf", "Hympelrain", "Delle",
				"Schweizi", "Hölstegrabe", "Asp", "Chüngeligrund", "Zwange", "Weiermatt", "Ielte", "Bureweid",
				"Heuberg", "Chindliste", "Chalchofe", "Mülleracher", "Birch", "Büechli", "Daholde", "Lochacher",
				"Hofacher", "Scheidig", "Fohr", "Buesge", "Wälschacher", "Löracher", "Ägerte", "Weid", "Neuweg",
				"Tämpel", "Unterdorf", "Bodenacher", "Widacher", "Holibaum", "Hinterhag", "Äugstler", "Obermatt",
				"Lache", "Bolzacher", "Chamber", "Vogelmättli", "Schore", "Schorematt", "Hämisbüel", "Moos", "Breite",
				"Schuelland", "Grabe", "Föhrliacher", "Chnächtlisacher", "Rottannehölzli", "Tschattnau", "Dürrhübel",
				"Räbe", "Unterfur", "Rötle", "Höchi", "Grossacher", "Ischlag", "Langerüti", "Weid", "Steigruebe",
				"Wasserbaum", "Giesgerain", "Barmerain", "Rosenau", "Räberain", "Munimatt", "Gmeiniweid", "Schoreberg",
				"Giesge", "Chäbismatt", "Mapprech", "Hofmatt", "Wolstel", "Rüti", "Geisshörnli", "Chleiacher",
				"Baufle", "Mättebol", "Giess", "Erzweid", "Langholde", "Muelte", "Chilpe", "Schuechrüti", "Rintel",
				"Chläberain", "Forerain", "Erliacher", "Erli", "Duschacher", "Rössliberg", "Hard", "Schneitweg",
				"Gross-Dietisberg", "Rossweid", "Forehübel");

		URL url = ShapeServiceTest.class.getResource("/No_Flurname_A.shp");
		String pathToShapeFile = url.getFile();
		File shapeFile = new File(pathToShapeFile);
		List<String> namesOfShapeFile = shapeService.getNamesOfShapeFile(shapeFile);
		namesOfShapeFile.forEach(name -> {
			Assert.assertTrue(expectedNames.contains(name));
		});
	}

}