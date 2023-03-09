package tfip.ssf.assessment;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tfip.ssf.assessment.Model.Quotation;
import tfip.ssf.assessment.Service.QuotationService;

@SpringBootApplication
public class AssessmentApplication {
// public class AssessmentApplication implements CommandLineRunner{

	@Autowired
	QuotationService qsvc;

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}
/* 
	 @Override
	  public void run(String... args) throws Exception {
	  	List<String> items = new LinkedList<>();
	   	items.add("apple");
	  	items.add("orange");
	   	items.add("mineral_water");

	  Quotation q = qsvc.getQuotations(items);
	  System.out.println(q);
	  }
*/
}
