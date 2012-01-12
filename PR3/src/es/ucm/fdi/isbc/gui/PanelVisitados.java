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
import java.net.URL;
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
		private static ArrayList<DescripcionVivienda> visitadas;
		private static ArrayList<Integer> idVisitadas;
		private static Galeria galeria;

		private static JButton bAnterior;
		private static JButton bSiguiente;
		private static JPanel[] panel;
		private static JLabel[] label;
		private static int[] imagen;

		private static final int LENGTH_LABELS = 6;
		private static final Dimension DIM_PANEL_PRINCIPAL = new Dimension(120, 120);
		private static final Dimension DIM_PANEL_FOTOS = new Dimension(120, 120);
		private static final Dimension DIM_LABEL = new Dimension(100, 100);
		private static final Color COLOR_PANEL_FOTOS = Color.white;
		private static final Color COLOR_BORDE_PANEL_FOTOS = Color.black;

	/** Constructores **/

		public PanelVisitados(Galeria galeria)
		{
			GridBagConstraints gridBagConstraints;
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createEtchedBorder());

			visitadas = new ArrayList<DescripcionVivienda>();
			idVisitadas = new ArrayList<Integer>();
			this.galeria = galeria;

			dim = Toolkit.getDefaultToolkit().getScreenSize();
			dim.setSize(dim.width - 100, dim.height - 100);

			setMinimumSize(DIM_PANEL_PRINCIPAL);
			setMaximumSize(DIM_PANEL_PRINCIPAL);
			setSize(DIM_PANEL_PRINCIPAL);

			bAnterior = new JButton("<<");
			bSiguiente = new JButton(">>");
			panel = new JPanel[LENGTH_LABELS];
			label = new JLabel[LENGTH_LABELS];
			imagen = new int[LENGTH_LABELS];

			for (int i = 0; i < LENGTH_LABELS; i++) {
				panel[i] = new JPanel();
				panel[i].setPreferredSize(DIM_PANEL_FOTOS);
				panel[i].setBackground(COLOR_PANEL_FOTOS);
				panel[i].setBorder(BorderFactory.createLineBorder(COLOR_BORDE_PANEL_FOTOS, 2));
				panel[i].setLayout(new GridBagLayout());
				label[i] = new JLabel();
				label[i].setPreferredSize(DIM_LABEL);
				imagen[i] = -1;
				
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
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.insets = new Insets(5, 5, 5, 0);
			add(bAnterior, gridBagConstraints);

			bSiguiente.setPreferredSize(new Dimension(55, 120));
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 7;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.insets = new Insets(5, 0, 5, 5);
			add(bSiguiente, gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			
			// Se inicia la vista previa
			for (int i = 0; i < LENGTH_LABELS; i++)
				label[i].setIcon(galeria.getFoto(imagen[i]));
			
			for (int i = 0; i < LENGTH_LABELS; i++)
				label[i].addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						boolean encontrado = false;
						for (int j = 0; j < LENGTH_LABELS && !encontrado; j++)
							if (e.getSource() == label[j]) {
								DescripcionVivienda dV = getVivienda(imagen[j]);
								//actualizarPanel(); No hay nada que actualizar en este panel
								if(dV != null)
									VentanaPpal.lanzarVentanaDescripcion(dV);
								encontrado = true;
							}
					}
				}
				);

			bAnterior.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (imagen[0] > 0) {
						for (int i = LENGTH_LABELS - 1; i > 0; i--) {
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
					if (imagen[LENGTH_LABELS - 1] < visitadas.size() - 1) {
						for (int i = 0; i < LENGTH_LABELS - 1; i++) {
							imagen[i] = imagen[i + 1];
							label[i].setIcon(label[i + 1].getIcon());
							label[i].setToolTipText(label[i + 1].getToolTipText());
						}
						imagen[LENGTH_LABELS - 1]++;
						actualizarToolTip(LENGTH_LABELS - 1);
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
				if (index >= 0 && index < visitadas.size()) return visitadas.get(index);
				else return null;
			}
			
			public ArrayList<Integer> getIdDescViviendVisitadas()
			{
				return idVisitadas;
			}
			
			/*public ArrayList<ImageIcon> getImagenes()
			{
				return galeria.getFotos();
			}*/

		/* Setters */
			
			public void addVivienda(DescripcionVivienda vivienda)
			{
				visitadas.add(vivienda);
				idVisitadas.add(vivienda.getId());
				galeria.addFoto(vivienda.getUrlFoto(), vivienda.getId());
				//Actualizamos el id de la imagen correspondiente para que 
				//cuando actualice, ponga bien la imagen de la vivienda
				if(visitadas.size() > LENGTH_LABELS) 
					imagen[LENGTH_LABELS-1] = visitadas.size()-1;
				else
					imagen[visitadas.size()-1] = visitadas.size()-1;
			}

		/* Otros métodos */

			/**
			 * @param index
			 * @return si el índice es correcto, devuelve el elemento que NO quita de la lista.
			 */
			public DescripcionVivienda getDescVivienda(int index)
			{
				if (index >= 0 && index < visitadas.size()) 
					//return vistas.remove(index); -> Por que haces un remove?? no entiendo
					// Si ya forma parte de visitados, no se debe eliminar.
					return visitadas.get(index); 
				else 
					return null;
			}
			
			/**
			 *  Sirve para actualizar el panel a las últimas casas visitadas por el usuario
			 */
		 	public void actualizarPanel()
			{		 		
				//Rellenamos con los últimos añadidos por el usuario.
				if (visitadas.size() > LENGTH_LABELS)
					for (int i = 0; i < LENGTH_LABELS; i++) {
						imagen[i] = visitadas.size() - LENGTH_LABELS + i;
						actualizarToolTip(i);
					}
				else actualizarToolTip(visitadas.size() - 1);
			}

		/* AUXILIARES */

			/**
			 * Esto sólo es para ver en la consola el contenido del ArrayList y depurar más rápido.
			 */
			void mostrarViviendas()
			{
				for (Iterator<DescripcionVivienda> it = visitadas.iterator(); it.hasNext(); )
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
				//Si tienen mismo id, entonces tienen mismo contenido
				//if (visitadas.get(imagen[index]).getId() != galeria.getIdIcon((ImageIcon)label[index].getIcon())) {
				if(imagen[index] >= 0)
				{
					DescripcionVivienda dV = visitadas.get(imagen[index]);
					String localización = "Madrid, ";
					String[] loca1 = dV.getLocalizacion().split("/");
					loca1[0] = loca1[loca1.length - 1].replaceAll("-", " ");
					localización += VentanaPpal.transformar(loca1[0].substring(0, 1).toUpperCase() + loca1[0].substring(1));
					String mensaje = "<html>" +
						"Nombre:  " + dV.getTitulo() + "<br>" +
						"Tipo:  " + tipoToString(dV.getTipo()) + "<br>" +
						"Estado:  " + estadoToString(dV.getEstado()) + "<br>" +
						"Localización:  " + localización + "<br>" +
						"Superficie:  " + dV.getSuperficie() +"m<sup>2</sup></html>";

					label[index].setToolTipText(mensaje);
					label[index].setIcon(galeria.getFoto(dV.getId()));
				}
				//}
			}

}
