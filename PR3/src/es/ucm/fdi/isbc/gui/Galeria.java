package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class Galeria
{

	/** Atributos **/
	
		private ArrayList<ImageIcon> fotos;
		private ArrayList<Integer> idFotos;
		private static Image noFoto;
		private static final String NO_FOTO_PATH = "http://farm8.staticflickr.com/7008/6591881587_2c24e9ab39_z.jpg";

    /** Constructores **/
	
		public Galeria()
		{
			try {
				fotos = new ArrayList<ImageIcon>();
				idFotos = new ArrayList<Integer>();
				ImageIcon imageIcon = new ImageIcon(new URL(NO_FOTO_PATH), "No hay foto");
				noFoto = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		public Galeria(ArrayList<ImageIcon> fotos, ArrayList<Integer> idFotos)
		{
			try {
				this.fotos = fotos;
				this.idFotos = idFotos;
				ImageIcon imageIcon = new ImageIcon(new URL(NO_FOTO_PATH), "No hay foto");
				noFoto = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

	/** Métodos **/
		
		/* Getters */

			public ArrayList<ImageIcon> getFotos()
			{
				return fotos;
			}

			public ImageIcon getFoto(int pos)
			{
				return fotos.get(pos);
			}

			public int getIdPos(int index)
			{
				return idFotos.get(index);
			}

		/* Setters */
		
			public void setFotos(ArrayList<ImageIcon> fotos, ArrayList<Integer> idFotos)
			{
				this.fotos = fotos;
				this.idFotos = idFotos;
			}
			
			public void addFoto(ImageIcon newFoto, int idFoto)
			{
				fotos.add(newFoto);
				idFotos.add(idFoto);
			}
			
			//Dada una imagen, devuelve su id correspondiente Importante: usa la descripcion de la imagen
			public Integer getIdIcon(ImageIcon img) {
				
				ImageIcon imageIconFake = new ImageIcon(noFoto, "No hay foto");
				
				if(imageIconFake.getDescription() == img.getDescription())
					return -1;
				
				int pos = 0;
				boolean found = false;
				Iterator<ImageIcon> it = fotos.iterator();
				while(!found && it.hasNext())
				{
					found = it.next().getDescription() == img.getDescription();
					if(found)
						return idFotos.get(pos);
					else
						pos++;
				}
				return -1; //No se encontró la foto y por tanto el id
			}

			
			// Busca una imagen através del id de su descripcion y devuelve la imagen
			public ImageIcon getidFoto(int idFoto)
			{
				int pos = 0;
				boolean found = false;
				Iterator<Integer> it = idFotos.iterator();
				while(!found && it.hasNext())
				{
					found = it.next() == idFoto;
					if(found)
						return fotos.get(pos);
					else
						pos++;
				}
				return null; //No se encontró la foto
			}
	
		/* Otros métodos */
			
			/**
			 * @param num
			 * @return una imagen de tamaño 100 x 100 VISTA PREVIA
			 */
			public Icon getVistaPrevia(final int NUM)
			{
				if (NUM >= 0 && NUM < fotos.size()) {
					Image mini = fotos.get(NUM).getImage().getScaledInstance(100, 100, Image.SCALE_FAST); //Image.SCALE_AREA_AVERAGING
					return new ImageIcon(mini, fotos.get(NUM).getDescription());
				}
				else return new ImageIcon(noFoto, "No hay foto");
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
				else return new ImageIcon(noFoto.getScaledInstance(400, 400, Image.SCALE_FAST)); //Image.SCALE_AREA_AVERAGING
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
				Image mini = imageIcon.getImage().getScaledInstance(valEscalaX, valEscalaY, Image.SCALE_FAST); //Image.SCALE_AREA_AVERAGING
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
