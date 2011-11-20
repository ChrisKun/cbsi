package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.GridLayout;
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
import javax.swing.JTextArea;

import es.ucm.fdi.isbc.viviendas.representacion.Coordenada;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;

@SuppressWarnings("serial")
public class PanelCaract extends JPanel{
	private JTextArea[] textAreas;
	private JLabel[] labels;
	private JComboBox tipoVivienda, estadoVivienda;
	private Coordenada coordenada;
	
	public PanelCaract(){
		super();
		setLayout(new GridLayout(10,2));
		JLabel tipoLabel = new JLabel("Tipo vivienda ");
		JLabel estadoLabel = new JLabel("Estado vivienda ");
		tipoVivienda = new JComboBox(TipoVivienda.values());
		estadoVivienda = new JComboBox(EstadoVivienda.values());
		add(tipoLabel); add(tipoVivienda);
		add(estadoLabel); add(estadoVivienda);
		labels = new JLabel[8];
		labels[0] = new JLabel("Superficie ");
		labels[1]  = new JLabel("Habitaciones ");
		labels[2]  = new JLabel("Baños ");
		labels[3]  = new JLabel("Precio medio ");
		labels[4]  = new JLabel("Precio zona ");
		labels[5] = new JLabel("Localización ");
		labels[6]  = new JLabel("Coordenada Latitud ");
		labels[7] = new JLabel("Coordenada Longitud ");
		
		textAreas = new JTextArea[8];
		int i = 0;
		for (JLabel l: labels){
			add(l);
			textAreas[i] = new JTextArea(1,20);
			textAreas[i].setText("");
			textAreas[i].setBorder(BorderFactory.createLineBorder(Color.black));
			add(textAreas[i]);
			i++;
		} 
		// Las coordenadas se obtienen a partir de la localización
		textAreas[6].setEditable(false);
		textAreas[7].setEditable(false);
	}
	
	public DescripcionVivienda getDescripcionVivienda(int id){
		// TODO: id de extras básicos y demás?
		DescripcionVivienda caract = new DescripcionVivienda(id);
		caract.setTipo((TipoVivienda)tipoVivienda.getSelectedItem());
		caract.setEstado((EstadoVivienda)estadoVivienda.getSelectedItem());
		// Si la conversión es errónea suponemos que el usuario ha introducido datos
		try{
			if (textAreas[0].getText() != "")
				caract.setSuperficie(Integer.parseInt(textAreas[0].getText()));
			else caract.setSuperficie(null);
		}catch(NumberFormatException e){}
		try{
			if (textAreas[1].getText() != "")
				caract.setHabitaciones(Integer.parseInt(textAreas[1].getText()));
			else caract.setHabitaciones(null);
		}catch(NumberFormatException e){}
		try{
			if (textAreas[2].getText() != "")
				caract.setBanios(Integer.parseInt(textAreas[2].getText()));
			else caract.setBanios(null);
		}catch(NumberFormatException e){}
		try{
			if (textAreas[3].getText() != "")
				caract.setPrecioMedio(Integer.parseInt(textAreas[3].getText()));
			else caract.setPrecioMedio(null);
		}catch(NumberFormatException e){}
		try{
			if (textAreas[4].getText() != "")
				caract.setPrecioZona(Integer.parseInt(textAreas[4].getText()));
			else caract.setPrecioZona(null);
		}catch(NumberFormatException e){}
		if (textAreas[5].getText() != ""){
			caract.setLocalizacion(textAreas[5].getText());
			// A partir de la localización obtenemos las coordenadas
			coordenada = new Coordenada();
			try {
				this.getStringCoordinate(textAreas[5].getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			caract.setCoordenada(coordenada);
			textAreas[6].setText(((Double)coordenada.getLatitud()).toString());
			textAreas[7].setText(((Double)coordenada.getLongitud()).toString());
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


}
