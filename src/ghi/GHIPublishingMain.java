package ghi;

import org.restlet.*;
 
import java.io.File;

import java.io.File;

import org.restlet.Component;
import org.restlet.Restlet;

import org.restlet.data.Protocol;

public class GHIPublishingMain 
  {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	  {
		try {   
	        // Create a new Component.   
	        Component component = new Component();   
	  
	        // Add a new HTTP server listening on port 8182.   
	        component.getServers().add(Protocol.HTTP, 8083);   
	  
	        // Attach the sample application.   
	        component.getDefaultHost().attach(new GHIApplication());   
	  
	        // Start the component.   
	        component.start();   
	    } catch (Exception e) {   
	        // Something is wrong.   
	        e.printStackTrace();   
	    }   

	  }

}
