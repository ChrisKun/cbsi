package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class Galeria
{

	/** Atributos **/
	
		private ArrayList<ImageIcon> fotos;
		private static Image noFoto;

    /** Constructores **/
	
		public Galeria()
		{
			try {
				fotos = new ArrayList<ImageIcon>();
				ImageIcon imageIcon = new ImageIcon(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"), 
						"No hay foto");
				noFoto = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		public Galeria(ArrayList<ImageIcon> fotos)
		{
			try {
				this.fotos = fotos;
				ImageIcon imageIcon = new ImageIcon(new URL("http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg"), 
						"No hay foto");
				noFoto = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

	/** Métodos **/
		
		/* Setters */
		
			public void setFotos(ArrayList<ImageIcon> fotos)
			{
				this.fotos = fotos;
			}
	
		/* Otros métodos */
			
			/**
			 * @param num
			 * @return una imagen de tamaño 100 x 100 VISTA PREVIA
			 */
			public Icon getVistaPrevia(final int NUM, final int LENGTH)
			{
				if (NUM >= 0 && NUM < fotos.size()) {
					Image mini = fotos.get(NUM).getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
					return new ImageIcon(mini);
				}
				else return new ImageIcon(noFoto);
			}

			/**
			 * @param num
			 * @param dim
			 * @return la foto original, pero si el tamaño es mayor al contenedor, lo redimensiona.
			 */
			public Icon getFoto(int num, Dimension dim)
			{
				if (num >= 0 && num < fotos.size()) {
					// Si la foto es más grande que el contendor entonces redimensionamos.
					if (fotos.get(num).getIconWidth() > dim.getWidth()) {
						float v = getValor(fotos.get(num).getIconWidth(), dim.width);
						return Disminuir(fotos.get(num), v);
					}
					else if (fotos.get(num).getIconHeight() > dim.getHeight()) {
						float v = getValor(fotos.get(num).getIconHeight(), dim.height);               
						return Disminuir(fotos.get(num), v);
					}
					else return fotos.get(num);
				}
				else return new ImageIcon(noFoto.getScaledInstance(400, 400, Image.SCALE_AREA_AVERAGING));
			}
			
			/**
			 * @param imageIcon
			 * @param value
			 * @return redimensiona la imagen para que ingrese al contendor pero manteniendo sus proporciones.
			 */
			private ImageIcon Disminuir(ImageIcon imageIcon, float value)
			{
				int valEscalaX = (int) (imageIcon.getIconWidth() * value );
				int valEscalaY = (int) (imageIcon.getIconHeight() * value );
				Image mini = imageIcon.getImage().getScaledInstance(valEscalaX, valEscalaY, Image.SCALE_AREA_AVERAGING);
				return new ImageIcon(mini);
			}

			/**
			 * @param a
			 * @param b
			 * @return el valor de escala para redimensionar la imagen
			 */
			private float getValor(int a, int b)
			{
				return Math.abs((a / new Float(b)) - 2f);
			}
}
