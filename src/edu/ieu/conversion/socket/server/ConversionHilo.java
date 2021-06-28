package edu.ieu.conversion.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConversionHilo extends Thread {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public ConversionHilo(Socket socket) {
		this.clientSocket = socket;
	}
	
	@Override
	public void run() {		
		try {
			out = new PrintWriter( clientSocket.getOutputStream(), true );
			in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream() ) );
			
			String monedaId;
			while( (monedaId = in.readLine()) != null) {
				if(".".contentEquals(monedaId)) {
					out.println("good bye");
					break;
				}
				TipoCambio tipoCambio = buscarTipoCambioPorId(monedaId);
				if(tipoCambio == null) {
					out.println("No existe la conversion " + monedaId);
				}else {
					out.println( String.valueOf(tipoCambio.getFactorConversion()) );					
				}				
			}
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public TipoCambio buscarTipoCambioPorId(String id) {
		return ConversionServer.cambioMoneda.get(id);		
	}
	
}
