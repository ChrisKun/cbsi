package es.ucm.fdi.isbc.viviendas.representacion;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.recommendation.casesDisplay.DisplayCasesTableMethod;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.isbc.viviendas.ViviendasConnector;
import funcSimilitud.MyTreeSimilarityFunction;

public class RecomendadorVivienda implements StandardCBRApplication {

	/** Connector object */
	ViviendasConnector _connector;
	/** CaseBase object */
	CBRCaseBase _caseBase;
	/** Árbol de localización */
	private JTree tree;

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
		 simConfig.addMapping(new Attribute("HolidayType",DescripcionVivienda.class), new Equal());
		 simConfig.addMapping(new Attribute("NumberOfPersons",
		 DescripcionVivienda.class), new Interval(12));
		 simConfig.addMapping(new Attribute("Region",DescripcionVivienda.class), new Equal());
		 simConfig.addMapping(new Attribute("Transportation",DescripcionVivienda.class), new Equal());
		 simConfig.addMapping(new Attribute("Duration",DescripcionVivienda.class), new Interval(21));
		 simConfig.addMapping(new Attribute("Season",DescripcionVivienda.class), new Equal());
		 simConfig.addMapping(new Attribute("Accommodation",DescripcionVivienda.class), new Equal());
		
		 // Es posible modificar el peso de cada atributo en la media ponderada.
		 // Por defecto el peso es 1.
		  simConfig.setWeight(new Attribute("localizacion", DescripcionVivienda.class), 0.5);
		
		 // Ejecutamos la recuperación por vecino más próximo
		  Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query,
		 simConfig);
		
		 // Seleccionamos los k mejores casos
		 eval = SelectCases.selectTopKRR(eval, 5);
		
		 // Imprimimos el resultado del k-NN y obtenemos la lista de casos recuperados
		 Collection<CBRCase> casos = new ArrayList<CBRCase>();
		 System.out.println("Casos Recuperados: ");
		 for (RetrievalResult nse: eval){
			 System.out.println(nse);
			 casos.add(nse.get_case());
		 }
		
		 // Aquí se incluiría el código para adaptar la solución
		
		 // Sólamente mostramos el resultado
		 DisplayCasesTableMethod.displayCasesInTableBasic(casos);
	}

	@Override
	public void postCycle() throws ExecutionException {
		this._caseBase.close();
	}

	public static void main(String[] args) {
		// Lanzar el SGBD
		jcolibri.test.database.HSQLDBserver.init();

		// Crear el objeto que implementa la aplicación CBR
		RecomendadorVivienda rv = new RecomendadorVivienda();
		try {
			// Configuración
			rv.configure();
			// Preciclo
			rv.preCycle();
			// Crear el objeto que almacena la consulta
			CBRQuery query = new CBRQuery();
			// TODO: Id vivienda? Pongo 0 de momento
			query.setDescription(new DescripcionVivienda(960));
			// Mientras que el usuario quiera (se muestra la ventana de
			// continuar)
			do {
				// Obtener los valores de la consulta
//				ObtainQueryWithFormMethod.obtainQueryWithInitialValues(query, null, null);
				// Ejecutar el ciclo
				rv.cycle(query);
			} while (JOptionPane.showConfirmDialog(null, "Continuar?") == JOptionPane.OK_OPTION);
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory
					.getLog(RecomendadorVivienda.class);
		}

		// Apagar el SGBD
		jcolibri.test.database.HSQLDBserver.shutDown();
	}

}
