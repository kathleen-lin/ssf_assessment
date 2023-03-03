package tfip.ssf.assessment.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import tfip.ssf.assessment.Model.Cart;
import tfip.ssf.assessment.Model.DeliveryInfo;
import tfip.ssf.assessment.Model.Item;
import tfip.ssf.assessment.Model.Quotation;
import tfip.ssf.assessment.Service.QuotationService;

@Controller
@RequestMapping
public class PurchaseOrderController {

    @Autowired
    QuotationService qSvc;

    @GetMapping("/")
    public String showShoppingForm(Model model, HttpSession session){
        
        session.setAttribute("cart", new Cart());
        Cart c = (Cart) session.getAttribute("cart");
        List<Item> items = c.getItems();
        model.addAttribute("item", new Item());
        model.addAttribute("shoppingcart", items);

        return "view1";

    }

    @PostMapping("/add")
    public String addItem(@Valid Item itm, BindingResult result, Model model, HttpSession session){

        if(result.hasErrors()){
            return "view1";
        }
        /*
        if (!(itm.name == "apple" || itm.name == "orange" || itm.name == "bread" || itm.name == "cheese" || itm.name == "chicken" || itm.name == "mineral_water" || itm.name == "instant_noodles")){
            ObjectError err = new ObjectError("globalError", "We do not stock %s".formatted(itm.getName()));
            result.addError(err);
            return "view1";
        }
         */


        // update cart
        Cart cart = (Cart) session.getAttribute("cart");
        cart.updateCart(itm);
        System.out.println(cart);

        model.addAttribute("shoppingcart", cart.getItems());

        return "view1";
    }


    @GetMapping("/shippingaddress")
    public String showShippingForm(Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.getItems().isEmpty()){
            return "redirect:/";
        }

        model.addAttribute("info", new DeliveryInfo());

        return "view2";

    }

    @PostMapping("/orderReceived")
    public String postShippingForm(@Valid DeliveryInfo info, BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            return "view2";
        }

        Cart cart = (Cart) session.getAttribute("cart");
        System.out.println(cart.getItems().toString());

        Quotation quote = new Quotation();

        Map<String,Float> unitCost = new HashMap<>();
        unitCost.put("apple", (float) 0.5);
        unitCost.put("orange", (float) 0.6);
        unitCost.put("bread", (float) 1);
        unitCost.put("chicken", (float) 1.5);
        unitCost.put("mineral_water", (float) 0.3);
        unitCost.put("instant_noodle", (float) 1.2);

        String randomId = UUID.randomUUID().toString().substring(0,8);

        quote.setQuoteId(randomId);
        quote.setQuotations(unitCost);

        Float totalCost = qSvc.calculateCost(cart.getItems(), quote);

        model.addAttribute("invoiceId", randomId);
        model.addAttribute("name", info.getName());
        model.addAttribute("address", info.getAddress());
        model.addAttribute("total", totalCost);

        return "view3";
    }


   
    
}
