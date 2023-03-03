package tfip.ssf.assessment.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.ssf.assessment.Repository.shoppingCartRepo;

@Service
public class QuotationService {
    
    @Autowired
    shoppingCartRepo repo;
}
