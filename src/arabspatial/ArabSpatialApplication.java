package arabspatial;


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
public class ArabSpatialApplication extends org.restlet.Application 
  {
  private arabspatial.ConnectorToOntology connector; // this is a connector to ontology to retrieve data
  private LODReporter reporter; // this is a writer to xml or other format

  public ArabSpatialApplication() 
    {
	System.out.println("Arab spatial Application");  

	// connect to ontology to retrieve data
    
	connector = new ConnectorToOntology();
    reporter = new LODReporter(connector);
    System.out.println("connecor:"+connector.getSubClasses("http://data.ifpri.org/lod/arabspatial/resource/Indicator"));
  
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
  public ConnectorToOntology getOntology()
    {
    return this.connector;
    }
  
  public LODReporter getReporter()
    {
    return this.reporter;
    }

  }
