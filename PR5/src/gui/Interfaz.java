package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;

public class Interfaz extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/** Atributos **/
	private JPanel panelResultado;
	private JButton cargarFotoBoton;
	private JButton marcarFotoBoton;
	private JButton botonBuscar;
	private JRadioButton radio1, radio2, radio3;
	private Galeria galeria;

	private JPanel panelFotos;

	private static OntoBridge ob;
	private static OntologyDocument mainOnto;

	/** Constructores **/

	public Interfaz() {
		super("Ontología");
		galeria = new Galeria();
		this.setJMenuBar(getMiJMenuBar());
		abrir();

		this.getContentPane().add(getPanelPrincipal());
		this.setPreferredSize(new Dimension(900, 600));
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this
				.getPreferredSize().width / 2);
		int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this
				.getPreferredSize().height / 2);
		this.setLocation(x, y);
		this.setVisible(true);
		this.pack();

	}

	private JMenuBar getMiJMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu archivo = new JMenu("Archivo");
		JMenuItem abrir = new JMenuItem("Abrir");
		JMenuItem salir = new JMenuItem("Salir");

		menuBar.add(archivo);
		archivo.add(abrir);
		archivo.addSeparator();
		archivo.add(salir);

		abrir.addActionListener(this);
		salir.addActionListener(this);

		return menuBar;
	}

	private JSplitPane getPanelPrincipal() {
		JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		panel.setDividerSize(0);
		panel.setLeftComponent(getPanelOntologia());
		panel.setRightComponent(getPanelResultado());

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Marcar", this.getPanelMarcar());
		tabbedPane.addTab("Buscar", this.getPenalBuscar());
		panelResultado.add(tabbedPane);
		return panel;
	}

	private JSplitPane getPenalBuscar() {
		JSplitPane panelPpalBuscar = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane panelFotosBoton = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		panelFotosBoton.setDividerSize(0);
		panelPpalBuscar.setDividerSize(0);

		botonBuscar = new JButton("OK");
		botonBuscar.addActionListener(this);
		JPanel panelConsultas = new JPanel(new GridLayout(3, 1));

		// set border and layout
		Border emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		Border titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Consultas");
		Border compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		panelConsultas.setBorder(compoundBorder);

		ButtonGroup grupo = new ButtonGroup();
		radio1 = new JRadioButton(
				"Actores que hayan participado en más de una película");
		radio2 = new JRadioButton("Escenas con objetos violentos");
		radio3 = new JRadioButton(
				"Personajes que aparecen alguna serie o película española");
		grupo.add(radio1);
		grupo.add(radio2);
		grupo.add(radio3);
		radio1.setSelected(true);

		panelConsultas.add(radio1);
		panelConsultas.add(radio2);
		panelConsultas.add(radio3);
		panelPpalBuscar.setTopComponent(panelConsultas);

		panelFotos = new JPanel();
		panelFotos.setPreferredSize(new Dimension(500, 350));

		panelFotosBoton.setTopComponent(panelFotos);
		JPanel panelBoton = new JPanel();
		panelBoton.add(botonBuscar);
		panelFotosBoton.setBottomComponent(panelBoton);

		panelPpalBuscar.setBottomComponent(panelFotosBoton);

		return panelPpalBuscar;
	}

	private JSplitPane getPanelMarcar() {
		JSplitPane panelPpalMarcado = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		panelPpalMarcado.setDividerSize(0);
		JSplitPane panelBotonFotos = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		panelBotonFotos.setDividerSize(0);

		cargarFotoBoton = new JButton("Cargar foto");
		cargarFotoBoton.addActionListener(this);
		marcarFotoBoton = new JButton("Marcar foto");
		marcarFotoBoton.addActionListener(this);

		JPanel panelBoton = new JPanel();
		panelBoton.add(cargarFotoBoton);
		panelBotonFotos.setTopComponent(panelBoton);
		JPanel panelFotos = new JPanel();
		panelFotos.setPreferredSize(new Dimension(500, 350));
		panelBotonFotos.setBottomComponent(panelFotos);

		panelPpalMarcado.setTopComponent(panelBotonFotos);
		JLabel label = new JLabel("Opciones de marcado: ");
		JComboBox comboRelaciones = new JComboBox();
		JComboBox comboIndividuos = new JComboBox();
		JPanel panelOpcionesMarcado = new JPanel(new GridLayout(1, 4));
		panelOpcionesMarcado.setPreferredSize(new Dimension(500, 20));

		panelOpcionesMarcado.add(label);
		panelOpcionesMarcado.add(comboRelaciones);
		panelOpcionesMarcado.add(comboIndividuos);
		panelOpcionesMarcado.add(marcarFotoBoton);

		JPanel panel = new JPanel();
		panel.add(panelOpcionesMarcado);

		panelPpalMarcado.setBottomComponent(panel);

		return panelPpalMarcado;
	}

	private JPanel getPanelOntologia() {
		JPanel panel = new JPanel();

		PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(ob,
				true);
		tree.setPreferredSize(new Dimension(300, 500));
		panel.add(tree);

		return panel;

	}

	private JPanel getPanelResultado() {
		panelResultado = new JPanel();
		panelResultado.setPreferredSize(new Dimension(300, 600));

		return panelResultado;
	}

	private void abrir() {

		// Creamos el objeto de OntoBridge
		ob = new OntoBridge();

		// Inicializamos el objeto de OntoBridge con un razonador Pellet
		ob.initWithPelletReasoner();

		JOptionPane.showMessageDialog(this,
				"Selecciona el archivo para la ontología principal",
				"Ontología principal", JOptionPane.INFORMATION_MESSAGE);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Archivos .owl", "owl");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		boolean seleccionCorrecta = fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION;
		boolean extensionCorrecta = false;
		File file = null;
		if (seleccionCorrecta) {
			file = fileChooser.getSelectedFile();
			extensionCorrecta = file.getName().endsWith(".owl");
		}

		if (seleccionCorrecta && extensionCorrecta) {
			// Se mete la ontologia principal.
			String rutaOnto = "file:" + file.getPath();
			mainOnto = new OntologyDocument(rutaOnto, rutaOnto);

			// Cargar la ontologia
			ob.loadOntology(mainOnto, new ArrayList<OntologyDocument>(), false);

		} else {
			JOptionPane.showMessageDialog(this,
					"La ontología introducida no es válida", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Salir")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Abrir")) {
			abrir();
		} else if (e.getSource().equals(botonBuscar)) {
			if (radio1.isSelected()) {
				mostrarActoresEnMasDeUnaPelicula();
			} else if (radio2.isSelected()) {
				mostrarEscenasConObjetosViolentos();
			} else if (radio3.isSelected()) {
				mostrarPersonajesEnPeliEspaniola();
			}
		}
	}

	/*--------------- Consultas-----------------------*/
	private void mostrarActoresEnMasDeUnaPelicula() {
		Iterator<String> it = ob.listInstances("actuaEnMasDeUnaPeliculas");

		ArrayList<String> actor = new ArrayList<String>();
		ArrayList<String> personaje = new ArrayList<String>();
		ArrayList<String> escena = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();

		String instance = "";
		while (it.hasNext()) {
			instance = it.next();
			actor.add(instance);
			// Listar personajes de cada actor
			personaje = this.consultar(instance, "actorInPersonaje");

			// Listar escenas donde aparecen esos personajes
			for (int i = 0; i < personaje.size(); i++) {
				escena = this.consultar(personaje.get(i), "apareceEnEscena");
				// Obtener path de las escenas
				for (int j = 0; j < escena.size(); j++) {
					path.addAll(this.consultar(escena.get(j), "path"));
				}
			}
		}
		// Prueba
		JLabel	label = new JLabel(new ImageIcon("img/"+path.get(0)));
		panelFotos.add(label);

		System.out.println(path.toString());

		// TODO: añadirlas a la galeria
	}

	private void mostrarPersonajesEnPeliEspaniola() {
		ArrayList<String> actor = new ArrayList<String>();
		ArrayList<String> personaje = new ArrayList<String>();
		ArrayList<String> escena = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		Iterator<String> it = ob.listInstances("actuaEnSerieOPeliculaEspañola");
		
		String instance = "";
		while (it.hasNext()) {
			instance = it.next();
			actor.add(instance);
			// Listar personajes de cada actor
			personaje = this.consultar(instance, "actorInPersonaje");
			
			// Listar escenas donde aparecen esos personajes
			for (int i = 0; i < personaje.size(); i++){
				escena = this.consultar(personaje.get(i), "apareceEnEscena");
				// Obtener path de las escenas
				for (int j = 0; j < escena.size(); j++){
					path.addAll(this.consultar(escena.get(j), "path"));
				}
			}
		}
		// Prueba
		JLabel	label = new JLabel(new ImageIcon("img/"+path.get(0)));
		panelFotos.add(label);

		System.out.println(path.toString());
	}

	private void mostrarEscenasConObjetosViolentos() {
		Iterator<String> it = ob.listInstances("EscenasConObjetosViolentos");
		ArrayList<String> escena = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		
		while (it.hasNext()) {
			escena.add(it.next());
			// Obtener path de las escenas
			for (int i = 0; i < escena.size(); i++){
				path.addAll(this.consultar(escena.get(i), "path"));
			}
		}
		// Prueba
		JLabel	label = new JLabel(new ImageIcon("img/"+path.get(0)));
		panelFotos.add(label);

		System.out.println(escena.toString());
	}

	/**
	 * Realiza una consulta
	 * 
	 * @param individuo
	 *            Individuo sobre el que se realiza la consulta
	 * @param relacion
	 *            Relación a buscar
	 * @return Array con el resultado de la consulta
	 */
	private ArrayList<String> consultar(String individuo, String relacion) {
		ArrayList<String> prop = new ArrayList<String>();
		ArrayList<String> val = new ArrayList<String>();
		ArrayList<String> res = new ArrayList<String>();
		ob.listInstancePropertiesValues(individuo, prop, val);
		String a[];

		int i = 0;
		boolean encontrado = false;
		while (!encontrado && i < prop.size()) {
			a = prop.get(i).split("#");
			encontrado = a[1].equals(relacion);
			if (encontrado) {
				res.add(val.get(i));
			}
			i++;
		}

		return res;
	}

}
