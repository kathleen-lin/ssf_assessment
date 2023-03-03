package tfip.ssf.assessment.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Item {
    
    public String name;

    @Min(value = 1, message="You must add at least 1 item")
    @NotNull(message = "You must add at least 1 item")
    public Integer quantity;

    
    public Item() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    



}
