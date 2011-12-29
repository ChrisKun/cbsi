package es.ucm.fdi.isbc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.BorderUIResource;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class VentanaResult extends JFrame
{

	/** Atributos **/
	
		private static final long serialVersionUID = 1L;
		private static Dimension dim;
		private static JLabel imagen;
		private static ArrayList<DescripcionVivienda> aL;

	/** Constructores **/
		
		public VentanaResult()
		{
			super("Viviendas");
			setLayout(new BorderLayout());
			
			JPanel[] margen = new JPanel[4];
			for (int i = 0; i < margen.length; i++)
				margen[i] = new JPanel();
			
			/* 
			 * Colores = {aqua, black, blue, fuchsia, gray, green, lime, maroon, navy, olive, purple, red, silver, teal,
			 * white, yellow} 
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
			setPreferredSize(new Dimension(800, 600));
			setSize(dim.width - 150, dim.height - 150);
			setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
			
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
				p.setLayout(new GridLayout(descrs.size(), 2, 20, 20));
				
				aL = descrs;
				
				for (int i = 0; i < descrs.size(); i++) {
					
					String título = transformar(descrs.get(i).getTitulo());
					String localización = "Madrid, ";
					String[] loca = descrs.get(i).getLocalizacion().split("/");
					loca[0] = loca[loca.length - 1].replaceAll("-", " ");
					localización += transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));
					String descripción = transformar(descrs.get(i).getDescripcion());
					
					String descr =	"<html><p align=\"justify\">" +
							"<b><u>NOMBRE</u></b>: " + título + "<br>" +
							"<b><u>LOCALIZACIÓN</u></b>: " + localización + "<br>" +
							"<b><u>PRECIO</u></b>: " + descrs.get(i).getPrecio() + " €<br>" +
							"<b><u>DESCRIPCIÓN</u></b>: " + descripción + "</p></html>";			
					
					imagen = new JLabel();
					JLabel label = new JLabel(descr);
					
					try {
						ImageIcon imageIcon = new ImageIcon(new URL(descrs.get(i).getUrlFoto()));
						if (imageIcon.getIconHeight() == -1)
							imageIcon = new ImageIcon(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"), 
									"No hay foto");
						
						imagen.setIcon(imageIcon);
						imagen.setName(String.valueOf(i));
						imagen.setBorder(BorderUIResource.getLoweredBevelBorderUIResource());
					}
					catch (MalformedURLException e) {
						e.printStackTrace();
					}

					imagen.addMouseListener(new MouseListener() {
						
						public void mouseClicked(MouseEvent e) {;}

						public void mouseEntered(MouseEvent e) {;}

						public void mouseExited(MouseEvent e) {;}

						public void mousePressed(MouseEvent e) {;}

						public void mouseReleased(MouseEvent e)
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

							VentanaPpal.panelVisitados.setVivienda(aL.get(Integer.parseInt(imagen.getName())));
							VentanaPpal.panelVisitados.actualizarPanel();
						}
					}
					);
					
					p.add(imagen);
					p.add(label);
					
				}
				this.add(p, BorderLayout.CENTER);
				this.setVisible(true);
			}

		/* AUXILIARES */
			
			private String transformar(String string)
			{
				String s = string;
				String transformar = "";
				while (!s.isEmpty()) {
					String car = s.substring(0, 1);
					s = s.substring(1);
					if (car.equals("Ã") || car.equals("Â") || car.equals("m")) {
						car += s.substring(0, 1);
						s = s.substring(1);
						if (car.charAt(0) == 'm' && car.charAt(1) != '2') {
							transformar += "m";
							car = car.substring(1);
							if (car.equals("Ã") || car.equals("Â")) {
								car += s.substring(0, 1);
								s = s.substring(1);
							}
						}
					}
					transformar += codificar(car);
				}
				return transformar;
			}
			
			private String codificar(String cod)
			{
				// Debido a la propia codificación del Eclipse y a que no reconoce ciertos caracteres: Á, Í, Ï se escriben aquí
				// de la misma forma lo cuál generaría un problema en la conversión, por suerte, las dos últimas no son letras
				// muy utilizadas.
				
				if (cod.equals("Ã­")) return "í";
				else if (cod.equals("Ã©")) return "é";
				else if (cod.equals("Ã¡")) return "á";
				else if (cod.equals("Ã³")) return "ó";
				else if (cod.equals("Ãº")) return "ú";
				else if (cod.equals("Ã±")) return "ñ";
				else if (cod.equals("Ã�")) return "Á";
				else if (cod.equals("Ã‰")) return "É";
				else if (cod.equals("Ã�")) return "Í";
				else if (cod.equals("Ã“")) return "Ó";
				else if (cod.equals("Ãš")) return "Ú";
				else if (cod.equals("Ã‘")) return "Ñ";
				else if (cod.equals("Ã„")) return "Ä";
				else if (cod.equals("Ã‹")) return "Ë";
				else if (cod.equals("Ã�")) return "Ï";
				else if (cod.equals("Ã–")) return "Ö";
				else if (cod.equals("Ãœ")) return "Ü";
				else if (cod.equals("Ã¤")) return "ä";
				else if (cod.equals("Ã«")) return "ë";
				else if (cod.equals("Ã¯")) return "ï";
				else if (cod.equals("Ã¶")) return "ö";
				else if (cod.equals("Ã¼")) return "ü";
				else if (cod.equals("Âª")) return "ª";
				else if (cod.equals("Âº")) return "º";
				else if (cod.equals("m2")) return "m<sup>2</sup>";
				else return cod;
			}

}
