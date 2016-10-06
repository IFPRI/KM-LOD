package arabspatial;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource ;



import com.hp.hpl.jena.rdf.model.RDFWriter;
/**
 * This resource represents an individual self-governing instance
 */

public class RDFResource extends ServerResource  
  {

  private String resourceName = null;
  private String statistics = null;
  ArabSpatialApplication LODApplication;
  ConnectorToOntology connector;
  LODReporter reporter;
  String xml = null;
  String currentServerPath;
  ServletOutputStream out ;


    /**
     * Constructor disabling content negotiation and indicating if the
     * identified resource already exists.
     */

 
 
    @Override
    protected void doInit() throws ResourceException 
      {
      LODApplication= (ArabSpatialApplication)this.getApplication();
  	  connector = LODApplication.getOntology();
  	  reporter = LODApplication.getReporter();
      this.resourceName  = getRequest().getAttributes().get("name").toString();
 
              // showing the main page
      String serverPath = this.getRequest().getRootRef().toString();
      int index = serverPath.indexOf("/lod");
      this.currentServerPath = serverPath.substring(0, index);
      }

    /**
     * Handle the HTTP GET method by returning a simple textual representation.
     */


  public Representation get()
    {
	final  com.hp.hpl.jena.rdf.model.Model  selectedRDFmodel= connector.getRDF(resourceName);
	
	OutputRepresentation rep = new OutputRepresentation(MediaType.APPLICATION_RDF_XML) 
	  {
        @Override
      public void write(OutputStream output) throws IOException 
        {
        try
          {
          RDFWriter writer = selectedRDFmodel.getWriter();
          writer.write(selectedRDFmodel, output,"");
          }
        catch (Exception e)
          {
          }
        }
      };
	return rep;
    }
  
  
}