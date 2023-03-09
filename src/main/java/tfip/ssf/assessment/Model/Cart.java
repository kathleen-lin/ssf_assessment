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

        String name = i.getName();
        // System.out.println("item to add is " + name);
        Boolean isDuplicate = false;
        Integer index = null;
        for (int j = 0; j < items.size(); j++){
            // System.out.println("exisitng items " + items.get(j).getName());
            if (items.get(j).getName().equals(name)){
                System.out.println("found duplicate");
                isDuplicate = true;
                index = j;
                break;
            }
        }

        if (isDuplicate){
            // System.out.println("there is a duplicate");
            Item curr = items.get(index);
            int newQuantity = curr.getQuantity() + i.getQuantity();
            curr.setQuantity(newQuantity);
        } else {
            // System.out.println("this is new item");

            items.add(i);
        }

    }

     
}
