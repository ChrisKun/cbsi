package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.eventos.MuestraSolEvent;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class VentanaPpal extends JFrame implements Observer
{

	/** Atributos **/

		private static final long serialVersionUID = 1L;

		private static JMenuBar barraMenus;
		private static JMenu mArchivo;
		private static JMenuItem salir;
		private static JSplitPane horizontal, vertical;
		private static Galeria galeria;

		static PanelVisitados panelVisitados;
		static PanelFiltro panelFiltro;
		static PanelDiversidad panelDiversidad;
		static VentanaResult vResult;

		private boolean flag = false;

	/** Constructores **/

		public VentanaPpal()
		{
			super("Recomendador Viviendas");
			setExtendedState(MAXIMIZED_BOTH);
			
			barraMenus = inicializaMenus();
			this.setJMenuBar(barraMenus);

			galeria = new Galeria();
			panelFiltro = new PanelFiltro();
			panelDiversidad = new PanelDiversidad(galeria);
			panelVisitados = new PanelVisitados(galeria);

			vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelDiversidad, panelVisitados);
			vertical.setDividerSize(7);
			vertical.setDividerLocation(0.8);
			vertical.setOneTouchExpandable(false);
			vertical.setEnabled(false);

			horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelFiltro, vertical);
			horizontal.setDividerSize(3);
			horizontal.setOneTouchExpandable(false);
			horizontal.setEnabled(false);

			this.setContentPane(horizontal);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			setMinimumSize(new Dimension(dim.width - 100, dim.height - 100));
			setSize(dim.width - 100, dim.height - 100);
			setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);

			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
		}

	/** Metodos **/
		
		/* MÃ©todos que implementan la interfaz Observer */

			public void update(Observable o, Object arg)
			{
				if (arg instanceof MuestraSolEvent) {

					lanzarVentanaResult(((MuestraSolEvent)arg).getDescrs());

				}
				else {
					if (flag) {
						panelFiltro.enableButton(false);
						flag = false;
					}
					else {
						panelFiltro.enableButton(true);
						flag = true;
					}
				}
			}

		/* Otros mÃ©todos */

			/**
			* @return El menÃº de la ventana
			*/
			private JMenuBar inicializaMenus()
			{
				JMenuBar barra = new JMenuBar();
				mArchivo = new JMenu("Archivo");
				salir = new JMenuItem("Salir");
				mArchivo.add(salir);
				barra.add(mArchivo);

				salir.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.exit(0);
					}
				}
				);

				return barra;
			}
			
		/* AUXILIARES */
			
			/**
			 * Lanza a ejecuciÃ³n la ventana resultado en un nuevo hilo para que no se bloqueen las ventanas ni el
			 * se tenga que quedar el proceso princial esperando a que acaben todas las acciones generadas por esta
			 * ventana.
			 * 
			 * @param ArrayList<DescripcionVivienda> VIVIENDAS
			 */
		    static void lanzarVentanaResult(final ArrayList<DescripcionVivienda> VIVIENDAS)
		    {
				Runnable runnable = new Runnable()
				{
					public void run()
					{
						vResult = new VentanaResult(galeria);
						vResult.setResultado(VIVIENDAS);
					}
				};
				Thread hilo = new Thread(runnable);
				hilo.start();
		    }
		    
			/**
			 * Lanza a ejecucion la ventana descripcion en un nuevo hilo para que no se bloqueen las ventanas ni el
			 * se tenga que quedar el proceso princial esperando a que acaben todas las acciones generadas por esta
			 * ventana.
			 * 
			 * @param DescripcionVivienda VIVIENDAS
			 */
		    static void lanzarVentanaDescripcion(final DescripcionVivienda VIVIENDA)
		    {
				Runnable runnable = new Runnable()
				{
					public void run()
					{
						new VentanaDescripcion(VIVIENDA, VIVIENDA.getId(), galeria);
					}
				};
				Thread hilo = new Thread(runnable);
				hilo.start();
		    }
			
		    /*/**
			 * Si guardamos las imÃ¡genes en una carpeta, cada vez que queramos acceder a ellas serÃ­a mucho
			 * mÃ¡s rÃ¡pido ya que la tendremos en el propio ordenador. Esto se ejecutarÃ­a durante el preciclo
			 * asÃ­ cargamos todas las fotos y luego intentamos cargar images\\id.png, si el archivo no existe
			 * porque no existiese ya la foto en la web cargamos la NO_FOTO tambiÃ©n de la carpeta y ya estÃ¡.
			 * Esto habrÃ­a que hacerlo una sÃ³la vez, se descargan todas las fotos posibles y una vez hecho se
			 * cambian los mÃ©todos que emplen new URL(String url) por "images\\" + dV.getId() + "png" o como sea.
			 * 
			 * @param dV
			 */
		    /*public static void guardarImagenes(int id, String urlFoto)
			{
				try {
					URL url = new URL(urlFoto);
					VentanaPpal.writeTo(url.openStream(), new FileOutputStream(new File(
							"images\\" + id + ".png")));
				}
				catch (IOException e) {;}
			}
			
			private static void writeTo(InputStream in, OutputStream out) throws IOException
			{
				try {
					int c;
					while ((c = in.read()) != -1)
						out.write(c);
				}
				finally {
					if (in != null) {
						try { in.close(); }
						catch (Exception e) {;} 
					}
					if (out != null) {
						try { out.close(); }
						catch (Exception e) {;}
					}
				}
			}*/
			
			/*static ImageIcon redimensionarImageIcon(Image image, int width, int height)
			{
				return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_FAST));
			}*/
			
			static boolean enteroEsCorrecto(String entero)
			{
				/**
				 * Para que el entero sea correcto tiene que tener formato de Integer y ser mayor o igual
				 * que cero ya que la superficie, el nÃºmero de habitaciones y de baÃ±os no puede ser negativo.
				 * Si la cadena es vacÃ­a devolvemos true;
				 */

				if (entero.isEmpty()) return true;
				try {
					Integer ent = Integer.valueOf(entero);
					return ent >= 0;
				}
				catch (NumberFormatException e) {
					return false;
				}
			}
			
			static String cortarString(String string, final int TOPE, final int PRIMERO)
			{
				String s = string;
				String cortarString = "";
				
				try {
					int cantidad = TOPE - PRIMERO;
					cortarString += s.substring(0, cantidad);
					s = s.substring(cantidad);
					while (!s.isEmpty() && s.charAt(0) != ' ') {
						cortarString += s.charAt(0);
						s = s.substring(1);
					}
					if (!s.isEmpty()) {
						cortarString += "<br>";
						s = s.substring(1);
					}
					while (!s.isEmpty()) {
						cortarString += s.substring(0, TOPE);
						s = s.substring(TOPE);
						while (!s.isEmpty() && s.charAt(0) != ' ') {
							cortarString += s.charAt(0);
							s = s.substring(1);
						}
						if (!s.isEmpty()) {
							cortarString += "<br>";
							s = s.substring(1);
						}
					}
				}
				catch (StringIndexOutOfBoundsException ex) {
					cortarString += s;
				}
				
				return cortarString;
			}
			
			static String transformar(String string)
			{
			/*	String s = string;
				String transformar = "";
				while (!s.isEmpty()) {
					String car = s.substring(0, 1);
					s = s.substring(1);
					if (car.equals("Ãƒ") || car.equals("Ã‚") || car.equals("m") || car.equals(",") || car.equals(".")) {
						if (s.length() > 0) {
							car += s.substring(0, 1);
							s = s.substring(1);
							if (car.charAt(0) == 'm' && car.charAt(1) != '2') {
								transformar += "m";
								car = car.substring(1);
								if (car.equals("Ãƒ") || car.equals("Ã‚")) {
									car += s.substring(0, 1);
									s = s.substring(1);
								}
							}
							else if (car.charAt(0) == ',' && car.charAt(1) != ' ') {
								transformar += ", ";
								car = car.substring(1);
								if (car.equals("Ãƒ") || car.equals("Ã‚")) {
									car = s.substring(0, 1);
									s = s.substring(1);
								}
							}
							else if (car.charAt(0) == '.' && car.charAt(1) != ' ' && car.charAt(1) != '.'
									&& !VentanaPpal.enteroEsCorrecto(car.substring(1))) {
								transformar += ". ";
								car = car.substring(1);
								if (car.equals("Ãƒ") || car.equals("Ã‚")) {
									car = s.substring(0, 1);
									s = s.substring(1);
								}
							}
						}
					}
					else if (car.equals("Ã¢")) {
						if (s.length() > 1) {
							car += s.substring(0, 1);
							s = s.substring(1);
							if (car.charAt(1) == 'â€š') {
								car += s.substring(0, 1);
								s = s.substring(1);
								if (car.charAt(2) != 'Â¬') {
									transformar += car.substring(0, 2);
									car = car.substring(2);
									if (car.equals("Ãƒ") || car.equals("Ã‚")) {
										car = s.substring(0, 1);
										s = s.substring(1);
									}
								}
							}
							else {
								transformar += "Ã¢";
								car = car.substring(1);
								if (car.equals("Ãƒ") || car.equals("Ã‚")) {
									car = s.substring(0, 1);
									s = s.substring(1);
								}
							}
						}
					}
					transformar += codificar(car);
				}
				return transformar;*/
				return string;
			}

			static String codificar(String cod)
			{
				// Debido a la propia codificaciÃ³n del Eclipse y a que no reconoce ciertos caracteres: Ã�, Ã�, Ã� se escriben aquÃ­
				// de la misma forma lo cuÃ¡l generarÃ¡ un problema en la conversiÃ³n, por suerte, las dos Ãºltimas no son letras
				// muy utilizadas.
				
				if (cod.equals("ÃƒÂ¡")) return "Ã¡";
				else if (cod.equals("ÃƒÂ©")) return "Ã©";
				else if (cod.equals("ÃƒÂ­")) return "Ã­";
				else if (cod.equals("ÃƒÂ³")) return "Ã³";
				else if (cod.equals("ÃƒÂº")) return "Ãº";
				else if (cod.equals("Ãƒï¿½")) return "Ã�";
				else if (cod.equals("Ãƒâ€°")) return "Ã‰";
				else if (cod.equals("Ãƒï¿½")) return "Ã�";
				else if (cod.equals("Ãƒâ€œ")) return "Ã“";
				else if (cod.equals("ÃƒÅ¡")) return "Ãš";
				else if (cod.equals("ÃƒÂ±")) return "Ã±";
				else if (cod.equals("Ãƒâ€˜")) return "Ã‘";
				else if (cod.equals("ÃƒÂ¤")) return "Ã¤";
				else if (cod.equals("ÃƒÂ«")) return "Ã«";
				else if (cod.equals("ÃƒÂ¯")) return "Ã¯";
				else if (cod.equals("ÃƒÂ¶")) return "Ã¶";
				else if (cod.equals("ÃƒÂ¼")) return "Ã¼";
				else if (cod.equals("Ãƒâ€ž")) return "Ã„";
				else if (cod.equals("Ãƒâ€¹")) return "Ã‹";
				else if (cod.equals("Ãƒï¿½")) return "Ã�";
				else if (cod.equals("Ãƒâ€“")) return "Ã–";
				else if (cod.equals("ÃƒÅ“")) return "Ãœ";
				else if (cod.equals("Ã¢â€šÂ¬")) return "â‚¬";
				else if (cod.equals("Ã‚Âº")) return "Âº";
				else if (cod.equals("Ã‚Âª")) return "Âª";
				else if (cod.equals("Ã‚Â¡")) return "Â¡";
				else if (cod.equals("m2")) return " metros cuadrados";
				else return cod;
			}

}
