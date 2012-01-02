package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class PanelVisitados extends JPanel
{

	/** Atributos **/

		private static final long serialVersionUID = 1L;

		private static Dimension dim;
		private static ArrayList<DescripcionVivienda> vistas;
		private static ArrayList<ImageIcon> imágenes;
		private static Galeria galeria;
		
		private static JButton bAnterior;
		private static JButton bSiguiente;
		private static JPanel[] panel;
		private static JLabel[] label;
		private static int[] imagen;
		
		private static final int LENGTH = 6;
		

	/** Constructores **/

		public PanelVisitados()
		{
			GridBagConstraints gridBagConstraints;
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createEtchedBorder());
			
			vistas = new ArrayList<DescripcionVivienda>();
			imágenes = new ArrayList<ImageIcon>();
			galeria = new Galeria();
	
			dim = Toolkit.getDefaultToolkit().getScreenSize();
			dim.setSize(dim.width - 100, dim.height - 100);

			//setMinimumSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
			//setSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
			setMinimumSize(new Dimension(120, 120));
			setSize(120, 120);

			bAnterior = new JButton("<<");
			bSiguiente = new JButton(">>");
			panel = new JPanel[LENGTH];
			label = new JLabel[LENGTH];
			imagen = new int[LENGTH];
			
			for (int i = 0; i < LENGTH; i++) {
				panel[i] = new JPanel();
				panel[i].setPreferredSize(new Dimension(120, 120));
				panel[i].setBackground(new Color(255, 255, 255));
				panel[i].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
				panel[i].setLayout(new GridBagLayout());
				label[i] = new JLabel();
				label[i].setPreferredSize(new Dimension(100, 100));
				imagen[i] = i;
				
				gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
				panel[i].add(label[i], gridBagConstraints);
				
				gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = i + 1;
				gridBagConstraints.gridy = 0;
				gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
				gridBagConstraints.insets = new Insets(5, 5, 5, 5);
				add(panel[i], gridBagConstraints);
			}

			bAnterior.setPreferredSize(new Dimension(55, 120));
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets = new Insets(5, 5, 5, 0);
			add(bAnterior, gridBagConstraints);

			bSiguiente.setPreferredSize(new Dimension(55, 120));
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 7;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets = new Insets(5, 0, 5, 5);
			add(bSiguiente, gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			
			// Se inicia la vista previa
			for (int i = 0; i < LENGTH; i++)
				label[i].setIcon(galeria.getVistaPrevia(imagen[i]));
			
			for (int i = 0; i < LENGTH; i++)
				label[i].addMouseListener(new MouseAdapter()
				{
					public void mouseReleased(MouseEvent e)
					{
						/**
						 * Aquí habría que implementar el método de recuperación de las casas ya visitadas...
						 */
					}
				}
				);
			
			bAnterior.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (int i = 0; i < LENGTH; i++) {
						imagen[i]--;
						label[i].setIcon(galeria.getVistaPrevia(imagen[i]));
					}
				}
			}
			);
			
			bSiguiente.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (int i = 0; i < LENGTH; i++) {
						imagen[i]++;
						label[i].setIcon(galeria.getVistaPrevia(imagen[i]));
					}
				}
			}
			);

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
			
			public ArrayList<ImageIcon> getImágenes()
			{
				return imágenes;
			}

		/* Setters */
			
			public void setVivienda(DescripcionVivienda vivienda)
			{
				try {
					ImageIcon imageIcon = new ImageIcon(new URL(vivienda.getUrlFoto()));
					Image image1 = Toolkit.getDefaultToolkit().getImage(new URL(vivienda.getUrlFoto()));
					if (imageIcon.getIconHeight() == -1)
						image1 = Toolkit.getDefaultToolkit().getImage(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"));
					Image image2 = image1.getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
					imageIcon.setImage(image2);
					
					vistas.add(vivienda);
					imágenes.add(imageIcon);
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
				galeria.setFotos(imágenes);
				for (int i = 0; i < LENGTH; i++)
					label[i].setIcon(galeria.getVistaPrevia(imagen[i]));
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

