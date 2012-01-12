package es.ucm.fdi.isbc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

class VentanaResult extends JDialog
{

	/** Atributos **/
	
		private static final long serialVersionUID = 1L;
		private static Dimension dim;
		private static ArrayList<DescripcionVivienda> aL;
		private static JLabel[] imagen;
		private static final double TOPE_FIJO = 70;
		private static final double WIDTH_FIJO = 1130;
		private Galeria galeria;
		static VentanaDescripcion vD;
		static int TOPE;
		
	/** Constructores **/
		
		public VentanaResult(Galeria galeria)
		{
			setTitle("Viviendas");
			setModalityType(DEFAULT_MODALITY_TYPE);	
			setLayout(new BorderLayout());
			
			this.galeria = galeria;

			JPanel[] margen = new JPanel[4];
			for (int i = 0; i < margen.length; i++)
				margen[i] = new JPanel();

			/** 
			 * Colores html = {aqua, black, blue, fuchsia, gray, green, lime, maroon, navy, olive, purple, red, silver, 
			 * teal, white, yellow}
			 */

			String res = "<html><p align=\"center\"><font face=\"Comic Sans MS, verdana\"; color=\"teal\"; size = 24>" +
					"RESULTADOS</font></p></html>";

			JLabel resultados = new JLabel(res);
			margen[0].add(resultados);

			add(margen[0], BorderLayout.NORTH);
			add(margen[1], BorderLayout.WEST);
			add(margen[2], BorderLayout.EAST);
			add(margen[3], BorderLayout.SOUTH);

			dim = Toolkit.getDefaultToolkit().getScreenSize();
			setPreferredSize(new Dimension(1024, 768));
			setMinimumSize(new Dimension(dim.width - 150, dim.height - 150));
			setSize(dim.width - 150, dim.height - 150);
			setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
			TOPE = (int) (((dim.width - 150) / WIDTH_FIJO) * TOPE_FIJO);

			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

	/** Métodos **/	
		
		/* Otros métodos */
		
			/**
			* Cuando hayamos obtenido el resultado con la lista de viviendas similares..
			* @param descrs
			*/
			public void setResultado(ArrayList<DescripcionVivienda> descrs)
			{				
				JPanel p = new JPanel();
				aL = descrs;
				p.setLayout(new GridLayout(aL.size(), 2, 10, 20));
				imagen = new JLabel[aL.size()];

				for (int i = 0; i < aL.size(); i++) {

					String titulo = VentanaPpal.transformar(descrs.get(i).getTitulo());
					String localizacion = "Madrid, ";
					String[] loca = descrs.get(i).getLocalizacion().split("/");
					loca[0] = loca[loca.length - 1].replaceAll("-", " ");
					localizacion += VentanaPpal.transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));
					String descripcion = VentanaPpal.cortarString(VentanaPpal.transformar(descrs.get(i).getDescripcion()), 
							TOPE, "DESCRIPCIÓN: ".length());
					descripcion = descripcion.substring(0, 1).toUpperCase() + descripcion.substring(1);

					// Actualizamos las DescripcionesVivienda con los textos ya corregidos para no tener que volver a corregirlos
						// luego. La localización no la actualizamos por si tenemos que usar la ruta del árbol más adelante.
					descrs.get(i).setTitulo(titulo);
					descrs.get(i).setDescripcion(descripcion);

					String descr =	"<html><p align=\"justify\">" +
							"<b><u>NOMBRE</u></b>: " + titulo + "<br>" +
							"<b><u>LOCALIZACIÓN</u></b>: " + localizacion + "<br>" +
							"<b><u>SUPERFICIE</u></b>: " + descrs.get(i).getSuperficie() + "m<sup>2</sup><br>" +
							"<b><u>PRECIO</u></b>: " + descrs.get(i).getPrecio() + " €<br>" +
							"<b><u>DESCRIPCIÓN</u></b>: " + descripcion + "</p></html>";			

					imagen[i] = new JLabel();
					JLabel label = new JLabel(descr);
					JScrollPane scrollPane = new JScrollPane(label,
							JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

					//A�adimos la foto del panel:
					int pos = galeria.addFoto(descrs.get(i).getUrlFoto(), descrs.get(i).getId());
					//La cargamos:
					imagen[i].setIcon(galeria.getFotoOrigPos(pos));
					imagen[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

					imagen[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e)
						{
							boolean encontrado = false;
							ArrayList<Integer> idYaVisitadas = VentanaPpal.panelVisitados.getIdDescViviendVisitadas();
							for (int j = 0; j < aL.size() && !encontrado; j++)
								if (e.getSource() == imagen[j]) {
									if (!idYaVisitadas.contains(aL.get(j).getId()))
										//La a�adimos a visitados por haber pinchado sobre ella. Y no en VentanaDescripcion
										VentanaPpal.panelVisitados.addVivienda(aL.get(j));
										VentanaPpal.panelVisitados.actualizarPanel();
										VentanaPpal.lanzarVentanaDescripcion(aL.get(j));
									encontrado = true;
									dispose();
								}
						}
					}
					);
					
					p.add(imagen[i]);
					p.add(scrollPane);

				}

				this.add(p, BorderLayout.CENTER);
				this.setVisible(true);
			}

		/* AUXILIARES */

}
