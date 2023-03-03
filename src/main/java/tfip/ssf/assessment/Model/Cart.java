package tfip.ssf.assessment.Model;

import java.util.LinkedList;
import java.util.List;

public class Cart {
    
    private List<Item> items = new LinkedList<Item>();

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void updateCart(Item i){

        items.add(i);

    }

     
}
