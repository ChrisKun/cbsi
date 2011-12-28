package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.eventos.MuestraSolEvent;

public class VentanaPpal extends JFrame implements Observer
{

	private static final long serialVersionUID = 1L;

	private static PanelVisitados panelVisitados;
	private static PanelFiltro panelFiltro;
	private static PanelDiversidad panelDiversidad;
	private static JMenuBar barraMenus;
	private static JMenu mArchivo;
	private static JMenuItem salir;
	private static JSplitPane horizontal, vertical;

	private VentanaResult vResult;

	private boolean flag = false;

	public VentanaPpal()
	{
		super("Recomendador Viviendas");

		barraMenus = inicializaMenus();
		this.setJMenuBar(barraMenus);

		panelFiltro = new PanelFiltro();
		panelDiversidad = new PanelDiversidad();
		panelVisitados = new PanelVisitados();

		vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelDiversidad, panelVisitados);
		vertical.setDividerSize(5);
		vertical.setDividerLocation(0.8);
		vertical.setOneTouchExpandable(false);
		vertical.setEnabled(false);

		horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelFiltro, vertical);
		horizontal.setDividerSize(3);
		horizontal.setOneTouchExpandable(false);
		horizontal.setEnabled(false);

		this.setContentPane(horizontal);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim.width - 100, dim.height - 100);
		setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Devuelve el menú de la ventana
	 * @return El menú de la ventana
	 */
	private JMenuBar inicializaMenus()
	{
		JMenuBar barra = new JMenuBar();
		mArchivo = new JMenu("Archivo");
		salir = new JMenuItem("Salir");
		mArchivo.add(salir);
		barra.add(mArchivo);

		salir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}
		);

		return barra;
	}

	public void update(Observable o, Object arg)
	{
		if (arg instanceof MuestraSolEvent) {
			
			if (vResult == null) vResult = new VentanaResult();
			vResult.setResultado(((MuestraSolEvent)arg).getDescrs());
			
		}
		else {
			if (flag) {
				panelFiltro.enableButton(false);
				flag = false;
			}
			else {
				panelFiltro.enableButton(true);
				flag = true;
			}
		}
	}

}
