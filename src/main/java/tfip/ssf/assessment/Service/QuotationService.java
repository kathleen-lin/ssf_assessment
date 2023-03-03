package tfip.ssf.assessment.Service;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import tfip.ssf.assessment.Model.Quotation;
import tfip.ssf.assessment.Repository.shoppingCartRepo;

@Service
public class QuotationService {
    
    @Autowired
    shoppingCartRepo repo;

    public Quotation getQuotations(List<String> items) throws Exception {

        JsonArray cartItms = Json.createArrayBuilder().build();
        items.stream().forEach(itm -> cartItms.add(Json.createValue(itm)));

        System.out.println(cartItms.toString());

        // post to "https://quotation.chucklee.com"
        RequestEntity<String> req = RequestEntity.post("https://quotation.chucklee.com").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(cartItms.toString(),String.class);

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);

        String payload = resp.getBody();
        // convert into a quotation with quoteId and quotations

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();

        Quotation quotation = new Quotation();
        String quoteId = json.get("quoteId").toString();
        quotation.setQuoteId(quoteId);

        JsonObject quotations = json.get("quotations").asJsonObject();

        Map<String, Float> quotes = new HashMap<>();

        quotations.keySet().forEach(key -> {Float value = (float) quotations.getInt(key);
                                            quotes.put(key, value);});
        
        quotation.setQuotations(quotes);
    }

        




    }
    
}
