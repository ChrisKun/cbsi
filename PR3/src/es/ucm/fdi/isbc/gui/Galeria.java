package es.ucm.fdi.isbc.gui;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class Galeria
{

	/** Atributos **/
	
		private ArrayList<ImageIcon> fotos;
		private ArrayList<Integer> idFotos;
		private static final String NO_FOTO_PATH = "images\\No hay foto.png";
		private static final Image NO_FOTO = new ImageIcon(NO_FOTO_PATH, "No hay foto").getImage();
		static final ImageIcon NO_FOTO_NORMAL = new ImageIcon(NO_FOTO);
		static final ImageIcon NO_FOTO_100x100 =  new ImageIcon(NO_FOTO.getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING));

    /** Constructores **/
	
		public Galeria()
		{
			fotos = new ArrayList<ImageIcon>();
			idFotos = new ArrayList<Integer>();
		}
		
		public Galeria(ArrayList<ImageIcon> fotos, ArrayList<Integer> idFotos)
		{
			this.fotos = fotos;
			this.idFotos = idFotos;
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
			public Integer getIdIcon(ImageIcon img)
			{	
				ImageIcon imageIconFake = new ImageIcon(NO_FOTO, "No hay foto");
				
				if (imageIconFake.getDescription().equals(img.getDescription()))
					return -1;
				
				int pos = 0;
				boolean found = false;
				Iterator<ImageIcon> it = fotos.iterator();
				while(!found && it.hasNext()) {
					if (it.next().getDescription() == img.getDescription())
						found = true;
					else pos++;
				}

				if (found) return idFotos.get(pos);
				else return -1; //No se encontró la foto y por tanto el id
			}
	
		/* Otros métodos */
			
			/**
			 * @param num
			 * @return una imagen de tamaño 100 x 100 VISTA PREVIA
			 */
			public Icon getVistaPrevia(final int NUM)
			{
				if (NUM >= 0 && NUM < fotos.size())
					return VentanaPpal.redimensionarImageIcon(fotos.get(NUM).getImage(), 100, 100);
				else return NO_FOTO_100x100;
			}

}
