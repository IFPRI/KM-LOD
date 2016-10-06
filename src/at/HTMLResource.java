package at;

import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource ;

import org.restlet.resource.ResourceException;
/**
 * This resource represents an individual self-governing instance
 */

public class HTMLResource extends ServerResource  
  {
  private String resourceName = null;
  private String statistics = null;
  ATApplication LODApplication;
  at.ConnectorToOntology connector;
  LODReporter reporter;
  String xml = null;
  String currentServerPath;

    /**
     * Constructor disabling content negotiation and indicating if the
     * identified resource already exists.
     */

 
    @Override
    protected void doInit() throws ResourceException 
      {
      LODApplication= (ATApplication)this.getApplication();
	  connector = (at.ConnectorToOntology)LODApplication.getOntology();
	  reporter = LODApplication.getReporter();
      this.resourceName  = getRequest().getAttributes().get("name").toString();
            // showing the main page
      String serverPath = this.getRequest().getRootRef().toString();
      int index = serverPath.indexOf("/lod");
      this.currentServerPath = serverPath.substring(0, index);
      System.out.println("currentServerPath"+currentServerPath);
          
      System.out.println("resourceName:"+ resourceName);
      }

    /**
     * Handle the HTTP GET method by returning a simple textual representation.
     */


  public Representation get()
    {
	com.hp.hpl.jena.rdf.model.Model selectedRDFmodel= connector.getRDF(resourceName);
	xml = reporter.getHTML(selectedRDFmodel, resourceName,currentServerPath );
	return new StringRepresentation(xml,MediaType.TEXT_HTML, Language.ENGLISH,CharacterSet.UTF_8);
    }
  
  
}