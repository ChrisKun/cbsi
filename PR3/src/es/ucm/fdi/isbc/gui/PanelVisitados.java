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
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;

class PanelVisitados extends JPanel
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
		private static final Dimension DIM_PANEL_PRINCIPAL = new Dimension(120, 120);
		private static final Dimension DIM_PANEL_FOTOS = new Dimension(120, 120);
		private static final Dimension DIM_LABEL = new Dimension(100, 100);
		private static final Color COLOR_PANEL_FOTOS = Color.white;
		private static final Color COLOR_BORDE_PANEL_FOTOS = Color.black;

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
			setMinimumSize(DIM_PANEL_PRINCIPAL);
			setMaximumSize(DIM_PANEL_PRINCIPAL);
			setSize(DIM_PANEL_PRINCIPAL);

			bAnterior = new JButton("<<");
			bSiguiente = new JButton(">>");
			panel = new JPanel[LENGTH];
			label = new JLabel[LENGTH];
			imagen = new int[LENGTH];
			
			for (int i = 0; i < LENGTH; i++) {
				panel[i] = new JPanel();
				panel[i].setPreferredSize(DIM_PANEL_FOTOS);
				panel[i].setBackground(COLOR_PANEL_FOTOS);
				panel[i].setBorder(BorderFactory.createLineBorder(COLOR_BORDE_PANEL_FOTOS, 2));
				panel[i].setLayout(new GridBagLayout());
				label[i] = new JLabel();
				label[i].setPreferredSize(DIM_LABEL);
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
				
//				label[i].addMouseMotionListener(new MouseMotionAdapter()
//				{
//					public void mouseMoved(MouseEvent e)
//					{
//						int j;
//						boolean encontrado = false;
//						for (j = 0; j < LENGTH && !encontrado; j++)
//							if (e.getSource() == label[j]) encontrado = true;
//						
//						if (j <= vistas.size()) {
//							DescripcionVivienda dV = vistas.get(imagen[j - 1]);
//							String localización = "Madrid, ";
//							String[] loca = dV.getLocalizacion().split("/");
//							loca[0] = loca[loca.length - 1].replaceAll("-", " ");
//							localización += VentanaPpal.transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));
//							String mensaje = "<html><p>Nombre:" + VentanaPpal.transformar(dV.getTitulo()) + "<br>" +
//									"Tipo:" + tipoToString(dV.getTipo()) + "<br>" +
//									"Estado:" + estadoToString(dV.getEstado()) + "<br>" +
//									"Localización:" + localización + "<br>" +
//									"Superficie:" + dV.getSuperficie() +"m<sup>2</sup></p></html>";
//							label[j].setToolTipText(mensaje);
//
//							JOptionPane.showMessageDialog(PanelVisitados.this, mensaje, 
//									"Nombre", JOptionPane.INFORMATION_MESSAGE);
//						}
//					}
//				}
//				);
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
				label[i].setIcon(galeria.getVistaPrevia(imagen[i], LENGTH));
			
			for (int i = 0; i < LENGTH; i++)
				label[i].addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
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
					if (imagen[0] > 0) {
						for (int i = 0; i < LENGTH; i++) {
							imagen[i]--;
							label[i].setIcon(galeria.getVistaPrevia(imagen[i], LENGTH));
						}
					}
				}
			}
			);

			bSiguiente.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (imagen[LENGTH - 1] < imágenes.size() - 1) {
						for (int i = 0; i < LENGTH; i++) {
							imagen[i]++;
							label[i].setIcon(galeria.getVistaPrevia(imagen[i], LENGTH));
						}
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
			
			public void setVivienda(DescripcionVivienda vivienda, ImageIcon imagen)
			{
				vistas.add(vivienda);
				imágenes.add(new ImageIcon(imagen.getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING)));
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
				for (int i = 0; i < LENGTH; i++) {
					if (imágenes.size() > LENGTH)
						imagen[i] = imágenes.size() - LENGTH + i;
					label[i].setIcon(galeria.getVistaPrevia(imagen[i], LENGTH));
					
					if (i < vistas.size()){
						DescripcionVivienda dV = vistas.get(imagen[i]);
						String localización = "Madrid, ";
						String[] loca = dV.getLocalizacion().split("/");
						loca[0] = loca[loca.length - 1].replaceAll("-", " ");
						localización += VentanaPpal.transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));
						String mensaje = "<html><p>Nombre:" + VentanaPpal.transformar(dV.getTitulo()) + "<br>" +
								"Tipo:" + tipoToString(dV.getTipo()) + "<br>" +
								"Estado:" + estadoToString(dV.getEstado()) + "<br>" +
								"Localización:" + localización + "<br>" +
								"Superficie:" + dV.getSuperficie() +"m<sup>2</sup></p></html>";
						label[i].setToolTipText(mensaje);
					}
				}
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
			
			private String tipoToString(TipoVivienda tipo)
			{
				switch(tipo) {
					case Atico : return "Ático";
					case Plantabaja: return "Planta baja";
					case Casaadosada: return "Adosado";
					case CasaChalet: return "Chalet";
					case Duplex: return "Dúplex";
					case Fincarustica: return "Finca rústica";
					default: return tipo.toString();
				}
			}
			
			private String estadoToString(EstadoVivienda estado)
			{
				switch(estado) {
					case Muybien : return "Muy bien";
					case Areformar: return "A reformar";
					case Casinuevo: return "Casi nuevo";
					default: return estado.toString();
				}
			}

}

