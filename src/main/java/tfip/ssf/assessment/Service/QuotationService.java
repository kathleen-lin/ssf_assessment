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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import tfip.ssf.assessment.Model.Item;
import tfip.ssf.assessment.Model.Quotation;
import tfip.ssf.assessment.Repository.shoppingCartRepo;

@Service
public class QuotationService {
    
    @Autowired
    shoppingCartRepo repo;

    public Quotation getQuotations(List<String> items) throws Exception {

        // >>>>>>>>>>>>>>>> Build array to get request
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        items.stream().forEach(itm -> arrayBuilder.add(Json.createValue(itm)));
        JsonArray cartItms = arrayBuilder.build();

        // System.out.println(cartItms.toString());

        // >>>>>>>>>>>>>>>> post to "https://quotation.chucklee.com"
        RequestEntity<String> req = RequestEntity.post("https://quotation.chuklee.com/quotation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(cartItms.toString(),String.class);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;
        String payload = "";
        int statusCode = 0;
        try {
            resp = template.exchange(req, String.class);
            payload = resp.getBody();
            statusCode = resp.getStatusCode().value();
        } catch (HttpClientErrorException ex) {
            payload = ex.getResponseBodyAsString();
            statusCode = ex.getStatusCode().value();
        } 
        System.out.println(">>> payload: " + payload);

        Quotation quotation = new Quotation();
        // >>>>>>>>>>>>>>>> Convert payload to Json object
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();

        
        JsonArray quotations = json.getJsonArray("quotations");

        Map<String, Float> quotes = new HashMap<>();

        for (int i = 0; i < quotations.size(); i++){
            JsonObject jsonQuote = quotations.getJsonObject(i);
            String item = jsonQuote.getString("item");
            Float unitPrice = jsonQuote.getJsonNumber("unitPrice").bigDecimalValue().floatValue();
            quotes.put(item, unitPrice);
        }


        String quoteId = json.get("quoteId").toString();

        quotation.setQuotations(quotes);
        quotation.setQuoteId(quoteId);
        System.out.println(quotation.getQuoteId());
        System.out.println(">>>"+ quotation.getQuotations().toString());

        return quotation;
    }

    public Float calculateCost(List<Item> items, Map<String,Float> quote){
        
        Float cost = 0f;

        for (int i = 0; i < items.size(); i++){
            // item = items.get(i).getName()
            float unitCost = quote.get(items.get(i).name);
            cost += unitCost * items.get(i).quantity;
            System.out.printf("item cost = %f", cost);
        }
        System.out.println(">>> total cost: " + cost);
        return cost;
    }


}
    

