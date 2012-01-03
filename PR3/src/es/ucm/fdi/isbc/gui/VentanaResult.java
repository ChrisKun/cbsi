package es.ucm.fdi.isbc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
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
		private static int TOPE;
		private static final double TOPE_FIJO = 70;
		private static final double WIDTH_FIJO = 1130;
		
	/** Constructores **/
		
		public VentanaResult()
		{
			setName("Viviendas");
			setModalityType(DEFAULT_MODALITY_TYPE);	
			setLayout(new BorderLayout());

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

					String título = VentanaPpal.transformar(descrs.get(i).getTitulo());
					String localización = "Madrid, ";
					String[] loca = descrs.get(i).getLocalizacion().split("/");
					loca[0] = loca[loca.length - 1].replaceAll("-", " ");
					localización += VentanaPpal.transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));
					String descripción = VentanaPpal.cortarString(VentanaPpal.transformar(descrs.get(i).getDescripcion()), 
							TOPE, "DESCRIPCIÓN: ".length());

					String descr =	"<html><p align=\"justify\">" +
							"<b><u>NOMBRE</u></b>: " + título + "<br>" +
							"<b><u>LOCALIZACIÓN</u></b>: " + localización + "<br>" +
							"<b><u>PRECIO</u></b>: " + descrs.get(i).getPrecio() + " €<br>" +
							"<b><u>DESCRIPCIÓN</u></b>: " + descripción + "</p></html>";			

					imagen[i] = new JLabel();
					JLabel label = new JLabel(descr);
					JScrollPane scrollPane = new JScrollPane(label,
							JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

					try {

						ImageIcon imageIcon = new ImageIcon(new URL(descrs.get(i).getUrlFoto()));

						if (imageIcon.getIconHeight() == -1)
							imageIcon = new ImageIcon(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"), 
									"No hay foto");

						imagen[i].setIcon(imageIcon);
						imagen[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

						imagen[i].addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent e)
							{
								/**
								 * Al pinchar en una foto tenemos que ir a una descripción detallada de la casa 
								 * seleccionada o volver a hacer la query con la casa seleccionada con lo de MoreLikeThis...
								 * ya no me acuerdo de qué teníamos que hacer al pinchar aquí.
								 * 
								 * El evento creo que es mejor que se produzca al soltar el ratón de la imagen de la casa por 
								 * si has pinchado sin querer y quieres mover el ratón o algo así para que no cargue y tal que 
								 * puedas hacerlo, pero esto ya es cuestión de gustos y da un poco igual realmente.
								 */
								
								boolean encontrado = false;
								for (int j = 0; j < aL.size() && !encontrado; j++)
									if (e.getSource() == imagen[j]) {
										if (!VentanaPpal.panelVisitados.getVistas().contains(aL.get(j))) {
											VentanaPpal.panelVisitados.setVivienda(aL.get(j), (ImageIcon) (imagen[j].getIcon()));
											VentanaPpal.panelVisitados.actualizarPanel();
										}
										encontrado = true;
										dispose();
									}
							}
						}
						);

					}
					catch (MalformedURLException e) {
						e.printStackTrace();
					}
					
					p.add(imagen[i]);
					p.add(scrollPane);
					
				}
				
				this.add(p, BorderLayout.CENTER);
				this.setVisible(true);
			}

		/* AUXILIARES */

}
