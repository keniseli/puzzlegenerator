package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShapeServiceTest {
	private ShapeService shapeService;
	
	@Before
	public void setUp() {
		shapeService = new ShapeService();
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

		URL url = ShapeServiceTest.class.getResource("file/No_Flurname_A.shp");
		String pathToShapeFile = url.getFile();
		File shapeFile = new File(pathToShapeFile);
		List<String> namesOfShapeFile = shapeService.getNamesOfShapeFile(shapeFile);
		namesOfShapeFile.forEach(name -> {
			Assert.assertTrue(expectedNames.contains(name));
		});
	}

}