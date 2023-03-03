package tfip.ssf.assessment.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DeliveryInfo {
    
    @NotNull (message = "Please provide a name")
    @Size(min=2, message="Name must be at least 2 character long")
    private String name;

    @NotNull (message="Please provide an address")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
}
