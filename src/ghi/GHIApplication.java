package ghi;


import java.io.File;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.data.Protocol;
import org.restlet.routing.Variable;


/**
 * The main Web application.
 * Previous version was     router.attach("/ghi/resource/{name}", HTMLResource.class);
 * Since we don't specify any url=pattern in the web.xml 
 * Now, the url pattern in web.xml is  <url-pattern>/lod/ghi/*</url-pattern>
 * So, the request URL is after ghi -->   router.attach("/resource/{name}", HTMLResource.class);
 
 * 

 */
public class GHIApplication extends org.restlet.Application 
  {
  private ConnectorToOntology connector; // this is a connector to ontology to retrieve data
  private LODReporter reporter; // this is a writer to xml or other format

  public GHIApplication() 
    {
	System.out.println("GHIApplication");  

	// connect to ontology to retrieve data
    
	connector = new ConnectorToOntology();
    reporter = new LODReporter(connector);
  
  
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
    router.attach("", OverviewPageResource.class);
    router.attach("/", OverviewPageResource.class);

    router.attach("/en", OverviewPageResource.class);
    router.attach("/EN", OverviewPageResource.class);
    router.attach("/ES", OverviewPageESResource.class);
    router.attach("/es", OverviewPageESResource.class);
    router.attach("/FR", OverviewPageFRResource.class);
    router.attach("/fr", OverviewPageFRResource.class);

    
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
