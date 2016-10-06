package at;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.*;
import java.io.*;
import java.sql.Statement;
import java.util.*;

import com.hp.hpl.jena.shared.*; 

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.*;
import org.w3c.dom.*;
import com.hp.hpl.jena.ontology.*;

public class ConnectorToOntology
  {
  Vector countries;
  
  static Document document;
  Model model= null;
  java.sql.Statement st = null;

  String fileName= "http://data.ifpri.org/lod/at.owl";
  boolean isValidISO3Flag = false; // if parameter ISO3 is valid, it is true
  String ontologyBase = "http://data.ifpri.org/lod/at/resource/";
   
  String base= "http://data.ifpri.org/lod/at/resource/";
  String resourceBase= "http://data.ifpri.org/lod/at/resource/";
  String dataBase= "http://data.ifpri.org/lod/at/data/";
  String pageBase= "http://data.ifpri.org/lod/at/page/";
  String queryBase;
  Property owlSameAsProperty; 
  RDFWriter w= null;
  
  
  
  public ConnectorToOntology()
    {
	  // read the geopolitical ontology file from www.fao.org/aims/geopolitical.owl
    
    model = ModelFactory.createDefaultModel();
    System.out.println("read file");    
    InputStream in= FileManager.get().open(fileName);
    if (in == null)
      {
      throw new IllegalArgumentException("File:"+ fileName+ " not found");
      }
    model.read(in,"RDF/XML");
    
//    model = changeNamespace(ontologyBase, resourceBase);
//    owlSameAsProperty = model.createProperty("http://www.w3.org/2002/07/owl#sameAs");
//    rewritePropertyWithSameAs("codeDBPediaID", "http://dbpedia.org/resource/");
//    rewritePropertyWithSameAs("codeAGROVOC", "http://aims.fao.org/aos/agrovoc/c_");
    
    System.out.println("done -read file");    
    
   
    }
  
  public Model getRDF(String resourceName)
    {
    if (resourceName == null)
      return null;
    
    
      
    String queryString = " construct { ?url ?relationship ?value } "+
                         " where { "+
                         "         {" +                        
                         "         ?url ?relationship ?value."+
                         "         FILTER (?url = <"+ontologyBase+resourceName+">) "+
                         "         }"+
                         "       UNION "+
                         "         { "+
                         "         ?url ?relationship ?value."+
                         "         FILTER (?relationship = <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>). "+
                         "         FILTER (?value = <"+ontologyBase+resourceName+">) "+
                         "          } "+


                         "        }";
    
    Model resultSetModel = executeQuery(queryString);
    return resultSetModel;
    }

  public Model getAllRDF(String resourceName)
    {
    if (resourceName == null)
      return null;
    
    
      
    String queryString = " construct { ?url ?relationship ?value } "+
                         " where { "+
                         "         {" +                        
                         "         ?url ?relationship ?value."+
                         "         FILTER (?url = <"+ontologyBase+resourceName+">) "+
                         "         }"+
                         "       UNION "+
                         "         { "+
                         "         ?url ?relationship ?value."+
                         "         FILTER (?relationship = <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>). "+
                         "         FILTER (?value = <"+ontologyBase+resourceName+">) "+
                         "          } "+

                         "        }";
    
    Model resultSetModel = executeQuery(queryString);
    return resultSetModel;
    }

  
  public Model getOverviewRDF()
    {
     
    String queryString = " construct { ?url ?relationship ?value } "+
                         " where { "+
                         "         {" +                        
                         "         ?url ?relationship ?value."+
                         "         FILTER (?url = <"+ontologyBase+">) "+
                         "         }"+
                          "        }";
    
    System.out.println(queryString);
    Model resultSetModel = executeQuery(queryString);
    System.out.println("resultSetModel"+resultSetModel.write(System.out));    
    return resultSetModel;
    }
  
  
  public List getInstances(String resourceName)
    {
    if (resourceName == null)
      return null;
    Property typeProperty =  model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    
    List instances = model.listStatements((Resource) null,typeProperty, (RDFNode)model.getResource(resourceBase+resourceName)).toList();
    return instances;
    }


  public List getClasses()
    {
    if (model.size() == 0)
      {
      return null;
      }
    
    List classses = model.listSubjectsWithProperty((Property)null, (RDFNode)model.getResource("http://www.w3.org/2002/07/owl#Class")).toList();
    return classses;
    }

  
  public List getSubClasses(String selectedClassName)
    {
    if (model.size() == 0)
      {
      return null;
      }
    Property subClassOfProperty = model.getProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");
    Resource selectedClass = model.getResource(selectedClassName);
    if (selectedClass ==null)
      return null;
      
    List classses = model.listSubjectsWithProperty(subClassOfProperty, (RDFNode)selectedClass).toList();
    return classses;
    }

  
  private Model executeQuery(String queryString)
    {  
    Model temModel = null;
    System.out.println(" executeQuery:"+queryString);
    
    
    Query query = QueryFactory.create(queryString);
   
         // Execute the query and obtain results
    
    QueryExecution qe = QueryExecutionFactory.create(query, this.model);
    temModel = qe.execConstruct();
      
     // Important - free up resources used running the query
    
    qe.close();
    
    return temModel;
    
    }

    // return 
  public Model getOntologyInRDF()
    {
    if (model != null)
      {    
        // add owl:sameAs
      owlSameAsProperty = model.createProperty("http://www.w3.org/2002/07/owl#sameAs");
//      rewritePropertyWithSameAs("codeDBPediaID", "http://dbpedia.org/resource/");
//      rewritePropertyWithSameAs("codeAGROVOC", "http://aims.fao.org/aos/agrovoc/c_");
      
        // change NameOfficialEN --> nameOfficial xml:lang
      
       }
    
    return model;
    }
  public Model getOntologyModel()
    {
    return this.model;
    }
  
  public String getBase()
    {
    return this.base;
    }
 
  
  
   /* this method is to provide a way to change the DBPedia (a literal value) ID into owl:sameAs */ 

 /*
  private void rewritePropertyWithSameAs(String name, String URI)
    {
    Resource selectedIndividual = null; 
	Property codeDBPediaIDProperty = model.getProperty(resourceBase+name);
    List list = model.listStatements((Resource)null, codeDBPediaIDProperty, (RDFNode)null).toList();
    for (Iterator it= list.iterator(); it.hasNext();)
      {
      com.hp.hpl.jena.rdf.model.Statement statement = (com.hp.hpl.jena.rdf.model.Statement)it.next();
      Resource subject = statement.getSubject();
      RDFNode object = statement.getObject();
      if (object.isLiteral() && subject != null)
        {
    	Literal literal = (Literal)object;
    	
    	selectedIndividual = model.getResource(resourceBase+subject.getLocalName());
    	Resource selectedResource = model.createResource(URI+literal.getValue());
    	if (selectedIndividual != null)
    	  {
    	  selectedIndividual.addProperty(owlSameAsProperty, (RDFNode)selectedResource);
          
    	  }
        }
      }
    
    }
*/  
  
  }

