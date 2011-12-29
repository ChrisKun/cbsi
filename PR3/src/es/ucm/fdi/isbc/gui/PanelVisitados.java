package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class PanelVisitados extends JPanel
{

	/** Atributos **/

		private static final long serialVersionUID = 1L;

		private static ArrayList<DescripcionVivienda> vistas;

	/** Constructores **/

		public PanelVisitados()
		{
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			vistas = new ArrayList<DescripcionVivienda>();

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			dim.setSize(dim.width - 100, dim.height - 100);

			setMinimumSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
			setSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
		}

	/** Métodos **/

		/* Getters */

			public DescripcionVivienda getVivienda(int index)
			{
				if (index >= 0 && index < vistas.size()) return vistas.get(index);
				else return null;
			}

		/* Setters */

			public void setVivienda(DescripcionVivienda vivienda)
			{
				vistas.add(vivienda);
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
}
