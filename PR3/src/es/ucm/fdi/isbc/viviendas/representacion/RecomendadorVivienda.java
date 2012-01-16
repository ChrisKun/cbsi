package es.ucm.fdi.isbc.viviendas.representacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import jcolibri.casebase.CachedLinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.DiverseByMedianRetrieval.ExpertClerkMedianScoring;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterBasedRetrievalMethod;
import jcolibri.method.retrieve.FilterBasedRetrieval.FilterConfig;
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
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;

public class RecomendadorVivienda extends Observable implements StandardCBRApplication {
	
	private static final double PESOLocaliz = 0.10;
	private static final double PESOTipo = 0.15;
	private static final double PESOSuperficie = 0.15;
	private static final double PESOHabitaciones = 0.08;
	private static final double PESOBanios = 0.03;
	private static final double PESOEstado = 0.12;
	private static final double PESOCoordenadas = 0.09;
	private static final double PESOPrecioM = 0.15;
	private static final double PESOPrecioZ = 0.15;
	private static final double PESOExtrasF = 0.05;
	private static final double PESOExtrasO = 0.07;
	private static final double PESOExtrasB = 0.01;
	
<<<<<<< .mine
	private double precio = 0;
	private double precioZona = 0;
	private double precioMedio = 0;
	private double habitaciones = 0;
	private double banios = 0;
	private double superficie = 0;
	
	public static final int NUMSELECTCASOS = 5;
	public static final int NUMDIVERSIDAD = 6;
=======
	public static final int NUMSELECTCASOS = 5;  //Numero de casos para la ventana result
	public static final int NUMCASOSBUSCADOS = 50;  //Numero de casos que se tienen en cuenta cuando buscamos
	public static final int NUMDIVERSIDAD = 6;  //Numero de casos para la ventana diversidad
>>>>>>> .r52
	
	private boolean once = true;
	

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
	private NNConfig simConfig;
	private Collection<CBRCase> cases;
	Controlador controlador;
	
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
		tree = new Arbol("/");
		
		precio = 0;
		precioZona = 0;
		precioMedio = 0;
		habitaciones = 0;
		banios = 0;
		superficie = 0;

		for (CBRCase c : cases) {
			
			DescripcionVivienda dV = (DescripcionVivienda)c.getDescription();
			tree.add(new Arbol(dV.getLocalizacion().toLowerCase()));
			
			precio += dV.getPrecio();
			precioZona += dV.getPrecioZona();
			precioMedio += dV.getPrecioMedio();
			habitaciones += dV.getHabitaciones();
			banios += dV.getBanios();
			superficie += dV.getSuperficie();
			
		}
		
		int size = cases.size();
		
		precio /= size;
		precioZona /= size;
		precioMedio /= size;
		habitaciones /= size;
		banios /= size;
		superficie /= size;
		
		return _caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {		 
		// Para configurar el KNN se utiliza un objeto NNConfig
		simConfig = new NNConfig();
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
		
		if (((DescripcionVivienda)query.getDescription()).getExtrasFinca() != null){
			 simConfig.addMapping(new Attribute("extrasFinca",DescripcionVivienda.class), new Average());	 
			 simConfig.addMapping(new Attribute("ascensor",ExtrasFinca.class), new Equal()); 
			 simConfig.addMapping(new Attribute("trastero",ExtrasFinca.class), new Equal()); 
			 simConfig.addMapping(new Attribute("energiaSolar",ExtrasFinca.class), new Equal());
			 simConfig.addMapping(new Attribute("servPorteria",ExtrasFinca.class), new Equal());
			 simConfig.addMapping(new Attribute("parkingComunitario",ExtrasFinca.class), new Equal());
			 simConfig.addMapping(new Attribute("garajePrivado",ExtrasFinca.class), new Equal());
			 simConfig.addMapping(new Attribute("videoportero",ExtrasFinca.class), new Equal());
		}
		if (((DescripcionVivienda)query.getDescription()).getExtrasBasicos() != null){
		 simConfig.addMapping(new Attribute("extrasBasicos",DescripcionVivienda.class), new Average());
		 simConfig.addMapping(new Attribute("lavadero",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("internet",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("microondas",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("horno",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("amueblado",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("cocinaOffice",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("parquet",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("domotica",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("armarios",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("tv",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("lavadora",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("electrodomesticos",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("suiteConBanio",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("puertaBlindada",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("gresCeramica",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("calefaccion",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("aireAcondicionado",ExtrasBasicos.class), new Equal());
		 simConfig.addMapping(new Attribute("nevera",ExtrasBasicos.class), new Equal());
		}
		if (((DescripcionVivienda)query.getDescription()).getExtrasOtros() != null){
		 simConfig.addMapping(new Attribute("extrasOtros",DescripcionVivienda.class), new Average());
		 simConfig.addMapping(new Attribute("patio",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("balcon",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("zonaDeportiva",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("zonaComunitaria",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("terraza",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("piscinaComunitaria",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("jardinPrivado",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("zonaInfantil",ExtrasOtros.class), new Equal());
		 simConfig.addMapping(new Attribute("piscina",ExtrasOtros.class), new Equal());
		}
		
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

		if (once) {
			// DIVERSOS
			// Obtenemos las viviendas diversas
			HashMap<Attribute,Double> thresholds = new HashMap<Attribute,Double>();
			thresholds.put(new Attribute("localizacion", DescripcionVivienda.class), 0.99);
			thresholds.put(new Attribute("tipo", DescripcionVivienda.class), 0.99);
			thresholds.put(new Attribute("superficie", DescripcionVivienda.class), superficie);
			thresholds.put(new Attribute("habitaciones", DescripcionVivienda.class), habitaciones);
			thresholds.put(new Attribute("banios", DescripcionVivienda.class), banios);
			thresholds.put(new Attribute("estado", DescripcionVivienda.class), 0.99);
			thresholds.put(new Attribute("coordenada", DescripcionVivienda.class), 0.99);
			thresholds.put(new Attribute("precio", DescripcionVivienda.class), precio);
			thresholds.put(new Attribute("precioMedio", DescripcionVivienda.class), precioMedio);
			thresholds.put(new Attribute("precioZona", DescripcionVivienda.class), precioZona);
			thresholds.put(new Attribute("extrasFinca", DescripcionVivienda.class), 0.99);
			thresholds.put(new Attribute("extrasBasicos", DescripcionVivienda.class), 0.99);
			thresholds.put(new Attribute("extrasOtros", DescripcionVivienda.class), 0.99);

			Collection<RetrievalResult> retrievedCases = ExpertClerkMedianScoring.getDiverseByMedian(_caseBase.getCases(), simConfig, thresholds);
			Collection<CBRCase> selectedCases = SelectCases.selectTopK(retrievedCases, NUMDIVERSIDAD);
			ArrayList<DescripcionVivienda> descrs = new ArrayList<DescripcionVivienda>();

			for (CBRCase nse: selectedCases)
				descrs.add((DescripcionVivienda) nse.getDescription());

			this.setChanged();
			this.notifyObservers(new MuestraSolEvent(descrs, "Diversos"));
			once = false;
		}
		else {
			// SIMILARES
			// Ejecutamos la recuperación por vecino más próximo
			Collection<RetrievalResult> eval = ParallelNNScoringMethod.evaluateSimilarityParallel(_caseBase.getCases(), query, simConfig);
			
			// Seleccionamos los k mejores casos
			cases = SelectCases.selectTopK(eval, NUMSELECTCASOS);
			
			// Guardamos los casos más similares
			ArrayList<DescripcionVivienda> descrs = new ArrayList<DescripcionVivienda>();
			
			for (CBRCase nse: cases)
				descrs.add((DescripcionVivienda) nse.getDescription());
			
			// Se los mandamos a la ventana de resultados
			this.setChanged();
			this.notifyObservers(new MuestraSolEvent(descrs, "Similares"));
		}

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

	
	/* Getters */
	
		public NNConfig getSimCongfig()
		{
			return simConfig;
		}
		
		public Collection<CBRCase> getCases()
		{
			return cases;
		}
		
		public CBRCaseBase getCaseBase()
		{
			return _caseBase;
		}
	
	/* Método principal MAIN */
	
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
		DescripcionVivienda descr = new DescripcionVivienda(-1);
		descr.setTipo(TipoVivienda.Apartamento);
		descr.setEstado(EstadoVivienda.Bien);		
		rv.repite(descr);

	}

	public void moreLikeThis(CBRQuery query, ArrayList<Integer> idYaVisitadas) {
		// Execute Filter
		cases = _caseBase.getCases();
		Collection<CBRCase> filtered = FilterBasedRetrievalMethod.filterCases(
				cases, query, new FilterConfig());

		Collection<RetrievalResult> retrievedCases = ParallelNNScoringMethod
				.evaluateSimilarityParallel(filtered, query,
						simConfig);

		ArrayList<DescripcionVivienda> aL = new ArrayList<DescripcionVivienda>();

		int cantidad = 0;
		Iterator<RetrievalResult> it = retrievedCases.iterator();
		while (it.hasNext() && cantidad < NUMSELECTCASOS){
			DescripcionVivienda descr = (DescripcionVivienda) it.next().get_case().getDescription();
			if (!idYaVisitadas.contains(descr.getId())) {
				aL.add(descr);
				cantidad++;
			}
		}
		this.setChanged();
		this.notifyObservers(new MuestraSolEvent(aL, "Similares"));
	}
	
	public void getNcasesDiverse(ArrayList<Integer> idYaVisitadas, int numCasos)
	{
		
	}
}
