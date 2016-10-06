package asti;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.*;
import com.hp.hpl.jena.ontology.*;

import asti.ConnectorToOntology;

import java.text.DateFormat;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Vector;
import java.util.Collections;


public class LODReporter 
  {
  String report = null;	
  String baseOntology = "http://data.ifpri.org/lod/asti/resource/";
  String resourceBase;
  String base;
  String currentServerPath; 
  String rdf;
  Model model;
  String base_xmlns = "speed";
  asti.ConnectorToOntology connector;
  java.io.OutputStream out = null;
  String resourceName= null;
  Property typeProperty = null;
  Property sameAsProperty = null;
  Resource selectedResource = null;
  Vector allClassesVector = new Vector();
  
  /*
  public LODReporter(ConnectorToOntology connector)
    {
    this.connector = connector;
	this.model = connector.getOntologyInRDF();
    base =connector.getBase();
	resourceBase = connector.getBase();
    rdf= connector.getBase()+"data/";
    }
 */ 
  public LODReporter(asti.ConnectorToOntology connector2) 
    {
    this.connector = (asti.ConnectorToOntology) connector2;
    this.model = connector.getOntologyInRDF();
	base =connector.getBase();
	resourceBase = connector.getBase();
	rdf= connector.getBase()+"data/";
	
    }

public String getOntologyOverview(Model selectedModel,String resourceName, String currentServerPath,String language)
    {
    this.resourceName = resourceName;
    this.currentServerPath = currentServerPath;
    System.out.println("currentServerPath"+currentServerPath);
    report = "";
    typeProperty =  selectedModel.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    
    if (selectedModel == null)
      {
      return null;
      }
    report += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
              "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">"+
              "<html xmlns=\"http://www.w3.org/1999/xhtml\" \n"+
              "     xmlns:geo=\"http://aims.fao.org/aos/geopolitical.owl#\" \n"+
              "     xmlns:"+this.base_xmlns+"=\""+resourceBase+"\" \n"+
              "     xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n"+
              "     xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n"+
              "     xmlns:dc=\"http://purl.org/dc/elements/1.1/\" \n"+
              "     xmlns:dcterms=\"http://purl.org/dc/terms/\" \n"+
              "     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" \n"+
              "     xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" \n"+
              "         version=\"XHTML+RDFa 1.0\" xml:lang=\"en\">  \n";
              
    writeHeader();          
              
    report += "  <body about=\""+base_xmlns+":"+resourceName+"\" >  \n";
    report += "    <span rel=\"owl:sameAs\" resource =\""+baseOntology+resourceName+"\"></span> \n "; 
    writeIFPRILogo(language);
    report += "<p></p> \n"; 
  	report += "<p></p> \n"; 
  	report += "<p></p> \n"; 
    boolean writeAllResourcesFlag = true; // true if displays the main page
    writeBody(selectedModel,writeAllResourcesFlag,language);
  	report += "</body></html> \n"; 
    return report;
    }
  
  public String getHTML(Model selectedModel,String resourceName, String currentServerPath)
    {
    this.currentServerPath = currentServerPath;
    this.resourceName = resourceName;
    report = "";
    if (selectedModel == null)
      return null;

    selectedResource = selectedModel.getResource(resourceBase+resourceName);
    typeProperty =  selectedModel.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

    report += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
              "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\">"+
              "<html xmlns=\"http://www.w3.org/1999/xhtml\" \n"+
              "     xmlns:geo=\"http://aims.fao.org/aos/geopolitical.owl#\" \n"+
              "     xmlns:"+this.base_xmlns+"=\""+resourceBase+"\" \n"+
              "     xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n"+
              "     xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n"+
              "     xmlns:dc=\"http://purl.org/dc/elements/1.1/\" \n"+
              "     xmlns:dcterms=\"http://purl.org/dc/terms/\" \n"+
              "     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" \n"+
              "     xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" \n"+
              "         version=\"XHTML+RDFa 1.0\" xml:lang=\"en\">  \n";
              
    writeHeader();          
              
    report += "  <body about=\""+base_xmlns+":"+resourceName+"\" >  \n";
    report += "    <span rel=\"owl:sameAs\" resource =\""+baseOntology+resourceName+"\"></span> \n "; 
    writeIFPRILogo("EN");
    report += "<p></p> \n"; 
  	report += "<p></p> \n"; 
  	report += "<p></p> \n"; 
    
    writeBody(selectedModel,false,"EN");
  	report += "</body></html> \n"; 
    return report;

    }
  
  private void writeContent(Model selectedModel)
    {
    if (selectedModel == null)
      return;
      // type 

    selectedResource = selectedModel.getResource(resourceBase+resourceName);
    System.out.println("selectedResource:"+selectedResource);
    Hashtable resultHashtable = new Hashtable();
    Vector resultVector = new Vector();
    if (selectedResource != null )
      {
      List typeStatements = selectedResource.listProperties(typeProperty).toList();
      
      for(Iterator it = typeStatements.iterator(); it.hasNext();)
        {
        com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)it.next(); 
        Resource subject = st.getSubject();
        RDFNode object = st.getObject();
        if (subject.getLocalName().equalsIgnoreCase(selectedResource.getLocalName()) && object != null)
          {
          resultHashtable.put(object.toString(), st); 
          selectedModel.remove(st);
          }
        }
      if (resultHashtable.size()>0)
        {
    	resultVector = sort(resultHashtable);
        writeOpenBoxWithTitle("Types");
        for (int i=0; i<resultVector.size();i++)
          {
          com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)resultVector.elementAt(i); 
          writeStatement(selectedModel,st);
          }
        writeCloseBoxWithTitle();
        }
      }

     // same as 
    
    sameAsProperty =  selectedModel.getProperty("http://www.w3.org/2002/07/owl#sameAs");
    resultHashtable = new Hashtable();
    resultVector = new Vector();
    if (selectedResource != null )
      {
      List typeStatements = selectedResource.listProperties(sameAsProperty).toList();
      
      for(Iterator it = typeStatements.iterator(); it.hasNext();)
        {
        com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)it.next(); 
        Resource subject = st.getSubject();
        RDFNode object = st.getObject();
        if (subject.getLocalName().equalsIgnoreCase(selectedResource.getLocalName()) && object != null)
          {
          resultHashtable.put(object.toString(), st); 
          selectedModel.remove(st);
          }
        }
      if (resultHashtable.size()>0)
        {
    	resultVector = sort(resultHashtable);
        writeOpenBoxWithTitle("Same As");
        for (int i=0; i<resultVector.size();i++)
          {
          com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)resultVector.elementAt(i); 
          writeSameAsStatement(selectedModel,st);
          }
         // write down same as the entity in the owl version 
        report += "<li>\n";
        report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+sameAsProperty.getNameSpace()+sameAsProperty.getLocalName()+"\" title=\""+sameAsProperty.getNameSpace()+sameAsProperty.getLocalName()+"\">"+sameAsProperty.getLocalName()+"</a>";
        report +="  &#160; <span rel=\""+sameAsProperty.getURI()+"\" resource=\""+baseOntology+resourceName+"\">  <a href=\""+baseOntology+resourceName+"\" title = \""+baseOntology+resourceName+"\">"+baseOntology+resourceName+"</a> </span>\n";
        report += "</li>\n";
        
        writeCloseBoxWithTitle();
        }
      }

    
    
    
      // literal 
    resultHashtable = new Hashtable();
    List statementList = selectedModel.listStatements().toList();
    if (statementList != null && statementList.size()>0)
      {
      

      for(Iterator it = statementList.iterator(); it.hasNext();)
        {
        com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)it.next(); 
      
        Resource subject = (Resource)st.getSubject();
        if (subject.getLocalName().equalsIgnoreCase(selectedResource.getLocalName()))
          {
          Property selectedProperty = st.getPredicate();
          RDFNode object = st.getObject();
          if ( object.isLiteral())
            {
            resultHashtable.put(""+selectedProperty.getLocalName()+object.toString(),st); 
            }
          } 
        }
      
      if (resultHashtable.size()>0)
        {
    	resultVector = sort(resultHashtable);
        writeOpenBoxWithTitle("Data values");
        for (int i=0; i<resultVector.size();i++)
          {
          com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)resultVector.elementAt(i); 
          writeStatement(selectedModel,st);
          }
        writeCloseBoxWithTitle();
        }
      }


      // Resource 
    resultHashtable = new Hashtable();    
    statementList = selectedModel.listStatements().toList();
    if (statementList != null && statementList.size()>0)
      {

      for(Iterator it = statementList.iterator(); it.hasNext();)
        {
        com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)it.next(); 
      
        Resource subject = (Resource)st.getSubject();
        if (subject.getLocalName().equalsIgnoreCase(selectedResource.getLocalName()))
          {
          Property selectedProperty = st.getPredicate();
          RDFNode object = st.getObject();
          if (object.isResource())
            {
            resultHashtable.put(""+selectedProperty.getLocalName()+object.toString(),st); 
            }
          } 
        }
      if (resultHashtable.size()>0)
        {
    	resultVector = sort(resultHashtable);
        writeOpenBoxWithTitle("Relations");
        for (int i=0; i<resultVector.size();i++)
          {
          com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)resultVector.elementAt(i); 
          writeStatement(selectedModel,st);
          }
        writeCloseBoxWithTitle();
        }
      } 


      // instances if selectedResource is a class
    resultHashtable = new Hashtable();     
    statementList = selectedModel.listStatements().toList();
    if (statementList != null &&  statementList.size()>0)
      {

      for(Iterator it = statementList.iterator(); it.hasNext();)
        {
        com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)it.next(); 
      
        Resource subject = (Resource)st.getSubject();
        Property selectedProperty = st.getPredicate();
        RDFNode object = st.getObject();
        System.out.println(st.toString());
            
        if (selectedProperty.getLocalName().equalsIgnoreCase("type") && object.toString().equalsIgnoreCase(selectedResource.getURI()))
          {
          System.out.println("instance");
          resultHashtable.put(subject.getLocalName(),st); 
          }
        }

      if (resultHashtable.size()>0)
        {
    	resultVector = sort(resultHashtable);
        writeOpenBoxWithTitle("Instances");
        for (int i=0; i<resultVector.size();i++)
          {
          com.hp.hpl.jena.rdf.model.Statement statement = (com.hp.hpl.jena.rdf.model.Statement)resultVector.elementAt(i); 
          writeInstanceStatement(statement,selectedResource);
          }
        writeCloseBoxWithTitle();
        }
      } 



    }
  

  public void writeStatement(Model selectedModel, com.hp.hpl.jena.rdf.model.Statement statement)
    {
    if (statement == null)
      return;
    
    Property predicate = statement.getPredicate();
    
      // find the xml namespace 
      String newPath = this.currentServerPath+"/lod/asti/resource/";
    
    String selectedNameSpace = predicate.getNameSpace();
    String nameSpace = null;
    if (selectedNameSpace == null)
      return;
    
    if (selectedNameSpace.equalsIgnoreCase("http://purl.org/dc/elements/1.1/"))
      nameSpace = "dc";
    
    else if (selectedNameSpace.equalsIgnoreCase("dcterms=\"http://purl.org/dc/terms/"))
        nameSpace = "dcterms";
    
    else if (selectedNameSpace.equalsIgnoreCase("http://www.w3.org/2000/01/rdf-schema#"))
        nameSpace = "rdfs";

    else if (selectedNameSpace.equalsIgnoreCase("http://www.w3.org/2000/01/rdf-schema#"))
        nameSpace = "rdf";
    else if (selectedNameSpace.startsWith("http://aims.fao.org/aos/geopolitical.owl"))
        nameSpace = "geo";
    
    else if(selectedNameSpace.equalsIgnoreCase(resourceBase))
        nameSpace = this.base_xmlns;
    else
    	nameSpace= this.base_xmlns;	
    RDFNode object = statement.getObject();
    Resource objectResource = selectedModel.getResource(object.toString());
    
    if (object.isResource() && object.toString().contains("http://"))
      {
      if (predicate.getNameSpace().startsWith("http://data.ifpri.org"))
        {
        report += "<li>\n";
        report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+newPath+predicate.getLocalName()+"\" title=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getLocalName()+"</a>";
        }
      
      else
        {
        report += "<li>\n";
        report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+newPath+predicate.getLocalName()+"\" title=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getLocalName()+"</a>";
        }
      
      if (object.toString().startsWith("http://aims.fao.org"))
        {
        report +="  &#160; <span rel=\""+predicate.getURI()+"\" resource=\""+object.toString()+"\">  <a href=\"http://www.fao.org/countryprofiles/geoinfo/geopolitical/resource/"+objectResource.getLocalName()+"\" title = \""+objectResource.getLocalName()+"\">"+objectResource.getLocalName()+"</a> </span>\n";
        report += "</li>\n";
        }
      else
        {
        report +="  &#160; <span rel=\""+predicate.getURI()+"\" resource=\""+object.toString()+"\">  <a href=\""+newPath+objectResource.getLocalName()+"\" title = \""+object.toString()+"\">"+objectResource.getLocalName()+"</a> </span>\n";
        report += "</li>\n";
        }       
        
      }
    else if (object.isLiteral())
      {
      Literal objectLiteral = (Literal)object;
      String objectValue = objectLiteral.toString();
      if (objectValue.contains("&"))
    	  objectValue = objectValue.replace("&", "&#38;");
      if (objectValue.indexOf("^^")>0)
        objectValue= objectValue.substring(0, objectValue.indexOf("^^"));
      if (objectLiteral.getLanguage() != null && objectLiteral.getLanguage().length() >0 )
        {
        if (predicate.getNameSpace().startsWith("http://data.ifpri.org"))
          {
  	      report += "<li>\n";
    	  report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+newPath+predicate.getLocalName()+"\" title=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getLocalName()+"</a>&#160;<span class='literal' ><span property=\""+nameSpace+":"+predicate.getLocalName()+"\" xml:lang=\""+objectLiteral.getLanguage()+"\">"+objectLiteral.getString()+" ("+objectLiteral.getLanguage() + ")</span></span>\n";
          report += "</li>\n";
          }
        else
          {
    	  report += "<li>\n";
    	  report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+predicate.getNameSpace()+predicate.getLocalName()+"\" title=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getLocalName()+"</a>&#160;<span class='literal' ><span property=\""+nameSpace+":"+predicate.getLocalName()+"\" xml:lang=\""+objectLiteral.getLanguage()+"\">"+objectLiteral.getString()+" ("+objectLiteral.getLanguage() + ")</span></span>\n";
          report += "</li>\n";
          
          }
        }
      else
        {
        if (predicate.getNameSpace().startsWith("http://data.ifpri.org"))
          {
     	  report += "<li>\n";
    	  report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+newPath+predicate.getLocalName()+"\" title=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getLocalName()+"</a>&#160;<span class='literal' ><span property=\""+nameSpace+":"+predicate.getLocalName()+"\">"+objectValue+"</span></span>\n";
          report += "</li>\n";
          }
        else
          {
          report += "<li>\n";
    	  report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+predicate.getNameSpace()+predicate.getLocalName()+"\" title=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getLocalName()+"</a>&#160;<span class='literal' ><span property=\""+nameSpace+":"+predicate.getLocalName()+"\">"+objectValue+"</span></span>\n";
          report += "</li>\n";
          
          
          }  
        }
      
      }
 

    }

  public void writeSameAsStatement(Model selectedModel, com.hp.hpl.jena.rdf.model.Statement statement)
    {
    if (statement == null)
      return;
    
    Property predicate = statement.getPredicate();
    RDFNode object = statement.getObject();
    Resource objectResource = selectedModel.getResource(object.toString());
    if (object.isResource() && object.toString().contains("http://"))
      {
      report += "<li>\n";
      report += "<span class='active-entity'>"+resourceName+"</span>&#160;<a href=\""+predicate.getNameSpace()+predicate.getLocalName()+"\" title=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getLocalName()+"</a>";

      report +="  &#160; <span rel=\""+predicate.getURI()+"\" resource=\""+object.toString()+"\">  <a href=\""+object.toString()+"\" title = \""+object.toString()+"\">"+objectResource.getURI()+"</a> </span>\n";
      report += "</li>\n";
      }

    }


  
  // show all iNstances as a list 
  
  public void writeInstanceStatement( com.hp.hpl.jena.rdf.model.Statement statement,Resource selectedClass)
    {
    if (statement == null)
      return;
    
    report += "<li>\n";
    Resource subject = statement.getSubject();
    Property predicate = statement.getPredicate();
    RDFNode object = statement.getObject();
    System.out.println("statement null?"+ statement.toString());
    if (subject == null || predicate.getLocalName() == null || object.toString() == null || selectedClass.getURI() == null)
      return;
    
    if (predicate.getLocalName().equalsIgnoreCase(typeProperty.getLocalName())  && object.toString().equalsIgnoreCase(selectedClass.getURI()))
      {
        // server path adjustment
      if (subject.getURI().startsWith(this.currentServerPath))
        {
        report += "<span class='active-entity'> <span rel='owl:Individual' resource='"+subject.getURI()+"'>"+"<a href=\""+subject.getURI()+"\" title =\""+subject.getURI()+"\">"+subject.getLocalName()+"</a></span></span>";
        }
      else if (  subject.getURI().startsWith("http://data.ifpri.org/"))
        {
        report += "<span class='active-entity'> <span rel='owl:Individual' resource='"+this.currentServerPath+"/lod/asti/resource/"+subject.getLocalName()+"'>"+"<a href=\""+this.currentServerPath+"/lod/asti/resource/"+subject.getLocalName()+"\" title =\""+subject.getURI()+"\">"+subject.getLocalName()+"</a></span></span>";
        }  
      else
        report += "<span class='active-entity'> <span rel='owl:Individual' resource='"+subject.getURI()+"'>"+"<a href=\""+subject.getURI()+"\" title =\""+subject.getURI()+"\">"+subject.getLocalName()+"</a></span></span>";
        
      }
    report += "</li>\n";

    }
    // show all instances separated by "|"
  public void writeInstanceStatementWithoutList( com.hp.hpl.jena.rdf.model.Statement statement,Resource selectedClass)
    {
    if (statement == null)
      return;
    
    Resource subject = statement.getSubject();
    Property predicate = statement.getPredicate();
    RDFNode object = statement.getObject();
    if (subject == null || predicate.getLocalName() == null || object.toString() == null || selectedClass.getURI() == null)
      return;
    
    if (predicate.getLocalName().equalsIgnoreCase(typeProperty.getLocalName())  && object.toString().equalsIgnoreCase(selectedClass.getURI()))
      {
      if (subject.getURI().startsWith(this.currentServerPath))
        report += "<span class='active-entity'> <span rel='owl:Individual' resource='"+subject.getURI()+"'>"+"<a href=\""+subject.getURI()+"\" title =\""+subject.getURI()+"\">"+subject.getLocalName()+"</a></span></span> \n";
      else if (subject.getURI().startsWith("http://data.ifpri.org"))
        report += "<span class='active-entity'> <span rel='owl:Individual' resource='"+this.currentServerPath+"/lod/asti/resource/"+subject.getLocalName()+"'>"+"<a href=\""+this.currentServerPath+"/lod/asti/resource/"+subject.getLocalName()+"\" title =\""+subject.getURI()+"\">"+subject.getLocalName()+"</a></span></span> \n";
      else 
        report += "<span class='active-entity'> <span rel='owl:Individual' resource='"+subject.getURI()+"'>"+"<a href=\""+subject.getURI()+"\" title =\""+subject.getURI()+"\">"+subject.getLocalName()+"</a></span></span> \n";
        
      }

    }

  public void writeClasses( Resource selectedClass,int level, boolean selectFlag)
    {
    if (selectedClass == null)
      return;
      
    if (selectedClass.getURI().startsWith(this.currentServerPath))
      { 
      if (selectFlag)
        report += "<span class='active-entity'> <font color =\"red\"><a href=\""+selectedClass.getURI()+"\" title =\""+selectedClass.getURI()+"\">"+selectedClass.getLocalName()+"</a></font></span> \n";
      else
        report += "<span class='active-entity'> <a href=\""+selectedClass.getURI()+"\" title =\""+selectedClass.getURI()+"\">"+selectedClass.getLocalName()+"</a></span> \n";
      }
    else if (selectedClass.getURI().startsWith("http://data.ifpri.org"))
      {
      String newPath = this.currentServerPath+"/lod/asti/resource/";
      if (selectFlag)
        report += "<span class='active-entity'> <font color =\"red\"><a href=\""+newPath+selectedClass.getLocalName()+"\" title =\""+selectedClass.getLocalName()+"\">"+selectedClass.getLocalName()+"</a></font></span> \n";
      else
        report += "<span class='active-entity'> <a href=\""+newPath+selectedClass.getLocalName()+"\" title =\""+newPath+selectedClass.getLocalName()+"\">"+selectedClass.getLocalName()+"</a></span> \n";
      }
    else 
      {  
      if (selectFlag)
        report += "<span class='active-entity'> <font color =\"red\"><a href=\""+selectedClass.getURI()+"\" title =\""+selectedClass.getURI()+"\">"+selectedClass.getURI()+"</a></font></span> \n";
      else
        report += "<span class='active-entity'> <a href=\""+selectedClass.getURI()+"\" title =\""+selectedClass.getURI()+"\">"+selectedClass.getURI()+"</a></span> \n";
      
      }  
    }

/*
  public void writeClasses( Resource selectedClass)
    {
    if (selectedClass == null)
      return;
    
    report += "<span class='active-entity'> <span rel=\"owl:Class\" resource=\""+selectedClass.getURI()+"\">"+"<a href=\""+selectedClass.getURI()+"\" title =\""+selectedClass.getURI()+"\">"+selectedClass.getLocalName()+"</a></span></span>";
 
    }
*/
/*
  public void writeHTMLStatement(Model selectedModel, com.hp.hpl.jena.rdf.model.Statement statement)
    {
    if (statement == null)
      return;
    report += "<tr>\n";
    Property predicate = statement.getPredicate();
    report += "<td><a href=\""+predicate.getNameSpace()+predicate.getLocalName()+"\">"+predicate.getNameSpace()+predicate.getLocalName()+"</a></td>\n";
    RDFNode object = statement.getObject();
    
    if (object.isResource())
      {
      report +="    <td> <a href=\""+object.toString()+"\">"+object.toString()+"</a></td> </tr> \n";
      }
    else if (object.isLiteral())
      {
      Literal objectLiteral = (Literal)object;
      report +="    <td><strong>"+objectLiteral.getValue()+"</strong>("+objectLiteral.getDatatypeURI()+")</td> </tr> \n";
      
      }
    if (predicate != null && object != null)
      {
      }
    }
*/
  private void writeHeader()
    {
    report += "<head> \n";
    report += "<title>"+ this.resourceName + "</title> \n";


    writeCSS();
    report += "</head> \n";
    }
  
  private void writeCSS()
    {
    if (report == null)
      return;
    report += "<style  type=\"text/css\"> \n";  
    report +=" body {font-family:  Verdana, Arial, sans-serif; font-size: 0.8em; color: #000000; background-color: #ffffff }\n";
    report +=" h1 {margin-top: 10px; margin-bottom: 5px; font-size: 1.4em; }                                                \n";
    report +=" h2 {margin-top: 10px; margin-bottom: 5px; font-size: 1.2em; }                                                \n";
    report +=" h3 {margin-top: 10px; margin-bottom: 5px; font-size: 1.1em; }                                                \n";
    report +=" h4 {margin-top: 10px; margin-bottom: 5px; font-size: 0.9em; }                                                \n";
    report +=" div.codebox {overflow: hidden; background: #FFFFFF; width: 95%; padding: 5px;                                                                               \n";
    report +="     border-width: 1px; border-style: solid; border-color: #CCCCCC;                                           \n";
    report +=" }                                                                                                            \n";
    report +=" .literal {	color: Green; }                                                                                  \n";
    report +=" .annotation-uri {color: #7C7C7C;}                                                                            \n";
    report +=" .ontology-uri {color: #990099;}                                                                              \n";
    report +=" .active-ontology-uri {color: #CCCCCC;}                                                                       \n";
    report +=" .active-entity {color: #3D598B;	font-weight: bold; }                                                         \n";
    report +="                                                                                                              \n";
    report +=" .keyword {	color: #B2226E;}                                                                                 \n";
    report +=" .comment {color: #666666;}                                                                                   \n";
    report +=" .some {	color: #708090;}                                                                                     \n";
    report +=" .only {color: #660066;}                                                                                      \n";
    report +=" .value {color: #660066;}                                                                                     \n";
    report +=" .min {color: #660066;}                                                                                       \n";
    report +=" .max {color: #660066;}                                                                                       \n";
    report +=" .exactly {color: #660066;}                                                                                   \n";
    report +="                                                                                                              \n";
    report +=" p.footer {font-size: 10px; text-align: right;}                                                               \n";
    report +="                                                                                                              \n";
    report +=" p.name {FONT-WEIGHT: bold; FONT-SIZE: 1.2em; margin-top: 1px; margin-bottom: 4px;}                        \n";
    report +=" p.newsdet {FONT-SIZE: 1.1em;margin-top: 4px; margin-bottom: 4px;}                                            \n";
    report +=" p.description {FONT-SIZE: 0.9em;margin-top: 4px; margin-bottom: 4px;}                                            \n";
    report +="                                                                                                              \n";
    report +=" a {color: #336699; text-decoration: underline; }                                                             \n";
    report +=" a:hover {text-decoration: none; 	color: #FF6600; }                                                          \n";
    report +="                                                                                                              \n";
    report +="                                                                                                              \n";
    report +=" ul, li {margin-top: 8px; margin-bottom: 4px; margin-left: 8px; padding-left: 8px; }                          \n";
    report +=" li {margin-left: 2px; padding-left: 2%;}                                                                     \n";
    report +=" \n";
     
    report += "</style>\n";

    }


  private void writeIFPRILogo(String selectedLanguage)
    {
    if (report == null)
      return;
    
    if (selectedLanguage.equalsIgnoreCase("EN"))
      {
      report +="<TABLE cellSpacing=\"1\" cellPadding=\"2\" border=\"0\"> \n";
      report +="<TR> \n";
      report +="    <td><a href=\"http://data.ifpri.org\" target=\"_blank\"><img alt=\"IFPRI\" src=\"http://data.ifpri.org/IFPRILogo.jpg\" border=\"0\"></img></a></td>\n";
      report +="    <td><p class=\"name\"><h1>Linked Open Data - Agricultural Science and Technology Indicators (ASTI)</h1></p>\n";
//      report +="         <p align =\"right\" class=\"description\"> <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">French</a>, <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">Spanish</p>";
      report +="        <p class=\"newsdet\"><a href=\"http://data.ifpri.org/\" target=\"_blank\"><strong>IFPRI Data</strong></a>&gt;";
      report +="                             <a href=\"http://www.asti.cgiar.org/\"><strong>Agricultural Science and Technology Indicators</strong></a> <br>&nbsp; </br>";

      report +="         <p class=\"description\">The Agricultural Science and Technology Indicators (ASTI) initiative, led by the International Food Policy Research Institute (IFPRI), is a comprehensive and trusted source of information on agricultural research and development (R&D) systems across the developing world.</p>";
      report +="        </p> \n";
      report +="   </td> \n";
      report +="</TR>\n";
      report +="</TABLE>\n";
	  }
    else if (selectedLanguage.equalsIgnoreCase("ES"))
      {
      report +="<TABLE cellSpacing=\"1\" cellPadding=\"2\" border=\"0\"> \n";
      report +="<TR> \n";
      report +="    <td><a href=\"http://data.ifpri.org\" target=\"_blank\"><img alt=\"IFPRI\" src=\"http://data.ifpri.org/IFPRILogo.jpg\" border=\"0\"></img></a></td>\n";
      report +="    <td><p class=\"name\"><h1>Linked Open Data - Agricultural Science and Technology Indicators (ASTI)</h1></p>\n";
//      report +="         <p align =\"right\" class=\"description\"> <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">French</a>, <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">Spanish</p>";
      report +="        <p class=\"newsdet\"><a href=\"http://data.ifpri.org/\" target=\"_blank\"><strong>IFPRI Data</strong></a>&gt;";
      report +="                             <a href=\"http://www.asti.cgiar.org/\"><strong>Agricultural Science and Technology Indicators</strong></a> <br>&nbsp; </br>";

      report +="         <p class=\"description\">The Agricultural Science and Technology Indicators (ASTI) initiative, led by the International Food Policy Research Institute (IFPRI), is a comprehensive and trusted source of information on agricultural research and development (R&D) systems across the developing world.</p>";
      report +="        </p> \n";
      report +="   </td> \n";
      report +="</TR>\n";
      report +="</TABLE>\n";
	  }

    else if (selectedLanguage.equalsIgnoreCase("FR"))
      {
      report +="<TABLE cellSpacing=\"1\" cellPadding=\"2\" border=\"0\"> \n";
      report +="<TR> \n";
      report +="    <td><a href=\"http://data.ifpri.org\" target=\"_blank\"><img alt=\"IFPRI\" src=\"http://data.ifpri.org/IFPRILogo.jpg\" border=\"0\"></img></a></td>\n";
      report +="    <td><p class=\"name\"><h1>Linked Open Data - Agricultural Science and Technology Indicators (ASTI)</h1></p>\n";
//      report +="         <p align =\"right\" class=\"description\"> <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">French</a>, <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">Spanish</p>";
      report +="        <p class=\"newsdet\"><a href=\"http://data.ifpri.org/\" target=\"_blank\"><strong>IFPRI Data</strong></a>&gt;";
      report +="                             <a href=\"http://www.asti.cgiar.org/\"><strong>Agricultural Science and Technology Indicators</strong></a> <br>&nbsp; </br>";

      report +="         <p class=\"description\">The Agricultural Science and Technology Indicators (ASTI) initiative, led by the International Food Policy Research Institute (IFPRI), is a comprehensive and trusted source of information on agricultural research and development (R&D) systems across the developing world.</p>";
      report +="        </p> \n";
      report +="   </td> \n";
      report +="</TR>\n";
      report +="</TABLE>\n";
	  }

    else 
      {
       report +="<TABLE cellSpacing=\"1\" cellPadding=\"2\" border=\"0\"> \n";
      report +="<TR> \n";
      report +="    <td><a href=\"http://data.ifpri.org\" target=\"_blank\"><img alt=\"IFPRI\" src=\"http://data.ifpri.org/IFPRILogo.jpg\" border=\"0\"></img></a></td>\n";
      report +="    <td><p class=\"name\"><h1>Linked Open Data - Agricultural Science and Technology Indicators (ASTI)</h1></p>\n";
//      report +="         <p align =\"right\" class=\"description\"> <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">French</a>, <a href=\"http://data.ifpri.org/lod/arabspatial/fr\">Spanish</p>";
      report +="        <p class=\"newsdet\"><a href=\"http://data.ifpri.org/\" target=\"_blank\"><strong>IFPRI Data</strong></a>&gt;";
      report +="                             <a href=\"http://www.asti.cgiar.org/\"><strong>Agricultural Science and Technology Indicators</strong></a> <br>&nbsp; </br>";

      report +="         <p class=\"description\">The Agricultural Science and Technology Indicators (ASTI) initiative, led by the International Food Policy Research Institute (IFPRI), is a comprehensive and trusted source of information on agricultural research and development (R&D) systems across the developing world.</p>";
      report +="        </p> \n";
      report +="   </td> \n";
      report +="</TR>\n";
      report +="</TABLE>\n";
	  }
    
    }
    
    
  private void writeID (String resourceName, String selectedLangauge)
    {
    if (resourceName.length() >0)
      {
      if (selectedLangauge.equalsIgnoreCase("EN"))
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - SPEED  - "+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Raw data <a href=\""+this.currentServerPath+"/lod/asti/data/"+resourceName+"\" title=\""+rdf+resourceName+"\">(RDF) </a> </p> \n";
        }
      else if  (selectedLangauge.equalsIgnoreCase("ES"))
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - SPEED   - "+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Los datos brutos <a href=\""+this.currentServerPath+"/lod/asti/data/"+resourceName+"\" title=\""+rdf+resourceName+"\">(RDF) </a> </p> \n";
        } 
      else if  (selectedLangauge.equalsIgnoreCase("FR"))
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - SPEED  - "+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Les donn�es brutes  <a href=\""+this.currentServerPath+"/lod/asti/data/"+resourceName+"\" title=\""+rdf+resourceName+"\">(RDF) </a> </p> \n";
        } 
      else 
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - SPEED - "+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Raw data <a href=\""+this.currentServerPath+"/lod/asti/data/"+resourceName+"\" title=\""+rdf+resourceName+"\">(RDF) </a> </p> \n";
        } 
   
      }
    else // ?
      {

      if (selectedLangauge.equalsIgnoreCase("EN"))
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - SPEED "+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Raw data <a href=\"http://data.ifpri.org/lod/asti.owl"+"\" title=\""+rdf+resourceName+"\">(RDF) </a> </p> \n";
        }
      else if  (selectedLangauge.equalsIgnoreCase("ES"))
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - SPEED"+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Los datos brutos  <a href=\"http://data.ifpri.org/lod/asti.owl"+"\"  title=\""+rdf+resourceName+"\">(RDF) </a> </p> \n";
        } 
      else if  (selectedLangauge.equalsIgnoreCase("FR"))
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - SPEED "+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Les donn�es brutes  href=\"http://data.ifpri.org/lod/asti.owl"+"\"  title=\""+rdf+resourceName+"\">(RDF) </a> </p> \n";
        } 
      else 
        {
        report += "     <h2>Resource: IFPRI Linked Open Data - Arab Spatial  "+resourceName+"</h2><h3> URI: "+resourceBase+resourceName+"</h3> ";
        report += "     &#160; &#160; Raw data <a href=\"http://data.ifpri.org/lod/asti.owl"+"\" >(RDF) </a> </p> \n";
        } 
      }
    } 
  
  private void writeType(String type)
    {
    if (type != null)
      report += "   <li>  <a href=\""+type+"\" title=\""+type+"\">"+type+"</a></li>\n";
    }
    
  private void writeOpenBoxWithTitle(String titleName)
    {
    if (titleName == null)
      return;
    
    report += "  <p> \n";
    report += "  </p> \n";
    
    report += "<h3>"+titleName+"</h3>\n";
    report += "  <ul> \n";
    
    }


  private void writeCloseBoxWithTitle()
    {
    report += "    </ul> \n";
    report += "  <p> \n";
    report += "  </p> \n";
    
    }
  
  
  private Vector sort(Hashtable table)
    {
    if (table == null )
      return null;
    Vector  sortedVector = new Vector();
    Vector v = new Vector(table.keySet());
    Collections.sort(v);
    Iterator it  = v.iterator();
    while (it.hasNext()) 
      {
      String element =  (String)it.next();
      sortedVector.addElement(table.get(element));
      }
    return sortedVector;     
    }
  
  private void writeMenu(String selectedLanguge)
    {
    
    if (selectedLanguge.equalsIgnoreCase("EN"))
      {
      report += " <h2><a href=\""+this.currentServerPath+"/lod/asti/resource/"+"\">Overview</a> </h2>";
	  report += " <h2><a href=\"http://www.ifpri.org/book-39/ourwork/programs/priorities-public-investment/speed-database\">About</a> </h2>";
   
      report += " <h2>Categories </h2>";
      report += "   <div>  \n";
      
       // Classes  
      findClassTree("http://data.ifpri.org/lod/asti/resource/Indicator",0);
 
   
      report += "   </div>  \n";

      report +="<h2> Formats  </h2>";
      report += " <div> \n";
      report += " <ul> \n";
      report +="     <li> <a href=\"http://thedata.harvard.edu/dvn/dv/IFPRI/faces/study/StudyPage.xhtml?globalId=hdl:1902.1/20514\">Dataverse</a> </li>";
      report +="     <li> <a href=\"http://data.ifpri.org/lod/asti.owl\">OWL</a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
    
      report +="<h2> Useful links </h2>";
      report += " <div> \n";
      report += " <ul> \n";
      report +="     <li> <a href=\"http://public.tableausoftware.com/views/ASTI/ASTI?:embed=y&:display_count=no\">Data Visualization</a> </li>";
      report +="     <li> <a href=\"http://ebrary.ifpri.org/cdm/search/collection/p15738coll2/searchterm/asti/field/ifpri/mode/exact/conn/and/order/date/ad/desc\">ASTI publication</a> </li>";
      report +="     <li> <a href=\"http://unstats.un.org/unsd/methods/m49/m49alpha.htm\">Country ISO3 CODE</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/copyright\">IFPRI Copyright </a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/datasets/disclaimer \">IFPRI Data Disclaimer </a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
      }
      
    else if (selectedLanguge.equalsIgnoreCase("ES"))
      {
 
      report += " <h2><a href=\""+this.currentServerPath+"/lod/asti/resource/"+"\">visión de conjunto</a> </h2>";
	  report += " <h2><a href=\"http://www.ifpri.org/book-39/ourwork/programs/priorities-public-investment/speed-database\">Sobre</a> </h2>";
   
      report += " <h2>Categoría</h2>";
      report += "   <div>  \n";
      
       // Classes  
      findClassTree("http://www.w3.org/2002/07/owl#Thing",0);
    
      report += "   </div>  \n";


      report +="<h2>Formatos</h2>";
      report += " <div> \n";
      report += " <ul> \n";
 //     report +="     <li> <a href=\"http://www.ifpri.org/sites/default/files/ghi2012datam.xlsx\">EXCEL</a> </li>";
 //     report +="     <li> <a href=\"http://data.ifpri.org/lod/arabspatial.owl\">OWL</a> </li>";
      report +="     <li> <a href=\"http://data.ifpri.org/lod/speed.rdf\">RDF</a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
    
      report +="<h2>Enlaces de interés</h2>";
      report += " <div> \n";
      report += " <ul> \n";
      report +="     <li> <a href=\"http://data.ifpri.org/sparql\">SPARQL endpoint</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/book-8018/node/8059\">�ndice Global del Hambre Informe </a> </li>";
      report +="     <li> <a href=\"http://public.tableausoftware.com/shared/6XX5TPNP4\">Mapa �ndice Global del Hambre</a> </li>";
      report +="     <li> <a href=\"http://unstats.un.org/unsd/methods/m49/m49alpha.htm\">Pa�s ISO3 C�DIGO</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/copyright\">IFPRI Derechos de autor</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/datasets/disclaimer \">IFPRI datos Descargo de responsabilidad</a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
      


      }
      
    else if (selectedLanguge.equalsIgnoreCase("FR"))
      {
 
      report += " <h2><a href=\""+this.currentServerPath+"/lod/asti/resource/"+"\">Vue d'ensemble</a> </h2>";
	  report += " <h2><a href=\"http://www.ifpri.org/book-8018/node/8058\">Sur</a> </h2>";
   
      report += " <h2>Cat�gories</h2>";
      report += "   <div>  \n";
      
       // Classes  
      findClassTree("http://www.w3.org/2002/07/owl#Thing",0);
    
      report += "   </div>  \n";

      report +="<h2>Formats</h2>";
      report += " <div> \n";
      report += " <ul> \n";
      report +="     <li> <a href=\"http://www.ifpri.org/sites/default/files/ghi2012datam.xlsx\">EXCEL</a> </li>";
      report +="     <li> <a href=\"http://data.ifpri.org/lod/arabspatial.owl\">OWL</a> </li>";
      report +="     <li> <a href=\"http://data.ifpri.org/lod/arabspatial.rdf\">RDF</a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
    
      report +="<h2>Liens utiles</h2>";
      report += " <div> \n";
      report += " <ul> \n";
      report +="     <li> <a href=\"http://data.ifpri.org/sparql\">SPARQL Crit�re pour Global Hunger Index</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/book-8018/node/8059\">Rapport mondial Indice de la faim </a> </li>";
      report +="     <li> <a href=\"http://public.tableausoftware.com/shared/6XX5TPNP4\">Carte du Monde indice de la faim </a> </li>";
      report +="     <li> <a href=\"http://unstats.un.org/unsd/methods/m49/m49alpha.htm\">Pays ISO3 CODE</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/copyright\">IFPRI Droits d'auteur</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/datasets/disclaimer \">IFPRI donn�es Responsabilit�</a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
      


      }
    else
      {
      report += " <h2><a href=\""+this.currentServerPath+"/lod/asti/resource/"+"\">Overview</a> </h2>";
	  report += " <h2><a href=\"http://www.ifpri.org/book-39/ourwork/programs/priorities-public-investment/speed-database\">About</a> </h2>";
   
      report += " <h2>Categories </h2>";
      report += "   <div>  \n";
      
       // Classes  
      findClassTree("http://data.ifpri.org/lod/asti/resource/Indicator",0);
 
   
      report += "   </div>  \n";

      report +="<h2> Formats  </h2>";
      report += " <div> \n";
      report += " <ul> \n";
      report +="     <li> <a href=\"http://thedata.harvard.edu/dvn/dv/IFPRI/faces/study/StudyPage.xhtml?globalId=hdl:1902.1/20514\">Dataverse</a> </li>";
      report +="     <li> <a href=\"http://data.ifpri.org/lod/asti.owl\">OWL</a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
    
      report +="<h2> Useful links </h2>";
      report += " <div> \n";
      report += " <ul> \n";
      report +="     <li> <a href=\"http://public.tableausoftware.com/views/ASTI/ASTI?:embed=y&:display_count=no\">Data Visualization</a> </li>";
      report +="     <li> <a href=\"http://ebrary.ifpri.org/cdm/search/collection/p15738coll2/searchterm/asti/field/ifpri/mode/exact/conn/and/order/date/ad/desc\">ASTI publication</a> </li>";
      report +="     <li> <a href=\"http://unstats.un.org/unsd/methods/m49/m49alpha.htm\">Country ISO3 CODE</a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/copyright\">IFPRI Copyright </a> </li>";
      report +="     <li> <a href=\"http://www.ifpri.org/datasets/disclaimer \">IFPRI Data Disclaimer </a> </li>";
      report += " </ul> \n";
      report += " </div> \n";
      }

   
    }
 
  private void writeBody(Model selectedModel,boolean writeAllResourceFlag,String language)
    {
    report += "<table  cellpadding=\"10\" valign=\"top\" align=\"top\" width=\"100%\"> \n";
    report += "<tr> \n";
    report += "<td valign=\"top\" width=\"300px\">  \n ";
    writeMenu(language);
    report += "</td>    \n";
    report += "<td valign=\"top\" width=\"80%\">  \n ";
    writeID(resourceName,language);
     
    writeContent(selectedModel);

    if (writeAllResourceFlag)
      writeAllResources (selectedModel);  

    report += "</td>    \n";
    report += "</tr> \n";
    report += "</table>    \n";
    }

  private void findClassTree(String className, int level)
    {
    if (className == null)
      return;

    List classes = connector.getSubClasses(className);
    
    Hashtable resultHashtable = new Hashtable();
    
    Vector resultVector = new Vector();

    if (classes != null && classes.size()>0)
      {
      Resource subject = null;
      report += "<ul> \n";  
      
      level ++;
      for(Iterator it = classes.iterator(); it.hasNext();)
        {
        subject = (Resource)it.next();
        report += "<!-- same level class started --> \n";  
        if (!allClassesVector.contains(subject))
          allClassesVector.add(subject);
        report += "<li> \n";  
        writeClasses(subject,level,false);
        report += "<!-- new subclass started --> \n";  
        
        findClassTree(subject.getURI(),level);
        report += "</li> \n";  
        }
      report += "</ul> \n";  

      } 
    else
      return; 
    }

  public void writeAllResources(Model selectedModel)
    {
     // Classes  
    if (allClassesVector.size() == 0)
      return;
    report += "<p> </p> \n ";
    report += " <h3>All resources </h3>";

    for (int i=0;i< allClassesVector.size();i++)
      { 
      Resource selectedClass = (Resource)allClassesVector.elementAt(i);
      
      if (selectedClass == null)
        continue;

        
      Hashtable resultHashtable = new Hashtable();
      Vector  resultVector = new Vector();
      List instances = connector.getInstances(selectedClass.getLocalName());
      if (instances.size()>0)
        {
        report += " <h4>"+ selectedClass.getLocalName()+"</h4>";
        report += " <div> \n";  
        report += "   <ul> \n";  
 
        for (Iterator it2 = instances.iterator(); it2.hasNext();)
          {
          com.hp.hpl.jena.rdf.model.Statement st = (com.hp.hpl.jena.rdf.model.Statement)it2.next(); 
          if (st == null)
            continue;
          Resource instance = st.getSubject();
          RDFNode object = st.getObject();
          if (instance != null )
            {
            resultHashtable.put(""+instance.toString(),st); 
            }
          }
        if (resultHashtable.size()>0)
          {
   	      resultVector = sort(resultHashtable);
          
          for (int j=0; j<resultVector.size();j++)
            {
            com.hp.hpl.jena.rdf.model.Statement statement = (com.hp.hpl.jena.rdf.model.Statement)resultVector.elementAt(j); 
            writeInstanceStatementWithoutList(statement, selectedClass);
            report += "   |    ";   
            }
          report += " </ul> \n";  
          
          report += " </div> \n";  

          }
        
        }
      }
	  
    }
  
  
  } // end of class