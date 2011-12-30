package es.ucm.fdi.isbc.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.BorderUIResource;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class PanelVisitados extends JPanel
{

	/** Atributos **/

		private static final long serialVersionUID = 1L;

		private static Dimension dim;
		private ArrayList<DescripcionVivienda> vistas;
		private ArrayList<JLabel> imágenes;

	/** Constructores **/

		public PanelVisitados()
		{
			vistas = new ArrayList<DescripcionVivienda>();
			imágenes = new ArrayList<JLabel>();
			
			/*setLayout(new BorderLayout());
			String holaMundo = "<html><p align=\"center\"><font face=\"Comic Sans MS, verdana\"; color=\"teal\"; size = 24>" +
					"HOLA MUNDO</font></p></html>";	
			add(new JLabel(holaMundo), BorderLayout.CENTER);*/
			
			dim = Toolkit.getDefaultToolkit().getScreenSize();
			dim.setSize(dim.width - 100, dim.height - 100);

			setMinimumSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
			setSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
			
			setVisible(true);
		}

	/** Métodos **/

		/* Getters */

			public DescripcionVivienda getVivienda(int index)
			{
				if (index >= 0 && index < vistas.size()) return vistas.get(index);
				else return null;
			}
			
			public ArrayList<DescripcionVivienda> getVistas()
			{
				return vistas;
			}
			
			public ArrayList<JLabel> getImágenes()
			{
				return imágenes;
			}

		/* Setters */
			
			public void setVivienda(DescripcionVivienda vivienda)
			{
				try {
					JLabel imagen = new JLabel();
					ImageIcon imageIcon = new ImageIcon(new URL(vivienda.getUrlFoto()));
					if (imageIcon.getIconHeight() == -1)
						imageIcon = new ImageIcon(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"), 
								"No hay foto");

					imagen.setIcon(imageIcon);
					imagen.setBorder(BorderUIResource.getRaisedBevelBorderUIResource());
					vistas.add(vivienda);
					imágenes.add(imagen);
				}
				catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}

		/* Otros métodos */

			/**
			 * @param index
			 * @return si el índice es correcto, devuelve el elemento que quita de la lista.
			 */
			public DescripcionVivienda sacarVivienda(int index)
			{
				if (index >= 0 && index < vistas.size()) return vistas.remove(index);
				else return null;
			}

			public void actualizarPanel()
			{
				for (Iterator<JLabel> it = imágenes.iterator(); it.hasNext(); )
					add(it.next());
			}

		/* AUXILIARES */

			/**
			 * Esto sólo es para ver en la consola el contenido del ArrayList y depurar más rápido.
			 */
			public void mostrarViviendas()
			{
				for (Iterator<DescripcionVivienda> it = vistas.iterator(); it.hasNext(); )
					System.out.println(it.next().getTitulo());
				System.out.println();
			}

}

