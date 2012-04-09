package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
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

	/** Atributos **/

		private static final long serialVersionUID = 1L;
		private static JPanel panelResultado;
		private static JRadioButton radio1, radio2, radio3;
		private static JPanel panelFotosBuscar;
		private static JLabel labelFotoMarcar;
		private static JLabel labelFotoBuscar;
		private static ArrayList<String> images;
		private static int index;

		private static OntoBridge ob;
		private static OntologyDocument mainOnto;

	/** Constructores **/

		public Interfaz() {
			super("Ontología");
			//galeria = new Galeria();
			setJMenuBar(getMiJMenuBar());
			abrir();
	
			getContentPane().add(getPanelPrincipal());
			setPreferredSize(new Dimension(900, 600));
			setResizable(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getPreferredSize().width / 2),
						(int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getPreferredSize().height / 2));
			setVisible(true);
			pack();
		}

	/** Metodos **/

		/* Metodos que crean los distintos paneles de la interfaz */

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
				tabbedPane.addTab("Marcar", getPanelMarcar());
				tabbedPane.addTab("Buscar", getPanelBuscar());
				panelResultado.add(tabbedPane);
				return panel;
			}

			private JSplitPane getPanelBuscar() {
				JSplitPane panelPpalBuscar = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				JSplitPane panelFotosBoton = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				panelFotosBoton.setDividerSize(0);
				panelPpalBuscar.setDividerSize(0);

				JButton botonBuscar = new JButton("OK");
				JButton botonAnt = new JButton("<<");
				JButton botonSig = new JButton(">>");

				botonBuscar.addActionListener(this);
				botonAnt.addActionListener(this);
				botonSig.addActionListener(this);
				
				JPanel panelConsultas = new JPanel(new GridLayout(3, 1));

				// set border and layout
				Border emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
				Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
				Border titleBorder = BorderFactory.createTitledBorder(lineBorder, "Consultas");
				Border compoundBorder = BorderFactory.createCompoundBorder(titleBorder, emptyBorder);
				panelConsultas.setBorder(compoundBorder);

				ButtonGroup grupo = new ButtonGroup();
				radio1 = new JRadioButton("Actores que hayan participado en más de una película");
				radio2 = new JRadioButton("Escenas con objetos violentos");
				radio3 = new JRadioButton("Actores que aparecen alguna serie o película española");
				grupo.add(radio1);
				grupo.add(radio2);
				grupo.add(radio3);
				radio1.setSelected(true);

				panelConsultas.add(radio1);
				panelConsultas.add(radio2);
				panelConsultas.add(radio3);
				panelPpalBuscar.setTopComponent(panelConsultas);

				labelFotoBuscar = new JLabel();
				panelFotosBuscar = new JPanel();
				panelFotosBuscar.setPreferredSize(new Dimension(500, 350));
				panelFotosBuscar.add(labelFotoBuscar);

				panelFotosBoton.setTopComponent(panelFotosBuscar);
				JPanel panelBoton = new JPanel();
				JPanel panelBotonesImg = new JPanel(new GridLayout(1, 3));
				panelBotonesImg.add(botonAnt);
				panelBotonesImg.add(botonBuscar);
				panelBotonesImg.add(botonSig);
				panelBoton.add(panelBotonesImg);
				panelFotosBoton.setBottomComponent(panelBoton);

				panelPpalBuscar.setBottomComponent(panelFotosBoton);

				return panelPpalBuscar;
			}
		
			private JSplitPane getPanelMarcar() {
				JSplitPane panelPpalMarcado = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				JSplitPane panelBotonFotos = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

				panelPpalMarcado.setDividerSize(0);
				panelBotonFotos.setDividerSize(0);

				JButton botonCargarFoto = new JButton("Cargar foto");
				JButton botonMarcarFoto = new JButton("Marcar foto");

				botonCargarFoto.addActionListener(this);
				botonMarcarFoto.addActionListener(this);

				JPanel panelBoton = new JPanel();
				JPanel panelFotos = new JPanel();

				labelFotoMarcar = new JLabel();
				panelBoton.add(botonCargarFoto);
				panelBotonFotos.setTopComponent(panelBoton);
				panelBotonFotos.setBottomComponent(panelFotos);
				panelPpalMarcado.setTopComponent(panelBotonFotos);
				panelFotos.setPreferredSize(new Dimension(500, 350));
				panelFotos.add(labelFotoMarcar);

				JLabel labelOpcionesMarcado = new JLabel("Opciones de marcado: ");
				JComboBox comboRelaciones = new JComboBox();
				JComboBox comboIndividuos = new JComboBox();
				JPanel panelOpcionesMarcado = new JPanel(new GridLayout(1, 4));
				panelOpcionesMarcado.setPreferredSize(new Dimension(500, 20));

				panelOpcionesMarcado.add(labelOpcionesMarcado);
				panelOpcionesMarcado.add(comboRelaciones);
				panelOpcionesMarcado.add(comboIndividuos);
				panelOpcionesMarcado.add(botonMarcarFoto);

				JPanel panel = new JPanel();
				panel.add(panelOpcionesMarcado);

				panelPpalMarcado.setBottomComponent(panel);

				return panelPpalMarcado;
			}

			private JPanel getPanelOntologia() {
				JPanel panel = new JPanel();

				PnlConceptsAndInstancesTree tree = new PnlConceptsAndInstancesTree(ob, true);
				tree.setPreferredSize(new Dimension(300, 500));
				panel.add(tree);

				return panel;
			}

			private JPanel getPanelResultado() {
				panelResultado = new JPanel();

				panelResultado.setPreferredSize(new Dimension(300, 600));

				return panelResultado;
			}

		/* Metodos que implementan ActionListener */

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Salir"))
					System.exit(0);
				else if (e.getActionCommand().equals("Abrir"))
					abrir();
				else if (e.getActionCommand().equals("OK")) {
					if (radio1.isSelected())
						mostrarActoresEnMasDeUnaPelicula();
					else if (radio2.isSelected())
						mostrarEscenasConObjetosViolentos();
					else if (radio3.isSelected())
						mostrarPersonajesEnPeliEspaniola();
				}
				else if (e.getActionCommand().equals("<<"))
					anterior();
				else if (e.getActionCommand().equals(">>"))
					siguiente();
				else if (e.getActionCommand().equals("Cargar foto"))
					cargarFoto();
			}

		/* Metodos para las consultas */

			private void mostrarActoresEnMasDeUnaPelicula() {

				ArrayList<String> actores = new ArrayList<String>();
				ArrayList<String> personajes = new ArrayList<String>();
				ArrayList<String> escenas = new ArrayList<String>();
				images = new ArrayList<String>();

				for (Iterator<String> it = ob.listInstances("actuaEnMasDeUnaPeliculas"); it.hasNext(); ) {
					String instance = it.next();

					actores.add(instance);

					// Listar personajes de cada actor
					personajes = consultar(instance, "actorInPersonaje");

					// Listar escenas donde aparecen esos personajes
					for (String personaje : personajes) {
						escenas = consultar(personaje, "apareceEnEscena");
						for (String escena : escenas) {
							ArrayList<String> consultas = consultar(escena, "path");
							for (String consulta : consultas)
								if (!images.contains(consulta))
									images.add(consulta);
						}
					}
				}

				cambiarImagen(index = 0);
				System.out.println(images.toString());
			}

			private void mostrarPersonajesEnPeliEspaniola() {

				ArrayList<String> actores = new ArrayList<String>();
				ArrayList<String> personajes = new ArrayList<String>();
				ArrayList<String> escenas = new ArrayList<String>();
				images = new ArrayList<String>();

				for (Iterator<String> it = ob.listInstances("actuaEnSerieOPeliculaEspañola"); it.hasNext(); ) {
					String instance = it.next();

					actores.add(instance);

					// Listar personajes de cada actor
					personajes = consultar(instance, "actorInPersonaje");

					// Listar escenas donde aparecen esos personajes
					for (String personaje : personajes) {
						escenas = consultar(personaje, "apareceEnEscena");
						for (String escena : escenas) {
							ArrayList<String> consultas = consultar(escena, "path");
							for (String consulta : consultas)
								if (!images.contains(consulta))
									images.add(consulta);
						}
					}
				}

				cambiarImagen(index = 0);
				System.out.println(images.toString());
			}

			private void mostrarEscenasConObjetosViolentos() {

				ArrayList<String> escenas = new ArrayList<String>();
				images = new ArrayList<String>();

				for (Iterator<String> it = ob.listInstances("EscenasConObjetosViolentos"); it.hasNext(); ) {
					escenas.add(it.next());

					// Obtener path de las escenas
					for (String escena : escenas) {
						ArrayList<String> consultas = consultar(escena, "path");
						for (String consulta : consultas)
							if (!images.contains(consulta))
								images.add(consulta);
					}
				}

				cambiarImagen(index = 0);
				System.out.println(images.toString());
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

				boolean encontrado = false;
				for (int i = 0; i < prop.size() && !encontrado; i++)
					if (relacion.equals(prop.get(i).split("#")[1]))
						res.add(val.get(i));

				return res;
			}

		/* Otros metodos */

			private void abrir() {

				// Creamos el objeto de OntoBridge
				ob = new OntoBridge();

				// Inicializamos el objeto de OntoBridge con un razonador Pellet
				ob.initWithPelletReasoner();

				JOptionPane.showMessageDialog(Interfaz.this, "Selecciona el archivo para la ontología principal",
					"Ontología principal", JOptionPane.INFORMATION_MESSAGE);

				FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos .owl", "owl");
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(filter);
				boolean seleccionCorrecta = fileChooser.showOpenDialog(Interfaz.this) == JFileChooser.APPROVE_OPTION;
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

				}
				else JOptionPane.showMessageDialog(Interfaz.this, "La ontología introducida no es válida", "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			
			private void cargarFoto() {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos jpg y png", 
					"jpg", "png");

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(filter);
				File file = null;

				if (fileChooser.showOpenDialog(Interfaz.this) == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					String path = file.getPath();
					if (path.endsWith(".jpg") || path.endsWith(".png"))
						ponerImagen(path);
				}
			}

			private void anterior() {
				if (index > 0)
					cambiarImagen(--index);
			}

			private void siguiente() {
				if (index < images.size() - 1)
					cambiarImagen(++index);
			}

			private void cambiarImagen(int index) {
				labelFotoBuscar.setIcon(new ImageIcon((new ImageIcon("img/" + 
					images.get(index))).getImage().getScaledInstance(-1, 325, Image.SCALE_AREA_AVERAGING)));
				repaint();
			}

			private void ponerImagen(String ruta) {
				labelFotoMarcar.setIcon(new ImageIcon((new ImageIcon(
					ruta)).getImage().getScaledInstance(-1, 325, Image.SCALE_AREA_AVERAGING)));
				repaint();
			}

}
