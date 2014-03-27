package de.unigoettingen.sub.medas.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import de.unigoettingen.sub.medas.model.Doc;
import de.unigoettingen.sub.medas.model.Docs;

import javax.ws.rs.core.MediaType;


/**
 *
 * @author jdo
 */
public class MeDASClient {
    private Client client;
//    private final String BASE_URI = "http://10.0.2.206:8080/";
    private final String BASE_URI = "http://localhost:8080/";

    public MeDASClient(){
        client = Client.create();
    }
        
    public void getSingleDoc(String id){
        String path = "documents/"+id;
        WebResource webResource = client.resource(BASE_URI);
        webResource = webResource.path(path);
        Doc response = (Doc) webResource.accept(MediaType.APPLICATION_XML).get(Doc.class);
        System.out.println("doc " + response.getRelatedMetsDocid());
        System.out.println("title " + response.getTitle());
        System.out.println("related "+response.getRelatedItem().size() );
        System.out.println("type " + response.getRelatedItem().iterator().next().getRecordIdentifier().iterator().next().getSource());
        System.out.println("ddc "  + response.getClassification().iterator().next().getValue());
    }
    
    public Docs getAllDocuments(){
        String path = "documents/";
        WebResource webResource = client.resource(BASE_URI);
        webResource = webResource.path(path);        
//        webResource = webResource.queryParam("skip", "0").queryParam("limit", "5000");
        Docs response = (Docs) webResource.accept(MediaType.APPLICATION_XML).get(Docs.class);
        
        return response;
        
    }
    public static void main(String[] args){
        MeDASClient app = new MeDASClient();
        Docs all = app.getAllDocuments();
        System.out.println("all " + all.getDocs().size());
    }
}
