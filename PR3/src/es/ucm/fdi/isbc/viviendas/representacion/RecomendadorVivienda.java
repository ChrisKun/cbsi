package es.ucm.fdi.isbc.viviendas.representacion;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.ParallelNNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Table;
import jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.eventos.MuestraSolEvent;
import es.ucm.fdi.isbc.funcSimilitud.MyCoordinateSimilarityFunction;
import es.ucm.fdi.isbc.funcSimilitud.SimilitudArbol;
import es.ucm.fdi.isbc.gui.VentanaPpal;
import es.ucm.fdi.isbc.viviendas.ViviendasConnector;

public class RecomendadorVivienda extends Observable implements StandardCBRApplication {
	
	final double PESOLocaliz = 0.10;
	final double PESOTipo = 0.15;
	final double PESOSuperficie = 0.15;
	final double PESOHabitaciones = 0.08;
	final double PESOBanios = 0.03;
	final double PESOEstado = 0.12;
	final double PESOCoordenadas = 0.09;
	final double PESOPrecioM = 0.15;
	final double PESOPrecioZ = 0.15;
	final double PESOExtrasF = 0.05;
	final double PESOExtrasO = 0.07;
	final double PESOExtrasB = 0.01;
	
	final int NUMSELECTCASOS = 5;

	/** Connector object */
	ViviendasConnector _connector;
	/** CaseBase object */
	CBRCaseBase _caseBase;
	/** Árbol de localización */
	//public static JTree tree;
	public static Arbol tree;
	
	private CBRQuery query;
	@SuppressWarnings("unused")
	private boolean evaluacionSistema;
	Controlador controlador;
	
	private PrintWriter fichAc;
	private PrintWriter fichFa;
	private PrintWriter fich;
	
	@Override
	public void configure() throws ExecutionException {
		try {
			// Crear el conector con la base de casos
			_connector = new ViviendasConnector();
			// Inicializar el conector con su archivo xml de configuración

			_caseBase = new CachedLinealCaseBase();
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		// Cargar los casos desde el conector a la base de casos
		_caseBase.init(_connector);
		// Imprimir los casos leídos
		// Puedes comentar las siguientes líneas una vez que funcione.
		Collection<CBRCase> cases = _caseBase.getCases();
		
		/*String l = "", barrio = "", zona = "", calle = "";
		String[] s = {};*/
		
		// Raíz árbol
		//DefaultMutableTreeNode top = new DefaultMutableTreeNode("Madrid");
		
		//tree = new JTree(top);
		tree = new Arbol("/");
		for (CBRCase c : cases) {
			
			DescripcionVivienda dV = (DescripcionVivienda)c.getDescription();
			tree.add(new Arbol(dV.getLocalizacion().toLowerCase()));
			
			//VentanaPpal.guardarImagenes(dV.getId(), dV.getUrlFoto());
			
				/*// System.out.println(c);
				barrio = "";
				zona = "";
				calle = "";
				l = ((DescripcionVivienda) c.getDescription()).getLocalizacion();
				
				//Dividimos el string en campos.
				s = l.split("/");
				switch (s.length) {
				// s[0] es dato Madrid.
				case 2:
					zona = s[1];
					break;
				case 3:
					zona = s[1];
					barrio = s[2];
					break;
				case 4:
					zona = s[1];
					barrio = s[2];
					calle = s[3];
					break;
				}*/
				
				/*System.out.println(((DescripcionVivienda) c.getDescription())
						.getId().toString()
						+ "\n--------------\n Zona: "
						+ zona
						+ "\n Barrio: " + barrio + "\n Calle: " + calle);
				*/
				
				//Creamos el nodo dentro del arbol
				//createNodes(top, zona, barrio, calle);
		}
			//System.out.println("Árbol creado");
		return _caseBase;
	}

	/*private void createNodes(DefaultMutableTreeNode top, String zona, String barrio, String calle) {
		DefaultMutableTreeNode zonas = null;
		DefaultMutableTreeNode barrios = null;
		DefaultMutableTreeNode calles = null;
		DefaultMutableTreeNode hijo = null, nieto = null, bisnieto = null;

		zonas = new DefaultMutableTreeNode(zona);
		if (!barrio.equals(""))
			barrios = new DefaultMutableTreeNode(barrio);
		if (!calle.equals(""))
			calles = new DefaultMutableTreeNode(calle);

		// Inicialmente el árbol no tiene nada
		if (top.getChildCount() == 0) {
			if (calles != null) barrios.add(calles);
			if (barrios != null) zonas.add(barrios);
			top.add(zonas);
		} else {
			int i = 0, j = 0, k = 0;
			boolean found = false;
			// Buscar zonas
			while (!found && i < top.getChildCount()) {
				hijo = (DefaultMutableTreeNode) top.getChildAt(i);
				// Si no está la zona, el barrio y la calle tampoco
				found = hijo.getUserObject().equals(zonas.getUserObject());
				i++;
			}
			if (!found) {
				if (calles != null) barrios.add(calles);
				if (barrios != null) zonas.add(barrios);
				top.add(zonas);
			} else if (barrios != null) {
				// Si está la zona hay que comprobar que no esté el barrio
				boolean encontrado = false;
				while (!encontrado && j < hijo.getChildCount()) {
					nieto = (DefaultMutableTreeNode) hijo.getChildAt(j);
					encontrado = nieto.getUserObject().equals(barrios.getUserObject());
					j++;
				}
				if (!encontrado) {
					// Si no está el barrio, la calle tampoco (si != null)
					if (calles != null)
						barrios.add(calles);
					hijo.add(barrios);
				} else if (calles != null) {
					// Si está el barrio hay que comprobar que no esté la calle
					boolean foundF = false;
					while (!foundF	&& k < nieto.getChildCount()) {
						bisnieto = (DefaultMutableTreeNode) nieto.getChildAt(k);
						foundF = bisnieto.getUserObject().equals(calles.getUserObject());
						k++;
					}
					if (!foundF) {
						// añadir calle al barrio indicado
						nieto.add(calles);
					}

				}

			}
		}

	}*/

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {	
		 // Para configurar el KNN se utiliza un objeto NNConfig
		 NNConfig simConfig = new NNConfig();
		 // Fijamos la función de similitud global
		 simConfig.setDescriptionSimFunction(new Average());
		
		 if (((DescripcionVivienda)query.getDescription()).getLocalizacion() != null)
			 simConfig.addMapping(new Attribute("localizacion", DescripcionVivienda.class), new SimilitudArbol(tree)/*new MyTreeSimilarityFunction(tree)*/);
		 // Fijamos las funciones de similitud locales
		 if (((DescripcionVivienda)query.getDescription()).getTipo() != null)
			 simConfig.addMapping(new Attribute("tipo",DescripcionVivienda.class), new Table("tablaTipoVivienda.txt"));
		 if (((DescripcionVivienda)query.getDescription()).getSuperficie() != null)
			 simConfig.addMapping(new Attribute("superficie",DescripcionVivienda.class), new Interval(5));
		 if (((DescripcionVivienda)query.getDescription()).getHabitaciones() != null)	 
			 simConfig.addMapping(new Attribute("habitaciones",DescripcionVivienda.class), new Interval(1));
		 if (((DescripcionVivienda)query.getDescription()).getBanios() != null) 
			 simConfig.addMapping(new Attribute("banios",DescripcionVivienda.class), new Equal());
		 if (((DescripcionVivienda)query.getDescription()).getEstado() != null) 
			 simConfig.addMapping(new Attribute("estado",DescripcionVivienda.class), new Table("tablaEstadoVivienda.txt"));
		 if (((DescripcionVivienda)query.getDescription()).getCoordenada() != null)
			 simConfig.addMapping(new Attribute("coordenada",DescripcionVivienda.class), new MyCoordinateSimilarityFunction());
		 if (((DescripcionVivienda)query.getDescription()).getPrecioMedio() != null) 
			 simConfig.addMapping(new Attribute("precioMedio",DescripcionVivienda.class), new Interval(500));
		 if (((DescripcionVivienda)query.getDescription()).getPrecioZona() != null)	 
			 simConfig.addMapping(new Attribute("precioZona",DescripcionVivienda.class), new Interval(500));
		 	 
		 
//		 simConfig.addMapping(new Attribute("extrasFinca",DescripcionVivienda.class), new Average());	 
//		 simConfig.addMapping(new Attribute("ascensor",ExtrasFinca.class), new Equal()); 
//		 simConfig.addMapping(new Attribute("trastero",ExtrasFinca.class), new Equal()); 
//		 simConfig.addMapping(new Attribute("energiaSolar",ExtrasFinca.class), new Equal());
//		 simConfig.addMapping(new Attribute("servPorteria",ExtrasFinca.class), new Equal());
//		 simConfig.addMapping(new Attribute("parkingComunitario",ExtrasFinca.class), new Equal());
//		 simConfig.addMapping(new Attribute("garajePrivado",ExtrasFinca.class), new Equal());
//		 simConfig.addMapping(new Attribute("videoportero",ExtrasFinca.class), new Equal());
//	 
//		 simConfig.addMapping(new Attribute("extrasBasicos",DescripcionVivienda.class), new Average());
//		 simConfig.addMapping(new Attribute("lavadero",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("internet",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("microondas",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("horno",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("amueblado",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("cocinaOffice",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("parquet",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("domotica",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("armarios",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("tv",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("lavadora",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("electrodomesticos",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("suiteConBanio",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("puertaBlindada",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("gresCeramica",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("calefaccion",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("aireAcondicionado",ExtrasBasicos.class), new Equal());
//		 simConfig.addMapping(new Attribute("nevera",ExtrasBasicos.class), new Equal());
//	 
//		 simConfig.addMapping(new Attribute("extrasOtros",DescripcionVivienda.class), new Average());
//		 simConfig.addMapping(new Attribute("patio",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("balcon",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("zonaDeportiva",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("zonaComunitaria",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("terraza",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("piscinaComunitaria",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("jardinPrivado",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("zonaInfantil",ExtrasOtros.class), new Equal());
//		 simConfig.addMapping(new Attribute("piscina",ExtrasOtros.class), new Equal());
	  
	 
		 // Es posible modificar el peso de cada atributo en la media ponderada.
		 // Por defecto el peso es 1.
	 
		 simConfig.setWeight(new Attribute("localizacion", DescripcionVivienda.class), PESOLocaliz);
		 simConfig.setWeight(new Attribute("tipo", DescripcionVivienda.class), PESOTipo);
		 simConfig.setWeight(new Attribute("superficie", DescripcionVivienda.class), PESOSuperficie);
		 simConfig.setWeight(new Attribute("habitaciones", DescripcionVivienda.class), PESOHabitaciones);
		 simConfig.setWeight(new Attribute("banios", DescripcionVivienda.class), PESOBanios);
		 simConfig.setWeight(new Attribute("estado", DescripcionVivienda.class), PESOEstado);
		 simConfig.setWeight(new Attribute("coordenada", DescripcionVivienda.class), PESOCoordenadas);
		 simConfig.setWeight(new Attribute("precioMedio", DescripcionVivienda.class), PESOPrecioM);
		 simConfig.setWeight(new Attribute("precioZona", DescripcionVivienda.class), PESOPrecioZ);
		 simConfig.setWeight(new Attribute("extrasFinca", DescripcionVivienda.class), PESOExtrasF);
		 simConfig.setWeight(new Attribute("extrasBasicos", DescripcionVivienda.class), PESOExtrasB);
		 simConfig.setWeight(new Attribute("extrasOtros", DescripcionVivienda.class), PESOExtrasO);
		 
		 // Ejecutamos la recuperación por vecino más próximo
		  Collection<RetrievalResult> eval = ParallelNNScoringMethod.evaluateSimilarityParallel(_caseBase.getCases(), query, simConfig);
		
		 // Seleccionamos los k mejores casos
		 eval = SelectCases.selectTopKRR(eval, NUMSELECTCASOS);
		 
		 // Guardamos los casos más similares
		 ArrayList<DescripcionVivienda> descrs = new ArrayList<DescripcionVivienda>();
				 
		 for (RetrievalResult nse: eval){
			 descrs.add((DescripcionVivienda) nse.get_case().getDescription());
		 }
		
		// Se los mandamos a la ventana de resultados
		this.setChanged();
		this.notifyObservers(new MuestraSolEvent(descrs));
//		COSAS PRACTICA ANTERIOR	
//		 // Aquí se incluiría el código para adaptar la solución
//		Integer precio_prediccion = getPrediccionPrecio(eval);
//		System.out.println("Prediccion precio: " + precio_prediccion);
//		double confianza_prediccion = calcularConfianza(eval);
//		System.out.println("Confianza: " + confianza_prediccion);
//		
//		if (evaluacionSistema){
//			
//			CBRCase casoReal = (CBRCase)query;
//			SolucionVivienda solucionReal = (SolucionVivienda)casoReal.getSolution();
//			Integer precio_real = solucionReal.getPrecio();
//			
//			// Hemos acertado con margen < 10000 --> prediccion == 1
//			double prediccion = 0.0;
//			if (Math.abs(precio_prediccion - precio_real) < 0.1*precio_real)
//				prediccion = 1.0;
//			
//			DescripcionVivienda descrip = (DescripcionVivienda) query.getDescription();
//
////			tester(prediccion, descrip, precio_real, precio_prediccion, confianza_prediccion);
//			
//			System.out.println((descrip).getId().toString()
//					+ "\n--------------"
//					+ "\nPrecio Estimado: " + Integer.toString(precio_prediccion)
//					+ "\nPrecio Real : " + Integer.toString(precio_real)
//					+ "\nPrediccion :"+ Double.toString(prediccion)+"\n-------------------------------");
//			
//			Evaluator.getEvaluationReport().addDataToSeries("Aciertos", prediccion);
//			Evaluator.getEvaluationReport().addDataToSeries("Confianza", confianza_prediccion);
//		}
//		else{
//			this.setChanged();
//			this.notifyObservers(new MuestraSolEvent((DescripcionVivienda)query.getDescription(), precio_prediccion, confianza_prediccion));
//		}
	}
	
	@SuppressWarnings("unused")
	private void tester(double prediccion, DescripcionVivienda descrip, Integer precio_real, 
			Integer precio_prediccion, double confianza_prediccion) {
		if(prediccion == 1.0)
			fich = fichAc;
		else
			fich = fichFa;
		
		fich.println("\n\n-------------------------------------------\n");
		fich.println("Tipo "+descrip.getTipo().toString()+"\n");
		fich.println("Estado "+descrip.getEstado().toString()+"\n");
		fich.println("Superficie "+String.valueOf(descrip.getSuperficie())+"\n");
		fich.println("Habitaciones "+String.valueOf(descrip.getHabitaciones())+"\n");
		fich.println("Banio "+String.valueOf(descrip.getBanios())+"\n");
		fich.println("Precio Medio "+String.valueOf(descrip.getPrecioMedio())+"\n");
		fich.println("Precio Zona "+String.valueOf(descrip.getPrecioZona())+"\n");
		fich.println("Localizacion "+String.valueOf(descrip.getLocalizacion())+"\n");
		fich.println("Latitud "+String.valueOf(descrip.getCoordenada().getLatitud())+"\n");
		fich.println("Longitud "+String.valueOf(descrip.getCoordenada().getLongitud())+"\n\n");
		fich.println("Prediccion: "+String.valueOf(precio_prediccion)+"\n");
		fich.println("Real: "+String.valueOf(precio_real)+"\n");
		fich.println("Confianza: "+String.valueOf(confianza_prediccion)+"\n");
	}

	@SuppressWarnings("unused")
	private double calcularConfianza(Collection<RetrievalResult> eval) {
		double total = 0;
		 for (RetrievalResult nse: eval){
			 total += nse.getEval();
		 }
		 return total / eval.size();
	}

	@SuppressWarnings("unused")
	private Integer getPrediccionPrecio(Collection<RetrievalResult> eval) {
		double pesos = 0; double total = 0;
		 for (RetrievalResult nse: eval){
			 pesos += nse.getEval();
			 total += ((SolucionVivienda)nse.get_case().getSolution()).getPrecio() * nse.getEval();
		 }
		 
		 return (int) Math.round(total / pesos);
	}

	public void inicia(){
		// Lanza el SGBD
		jcolibri.test.database.HSQLDBserver.init();

		try {
			// Configuración
			this.configure();
			// Preciclo
			this.preCycle();
			// Crear el objeto que almacena la consulta
			query = new CBRQuery();

		} catch (Exception e) {
			org.apache.commons.logging.LogFactory
					.getLog(RecomendadorVivienda.class);
		}		
		this.setChanged();
		this.notifyObservers();
		
	}
	
	public void fin(){
		// Apagar el SGBD
		jcolibri.test.database.HSQLDBserver.shutDown();
	}
	
	public void repite(DescripcionVivienda descr){
		// Deshabilitar GUI
		this.setChanged();
		this.notifyObservers();
		
		query.setDescription(descr);
		
		try {
			this.cycle(query);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		// Habilitar GUI
		this.setChanged();
		this.notifyObservers();	
	}

	public void postCycle() throws ExecutionException {
		this._caseBase.close();
	}

	public static void main(String[] args) {
		
		// Crear el objeto que implementa la aplicación CBR
		Controlador controlador = Controlador.getInstance();
		RecomendadorVivienda rv = new RecomendadorVivienda();		
		controlador.setRecomendadorVivienda(rv);		
		VentanaPpal v = new VentanaPpal();

		rv.addObserver(v);
		
		// Configura y realiza el precycle
		rv.inicia();
		
		// Obtener kNN viviendas diversas e introducirlas en el panelDiversidad

	}
	
	
	/*public JTree getLocalizaciones() {
		return tree;
	}*/
}
