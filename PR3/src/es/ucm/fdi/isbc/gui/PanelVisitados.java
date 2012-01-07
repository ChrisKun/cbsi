package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
			galeria = new Galeria();
	
			dim = Toolkit.getDefaultToolkit().getScreenSize();
			dim.setSize(dim.width - 100, dim.height - 100);

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
						for (int i = LENGTH-1; i > 0; i--) {
							imagen[i] = imagen[i - 1];
							label[i].setIcon(label[i - 1].getIcon());
							label[i].setToolTipText(label[i - 1].getToolTipText());
						}
						imagen[0]--;
						actualizarToolTip(0);
					}
				}
			}
			);

			bSiguiente.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (imagen[LENGTH - 1] < vistas.size() - 1) {
						for (int i = 0; i < LENGTH-1; i++) {
							imagen[i] = imagen[i + 1];
							label[i].setIcon(label[i + 1].getIcon());
							label[i].setToolTipText(label[i + 1].getToolTipText());
						}
						imagen[LENGTH - 1]++;
						actualizarToolTip(LENGTH-1);
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
			
			public ArrayList<ImageIcon> getImagenes()
			{
				return galeria.getFotos();
			}

		/* Setters */
			
			public void setVivienda(DescripcionVivienda vivienda, ImageIcon imagen)
			{
				vistas.add(vivienda);
				galeria.addFoto(imagen, vivienda.getId());
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
			
			/**
			 *  Sirve para actualizar el panel a las últimas casas visitadas por el usuario
			 */
		 	public void actualizarPanel()
			{		 		
				//Rellenamos con los últimos añadidos por el usuario.
				if (vistas.size() > LENGTH)
					for (int i = 0; i < LENGTH; i++) {
						//imagen[i] = vistas.size() - LENGTH + i;
						imagen[i] = vistas.size() - LENGTH + i;
						actualizarToolTip(i);
					}
				else actualizarToolTip(vistas.size()-1);
			}

		/* AUXILIARES */

			/**
			 * Esto sólo es para ver en la consola el contenido del ArrayList y depurar más rápido.
			 */
			void mostrarViviendas()
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
			
			private void actualizarToolTip(int index)
			{
				
				if (index < vistas.size()) {
					DescripcionVivienda dV = vistas.get(imagen[index]);
					String localización = "Madrid, ";
					String[] loca = dV.getLocalizacion().split("/");
					loca[0] = loca[loca.length - 1].replaceAll("-", " ");
					localización += VentanaPpal.transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));
					String mensaje = "<html>" +
							"Nombre:  " + /*VentanaPpal.transformar(*/dV.getTitulo()/*)*/ + "<br>" +
							"Tipo:  " + tipoToString(dV.getTipo()) + "<br>" +
							"Estado:  " + estadoToString(dV.getEstado()) + "<br>" +
							"Localización:  " + localización + "<br>" +
							"Superficie:  " + dV.getSuperficie() + "m<sup>2</sup></html>";
					
					label[index].setToolTipText(mensaje);

					//Si tienen mismo id, entonces tienen mismo contenido
					if (vistas.get(imagen[index]).getId() != galeria.getIdIcon((ImageIcon)label[index].getIcon())) {
						dV = vistas.get(imagen[index]);
						localización = "Madrid, ";
						String[] loca1 = dV.getLocalizacion().split("/");
						loca1[0] = loca1[loca1.length - 1].replaceAll("-", " ");
						localización += VentanaPpal.transformar(loca1[0].substring(0, 1).toUpperCase() + loca1[0].substring(1));
						String mensaje1 = "<html>" +
								"Nombre:  " + VentanaPpal.transformar(dV.getTitulo()) + "<br>" +
								"Tipo:  " + tipoToString(dV.getTipo()) + "<br>" +
								"Estado:  " + estadoToString(dV.getEstado()) + "<br>" +
								"Localización:  " + localización + "<br>" +
								"Superficie:  " + dV.getSuperficie() +"m<sup>2</sup></html>";
						
						label[index].setToolTipText(mensaje1);
						label[index].setIcon(galeria.getVistaPrevia(imagen[index]));
					}
				}
			}

}
