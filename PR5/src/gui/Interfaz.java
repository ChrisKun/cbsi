package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;

public class Interfaz extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/** Atributos **/
	private Logger log;
	private JTextArea textArea;
	private JPanel panelResultado;
	private JMenuItem masDeUnaPeliculas;
	private JMenuItem escenasObjetosViolentos;
	private JMenuItem personajesEnPeliEspañola;

	private static OntoBridge ob;
	private static OntologyDocument mainOnto;

	/** Constructores **/

	public Interfaz() {
		log = Logger.getLogger("CP");
		this.setJMenuBar(getMiJMenuBar());
		abrir();

		this.getContentPane().add(getPanelPrincipal());
		this.setPreferredSize(new Dimension(800, 600));
		this.setResizable(false);
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

		JMenu consulta = new JMenu("Consultas");
		masDeUnaPeliculas = new JMenuItem(
				"Actores que hayan participado en más de una película");
		escenasObjetosViolentos = new JMenuItem(
				"Escenas con objetos violentos ");
		personajesEnPeliEspañola = new JMenuItem(
				"Personajes que aparecen alguna serie o " + "película española");

		menuBar.add(archivo);
		archivo.add(abrir);
		archivo.addSeparator();
		archivo.add(salir);

		menuBar.add(consulta);
		consulta.add(masDeUnaPeliculas);
		consulta.add(escenasObjetosViolentos);
		consulta.add(personajesEnPeliEspañola);

		abrir.addActionListener(this);
		salir.addActionListener(this);
		masDeUnaPeliculas.addActionListener(this);
		personajesEnPeliEspañola.addActionListener(this);
		escenasObjetosViolentos.addActionListener(this);

		return menuBar;
	}

	private JSplitPane getPanelPrincipal() {
		JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		panel.setDividerSize(0);
		panel.setLeftComponent(getPanelOntologia());
		panel.setRightComponent(getPanelResultado());
		return panel;
	}

	private JPanel getPanelOntologia() {
		JPanel panel = new JPanel();

		PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(ob,true);
		tree.setPreferredSize(new Dimension(300,500));
		panel.add(tree);

		return panel;

	}

	private JPanel getPanelResultado() {

		panelResultado = new JPanel();

		textArea = new JTextArea(20, 20);
		textArea.setEditable(false);
		panelResultado.add(textArea);
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
		if (seleccionCorrecta){
			file = fileChooser.getSelectedFile();
			extensionCorrecta = file.getName().endsWith(".owl");
		}

		if (seleccionCorrecta && extensionCorrecta) {
			// Se mete la ontologia principal.
			String rutaOnto = "file:" + file.getPath();
			mainOnto = new OntologyDocument(rutaOnto, rutaOnto);

			// Cargar la ontologia
			ob.loadOntology(mainOnto, new ArrayList<OntologyDocument>(), false);

		} else{
			JOptionPane.showMessageDialog(this, "La ontología introducida no es válida", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Salir")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Abrir")) {
			abrir();
		} else if (e.getSource().equals(masDeUnaPeliculas)) {
			mostrarActoresEnMasDeUnaPelicula();
		} else if (e.getSource().equals(personajesEnPeliEspañola)) {
			mostrarPersonajesEnPeliEspaniola();
		} else if (e.getSource().equals(escenasObjetosViolentos)) {
			mostrarEscenasConObjetosViolentos();
		}
	}

	/*--------------- Consultas-----------------------*/
	private void mostrarActoresEnMasDeUnaPelicula() {
		Iterator<String> it = ob.listInstances("actuaEnMasDeUnaPeliculas");

		ArrayList<String> actor = new ArrayList<String>();
		String[] a;
		String aux = "";
		int i = 0;
		while (it.hasNext()) {
			aux = it.next();
			a = aux.split("#");
			actor.add(a[1]);
			log.info("Actor " + actor.get(i));
			i++;
		}
		textArea.setText("");
		for (int j = 0; j < actor.size(); j++) {
			textArea.append(actor.get(j) + "\n");
		}
		// Para cada actor hay que mostrar las imágenes en las que aparece
	}

	private void mostrarPersonajesEnPeliEspaniola() {
		Iterator<String> it = ob
				.listInstances("actuaEnSerieOPeliculaEspañola");

		ArrayList<String> actor = new ArrayList<String>();
		String[] a;
		String aux = "";
		int i = 0;
		while (it.hasNext()) {
			aux = it.next();
			a = aux.split("#");
			actor.add(a[1]);
			log.info("Actor " + actor.get(i));
			i++;
		}
		textArea.setText("");
		for (int j = 0; j < actor.size(); j++) {
			textArea.append(actor.get(j) + "\n");
		}
		// Para cada actor hay que mostrar las imágenes en las que aparece
	}

	private void mostrarEscenasConObjetosViolentos() {
		Iterator<String> it = ob
				.listInstances("EscenasConObjetosViolentos");
		ArrayList<String> actor = new ArrayList<String>();
		String[] a;
		String aux = "";
		int i = 0;
		while (it.hasNext()) {
			aux = it.next();
			a = aux.split("#");
			actor.add(a[1]);
			log.info("Escena " + actor.get(i));
			i++;
		}
		textArea.setText("");
		for (int j = 0; j < actor.size(); j++) {
			textArea.append(actor.get(j) + "\n");
		}
		// Para cada actor hay que mostrar las imágenes en las que aparece
	}

}
