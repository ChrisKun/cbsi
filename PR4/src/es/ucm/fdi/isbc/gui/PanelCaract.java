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
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import jcolibri.datatypes.Text;
import es.ucm.fdi.isbc.viviendas.representacion.Coordenada;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;

public class PanelCaract extends JPanel implements TreeSelectionListener, FocusListener{
	private static final long serialVersionUID = 1L;
	private JTextField[] textFields;
	private JTextArea areaDescr;
	private JLabel[] labels;
	private JComboBox tipoVivienda, estadoVivienda;
	private Coordenada coordenada;
	private JTree localizaciones;
	
	public PanelCaract(JTree localiz){
		super();
		this.localizaciones = localiz;
		coordenada = new Coordenada();
		
		JPanel pDatos = new JPanel();
		GridLayout layout1 = new GridLayout(11,2);
		layout1.setVgap(18);
		pDatos.setLayout(layout1);
		
		
		JLabel tipoLabel = new JLabel("  Tipo vivienda ");
		JLabel estadoLabel = new JLabel("  Estado vivienda ");
		tipoVivienda = new JComboBox(TipoVivienda.values());
		estadoVivienda = new JComboBox(EstadoVivienda.values());
		pDatos.add(tipoLabel); pDatos.add(tipoVivienda);
		pDatos.add(estadoLabel); pDatos.add(estadoVivienda);
		
		labels = new JLabel[9];
		labels[0] = new JLabel("  Superficie ");
		labels[1]  = new JLabel("  Habitaciones ");
		labels[2]  = new JLabel("  Baños ");
		labels[3]  = new JLabel("  Precio medio ");
		labels[4]  = new JLabel("  Precio zona ");
		labels[5]  = new JLabel("  Localizacion ");
		labels[6]  = new JLabel("  Latitud ");
		labels[7]  = new JLabel("  Longitud ");
		labels[8] = new JLabel("  Descripción ");
	
		textFields = new JTextField[8];
		for (int i = 0; i < textFields.length; i++){
			pDatos.add(labels[i]);
			textFields[i] = new JTextField(20);
			textFields[i].setText("");
			textFields[i].setBorder(BorderFactory.createLineBorder(Color.black));
			pDatos.add(textFields[i]);
		} 
		
		pDatos.add(labels[labels.length - 1]);
		
		
		areaDescr = new JTextArea(3,5);
		JScrollPane scrollPane = new JScrollPane(areaDescr); 
		areaDescr.setEditable(true);
		
		
		textFields[5].addFocusListener(this);		
		
		//Create a tree that allows one selection at a time.
		localizaciones.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		//Listen for when the selection changes.
		localizaciones.addTreeSelectionListener(this);
		
		JSplitPane splitDatos = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitDatos.add(pDatos);
		splitDatos.add(scrollPane);
		
        //Dividimos la pantalla en dos.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(splitDatos);
		JScrollPane scrollocaliz = new JScrollPane(localizaciones);
		splitPane.add(scrollocaliz);
		
		Dimension minimumSize = new Dimension(170, 130);
        pDatos.setMinimumSize(minimumSize);
        localizaciones.setMinimumSize(minimumSize);
        splitDatos.setDividerLocation(420);
        splitPane.setDividerLocation(500); 
        splitDatos.setDividerSize(0);
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
        textFields[5].setText(localiz);
        try {
			this.getStringCoordinate(textFields[5].getText());
			textFields[6].setText(((Double)coordenada.getLatitud()).toString());
			textFields[7].setText(((Double)coordenada.getLongitud()).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public DescripcionVivienda getDescripcionVivienda(int id){
		DescripcionVivienda caract = new DescripcionVivienda(id);
		caract.setTipo((TipoVivienda)tipoVivienda.getSelectedItem());
		caract.setEstado((EstadoVivienda)estadoVivienda.getSelectedItem());
		// Si la conversión es errónea suponemos que el usuario ha introducido datos
		try{
			if (!textFields[0].getText().isEmpty())
				caract.setSuperficie(Integer.parseInt(textFields[0].getText()));
			else caract.setSuperficie(null);
		}catch(NumberFormatException e){}
		try{
			if (!textFields[1].getText().isEmpty())
				caract.setHabitaciones(Integer.parseInt(textFields[1].getText()));
			else caract.setHabitaciones(null);
		}catch(NumberFormatException e){}
		try{
			if (!textFields[2].getText().isEmpty())
				caract.setBanios(Integer.parseInt(textFields[2].getText()));
			else caract.setBanios(null);
		}catch(NumberFormatException e){}
		try{
			if (!textFields[3].getText().isEmpty())
				caract.setPrecioMedio(Integer.parseInt(textFields[3].getText()));
			else caract.setPrecioMedio(null);
		}catch(NumberFormatException e){}
		try{
			if (!textFields[4].getText().isEmpty())
				caract.setPrecioZona(Integer.parseInt(textFields[4].getText()));
			else caract.setPrecioZona(null);
		}catch(NumberFormatException e){}
		if (!textFields[5].getText().isEmpty()){
			caract.setLocalizacion(textFields[5].getText());
		}
		else caract.setLocalizacion(null);
		
		if ((!textFields[6].getText().isEmpty() && !textFields[7].getText().isEmpty()) 
				&& (!textFields[6].getText().equals("0.0") && !textFields[7].getText().equals("0.0"))){
			coordenada.setLatitud(Double.parseDouble(textFields[6].getText()));
			coordenada.setLongitud(Double.parseDouble(textFields[7].getText()));
			caract.setCoordenada(coordenada);

		}
		else if (!textFields[5].getText().isEmpty()){  
			//En caso de que le de al ok tras estar editando la localizacion
			try {
				this.getStringCoordinate(textFields[5].getText());
				if (coordenada.getLatitud() == 0.0 && coordenada.getLongitud() == 0.0)
					caract.setCoordenada(null);
				else
					caract.setCoordenada(coordenada);
			} catch (IOException e) {
				System.out.println("No se ha podido encontrar la localización dada.");
			}
		}
		if (!areaDescr.getText().isEmpty()){
			String userInput = areaDescr.getText();
			String escaped = RecomendadorVivienda.LUCENE_PATTERN.matcher(userInput).replaceAll(RecomendadorVivienda.REPLACEMENT_STRING);  
			caract.setDescripcion(new Text(escaped));
		}
		else caract.setDescripcion(null);
		
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
	        Vector<String> vector = new Vector<String>();

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
		public void focusLost(FocusEvent event) {
			//Llamamos al buscador de coordenadas
			if (!textFields[5].getText().isEmpty()){
				try {
					this.getStringCoordinate(textFields[5].getText());
					textFields[6].setText(((Double)coordenada.getLatitud()).toString());
					textFields[7].setText(((Double)coordenada.getLongitud()).toString());
				} catch (IOException e) {
					System.out.println("No se ha podido encontrar la localización dada.");
				}
			}
			
		}

		@Override
		public void focusGained(FocusEvent arg0) {
			// No tenemos que hacer nada
			
		}

}
