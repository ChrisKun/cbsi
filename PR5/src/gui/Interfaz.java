package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsAndInstancesTree;

public class Interfaz implements Serializable {
	private Logger log;

	private JTextArea textArea;

	private JPanel panelResultado;

	/** Atributos **/

	private static final long serialVersionUID = 1L;

	private static JFrame frame;
	private static OntoBridge ob;
	private static OntologyDocument mainOnto;
	private static ArrayList<OntologyDocument> subOntologies;
	private static ArrayList<String> subOntologiesPaths;
	private static boolean hayOntologia;

	/** Constructores **/

	public Interfaz() {

		hayOntologia = false;
		frame = new JFrame();
		frame.setJMenuBar(getMiJMenuBar());
		try {
			abrir();
			log = Logger.getLogger("CP");
			hayOntologia = true;
		} catch (Exception e) {
			;
		} finally {
			if (hayOntologia)
				frame.getContentPane().add(getPanelPrincipal());

			dimensionarFrame(frame.getContentPane());
			frame.setResizable(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			int x=(int) (Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getPreferredSize().width/2);
			int y=(int) (Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getPreferredSize().height/2);
			frame.setLocation(x, y);
			frame.setVisible(true);
		}

	}

	/** Metodos **/

	private void dimensionarFrame(Container container) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMinimumSize(new Dimension(500,550));
		if (container == null)
			frame.setLocation((int) ((dim.getWidth() - frame.getJMenuBar()
					.getWidth()) / 2), (int) ((dim.getHeight() - frame
					.getJMenuBar().getHeight()) / 2));
		else
			frame.setLocation(
					(int) ((dim.getWidth() - frame.getJMenuBar().getWidth() - container
							.getWidth()) / 2), (int) ((dim.getHeight()
							- frame.getJMenuBar().getHeight() - container
							.getHeight()) / 2));
	}

	private JMenuBar getMiJMenuBar() {

		JMenuBar menuBar = new JMenuBar();

		JMenu archivo = new JMenu("Archivo");
		JMenuItem abrir = new JMenuItem("Abrir");
		JMenuItem salir = new JMenuItem("Salir");

		JMenu consulta = new JMenu("Consultas");
		JMenuItem masDeUnaPeliculas = new JMenuItem(
				"Actores que hayan participado en más de una película");
		JMenuItem escenasObjetosViolentos = new JMenuItem(
				"Escenas con objetos violentos ");
		JMenuItem personajesEnPeliEspañola = new JMenuItem(
				"Personajes que aparecen alguna serie o " + "película española");

		menuBar.add(archivo);
		archivo.add(abrir);
		archivo.addSeparator();
		archivo.add(salir);

		menuBar.add(consulta);
		consulta.add(masDeUnaPeliculas);
		consulta.add(escenasObjetosViolentos);
		consulta.add(personajesEnPeliEspañola);

		abrir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					abrir();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, e.getMessage(),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		salir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});

		masDeUnaPeliculas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
				for (int j = 0; j < actor.size(); j++){
					textArea.append(actor.get(j) + "\n");
				}
				// Para cada actor hay que mostrar las imágenes en las que aparece
				
			}
		});

		personajesEnPeliEspañola.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Iterator<String> it = ob.listInstances("actuaEnSerieOPeliculaEspañola");
				
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
				for (int j = 0; j < actor.size(); j++){
					textArea.append(actor.get(j) + "\n");
				}
				// Para cada actor hay que mostrar las imágenes en las que aparece
				
			}
		});
		
		escenasObjetosViolentos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Iterator<String> it = ob.listInstances("EscenasConObjetosViolentos");
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
				for (int j = 0; j < actor.size(); j++){
					textArea.append(actor.get(j) + "\n");
				}
				// Para cada actor hay que mostrar las imágenes en las que aparece
				
			}
		});

		return menuBar;
	}

	private JPanel getPanelPrincipal() {

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		panel.setBorder(BorderFactory.createEtchedBorder());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		panel.add(getPanelOntologia());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		panel.add(getPanelResultado());

		return panel;
	}

	private JPanel getPanelOntologia() {

		JPanel panel = new JPanel();

		PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(ob,
				true);
		panel.add(tree);
		panel.setMinimumSize(new Dimension(400, 600));

		return panel;

	}

	private JPanel getPanelResultado() {

		panelResultado = new JPanel();

		textArea = new JTextArea(20 , 20);
		textArea.setEditable(false);
		panelResultado.add(textArea);
		panelResultado.setMinimumSize(new Dimension(300, 600));

		return panelResultado;
	}


	private void abrir() throws Exception {

		// Creamos el objeto de OntoBridge
		ob = new OntoBridge();

		// Inicializamos el objeto de OntoBridge con un razonador Pellet
		ob.initWithPelletReasoner();

		JOptionPane.showMessageDialog(frame,
				"Selecciona el archivo para la ontología principal",
				"Ontología principal", JOptionPane.INFORMATION_MESSAGE);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Archivos .owl", "owl");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		boolean seleccionCorrecta = fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION;
		File file = fileChooser.getSelectedFile();
		boolean extensionCorrecta = file.getName().endsWith(".owl");

		if (seleccionCorrecta && extensionCorrecta) {

			// Se mete la ontologia principal.
			String rutaOnto = "file:" + file.getPath();
			mainOnto = new OntologyDocument(rutaOnto, rutaOnto);

			// Crear el arrayList para las subontologias necesarias.
			subOntologies = new ArrayList<OntologyDocument>();
			subOntologiesPaths = new ArrayList<String>();

			subOntologiesPaths.add(rutaOnto);

			boolean intrSubOntologias = JOptionPane.showConfirmDialog(frame,
					"¿Quieres introducir alguna subontología?",
					"Subontologías", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

			while (intrSubOntologias) {

				seleccionCorrecta = fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION;
				file = fileChooser.getSelectedFile();

				extensionCorrecta = file.getName().endsWith(".owl");
				if (seleccionCorrecta && extensionCorrecta) {

					rutaOnto = "file:" + file.getPath();
					if (!subOntologiesPaths.contains(rutaOnto)) {
						subOntologiesPaths.add(rutaOnto);
						subOntologies.add(new OntologyDocument(rutaOnto,
								rutaOnto));
					} else
						JOptionPane.showMessageDialog(frame,
								"La ontología ya ha sido introducida", "AVISO",
								JOptionPane.WARNING_MESSAGE);

					intrSubOntologias = JOptionPane.showConfirmDialog(frame,
							"¿Desea introducir más subontologías?",
							"Subontologías", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

				} else if (!extensionCorrecta)
					throw new Exception(
							"La subontología introducida no es válida");

			}

			// Cargar la ontologia
			ob.loadOntology(mainOnto, subOntologies, false);

		} else
			throw new Exception("La ontología introducida no es válida");
	}

}
