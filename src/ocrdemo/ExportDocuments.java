package ocrdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.jaxb.CmisPropertiesType;
import org.apache.chemistry.opencmis.commons.impl.jaxb.CmisPropertyString;
import org.apache.chemistry.opencmis.commons.impl.jaxb.ObjectFactory;

public class ExportDocuments {
	
	public Session getCmisSession() {
		  // default factory implementation
		  SessionFactory factory = SessionFactoryImpl.newInstance();
		  Map<String, String> parameter = new HashMap<String, String>();
		  // connection settings
		  parameter.put(SessionParameter.ATOMPUB_URL, "http://172.25.100.120:9080/fncmis/resources/Service");
		  parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		  parameter.put(SessionParameter.AUTH_HTTP_BASIC, "true");
		  parameter.put(SessionParameter.USER, "p8admin");
		  parameter.put(SessionParameter.PASSWORD, "test@1234");
		  parameter.put(SessionParameter.REPOSITORY_ID, "icmcmtos");
		  List<Repository> repositories = factory.getRepositories(parameter);
		  
		  return repositories.get(0).createSession();
		}
	
	public Folder getFolder(Session session){
		
		Folder exportFolder = null;
		try {
			exportFolder = (Folder) session.getObjectByPath("/BOC Account Docs");
		} catch (CmisObjectNotFoundException onfe) {
			System.out.println("Exception in file retrieve : "+onfe);
		}
		return exportFolder;	
	}
	
	public void exportDoc(Session session, Folder exportFolder){
		
		try {
		    //create a document
		    Map<String, Object> docProps = new HashMap<String, Object>();
		    docProps.put(PropertyIds.OBJECT_TYPE_ID, "BOCDocuments");
		    docProps.put(PropertyIds.NAME, "CMIS Doc");	
		    docProps.put("MEDI_PO_Number", "1234");	
		            
		    File f = new File("C:\\Tesseract-OCR\\invoice_1.jpg");
		    InputStream isFile = new FileInputStream(f);
		            
		    ContentStream contentStream = session.getObjectFactory().createContentStream(f.getName(), f.length(), "application/jpg", isFile);
		    System.out.println("ExportFolder : "+exportFolder.getId());
		    	    
		    Document d = exportFolder.createDocument(docProps, contentStream, VersioningState.MAJOR);
		            
		    System.out.println(d.getId());
		    isFile.close();

		    
		} catch (Exception ex) {
		    System.out.println("Exception :"+ex);
		    ex.printStackTrace();
		}
		
	}	

	public static void main(String[] args) {
		ExportDocuments x= new ExportDocuments();
		x.getCmisSession();
		x.exportDoc(x.getCmisSession(), x.getFolder(x.getCmisSession()));
		

	}

}
