package es.ucm.fdi.isbc.viviendas;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import jcolibri.datatypes.Text;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import es.ucm.fdi.isbc.viviendas.representacion.Coordenada;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.ExtrasBasicos;
import es.ucm.fdi.isbc.viviendas.representacion.ExtrasFinca;
import es.ucm.fdi.isbc.viviendas.representacion.ExtrasOtros;



/**
 * Clase encargada de analizar los archivos html y generar una representaciÃ³n adecuada de la informaciÃ³n
 * @author Juan A. Recio
 * @version 1.0 25-11-2011
 */
public class PisoParser {

	String code;
	
	int num=0;

	BufferedWriter out;
	
	HashSet<String> conjunto;
	
	
	
	public HashSet<String> getConjunto() {
		return conjunto;
	}

	void init(String file) throws IOException 
	{
		this.out = new BufferedWriter(new FileWriter(file));
		num=0;
		conjunto = new HashSet<String>();
	}
	
	void deInit() throws IOException
	{
		this.out.close();
		System.out.println("Archivos analizados: "+num);
	}
	
	
	public void parse(String file)
	{
		try {
			
			//System.out.println(file);
			
			Parser parser = new Parser (file);
		
			DescripcionVivienda vivienda = new DescripcionVivienda(num);
			
			NodeList list = parser.parse(new HasAttributeFilter("class","title g13_24"));
			vivienda.setTitulo(list.elementAt(0).getChildren().elementAt(0).getText().trim());
			//System.out.println(vivienda.getTitulo());
			String cad = vivienda.getTitulo().substring(0,vivienda.getTitulo().indexOf(" en ")).trim();
			conjunto.add(cad);
			TipoVivienda tipo = TipoVivienda.Piso;
			if(cad.equals("Ã�tico")) tipo = TipoVivienda.Apartamento;
			else if(cad.equals("Planta baja")) tipo = TipoVivienda.Plantabaja;
			else if(cad.equals("Piso")) tipo = TipoVivienda.Piso;
			else if(cad.equals("Loft")) tipo = TipoVivienda.Loft;
			else if(cad.equals("Casa adosada")) tipo = TipoVivienda.Casaadosada;
			else if(cad.equals("Casa-Chalet")) tipo = TipoVivienda.CasaChalet;
			else if(cad.equals("DÃºplex")) tipo = TipoVivienda.Duplex;
			else if(cad.equals("Estudio")) tipo = TipoVivienda.Estudio;
			else if(cad.equals("Finca rÃºstica")) tipo = TipoVivienda.Fincarustica;
			else if(cad.equals("Apartamento")) tipo = TipoVivienda.Apartamento;
			vivienda.setTipo(tipo);
				
			parser.reset();
			list = parser.parse(new HasAttributeFilter("rel","canonical"));   //new TagNameFilter("link"));
			TagNode tag = (TagNode)list.elementAt(0);
			String url = tag.getAttribute("href");
			vivienda.setUrl(url);
			//System.out.println(vivienda.getUrl());
			
			parser.reset();
			list = parser.parse(new HasAttributeFilter("id","ctl00_content1_uie_pnl_BreadCrumb"));
			list = list.extractAllNodesThatMatch(new TagNameFilter("a"),true);
			LinkTag lnode = (LinkTag)  list.elementAt(list.size()-1);
			cad = lnode.extractLink().substring(22);
			if(cad.contains("/listado"))
				cad = cad.substring(0, cad.indexOf("/listado"));
			if(cad.contains("/filtro"))
				cad = cad.substring(0, cad.indexOf("/filtro"));
			vivienda.setLocalizacion(cad);
			//System.out.println(vivienda.getLocalizacion());
			
			parser.reset();
			ImageTag img = (ImageTag)parser.parse(new HasAttributeFilter("id","ctl00_imgMain")).elementAt(0);
			vivienda.setUrlFoto(img.extractImageLocn());
			//System.out.println(vivienda.getUrlFoto());
			
			parser.reset();
			Node node = parser.parse(new HasAttributeFilter("id","ctl00_ddSurface")).elementAt(0);
			cad = node.getFirstChild().getText().trim();
			cad = cad.substring(0, cad.indexOf('&'));
			vivienda.setSuperficie(Integer.valueOf(cad));
			//System.out.println(vivienda.getSuperficie());
			
			parser.reset();
			node = parser.parse(new HasAttributeFilter("id","ctl00_ddRoom")).elementAt(0);
			cad = node.getFirstChild().getText().trim();
			vivienda.setHabitaciones(Integer.valueOf(cad));
			
			parser.reset();
			node = parser.parse(new HasAttributeFilter("id","ctl00_ddBath")).elementAt(0);
			cad = node.getFirstChild().getText().trim();
			vivienda.setBanios(Integer.valueOf(cad));
			
			parser.reset();
			node = parser.parse(new HasAttributeFilter("id","ctl00_ddStatus")).elementAt(0);
			cad = node.getFirstChild().getText().trim();
			//conjunto.add(cad);
			EstadoVivienda estado = EstadoVivienda.Bien;
			if(cad.equals("Muy bien")) estado = EstadoVivienda.Muybien;			
			else if(cad.equals("Reformado")) estado = EstadoVivienda.Reformado;
			else if(cad.equals("A reformar")) estado = EstadoVivienda.Areformar;
			else if(cad.equals("Casi nuevo")) estado = EstadoVivienda.Casinuevo;
			else if(cad.equals("Bien")) estado = EstadoVivienda.Bien;

			vivienda.setEstado(estado);
			
			
			parser.reset();
			node = parser.parse(new HasAttributeFilter("id","ctl00_ddDescription")).elementAt(0);
			cad = node.getChildren().extractAllNodesThatMatch(new TagNameFilter("p"),true).elementAt(0).getFirstChild().getText();
			vivienda.setDescripcion(new Text(cad.trim().toLowerCase().replace('\n', '.')));
//			System.out.println(vivienda.getDescripcion());
			
			parser.reset();
			node = parser.parse(new StringFilter("mapLat")).elementAt(0);
			cad = node.toHtml();
			String[] values = cad.split(";");
			Coordenada coordenada = new Coordenada();
			vivienda.setCoordenada(coordenada);
			for(String val : values)
			{
				if(val.contains("mapLat"))
				{
					String[] parts = val.split(" ");
					coordenada.setLatitud(Double.valueOf(parts[parts.length-1]));
				}
				if(val.contains("mapLng"))
				{
					String[] parts = val.split(" ");
					coordenada.setLongitud(Double.valueOf(parts[parts.length-1]));
				}
			}
			//System.out.println(vivienda.getCoord_lat()+","+vivienda.getCoord_long());
			
			parser.reset();
			node = parser.parse(new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("id","ctl00_AveragePriceSection"))).elementAt(0);
			list = node.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("class","title bold"),true);
			
			vivienda.setPrecio(Integer.valueOf(list.elementAt(0).getFirstChild().getText().trim().split(" ")[0].replaceAll("\\.", "")));
			vivienda.setPrecioMedio(Integer.valueOf(list.elementAt(1).getFirstChild().getText().trim().split(" ")[0].replaceAll("\\.", "")));
			vivienda.setPrecioZona(Integer.valueOf(list.elementAt(2).getFirstChild().getText().trim().split(" ")[0].replaceAll("\\.", "")));
			
			vivienda.setExtrasFinca(analizaExtrasFinca(parser));
			vivienda.setExtrasBasicos(analizaExtrasBasicos(parser));
			vivienda.setExtrasOtros(analizaExtrasOtros(parser));
			
			
			out.write(vivienda.toString());
			out.newLine();
		
			num++;
			
			System.out.println(vivienda.toString());
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	private ExtrasFinca analizaExtrasFinca(Parser parser) throws Exception {
		
		ExtrasFinca ef = new ExtrasFinca(num);
		
		parser.reset();
		Node node = parser.parse(new HasAttributeFilter("id","ctl00_rptBuildingExtra_ctl01_lblExtra")).elementAt(0);
		ef.setAscensor(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptBuildingExtra_ctl02_lblExtra")).elementAt(0);
		ef.setTrastero(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptBuildingExtra_ctl03_lblExtra")).elementAt(0);
		ef.setEnergiaSolar(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptBuildingExtra_ctl04_lblExtra")).elementAt(0);
		ef.setServPorteria(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptBuildingExtra_ctl05_lblExtra")).elementAt(0);
		ef.setParkingComunitario(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptBuildingExtra_ctl06_lblExtra")).elementAt(0);
		ef.setGarajePrivado(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptBuildingExtra_ctl07_lblExtra")).elementAt(0);
		ef.setVideoportero(!node.toHtml().contains("ico16 hasnt"));

		return ef;
		
	}
	
	
	private ExtrasBasicos analizaExtrasBasicos(Parser parser) throws Exception {
		
		ExtrasBasicos eb = new ExtrasBasicos(num);
		
		parser.reset();
		Node node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl01_lblExtra")).elementAt(0);
		eb.setLavadero(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl02_lblExtra")).elementAt(0);
		eb.setTv(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl03_lblExtra")).elementAt(0);
		eb.setInternet(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl04_lblExtra")).elementAt(0);
		eb.setNevera(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl05_lblExtra")).elementAt(0);
		eb.setMicroondas(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl06_lblExtra")).elementAt(0);
		eb.setLavadora(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl07_lblExtra")).elementAt(0);
		eb.setHorno(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl08_lblExtra")).elementAt(0);
		eb.setElectrodomesticos(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl09_lblExtra")).elementAt(0);
		eb.setAmueblado(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl10_lblExtra")).elementAt(0);
		eb.setSuiteConBanio(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl11_lblExtra")).elementAt(0);
		eb.setCocinaOffice(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl12_lblExtra")).elementAt(0);
		eb.setPuertaBlindada(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl13_lblExtra")).elementAt(0);
		eb.setParquet(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl14_lblExtra")).elementAt(0);
		eb.setGresCeramica(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl15_lblExtra")).elementAt(0);
		eb.setDomotica(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl16_lblExtra")).elementAt(0);
		eb.setCalefaccion(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl17_lblExtra")).elementAt(0);
		eb.setArmarios(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptInteriorExtra_ctl18_lblExtra")).elementAt(0);
		eb.setAireAcondicionado(!node.toHtml().contains("ico16 hasnt"));

		return eb;
		
	}
	
	private ExtrasOtros analizaExtrasOtros(Parser parser) throws Exception {
		
		ExtrasOtros eo = new ExtrasOtros(num);
		
		parser.reset();
		Node node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl01_lblExtra")).elementAt(0);
		eo.setPatio(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl02_lblExtra")).elementAt(0);
		eo.setBalcon(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl03_lblExtra")).elementAt(0);
		eo.setZonaDeportiva(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl04_lblExtra")).elementAt(0);
		eo.setZonaComunitaria(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl05_lblExtra")).elementAt(0);
		eo.setTerraza(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl06_lblExtra")).elementAt(0);
		eo.setPiscinaComunitaria(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl07_lblExtra")).elementAt(0);
		eo.setJardinPrivado(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl08_lblExtra")).elementAt(0);
		eo.setZonaInfantil(!node.toHtml().contains("ico16 hasnt"));

		parser.reset();
		node = parser.parse(new HasAttributeFilter("id","ctl00_rptExteriorExtra_ctl09_lblExtra")).elementAt(0);
		eo.setPiscina(!node.toHtml().contains("ico16 hasnt"));

		return eo;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			PisoParser parser = new PisoParser();
			parser.init("viviendas");

			// TODO Cambiar la ruta a donde estÃ© la base casos
			
			File dir= new File("/datos3-ext/datos/basescasos/viviendas");
			for(String file: dir.list())
				parser.parse("/datos3-ext/datos/basescasos/viviendas/"+file);
			
			
			System.out.println(parser.getConjunto());
			
			parser.deInit();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error main");
			e.printStackTrace();
		}
	}

}
