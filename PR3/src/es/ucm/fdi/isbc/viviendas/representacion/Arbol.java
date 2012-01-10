package es.ucm.fdi.isbc.viviendas.representacion;


import java.util.ArrayList;
import java.util.Iterator;

/**
 *	Partimos del nodo raíz siempre.
 *	Representamos un árbol general mediante un árbol binario de la forma hijo-izq = hijo, hijo-der = hermano.
 *
 */

public class Arbol
{
	/** Atributos **/
	
		private String valor;
		private Integer nivel;
		private Arbol hijo;
		private Arbol hermano;
	
	/** Constructoras **/
	
		public Arbol(String valor, Integer nivel)
		{
			this.valor = valor;
			this.nivel = nivel;
			hijo = null;
			hermano = null;
		}
	
		public Arbol(String localizacion)
		{
			valor = "madrid";
			nivel = 0;
			hermano = null;
			if (localizacion == null || localizacion.isEmpty() || localizacion.equals("/"))
				hijo = null;
			else {
				String[] nodos = localizacion.split("/");
				hijo = new Arbol(nodos[1], nivel + 1);
				Arbol aux = hijo;
			
				for (int i = 2; i < nodos.length; i++) {
					aux.setHijo(new Arbol(nodos[i], aux.getNivel() + 1));
					aux = aux.hijo;
				}
			}
		}
	
	/** Métodos **/
	
		/* Getters */
		
			public String getValor()
			{
				return valor;
			}
			
			public Integer getNivel()
			{
				return nivel;
			}
			
			public Arbol getHijo()
			{
				if (isLeaf()) return null;
				return hijo;
			}
			
			public Arbol getHermano()
			{
				if (isLast()) return null;
				return hermano;
			}
		
		/* Setters */
		
			public void setValor(String valor)
			{
				this.valor = valor;
			}
			
			public void setNivel(Integer nivel)
			{
				this.nivel = nivel;
			}
			
			public void setHijo(Arbol hijo)
			{
				this.hijo = hijo;
			}
			
			public void setHermano(Arbol hermano)
			{
				this.hermano = hermano;
			}
		
		/* Métodos sobreescritos */

			public boolean equals(Object object)
			{
				if (object == null) return false;
				if (!(object instanceof Arbol)) return false;
				
				Arbol a = (Arbol)object;
				
				if (isLeaf() && isLast())
					return	valor.equals(a.getValor());
				else if (isLast())
					return	valor.equals(a.getValor()) &&
							hijo.equals(a.getHijo());
				else if (isLeaf())
					return	valor.equals(a.getValor()) &&
							hermano.equals(a.getHermano());
				else
					return	valor.equals(a.getValor()) &&
							hijo.equals(a.getHijo()) &&
							hermano.equals(a.getHermano());
			}
			
			public boolean contains(Object object) throws ClassCastException, NullPointerException
			{
				if (object == null)
					throw new NullPointerException("El objeto recibido es null.");
					
				if (!(object instanceof Arbol))
					throw new ClassCastException("El objeto recibido no es de la clase Arbol.");
				
				Arbol a1 = this;
				Arbol a2 = (Arbol)object;
				
				boolean terminar = false;
				while (!terminar && (a1 != null) && (a2 != null)) {
				
					// Mientras los nodos del árbol general y de la ruta sean iguales, bajar de nivel.
					while (a1.getValor().equals(a2.getValor()) && ((a2 = a2.getHijo()) != null) && ((a1 = a1.getHijo()) != null));
					
					if ((a1 != null) && (a2 != null)) {
					
						// Cuando los nodos sean distintos, mirar a ver si son hermanos.	
						while (!a1.getValor().equals(a2.getValor()) && ((a1 = a1.getHermano()) != null))
							if (a1.isLast() && !a1.getValor().equals(a2.getValor()))
								terminar = true;

					}

				}

				if (a2 != null) return false;
				else return true;
			}
			
		/* Otros métodos */
		
			// Devuelve true si el Árbol implícito no tiene hijos.
			public boolean isLeaf()
			{
				return hijo == null;
			}
			
			// Devuelve true si el Árbol implícito es el último hermano o si es hijo único.
			public boolean isLast()
			{
				return hermano == null;
			}
			
			// Devuelve el número total de los hijos del árbol implícito.
			public int getNumHijos()
			{
				int hijos = 0;
				if (!isLeaf()) {
					Arbol arbol = hijo;
					hijos++;
					while ((arbol = arbol.getHermano()) != null)
						hijos++;
				}
				return hijos;
			}
			
			// Devuelve todos los hijos del árbol implícito.
			public ArrayList<Arbol> getHijos()
			{
				ArrayList<Arbol> hijos = new ArrayList<Arbol>();
				
				if (!isLeaf()) {
					Arbol a = hijo;
					hijos.add(a);
					while ((a = a.getHermano()) != null)
						hijos.add(a);
				}
				
				return hijos;
			}
			
			// Devuelve el nivel del padre común entre dos árboles
			public int getNivelPadreComun(Arbol arbol)
			{
				if (this.equals(arbol)) return getProfundidad();
				int nivel = 0;
				Arbol a1 = this;
				Arbol a2 = arbol;
				while (!a1.isLeaf() && !a2.isLeaf() && a1.getValor().equals(a2.getValor())) {
					nivel++;
					a1 = a1.getHijo();
					a2 = a2.getHijo();
				}
				return nivel;
			}
			
			// Devuelve la ruta de un string pasado por parámetro en un árbol implícito.
			public Arbol getPath(String localizacion)
			{
				boolean[] b = {false};
				String[] sol = {""};
				getPathRec(localizacion, "", b, sol);
				Arbol a = new Arbol(sol[0]);
				return a;
			}
			
			private void getPathRec(String loca, String acum, boolean[] encontrado, String[] sol)
			{
				if (getValor().startsWith(loca)) {
					encontrado[0] = true;
					sol[0] = acum;
				}
				else {
					String acum2 = new String(acum);
					if (acum2.contains("/")) acum2 = acum2.substring(0, acum.lastIndexOf('/'));
					if (!isLeaf() && !encontrado[0]) {
						Arbol hijo = getHijo();
						hijo.getPathRec(loca, acum + "/" + hijo.getValor(), encontrado, sol);
					}
					if (!isLast() && !encontrado[0]) {
						Arbol hermano = getHermano();
						hermano.getPathRec(loca, acum2 + "/" + hermano.getValor(), encontrado, sol);
					}
				}
			}
			
			// Devuelve la profundidad de una ruta
			public int getProfundidad()
			{
				Arbol a1 = this;
				int nivel = 0;
				
				while ((a1 = a1.getHijo()) != null)
					nivel++;
				
				return nivel;
			}
			
			// Agregar árbol al árbol implícito
			public void add(Arbol arbol)
			{
				if (arbol != null) {
					Arbol a1 = this;	// El árbol implícito al que queremos agregar el pasado por parámetro
					Arbol a2 = arbol.getHijo();	// El árbol pasado por parámetro.
					boolean agregado = false;
					while (!agregado && (a2 != null)) {
						
						// Lo primero, se mira si la raíz del árbol pasado por parámetro es algún hijo del nodo que 
							// se explora.

						boolean encontrado = false;
						ArrayList<Arbol> hijos = a1.getHijos();
						for (Iterator<Arbol> it = hijos.iterator(); !encontrado && it.hasNext(); ) {
						
							Arbol aux = it.next();
							encontrado = a2.getValor().equals((aux.getValor()));
							
							// En caso de que se encuentre que los valores mirados son iguales, avanzamos por ese camino
								// y esa parte del parámetro ya no la agregamos (para evitar repeticiones).
							
							if (encontrado) {
								a1 = aux;			// El árbol principal ahora es el que ha coincidido, con todos sus hijos
														// y hermanos.
								a2 = a2.getHijo();	// El árbol de ruta ahora empieza en el hijo del nodo que estaba
														// explorando.
							}
							
						}
						
						// En caso de que no se haya encontrado ninguna coincidencia entonces la localización es nueva y,
							// por tanto, hay que agregarla al árbol.
						
						if (!encontrado) {
							if (a1.isLeaf())		// Si el árbol de exploración es una hoja entonces no tiene hijos.
								a1.setHijo(a2);		// a2 (la parte de la ruta válida) es el primer hijo de a1.
							else {
								a1 = a1.getHijo();
								while (!a1.isLast())
									a1 = a1.getHermano();	// Si ya tiene hijos, posicionarse en el último
								a1.setHermano(a2);			// hermano y hacer que a2 sea el último hermano.
							}
							agregado = true;
						}
					}
				}
			}
			
			public String rutaToString()
			{
				String s = "";
				
				Arbol a = this;
				while ((a = a.getHijo()) != null)
					s += "/" + a.getValor();
				
				return s;
			}
}
