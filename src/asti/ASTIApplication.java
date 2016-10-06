package asti;

import java.io.File;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.data.Protocol;
import org.restlet.routing.Variable;


/**
 * The main Web application.
 * 
 */
public class ASTIApplication extends org.restlet.Application 
  {
  private asti.ConnectorToOntology connector; // this is a connector to ontology to retrieve data
  private LODReporter reporter; // this is a writer to xml or other format

  public ASTIApplication() 
    {
	System.out.println("ASTI Application");  

	// connect to ontology to retrieve data
    
	connector = new asti.ConnectorToOntology();
    reporter = new LODReporter(connector);
    System.out.println("connecor:"+connector.getSubClasses("http://data.ifpri.org/lod/asti/resource/ASTI_Indicator"));
  
   }

    @Override
  public Restlet createInboundRoot() 
    {
    Router router = new Router(getContext());
//    Route defaultRoute = router.attachDefault(NeighborsResource.class);

    
      // neighbors 
     
//    router.attach("/", OntologyResource.class);
//    router.attach("/resource/", OntologyOverviewResource.class);
//    router.attach("/resource", OntologyOverviewResource.class);

 //   router.attach("/resource/{name}", HTMLResource.class);
 //   router.attach("/data/{name}", RDFResource.class);
    router.attach("/resource/{name}", HTMLResource.class);

    
    router.attach("/page/{name}", HTMLResource.class);
    router.attach("/resource/", OverviewPageResource.class);
    router.attach("/resource", OverviewPageResource.class);
 
    router.attach("/data/{name}", RDFResource.class);
    router.attach("/", OverviewPageResource.class);
    router.attach("", OverviewPageResource.class);

    
    // Add a route for user resources

    return router;
    }
  public asti.ConnectorToOntology getOntology()
    {
    return (asti.ConnectorToOntology)this.connector;
    }
  
  public LODReporter getReporter()
    {
    return this.reporter;
    }

  }
