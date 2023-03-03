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

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        items.stream().forEach(itm -> arrayBuilder.add(Json.createValue(itm)));
        JsonArray cartItms = arrayBuilder.build();

        System.out.println(cartItms.toString());

        // post to "https://quotation.chucklee.com"
        RequestEntity<String> req = RequestEntity.post("https://quotation.chuklee.com").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(cartItms.toString(),String.class);

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

        quotations.keySet().forEach(key -> {Float value = Float.valueOf(quotations.getString(key));
                                            quotes.put(key, value);});
        
        quotation.setQuotations(quotes);

        return quotation;
    }

    public Float calculateCost(List<Item> items, Quotation quote){
        Float cost = 0f;

        for (int i = 0; i < items.size(); i++){
            if (items.get(i).getName() == "apple"){
                cost += quote.getQuotations().get("apple") * items.get(i).quantity;
            }if (items.get(i).getName() == "orange"){
                cost += quote.getQuotations().get("orange") * items.get(i).quantity;
            }if (items.get(i).getName() == "bread"){
                cost += quote.getQuotations().get("bread") * items.get(i).quantity;
            }if (items.get(i).getName() == "cheese"){
                cost += quote.getQuotations().get("cheese") * items.get(i).quantity;
            }if (items.get(i).getName() == "chicken"){
                cost += quote.getQuotations().get("chicken") * items.get(i).quantity;
            }if (items.get(i).getName() == "mineral_water"){
                cost += quote.getQuotations().get("mineral_water") * items.get(i).quantity;
            }if (items.get(i).getName() == "instant_noodle"){
                cost += quote.getQuotations().get("instant_noodle") * items.get(i).quantity;
            }

        }

        return cost;
    }


}
    

