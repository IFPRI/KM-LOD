package ghi;

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

public class OverviewPageResource extends ServerResource  
  {
  private String resourceName = null;
  private String statistics = null;
  GHIApplication LODApplication;
  ConnectorToOntology connector;
  LODReporter reporter;
  String xml = null;
  String currentServerPath = null;




    /**
     * Constructor disabling content negotiation and indicating if the
     * identified resource already exists.
     */

 
 
    @Override
    protected void doInit() throws ResourceException 
      {
      LODApplication= (GHIApplication)this.getApplication();
	  connector = LODApplication.getOntology();
	  reporter = LODApplication.getReporter();
      
      String serverPath = getRequest().getRootRef().toString();

      int index = serverPath.indexOf("/lod");
      this.currentServerPath = serverPath.substring(0, index);
      System.out.println("currentServerPath"+currentServerPath);
          // showing the main page
          
      
      }

    /**
     * Handle the HTTP GET method by returning a simple textual representation.
     */


  public Representation get()
    {
	com.hp.hpl.jena.rdf.model.Model selectedRDFmodel= connector.getOverviewRDF();
	xml = reporter.getOntologyOverview(selectedRDFmodel,"", currentServerPath, "EN");
	return new StringRepresentation(xml,MediaType.TEXT_HTML, Language.ENGLISH,CharacterSet.UTF_8);
    }
  
  public String getCurrentServerPath()
    {
    return this.currentServerPath;
    }
  }
  
