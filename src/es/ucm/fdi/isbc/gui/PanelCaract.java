package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import es.ucm.fdi.isbc.viviendas.representacion.Coordenada;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;

@SuppressWarnings("serial")
public class PanelCaract extends JPanel implements TreeSelectionListener, FocusListener{
	private JTextField[] textAreas;
	private JLabel[] labels;
	private JComboBox tipoVivienda, estadoVivienda;
	private Coordenada coordenada;
	private JTree localizaciones;
	
	public PanelCaract(JTree localiz){
		super();
		this.localizaciones = localiz;
		coordenada = new Coordenada();
		
		JPanel pDatos = new JPanel();
		GridLayout layout1 = new GridLayout(10,2);
		layout1.setVgap(18);
		pDatos.setLayout(layout1);
		
		
		JLabel tipoLabel = new JLabel("  Tipo vivienda ");
		JLabel estadoLabel = new JLabel("  Estado vivienda ");
		tipoVivienda = new JComboBox(TipoVivienda.values());
		estadoVivienda = new JComboBox(EstadoVivienda.values());
		pDatos.add(tipoLabel); pDatos.add(tipoVivienda);
		pDatos.add(estadoLabel); pDatos.add(estadoVivienda);
		
		labels = new JLabel[8];
		labels[0] = new JLabel("  Superficie ");
		labels[1]  = new JLabel("  Habitaciones ");
		labels[2]  = new JLabel("  Baños ");
		labels[3]  = new JLabel("  Precio medio ");
		labels[4]  = new JLabel("  Precio zona ");
		labels[5]  = new JLabel("  Localizacion ");
		labels[6]  = new JLabel("  Latitud ");
		labels[7]  = new JLabel("  Longitud ");
	
		textAreas = new JTextField[8];
		int i = 0;
		for (JLabel l: labels){
			pDatos.add(l);
			textAreas[i] = new JTextField(20);
			textAreas[i].setText("");
			textAreas[i].setBorder(BorderFactory.createLineBorder(Color.black));
			pDatos.add(textAreas[i]);
			i++;
		} 
		
		textAreas[5].addFocusListener(this);		
		
		//Create a tree that allows one selection at a time.
		localizaciones.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		//Listen for when the selection changes.
		localizaciones.addTreeSelectionListener(this);
		
        //Dividimos la pantalla en dos.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(pDatos);
		splitPane.add(localizaciones);
		
		Dimension minimumSize = new Dimension(170, 130);
        pDatos.setMinimumSize(minimumSize);
        localizaciones.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(420); 
        splitPane.setDividerSize(0);
        splitPane.setPreferredSize(new Dimension(500, 700));
 
        //Add the split pane to this panel.
        add(splitPane);
	}
	
	/** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent event) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) localizaciones.getLastSelectedPathComponent();
 
        if (node == null) return;
        
        TreeNode[] nodos = node.getPath();
        String localiz = new String("");
        for(int i = 0; i < nodos.length-1; i++)
        	localiz += nodos[i] + "/";
        localiz += nodos[nodos.length-1];
        textAreas[5].setText(localiz);
        try {
			this.getStringCoordinate(textAreas[5].getText());
			textAreas[6].setText(((Double)coordenada.getLatitud()).toString());
			textAreas[7].setText(((Double)coordenada.getLongitud()).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public DescripcionVivienda getDescripcionVivienda(int id){
		DescripcionVivienda caract = new DescripcionVivienda(id);
		caract.setTipo((TipoVivienda)tipoVivienda.getSelectedItem());
		caract.setEstado((EstadoVivienda)estadoVivienda.getSelectedItem());
		// Si la conversión es errónea suponemos que el usuario ha introducido datos
		try{
			if (!textAreas[0].getText().isEmpty())
				caract.setSuperficie(Integer.parseInt(textAreas[0].getText()));
			else caract.setSuperficie(null);
		}catch(NumberFormatException e){}
		try{
			if (!textAreas[1].getText().isEmpty())
				caract.setHabitaciones(Integer.parseInt(textAreas[1].getText()));
			else caract.setHabitaciones(null);
		}catch(NumberFormatException e){}
		try{
			if (!textAreas[2].getText().isEmpty())
				caract.setBanios(Integer.parseInt(textAreas[2].getText()));
			else caract.setBanios(null);
		}catch(NumberFormatException e){}
		try{
			if (!textAreas[3].getText().isEmpty())
				caract.setPrecioMedio(Integer.parseInt(textAreas[3].getText()));
			else caract.setPrecioMedio(null);
		}catch(NumberFormatException e){}
		try{
			if (!textAreas[4].getText().isEmpty())
				caract.setPrecioZona(Integer.parseInt(textAreas[4].getText()));
			else caract.setPrecioZona(null);
		}catch(NumberFormatException e){}
		if (!textAreas[5].getText().isEmpty()){
			caract.setLocalizacion(textAreas[5].getText());
		}
		else caract.setLocalizacion(null);
		
		if ((!textAreas[6].getText().isEmpty() && !textAreas[7].getText().isEmpty()) 
				&& (!textAreas[6].getText().equals("0.0") && !textAreas[7].getText().equals("0.0"))){
			// A partir de la localización obtenemos las coordenadas
			try {
				//this.getStringCoordinate(textAreas[5].getText());
				coordenada.setLatitud(Double.parseDouble(textAreas[6].getText()));
				coordenada.setLongitud(Double.parseDouble(textAreas[7].getText()));
				caract.setCoordenada(coordenada);
			} catch(NumberFormatException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (!textAreas[5].getText().isEmpty()){  
			//En caso de que le de al ok tras estar editando la localizacion
			try {
				this.getStringCoordinate(textAreas[5].getText());
				if (coordenada.getLatitud() == 0.0 && coordenada.getLongitud() == 0.0)
					caract.setCoordenada(null);
				else
					caract.setCoordenada(coordenada);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return caract;
	}
	
	   private void getStringCoordinate(String Address) throws IOException {
	        URL url = new URL(getGeoCoordinateURL(Address));
	        InputStream stream = url.openStream();
	        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
	        String web = buffer.readLine();

	        String coordenadas[] = Split(web, ',');
	        coordenada.setLatitud(Double.parseDouble(coordenadas[2]));
	        coordenada.setLongitud(Double.parseDouble(coordenadas[3]));
	    }
	   

	    private String[] Split(String Text, int Character) {
	        Vector vector = new Vector();

	        int current = 0;
	        int prior = 0;

	        while ((current = Text.indexOf(Character, prior)) >= 0) {
	            vector.addElement(Text.substring(prior, current));
	            prior = current + 1;
	        }

	        vector.addElement(Text.substring(prior));
	        String[] split = new String[vector.size()];
	        vector.copyInto(split);

	        return split;
	    } 

	    private String getGeoCoordinateURL(String Address) {
	        return "http://maps.google.com/maps/geo?q=" + UrlEncode(Address) +
	                "&output=csv&key=" + getKey();
	    }
	    
	    private String getKey() {
	    	// TODO: De momento pongo esto 
			return "1";
		}

		private String UrlEncode(String Url) {
	        StringBuffer buffer = new StringBuffer();
	        char word;
	        for (int i = 0; i < Url.length(); i++){
	            word = Url.charAt(i);
	            if ((word >= '0' && word <= '9') || (word >= 'A' && word <= 'Z') ||
	                    (word >= 'a' && word <= 'z')) {
	                buffer.append(word);
	            }
	            else {
	                buffer.append("%").append(Integer.toHexString((int) Url.charAt(i)));
	            }
	        }
	        return buffer.toString();
	    }

		@Override
		public void focusGained(FocusEvent e) {
			//Nada en particular tenemos que hacer
			
		}

		@Override
		public void focusLost(FocusEvent event) {
			//Llamamos al buscador de coordenadas
			if (!textAreas[5].getText().isEmpty()){
				try {
					this.getStringCoordinate(textAreas[5].getText());
					textAreas[6].setText(((Double)coordenada.getLatitud()).toString());
					textAreas[7].setText(((Double)coordenada.getLongitud()).toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

}
