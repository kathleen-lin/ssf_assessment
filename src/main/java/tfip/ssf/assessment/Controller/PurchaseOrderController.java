package tfip.ssf.assessment.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
        
        if (!(itm.name.equals( "apple") || itm.name.equals("orange") || itm.name.equals("bread") || itm.name.equals("cheese")|| itm.name.equals("chicken") || itm.name.equals("mineral_water") || itm.name.equals("instant_noodles") )){
            ObjectError err = new ObjectError("globalError", "We do not stock %s".formatted(itm.getName()));
            result.addError(err);
            return "view1";
        }
         
        // update cart
        Cart cart = (Cart) session.getAttribute("cart");
        cart.updateCart(itm);
        // System.out.println(cart);

        model.addAttribute("shoppingcart", cart.getItems());
        session.setAttribute("cart", cart);
        return "view1";
    }


    @GetMapping("/shippingaddress")
    public String showShippingForm(Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        // System.out.println(cart.getItems().size());
        if (cart.getItems().isEmpty()){
            return "redirect:/";
        } else {
            model.addAttribute("info", new DeliveryInfo());
            return "view2";

        }

    }

    @PostMapping("/orderReceived")
    public String postShippingForm(@Valid DeliveryInfo info, BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            System.out.println("sth is wrong");
            return "view2";
        }

        Cart cart = (Cart) session.getAttribute("cart");
        List<String> finalList = cart.getItems().stream().map(itm -> itm.getName()).collect(Collectors.toList());
        System.out.println("> final list: " + finalList.toString());
        try {
            Quotation q = qSvc.getQuotations(finalList);
            float totalCost = qSvc.calculateCost(cart.getItems(), q.getQuotations());
            model.addAttribute("invoiceId", q.getQuoteId());
            model.addAttribute("name", info.getName());
            model.addAttribute("address", info.getAddress());
            model.addAttribute("total", totalCost);
            // System.out.println(q.toString());
            session.invalidate();
            return "view3";
    
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error");
            return "redirect:/";

        }
        
    }


   
    
}
