package http;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author JuanDanielCR
 */
public class HttpRequest {
    private String payload;
    private Map<String, String> headers;

    public HttpRequest(String linea) {
        //this.metodo = metodo;
        this.headers = new HashMap<String, String>();
        String [] headerParams = linea.split(Pattern.quote("\n"));
        for(int i = 0; i<headerParams.length;i++){
            try{
                if(i==0){
                    String headerValues[] = headerParams[i].split(Pattern.quote(" "));
                    if(headerValues[0].length() > 0){
                        System.out.println("header REC: "+headerValues[0]+" value: "+headerValues[1]);
                        headers.put(headerValues[0], headerValues[1]);
                    }
                }else{
                    if(headerParams[i].length() > 0){
                        String headerValues[] = headerParams[i].split(Pattern.quote(":"));
                        headers.put(headerValues[0], headerValues[1]);
                    }
                }
            }catch(ArrayIndexOutOfBoundsException e){
                
            }
        }
    }
    
    public String getValue(String header){
        if(headers.containsKey(header)){
            return headers.get(header);
        }else{
            return "No se encontro este parametro";
        }
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    
}
