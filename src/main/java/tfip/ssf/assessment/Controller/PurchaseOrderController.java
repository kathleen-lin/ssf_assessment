package tfip.ssf.assessment.Controller;

import java.util.List;
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
import tfip.ssf.assessment.Model.Item;
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
        return "view2";

    }


   
    
}
