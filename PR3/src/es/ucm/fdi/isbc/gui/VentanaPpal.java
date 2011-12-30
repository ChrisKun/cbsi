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

			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
		}

	/** Métodos **/
		
		/* Métodos que implementan la interfaz Observer */

			public void update(Observable o, Object arg)
			{
				if (arg instanceof MuestraSolEvent) {
					
					vResult = new VentanaResult();
					vResult.setResultado(((MuestraSolEvent)arg).getDescrs());
					panelVisitados.actualizarPanel();
					vResult = null;
					System.gc();

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

		/* Otros métodos */

			/**
			* Devuelve el menú de la ventana
			* @return El menú de la ventana
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
			
			public static boolean enteroEsCorrecto(String entero)
			{
				/** Para que el entero sea correcto tiene que tener formato de Integer y ser mayor o igual
				 * que cero ya que la superficie, el número de habitaciones y de baños no puede ser negativo.
				 * Si la cadena es vacía devolvemos true;
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

}
