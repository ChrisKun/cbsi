package es.ucm.fdi.isbc.viviendas.representacion;

import java.util.Collection;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.LeaveOneOutEvaluator;
import jcolibri.evaluation.tools.EvaluationResultGUI;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Table;
import jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.funcSimilitud.MyTreeSimilarityFunction;
import es.ucm.fdi.isbc.gui.Gui;
import es.ucm.fdi.isbc.viviendas.ViviendasConnector;

public class RecomendadorVivienda extends Observable implements StandardCBRApplication {

	/** Connector object */
	ViviendasConnector _connector;
	/** CaseBase object */
	CBRCaseBase _caseBase;
	/** Árbol de localización */
	private JTree tree;
	private CBRQuery query;
	private boolean evaluacionSistema;
	
	public RecomendadorVivienda(){
		evaluacionSistema = false;
	}
	
	public RecomendadorVivienda(boolean evalSis){
		evaluacionSistema = evalSis;
	}
	

	@Override
	public void configure() throws ExecutionException {
		try {
			// Crear el conector con la base de casos
			_connector = new ViviendasConnector();
			// Inicializar el conector con su archivo xml de configuración
			// En este caso no hace falta porque carga los casos mediante
			// retrieveAllCases
			// La organización en memoria será lineal
			_caseBase = new LinealCaseBase();
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
		String l = "", barrio = "", zona = "", calle = "";
		String[] s = {};
		// Raíz árbol
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Madrid");
		tree = new JTree(top);
		for (CBRCase c : cases) {
			// System.out.println(c);
			barrio = "";
			zona = "";
			calle = "";
			l = ((DescripcionVivienda) c.getDescription()).getLocalizacion();
			// Construimos el árbol
			s = l.split("/");
			switch (s.length) {
			// s[0] es caca
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
			}
			System.out.println(((DescripcionVivienda) c.getDescription())
					.getId().toString()
					+ "\n--------------\n Zona: "
					+ zona
					+ "\n Barrio: " + barrio + "\n Calle: " + calle);
			createNodes(top, zona, barrio, calle);
		}
		System.out.println("Árbol creado");
		return _caseBase;
	}

	private void createNodes(DefaultMutableTreeNode top, String zona,
			String barrio, String calle) {
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

	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {	
		 // Para configurar el KNN se utiliza un objeto NNConfig
		 NNConfig simConfig = new NNConfig();
		 // Fijamos la función de similitud global
		 simConfig.setDescriptionSimFunction(new Average());
		
		 simConfig.addMapping(new Attribute("localizacion", DescripcionVivienda.class) ,new MyTreeSimilarityFunction(tree));
		 // Fijamos las funciones de similitud locales
		 simConfig.addMapping(new Attribute("tipo",DescripcionVivienda.class), new Table("tablaTipoVivienda.txt"));
		 simConfig.addMapping(new Attribute("superficie",DescripcionVivienda.class), new Interval(5));
		 simConfig.addMapping(new Attribute("habitaciones",DescripcionVivienda.class), new Interval(1));
		 simConfig.addMapping(new Attribute("banios",DescripcionVivienda.class), new Equal());
		 simConfig.addMapping(new Attribute("estado",DescripcionVivienda.class), new Table("tablaEstadoVivienda.txt"));
//		 simConfig.addMapping(new Attribute("coordenada",DescripcionVivienda.class), new Interval(50));
		 simConfig.addMapping(new Attribute("precioMedio",DescripcionVivienda.class), new Interval(10000));
		 simConfig.addMapping(new Attribute("precioZona",DescripcionVivienda.class), new Interval(10000));
		 
		 simConfig.addMapping(new Attribute("extrasFinca",DescripcionVivienda.class), new Average());
		 simConfig.addMapping(new Attribute("ascensor",ExtrasFinca.class), new Equal());
		 simConfig.addMapping(new Attribute("trastero",ExtrasFinca.class), new Equal());
		 simConfig.addMapping(new Attribute("energiaSolar",ExtrasFinca.class), new Equal());
		 simConfig.addMapping(new Attribute("servPorteria",ExtrasFinca.class), new Equal());
		 simConfig.addMapping(new Attribute("parkingComunitario",ExtrasFinca.class), new Equal());
		 simConfig.addMapping(new Attribute("garajePrivado",ExtrasFinca.class), new Equal());
		 simConfig.addMapping(new Attribute("videoportero",ExtrasFinca.class), new Equal());
		 
				 
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
		 
		 // Es posible modificar el peso de cada atributo en la media ponderada.
		 // Por defecto el peso es 1.
		 simConfig.setWeight(new Attribute("localizacion", DescripcionVivienda.class), 10.0);
		 simConfig.setWeight(new Attribute("tipo", DescripcionVivienda.class), 10.0);
		 simConfig.setWeight(new Attribute("superficie", DescripcionVivienda.class), 7.0);
		 simConfig.setWeight(new Attribute("habitaciones", DescripcionVivienda.class), 6.0);
		 simConfig.setWeight(new Attribute("banios", DescripcionVivienda.class), 5.0);
		 simConfig.setWeight(new Attribute("estado", DescripcionVivienda.class), 10.0);
		 simConfig.setWeight(new Attribute("coordenada", DescripcionVivienda.class), 10.0);
		 simConfig.setWeight(new Attribute("precioMedio", DescripcionVivienda.class), 15.0);
		 simConfig.setWeight(new Attribute("precioZona", DescripcionVivienda.class), 15.0);
		 
		 // Ejecutamos la recuperación por vecino más próximo
		  Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		 // Seleccionamos los k mejores casos
		 eval = SelectCases.selectTopKRR(eval, 5);
		
		 // Aquí se incluiría el código para adaptar la solución
		Integer precio_prediccion = getPrediccionPrecio(eval);
		System.out.println("Prediccion precio: " + precio_prediccion);
		double confianza_prediccion = calcularConfianza(eval);
		System.out.println("Confianza: " + confianza_prediccion);
		
		if (evaluacionSistema){
			
			CBRCase casoReal = (CBRCase)query;
			SolucionVivienda solucionReal = (SolucionVivienda)casoReal.getSolution();
			Integer precio_real = solucionReal.getPrecio();
			
			// Hemos acertado con margen < 10000 --> prediccion == 1
			double prediccion = 0.0;
			if (Math.abs(precio_prediccion - precio_real) < 10000)
				prediccion = 1.0;
			
			Evaluator.getEvaluationReport().addDataToSeries("Aciertos", prediccion);
			Evaluator.getEvaluationReport().addDataToSeries("Confianza", confianza_prediccion);
		}
		else{
			String s = "Predicción precio: "+ precio_prediccion+ "\n"+ "Confianza: "+ confianza_prediccion;
			JOptionPane.showMessageDialog(null, s, "Tasador", JOptionPane.INFORMATION_MESSAGE); 
		}
	}
	
	private double calcularConfianza(Collection<RetrievalResult> eval) {
		double total = 0;
		 for (RetrievalResult nse: eval){
			 total += nse.getEval();
		 }
		 return total / eval.size();
	}

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
		if (!evaluacionSistema){
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void fin(){
		// Apagar el SGBD
		jcolibri.test.database.HSQLDBserver.shutDown();
	}
	
	public void repite(DescripcionVivienda descr){
		// Obtener los valores de la consulta
		query.setDescription(descr);
		
		// Ejecutar el ciclo
		try {
			if (!evaluacionSistema) this.cycle(query);
			else{
				// Validación cruzada
				LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
				eval.init(new RecomendadorVivienda());
				eval.LeaveOneOut();
				
				Vector<Double> vectorAciertos = Evaluator.getEvaluationReport().getSeries("Aciertos");
				double media = 0.0;
				for (Double acierto: vectorAciertos)
					media += acierto;
				media = media / (double)Evaluator.getEvaluationReport().getNumberOfCycles();
				
				System.out.println(Evaluator.getEvaluationReport().toString());
				EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "Evaluación Tasador", false);
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if (!evaluacionSistema) {
			this.setChanged();
			this.notifyObservers();
		}

		
	}

	@Override
	public void postCycle() throws ExecutionException {
		this._caseBase.close();
	}

	public static void main(String[] args) {
		// Crear el objeto que implementa la aplicación CBR
		RecomendadorVivienda rv = new RecomendadorVivienda();
		Controlador controlador = new Controlador(rv);
		Gui gui = new Gui(controlador);
		gui.setVisible(true);
		rv.addObserver(gui);
		rv.inicia();
		
		// Evaluacion
//		RecomendadorVivienda rv = new RecomendadorVivienda(true);
//		rv.inicia();
//		rv.repite(null);
	}

}
