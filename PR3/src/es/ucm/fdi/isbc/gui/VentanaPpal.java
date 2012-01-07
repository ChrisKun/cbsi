package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.eventos.MuestraSolEvent;

public class VentanaPpal extends JFrame implements Observer
{

	/** Atributos **/

		private static final long serialVersionUID = 1L;

		static PanelVisitados panelVisitados;
		static PanelFiltro panelFiltro;
		static PanelDiversidad panelDiversidad;
		private static JMenuBar barraMenus;
		private static JMenu mArchivo;
		private static JMenuItem salir;
		private static JSplitPane horizontal, vertical;

		private VentanaResult vResult;

		private boolean flag = false;

	/** Constructores **/

		public VentanaPpal()
		{
			super("Recomendador Viviendas");
			setExtendedState(MAXIMIZED_BOTH);

			barraMenus = inicializaMenus();
			this.setJMenuBar(barraMenus);

			panelFiltro = new PanelFiltro();
			panelDiversidad = new PanelDiversidad();
			panelVisitados = new PanelVisitados();

			vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelDiversidad, panelVisitados);
			vertical.setDividerSize(7);
			vertical.setDividerLocation(0.8);
			vertical.setOneTouchExpandable(true);
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

	/** MÃ©todos **/
		
		/* MÃ©todos que implementan la interfaz Observer */

			public void update(Observable o, Object arg)
			{
				if (arg instanceof MuestraSolEvent) {
					
					vResult = new VentanaResult();
					vResult.setResultado(((MuestraSolEvent)arg).getDescrs());

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
			* Devuelve el menÃº de la ventana
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
			
		/*	static String transformar(String string)
			{
				String s = string;
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
				return transformar;
			}*/
			
			static String codificar(String cod)
			{
				// Debido a la propia codificaciÃ³n del Eclipse y a que no reconoce ciertos caracteres: Ã�, Ã�, Ã� se escriben aquÃ­
				// de la misma forma lo cuÃ¡l generarÃ­a un problema en la conversiÃ³n, por suerte, las dos Ãºltimas no son letras
				// muy utilizadas.
				
				if (cod.equals("ÃƒÂ­")) return "Ã­";
				else if (cod.equals("ÃƒÂ©")) return "Ã©";
				else if (cod.equals("ÃƒÂ¡")) return "Ã¡";
				else if (cod.equals("ÃƒÂ³")) return "Ã³";
				else if (cod.equals("ÃƒÂº")) return "Ãº";
				else if (cod.equals("ÃƒÂ±")) return "Ã±";
				else if (cod.equals("Ãƒï¿½")) return "Ã�";
				else if (cod.equals("Ãƒâ€°")) return "Ã‰";
				else if (cod.equals("Ãƒï¿½")) return "Ã�";
				else if (cod.equals("Ãƒâ€œ")) return "Ã“";
				else if (cod.equals("ÃƒÅ¡")) return "Ãš";
				else if (cod.equals("Ãƒâ€˜")) return "Ã‘";
				else if (cod.equals("Ãƒâ€ž")) return "Ã„";
				else if (cod.equals("Ãƒâ€¹")) return "Ã‹";
				else if (cod.equals("Ãƒï¿½")) return "Ã�";
				else if (cod.equals("Ãƒâ€“")) return "Ã–";
				else if (cod.equals("ÃƒÅ“")) return "Ãœ";
				else if (cod.equals("ÃƒÂ¤")) return "Ã¤";
				else if (cod.equals("ÃƒÂ«")) return "Ã«";
				else if (cod.equals("ÃƒÂ¯")) return "Ã¯";
				else if (cod.equals("ÃƒÂ¶")) return "Ã¶";
				else if (cod.equals("ÃƒÂ¼")) return "Ã¼";
				else if (cod.equals("Ã‚Âª")) return "Âª";
				else if (cod.equals("Ã‚Âº")) return "Âº";
				else if (cod.equals("Ã¢â€šÂ¬")) return "â‚¬";
				else if (cod.equals("m2")) return "m<sup>2</sup>";
				else return cod;
			}

}
