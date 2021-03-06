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
            //System.out.println("headers: "+headerParams[i]);
            try{
                if(i==0){
                    String headerValues[] = headerParams[i].split(Pattern.quote(" "));
                    if(headerValues[0].length() > 0){
                        headers.put(headerValues[0], headerValues[1]);
                    }
                }else{
                    if(headerParams[i].contains(":")){
                        String headerValues[] = headerParams[i].split(Pattern.quote(":"));
                        headers.put(headerValues[0], headerValues[1]);
                        //System.out.println("header REC: "+headerValues[0]+" value: "+headerValues[1]);
                    }else{
                        headers.put("params",headerParams[i]);
                    }
                }
            }catch(ArrayIndexOutOfBoundsException e){
                
            }
        }
    }
    
    public String getValue(String header){
        //System.out.println("Header: "+header);
        if(headers.containsKey(header)){
            //System.out.println("Value: "+headers.get(header));
            return headers.get(header);
        }else{
            return "-1";
        }
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    
}
