package es.ucm.fdi.isbc.gui;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;

public class Galeria {

	/** Atributos **/

	/* Estáticos */

	public static final String NO_FOTO_PATH = "images\\No_hay_foto.jpg";
	public static final Image NO_FOTO = new ImageIcon(NO_FOTO_PATH,
			"No hay foto").getImage();
	public static final ImageIcon NO_FOTO_NORMAL = new ImageIcon(NO_FOTO);
	public static final ImageIcon NO_FOTO_100x100 = new ImageIcon(
			NO_FOTO.getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING));
	public static ImageIcon[] IMAGENES;

	private static final int SCALED_WIDTH = 100;
	private static final int SCALED_HEIGTH = 100;

	/* No estáticos */

	private ArrayList<ImageIcon> fotos; // Las fotos escaladas
	private ArrayList<ImageIcon> fotosOrig; // Las fotos originales
	private ArrayList<Integer> idFotos; // Los ids correspondientes a las fotos

	/** Constructores **/

	public Galeria() {
		fotos = new ArrayList<ImageIcon>();
		idFotos = new ArrayList<Integer>();
		fotosOrig = new ArrayList<ImageIcon>();
	}

	/*
	 * public Galeria(ArrayList<ImageIcon> fotos, ArrayList<Integer> idFotos) {
	 * this.fotos = fotos; this.idFotos = idFotos; }
	 */

	/** Métodos **/

	/* Getters */

	/*
	 * public ArrayList<ImageIcon> getFotos() { //Puede que haga falta
	 * tratamiento para las null. return fotos; }
	 */

	/**
	 * Devolvemos todos los IDs que aparecen en la galeria, es decir, todas las
	 * fotos que tenemos dentro de la galeria ya cargadas
	 * 
	 * @return
	 */
	/*
	 * public ArrayList<Integer> getFotos() { return idFotos; }
	 */

	// ----------------------------------

	/**
	 * Devuelve la imagen correspondiente al id de entrada
	 * 
	 * @param id
	 * @return
	 */
	public ImageIcon getFoto(int id) {
		int pos = getPosId(id);
		if (pos >= 0) {
			ImageIcon i = fotos.get(pos);
			if (i == null)
				// Si es null, entonces devolvemos la por defecto
				i = NO_FOTO_100x100;

			return i;
		} else
			return NO_FOTO_100x100;
	}

	/**
	 * Dada la posicion dentro del array, te devuelve la foto
	 * 
	 * @param pos
	 * @return
	 */
	public ImageIcon getFotoPos(int pos) { // Devolvemos su imagen
											// correspondiente
		if (pos < fotos.size()) {
			ImageIcon i = fotos.get(pos);
			if (i == null)
				// Si es null, entonces devolvemos la por defecto
				i = NO_FOTO_100x100;

			return i;
		} else
			return NO_FOTO_100x100;
	}

	// ----------------------------------

	public ImageIcon getFotoOrig(int id) {
		int pos = getPosId(id);
		if (pos >= 0) {
			ImageIcon i = fotosOrig.get(pos);
			if (i == null)
				// Si es null, entonces devolvemos la por defecto
				i = NO_FOTO_NORMAL;

			return i;
		} else
			return NO_FOTO_NORMAL;
	}

	public ImageIcon getFotoOrigPos(int pos) {
		if (pos < fotosOrig.size()) {
			ImageIcon i = fotosOrig.get(pos);
			if (i == null) // Si es null, entonces devolvemos la por defecto
				i = NO_FOTO_NORMAL;

			return i;
		} else
			return NO_FOTO_NORMAL;
	}

	// ----------------------------------
	/**
	 * Devuelve la posicion en la que se encuentra un id dado
	 * 
	 * @param id
	 * @return
	 */
	public int getPosId(int id) {
		return idFotos.indexOf(id);
	}

	/**
	 * Devuelve el id que se encuentra en la posicion index
	 * 
	 * @param index
	 * @return
	 */
	public int getIdPos(int index) {
		if (index < idFotos.size())
			return idFotos.get(index);
		else
			return -1;
	}

	// Función que busca para ver si se encuentra el id ya almacenado
	public boolean foundIdFoto(int id) {
		return idFotos.contains(id);
	}

	// ----------------------------------

	/**
	 * Dada una imagen, devuelve su id correspondiente Importante: usa la
	 * descripcion de la imagen para comparar
	 * 
	 * @param img
	 * @return el id
	 */
	public Integer getIdIcon(ImageIcon img) {
		// Si es un "sin foto", acabamos rápido, no podemos saber su id
		if (NO_FOTO_100x100.getDescription().equals(img.getDescription()))
			return -1;

		int pos = 0;
		boolean found = false;
		ImageIcon i;
		Iterator<ImageIcon> it = fotos.iterator();
		while (!found && it.hasNext()) {
			i = it.next();
			if (i != null && i.getDescription() == img.getDescription())
				found = true;
			else
				pos++;
		}

		if (found)
			return idFotos.get(pos);
		else
			return -1; // No se encontró la foto y por tanto el id tmp
	}

	/* Setters */

	/**
	 * Añade una foto ya cargada teniendo en cuenta que viene en tamaño original
	 * 
	 * @param newFoto
	 * @param idFoto
	 * @return devuelve la posición en la que se ha insertado la foto
	 */
	public int addFoto(ImageIcon newFoto, int idFoto) {
		fotosOrig.add(newFoto); // La original
		ImageIcon i = getScaledImageIcon(newFoto, SCALED_WIDTH, SCALED_HEIGTH);
		fotos.add(i); // La imagen escalada
		idFotos.add(idFoto); // su id correspondiente
		return fotos.size() - 1;
	}

	/**
	 * Añade una foto a partir de una url, devuelve la posicion donde se guardó
	 * la foto
	 * 
	 * @param urlfoto
	 * @param id
	 * @return devuelve la posición en la que se ha insertado la foto
	 */
	public int addFoto(String urlfoto, int id) {

		if (foundIdFoto(id))
			return getPosId(id);

		ImageIcon iEscalada; // La imagen escalada
		boolean descargada = false; // Si la foto la hemos encontrado o no

		try { // La URL puede lanzar excepcion
			URL url = new URL(urlfoto);
			ImageIcon i = new ImageIcon(url);
			descargada = i.getIconHeight() > 0; // Se ha conseguido descargar?

			if (descargada) {
				iEscalada = getScaledImageIcon(i, SCALED_WIDTH, SCALED_HEIGTH);
				fotosOrig.add(i);
				fotos.add(iEscalada);
				idFotos.add(id);
			} else {
				i = null;
				iEscalada = null;
				fotosOrig.add(i);
				fotos.add(iEscalada);
				idFotos.add(id);
			}
		} catch (MalformedURLException e) {
			// La URL no es valida, el string no se puede convertir
			iEscalada = null;
			fotosOrig.add(null);
			fotos.add(iEscalada);
			idFotos.add(id);
		}

		return fotos.size() - 1;
	}

	/* Otros métodos */

	/**
	 * Realiza el escalado a partir de una imagen
	 * 
	 * @param origin
	 * @param width
	 * @param heigth
	 * @return La propia imagen escalada
	 */
	public ImageIcon getScaledImageIcon(ImageIcon origin, int width, int heigth) {
		if (width == 0)
			width = SCALED_WIDTH;
		if (heigth == 0)
			heigth = SCALED_HEIGTH;
		return new ImageIcon(origin.getImage().getScaledInstance(width, heigth,
				Image.SCALE_FAST));
	}

	/**
	 * @param num
	 * @return una imagen de tamaño 100 x 100 VISTA PREVIA
	 */
	/*
	 * public Icon getVistaPrevia(final int NUM) { if (NUM >= 0 && NUM <
	 * fotos.size()) return
	 * VentanaPpal.redimensionarImageIcon(fotos.get(NUM).getImage(), 100, 100);
	 * else return NO_FOTO_100x100; }
	 */

}
