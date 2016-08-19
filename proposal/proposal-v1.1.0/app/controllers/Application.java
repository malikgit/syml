package controllers;

import static play.data.Form.form;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import com.biz.rest.RestCall;
import com.biz.rest.RestCallClass;
import com.biz.syml.CouchBaseOperation;
import com.biz.syml.CouchbaseData;
import com.biz.syml.HttpsConnectionCase;
import com.biz.syml.RecommendationChainComparater;
import com.biz.syml.RecommendationTermComparator;
import com.biz.syml.SourcesReUnderwrite;
import com.biz.syml.TestDevCRM;
import com.biz.util.JsonConvertion;
import com.biz.util.MarketingNotesOperation;
import com.biz.util.RecommedationOperation;
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.vbucket.ConnectionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Application extends Controller {
	
	
	
	
	
	
	private static org.slf4j.Logger logger = Logger.underlying();
	public Result index() {

		return ok("hello");
	}

	public Result addProduct() {

		return ok("hello");

		// Recommendation person = new Recommendation();
		// return ok(addProduct.render(person));

	}
	

	public Result add() {

		String opp_id = request().getQueryString("id");
		// System.out.println("opp_id : "+opp_id);

		return ok(add.render(opp_id));
	}
	
	public Result preApproval(){
		return ok(preApproval.render("PRE APPROVAL"));
	}

	public Result addfixed() {

		String opp_id = request().getQueryString("id");
		// System.out.println("opp_id : "+opp_id);

		return ok(addfixed.render(opp_id));
	}

	public Result addloc() {

		String opp_id = request().getQueryString("id");
		// System.out.println("opp_id : "+opp_id);

		return ok(addloc.render(opp_id));
	}

	public Result addAndSave() {
		DynamicForm data = form().bindFromRequest();

		String opp_id = data.get("opp_id");
		logger.debug("data forn " + opp_id);
				

		String productId = data.get("productId");

		String lender = data.get("lender");
		String productName = data.get("productName");
		String term = data.get("term");
		String amortization = data.get("amortization");
		String mortgageAmount = data.get("mortgageAmount");
		String payment = data.get("payment");
		String interestrate = data.get("interestrate");
		String initialforApproval = data.get("initialforApproval");
		String position = data.get("cashbackPercent");
		String mortgageType = data.get("mortgageType");

		logger.debug("productId : " + productId);

		Recommendation rp = null;
		UwAppAllAlgo Uw = null;
		
		

		ObjectMapper objectMapper = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;

		try {
			cbdata = new CouchbaseData();

		
			couchOp = new CouchBaseOperation();
			

			Uw = cbdata.getDataFromCouchbase(opp_id);
			
			logger.debug("rp : rp : rp " + Uw.getRecommendations());

		} catch (NullPointerException e) {
			
			logger.error("Data  Not Found"+e.getMessage());
		}

		

		Set<Recommendation> recomendtionList = Uw.getRecommendations();

		for (Iterator<Recommendation> iterator = recomendtionList.iterator(); iterator
				.hasNext();)

		{

			Recommendation recommendation = (Recommendation) iterator.next();
			logger.debug(" before recomendtionList.size() :"
					+ recomendtionList.size());
			if (recommendation.getProductID().equals(productId)) {
				// = recommendation.getProductID()
				logger.debug("already exit there ");
				logger.debug("recommendation.getProductID():"
						+ recommendation.getProductID());
				// Uw.getRecommendations().remove(recommendation);
				break;
			}

			logger.debug(" after recomendtionList.size() :"
					+ recomendtionList.size());

		}

		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String create_date = df.format(today);
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(new Date()); // sets calendar time/date
	    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
	    cal.getTime(); 
		
	    Date newDate = DateUtils.addHours(today, 84);
	    
	    String expiry_date = df.format(newDate);
	    logger.debug("Expiry Data    time is"+expiry_date);
	    
	    
	   
	    
		rp = new Recommendation();

		rp.setProductID(productId);
		rp.setLender(lender);
		rp.setInterestRate(interestrate);
		rp.setInitialforApproval(initialforApproval);
		rp.setPayment(payment);
		rp.setProduct(productName);
		rp.setMortgageType(mortgageType);
		rp.setMaximumAmortization(amortization);
		rp.setMortgageAmount(mortgageAmount);
		rp.setPosition(position);
		rp.setTerm(term);
		rp.setCreate_date(create_date);
		
		

		Uw.getRecommendations().add(rp);

		try {
			logger.debug("getRecommendations : "
					+ objectMapper.writeValueAsString(Uw));

		} catch (JsonProcessingException e) {
			logger.error("Reccmadation Data  Not  Found "+e);
		}
		try {

			JSONObject js = new JSONObject(objectMapper.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"
					+ opp_id, js);
		} catch (Exception e) {
			logger.error("error  in UwAppAllAlgo  getting  Algo ", e);

		}

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		
		ArrayList<CombinedRecommendation> combineRecommended=null;
		Set<MarketingNotes> morketNotes = null;
		RecommendDetails recdetials = null;
		OrignalDetails detials = null;

		try {
			
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			combineRecommended= new ArrayList<CombinedRecommendation>();
			
			morketNotes = Uw.getMarketingNotes();
			
			
			
			

			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
				listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
				logger.info("------------listofMax--------------"+listofmax);

			
				
				Set<CombinedRecommendation> combine =  Uw.getCombinedRecommendation();
				combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
				logger.info("------combineRecommended size()-------------"+combineRecommended.size());
				
			
				Set<Recommendation> setRec = Uw.getRecommendations();
				logger.info("set REc " + setRec.size());
				for (Recommendation recommendation : setRec) {

				

				if (recommendation.getMortgageType().equalsIgnoreCase("2")) {
					System.out.println("array Variable  : "
							+ arrayVariable.size());
					arrayVariable.add(recommendation);
					Collections.sort(arrayVariable, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
				
					
				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"3")) {
					
					//logger.debug("######array fixed for  Fixed Options:######### " + arrayFixed.size());
					arrayFixed.add(recommendation);
					Collections.sort(arrayFixed, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					

					
					
				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"1")) {
				//	System.out.println("array array LOC : " + arrayLOC.size());
					arrayLOC.add(recommendation);
					

				}
			}
		} catch (Exception e) {

		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		
		
	String companyName="";
	
	
	try{
		companyName=HttpsConnectionCase.getCompanyName(opp_id);
	}catch(Exception e){
		logger.error("error while getting company name "+e.getMessage());
	}
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

	

	}

	String oppertunityId = null;
	
	
		public Result getData() {
			
			
			logger.debug("--------inside getData()----------");
			
		
			String opp_id=null;
			CouchbaseData data = new CouchbaseData();

		
		
		
		String casefor = null;
		
		opp_id = request().getQueryString("opp_id");;
	
		logger.info("oppertunityId before getdata  {}  opp Id  dd:  "+opp_id);
		
	
		String commpanyName="";
		/*TestDevCRM test = new TestDevCRM();
		
		try{
			commpanyName = test.getCompanyName(opp_id);
			logger.debug("company Name : "+commpanyName);
		}catch(Exception  e){
		
		
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
	
		/* for local crm used for company */
		
		
		
		
		try{
			commpanyName=HttpsConnectionCase.getCompanyName(opp_id);
			
		}catch(Exception e){
			logger.error("inside getdata() error while getting the company name"+e.getMessage());			
		
		}
		
		
		
		String clientName = request().getQueryString("action");
		
		
		logger.info("----Action coming--------  : " + clientName);

		

		UwAppAllAlgo algo = data.getDataFromCouchbase(opp_id);
		
		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		
		
		
		
		ArrayList<CombinedRecommendation> combineRecommended=null;
		Set<MarketingNotes> morketNotes=null;
		try{
		 morketNotes = algo.getMarketingNotes();
		}catch(ConnectionException e){
			logger.error("while morketNotes null from couchbase :  "+e.getMessage());
		}
		
		ArrayList<MarketingNotes> morketNotesforlist =null;
		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		logger.info("algo : " + algo.toString());

		
		OrignalDetails detials = new OrignalDetails();

		

		RecommendDetails recdetials = null;
		
		
		String  expiryDate=data.getDataCouchbase(opp_id);
		logger.debug("Form  Couchbase"+expiryDate);
		JSONObject  json=null;
		String  expirayTime=null;
				try{
          json=new JSONObject(expiryDate);
            expirayTime=json.get("ExpiryDateTime").toString();
            logger.debug("ExpiryTime  is"+expirayTime);
		}catch (JSONException e) {
			logger.error("Error  in  getting  Expiry  date"+e);		}
       
		
		
        Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String current_date = df.format(today);
		Date date_current_date=null;
		Date  date_expiryDate=null;
		try{
		 date_current_date = df.parse(current_date);
		
	  date_expiryDate=df.parse(expirayTime);
		}catch(ParseException e){
			logger.error("Date  Parsing  For Expiry  Failed"+e);
		}
		
		
		
		if (clientName != null) {
			casefor = clientName.toUpperCase();
			if(date_current_date.compareTo(date_expiryDate)>=0){
				return ok(expiry.render(opp_id,casefor));
			
		}
		
		
		else if(date_current_date.compareTo(date_expiryDate)<0){
			
			
			if (casefor.equals("C")) {
				return indexclient();
			} else if (casefor.equals("R")) {
				return indexreferal();
			}
			if (casefor.equals("B")) {
				logger.info("??????????/// insert in action B {}{ ??????????????: ");
				return indexbroker(casefor,opp_id);
			}

			
			} 
		}
		else
			// (clientName.equals("")||clientName.equals("null"))
			{
				
				arrayVariable = new ArrayList<Recommendation>();
				arrayFixed = new ArrayList<Recommendation>();
				arrayLOC = new ArrayList<Recommendation>();
				combineRecommended = new ArrayList<>();
				
				
				morketNotes = algo.getMarketingNotes();

				recdetials = new RecommendDetails();

				for (MarketingNotes marketingNotes : morketNotes) {
					if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
						detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
						
						
					} else if (marketingNotes.getNoteName().equalsIgnoreCase(
							"RecommendDetails")) {
					recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
					}
				}
					listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
					logger.info("------------listofMax--------------"+listofmax);
					
				
				morketNotesforlist=new ArrayList<>();
				
				
				//morketNotesforlist=	MarketingNotesOperation.getListofCombineTabe(morketNotes);
				
			//	logger.info("--------morketNotesforlist---------"+morketNotesforlist.size());
				

				
				Set<CombinedRecommendation> combine =  algo.getCombinedRecommendation();
				combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
				logger.info("------combineRecommended size()-------------"+combineRecommended.size());
				
				Logger.info("combine size : "+combine.size());

				
				
				
				

				Set<Recommendation> setRec = algo.getRecommendations();	
				Map<String,List<Recommendation>> mapofrec=	RecommedationOperation.getRecList(setRec);
				try{
				
		
			
			logger.info("---------------map -------------"+mapofrec.size());
			
			
			arrayLOC	=(ArrayList<Recommendation>) mapofrec.get("arrayLoc");
			logger.info("-------------arrayLoc-----------"+arrayLOC.size());
			
				
				}catch(NullPointerException e){
					logger.error("error while getting -->"+e.getMessage());
					}
				arrayFixed	=(ArrayList<Recommendation>) mapofrec.get("arrayFixed");
				logger.info("-------------arrayFixed-----------"+arrayFixed.size());
				arrayVariable	=(ArrayList<Recommendation>) mapofrec.get("arrayVariable");
				logger.info("-------------arrayVariable-----------"+arrayVariable.size());
				logger.debug("set REc " + setRec.size());
				logger.info ("set REc " + setRec.size());

				
			}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}

		return ok(index.render(algo, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,commpanyName));

	}
	public Result editOriginalDetails(){

		System.out.println("start from Original Details : ");
		String opp_id = request().getQueryString("opp_id");
		logger.info("opp_id : " + opp_id);
		
		
		

		String productID = request().getQueryString("productId");
		//System.out.println("productId our details  : "+productID);
		String propertyValue = request().getQueryString("propertyValue");
		String downpaymentEquity = request().getQueryString("downpaymentEquity");
		
		
		
		String mortgageAmount = request().getQueryString("mortgageAmount");
		String insuranceAmount = request().getQueryString("insuranceAmount");
		String totalMortgage = request().getQueryString("totalMortgage");
		
		String amortization = request().getQueryString("amortization");
		
		String mortgageType = request().getQueryString("mortgageType");
		String mortgageTerm = request().getQueryString("mortgageTerm");
		String paymentAmount = request().getQueryString("paymentAmount");
		String interestRate = request().getQueryString("interestRate");
		String lender = request().getQueryString("lender");
		
		
		
		
		
		
		//logger.info("intiialforapproval : " + intiialforapproval);

		OrignalDetails original = new OrignalDetails();

		original.setPropertyValue(propertyValue);
		original.setDownpaymentEquity(downpaymentEquity);
		original.setMortgageAmount(mortgageAmount);
		original.setInsuranceAmount(insuranceAmount);
		original.setTotalMortgage(totalMortgage);
		original.setAmortization(amortization);
		
		original.setMortgageType(mortgageType);
		original.setMortgageTerm(mortgageTerm);
		original.setPaymentAmount(paymentAmount);
		original.setInterestRate(interestRate);
		original.setLender(lender);
		original.setProductID(productID);
		
	
		/*rc.setProductID(productId);
		rc.setLender(lender);
		rc.setProduct(productName);
		rc.setTerm(term);
		rc.setMaximumAmortization(amortization);
		rc.setInterestRate(interestrate);
		rc.setPayment(monthlypayment);
		rc.setMortgageType(mortagetype);
*/
		//original.setInitialforApproval(intiialforapproval);
		System.out.println("start from Original Details : ");

		return ok(editOriginalDetails.render(original, opp_id));

	
	}
	public Result editOurRecommend(){


logger.info("start from Original Details : ");
		String opp_id = request().getQueryString("opp_id");
		logger.info("opp_id : " + opp_id);

		String productID =  request().getQueryString("productId");
		System.out.println("product Id recommend "+productID);
		String propertyValue = request().getQueryString("propertyValue");
		String downpaymentEquity = request().getQueryString("downpaymentEquity");
		
		
		
		String mortgageAmount = request().getQueryString("mortgageAmount");
		String insuranceAmount = request().getQueryString("insuranceAmount");
		String totalMortgage = request().getQueryString("totalMortgage");
		
		String amortization = request().getQueryString("amortization");
		
		String mortgageType = request().getQueryString("mortgageType");
		String mortgageTerm = request().getQueryString("mortgageTerm");
		String paymentAmount = request().getQueryString("paymentAmount");
		String interestRate = request().getQueryString("interestRate");
		String lender = request().getQueryString("lender");
		
		
		
		
		
		
		
		//logger.info("intiialforapproval : " + intiialforapproval);

		RecommendDetails recommend = new RecommendDetails();

		recommend.setPropertyValue(propertyValue);
		recommend.setDownpaymentEquity(downpaymentEquity);
		recommend.setMortgageAmount(mortgageAmount);
		recommend.setInsuranceAmount(insuranceAmount);
		recommend.setTotalMortgage(totalMortgage);
		recommend.setAmortization(amortization);
		
		recommend.setMortgageType(mortgageType);
		recommend.setMortgageTerm(mortgageTerm);
		recommend.setPaymentAmount(paymentAmount);
		recommend.setInterestRate(interestRate);
		recommend.setLender(lender);
		recommend.setProductID(productID);
		
	

		return ok(editOurRecommendation.render(recommend, opp_id));

	
	
	}
	public Result editOurRecommendCombined(){
logger.debug("inside edit Combined Recommed : ");

		
				String opp_id = request().getQueryString("opp_id");
				logger.info("opp_id : " + opp_id);

				String productID =  request().getQueryString("productId");
				String productID1 = request().getQueryString("productId1");
				String propertyValue = request().getQueryString("propertyValue");
				String downpaymentEquity = request().getQueryString("downpaymentEquity");
				String productName=request().getQueryString("productName");
				String productName1=request().getQueryString("productName1");
				
				logger.info("productName:"+productName+"productName1: "+productName1);
				String mortgageAmount = request().getQueryString("mortgageAmount");
				String mortgageAmount1 = request().getQueryString("mortgageAmount1");
				
				
				
				String totalMortgageamount = request().getQueryString("totalMortgageamount");
				String totalpayment = request().getQueryString("totalpayment");
				
				String amortization = request().getQueryString("amortization");
				String amortization1 = request().getQueryString("amortization1");
				
				String mortgageType = request().getQueryString("mortgageType");
				String mortgageType1 = request().getQueryString("mortgageType1");
				
				String mortgageTerm = request().getQueryString("mortgageTerm");
				String mortgageTerm1 = request().getQueryString("mortgageTerm1");
				
				String paymentAmount = request().getQueryString("paymentAmount");
				String paymentAmount1 = request().getQueryString("paymentAmount1");
				
				String interestRate = request().getQueryString("interestRate");
				String interestRate1 = request().getQueryString("interestRate1");
				String lender = request().getQueryString("lender");
				
				
				
				
				logger.info("all the value comes here :"+"productId"+productID+"productID1:"+productID1
						+"propertyValue : " +propertyValue +" downpaymentEquity : "+ downpaymentEquity
						+"mortgageAmount :" +mortgageAmount +" mortgageAmount1:" +mortgageAmount1
				+" totalMortgageamount:" +totalMortgageamount
				+"totalpayment: "+ totalpayment+ "amortization :" +amortization +"amortization1 :" +amortization1+ "mortgageType:"+mortgageType
				+"mortgageType1: " +mortgageType1+ "mortgageTerm:" +mortgageTerm+ "mortgageTerm1:"+mortgageTerm1+"paymentAmount:"+paymentAmount
				+"paymentAmount1:" +paymentAmount1+ " interestRate:" +interestRate+ "interestRate1: "+interestRate1+"lender:"+lender
						);
				
				
				
				
			

				RecommendDetails recommend = new RecommendDetails();

				recommend.setPropertyValue(propertyValue);
				recommend.setDownpaymentEquity(downpaymentEquity);
				recommend.setMortgageAmount(mortgageAmount);
				recommend.setMortgageAmount1(mortgageAmount1);
			
				recommend.setTotalMortgageAmount(totalMortgageamount);
				
				recommend.setAmortization(amortization);
				recommend.setAmortization1(amortization1);
				
				
				recommend.setMortgageType(mortgageType);
				recommend.setMortgageType1(mortgageType1);
				
				recommend.setMortgageTerm(mortgageTerm);
				recommend.setMortgageTerm1(mortgageTerm1);
				recommend.setPaymentAmount(paymentAmount);
				recommend.setPaymentAmount1(paymentAmount1);
				
				recommend.setInterestRate(interestRate);
				recommend.setInterestRate1(interestRate1);
				
				recommend.setLender(lender);
				recommend.setProductID(productID);
				recommend.setProductID1(productID1);
				recommend.setTotalPayment(totalpayment);
				recommend.setProductName(productName);
				recommend.setProductName1(productName1);
				
				
			

				return ok(editOurRecommendationCombined.render(recommend, opp_id));

			
			
			}

	
	public Result editProduct() {

		String opp_id = request().getQueryString("opp_id");
		logger.info("opp_id : " + opp_id);

		String mortagetype = request().getQueryString("mortgagetype");
		logger.info("mortagetype : " + mortagetype);

		String productId = request().getQueryString("productId");
		logger.info("productId : " + productId);

		String lender = request().getQueryString("lender");
		logger.info("lender : " + lender);

		String productName = request().getQueryString("productName");
		logger.info("productName : " + productName);

		String term = request().getQueryString("term");
		logger.info("term : " + term);

		String amortization = request().getQueryString("amortization");
		logger.info("amortization : " + amortization);

		String monthlypayment = request().getQueryString("monthlypayment");
		logger.info("monthlypayment : " + monthlypayment);

		String interestrate = request().getQueryString("interestrate");

		logger.info("interestrate : " + interestrate);

		String intiialforapproval = request().getQueryString(
				"intiialforapproval");
		logger.info("intiialforapproval : " + intiialforapproval);

		Recommendation rc = new Recommendation();
		rc.setProductID(productId);
		rc.setLender(lender);
		rc.setProduct(productName);
		rc.setTerm(term);
		rc.setMaximumAmortization(amortization);
		rc.setInterestRate(interestrate);
		rc.setPayment(monthlypayment);
		rc.setMortgageType(mortagetype);

		rc.setInitialforApproval(intiialforapproval);

		return ok(editProduct.render(rc, opp_id));

	}
	public Result editCombineProduct(){
		logger.debug("---inside the combine product edit---");
		String opp_id = request().getQueryString("opp_id");
		logger.info("opp_id : " + opp_id);

		String baseproductId = request().getQueryString("productId");
		logger.info("base productId : " + baseproductId);
		
		String addproductId= request().getQueryString("addproductID");
		logger.info("addition product Id : "+addproductId);

		String lender = request().getQueryString("lender");
		logger.info("base lender : " + lender);


		String baseproduct = request().getQueryString("baseproduct");
		logger.info("base product : " + baseproduct);

		String addproduct = request().getQueryString("addproduct");
		logger.info("add product : " + addproduct);

		String baseterm = request().getQueryString("baseterm");
		logger.info("base term : " + baseterm);

		String addterm = request().getQueryString("addterm");
		logger.info("add term : " + addterm);

		String baseamortization = request().getQueryString("baseamortization");
		logger.info(" base amortization : " + baseamortization);

		String addamortization = request().getQueryString("addamortization");
		logger.info("add amortization : " + addamortization);

		String baseamount = request().getQueryString("baseamount");
		logger.info("base mortgage amount: " + baseamount);

		String addamount = request().getQueryString("addamount");
		logger.info("add  mortgage amount: " + addamount);

		String basemonthly = request().getQueryString("basemonthly");
		logger.info("base monthlypayment : " + basemonthly);

		String addmonthly = request().getQueryString("addmonthly");

		logger.info("add monthlypayment : " + addmonthly);
		
		String baseinterest = request().getQueryString("baseinterest");

		logger.info("base interestrate : " + baseinterest);
		String addinterest = request().getQueryString("addinterest");

		logger.info("add interestrate : " + addinterest);
		
		String totalpayment = request().getQueryString("totalpayment");

		logger.info("total payment: " + totalpayment);

		String totalMortgageAmount = request().getQueryString("totalMortgageAmount");

		logger.info("totalMortgageAmount: " + totalMortgageAmount);

		
		String basePosition = request().getQueryString("basePosition");

		logger.info("basePosition : " + basePosition);

		String additionalPosition = request().getQueryString("additionalPosition");

		logger.info("additionalPosition: " + additionalPosition);

		String baseCashbackPercent = request().getQueryString("baseCashbackPercent");

		logger.info("baseCashbackPercent : " + baseCashbackPercent);

		
		String addcashback = request().getQueryString("addcashback");

		logger.info("addcashback : " + addcashback);


		String baseMortgageType = request().getQueryString("baseMortgageType");

		logger.info("baseMortgageType : " + baseMortgageType);


		String addMortgageType = request().getQueryString("addMortgageType");

		logger.info("addMortgageType : " + addMortgageType);

		
		
		
		
		String intiialforapproval = request().getQueryString(
				"intiialforapproval");
		logger.info("intiialforapproval : " + intiialforapproval);
		
		CombinedRecommendation cr  = new CombinedRecommendation();
		cr.setBaseProductID(baseproductId);
		cr.setAdditionalProductID(addproductId);
		
		
		cr.setBaseLender(lender);
		cr.setAdditionalLender(lender);
		//cr.setBaseLender(lender);
		cr.setBaseProduct(baseproduct);
		cr.setAdditionalProduct(addproduct);
		
		cr.setBaseTerm(baseterm);
		cr.setAdditionalTerm(addterm);
		
		cr.setBaseAmortization(baseamortization);
		cr.setAdditionalAmortization(addamortization);
		
		cr.setBaseMortgageAmount(baseamount);
		cr.setAdditionalMortgageAmount(addamount);
		
		cr.setBasePayment(basemonthly);
		cr.setAdditionalPayment(addmonthly);
		
		cr.setBaseInterestRate(baseinterest);
		cr.setAdditionalInterestRate(addinterest);
		
		
		cr.setTotalPayment(totalpayment);
		cr.setTotalMortgageAmount(totalMortgageAmount);

		cr.setBasePosition(basePosition);
		cr.setAdditionalPosition(additionalPosition);

		cr.setBaseCashbackPercent(baseCashbackPercent);
		cr.setAdditionalCashbackPercent(addcashback);
		
		cr.setBaseMortgageType(baseMortgageType);
		cr.setAdditionalMortgageType(addMortgageType);
		
		
		
		
		
	
		
		
		return ok(editCombineProduct.render(cr,opp_id));
	}
	
	
	
	public Result EditandSaveOriginalDetails(){
		logger.debug("inside when edit data to post :  ");

		DynamicForm data = form().bindFromRequest();

		String opp_id = data.get("opp_id");
		logger.info("opp_id : " + opp_id);
		
		
				
		String propertyValue = data.get("propertyValue");

		String productId = data.get("productId");
		logger.info("productId : " + productId);

		String downpaymentEquity = data.get("downpaymentEquity");
		logger.info("mortagetype : " + downpaymentEquity);

		String mortgageAmount = data.get("mortgageAmount");
		logger.info("lender : " + mortgageAmount);

		String insuranceAmount = data.get("insuranceAmount");
		logger.info("productName : " + insuranceAmount);

		String totalMortgage = data.get("totalMortgage");
		logger.info("productName : " + totalMortgage);
		System.out.println("term : " + totalMortgage);

		String amortization = data.get("amortization");
		logger.info("amortization : " + amortization);

		String mortgageType = data.get("mortgageType");
		logger.info("mortgageType : " + mortgageType);

		String mortgageTerm = data.get("mortgageTerm");
		logger.info("mortgageTerm : " + mortgageTerm);
		
		
		String paymentAmount = data.get("paymentAmount");
		logger.info("paymentAmount : " + paymentAmount);
		String interestRate = data.get("interestRate");
		logger.info("interestRate : " + interestRate);
		String lender = data.get("lender");
		logger.info("lender : " + lender);
		
		
		
		

	/*	String intiialforapproval = data.get("intiialforapproval");
		logger.info("intiialforapproval : " + intiialforapproval);
*/
		RecommendDetails rp = null;
		UwAppAllAlgo Uw = null;

		ObjectMapper ob = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		try {
			cbdata = new CouchbaseData();

			logger.debug("in try  catch for getting Connetion To couchbase : ");
			couchOp = new CouchBaseOperation();
			//CouchbaseClient cb = couchOp.getConnectionToCouchBase();

			Uw = cbdata.getDataFromCouchbase(opp_id);
			
			logger.info("Get Algo Recommendation : " + Uw.getMarketingNotes());

		} catch (Exception e) {
			logger.error("error", e);
		}
		
		Set<MarketingNotes> marketingnotess = Uw.getMarketingNotes();

		for(MarketingNotes market : marketingnotess){
			
			if(market.getNoteName().equals("OriginalDetails")){
				JSONObject json =null;
				try{
					json=	new JSONObject(market.getNoteContent());
		
					
		json.put("propertyValue", propertyValue);

		json.put("downpaymentEquity",downpaymentEquity);
		json.put("mortgageAmount",mortgageAmount);
		json.put("insuranceAmount",insuranceAmount);
		json.put("totalMortgage",totalMortgage);
		json.put("amortization",amortization);
		json.put("mortgageType",mortgageType);
		
		json.put("mortgageTerm",mortgageTerm);
		json.put("paymentAmount",paymentAmount);
		json.put("interestRate",interestRate);
		
		json.put("lender",lender);
		
				}
		catch(Exception e){
			logger.error("error while submit the original details : "+e);
		}
		market.setNoteContent(json.toString());
		

		}
	
		
		}
			
	
		
	

		try {

			System.out.println(" getRecommendations : "
					+ ob.writeValueAsString(Uw));

		} catch (Exception e) {

		}
		try {

			JSONObject js = new JSONObject(ob.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"
					+ opp_id, js);
		} catch (Exception e) {
			logger.error("error", e);

		}

		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		Set<MarketingNotes> morketNotes = null;
		
		ArrayList<CombinedRecommendation> combineRecommended =null;
		RecommendDetails recdetials = null;
		OrignalDetails detials = null;

		try {
			
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			combineRecommended = new ArrayList<>();
			morketNotes = Uw.getMarketingNotes();
			
			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
			
			listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
			logger.info("------------listofMax--------------"+listofmax);
			
			Set<CombinedRecommendation> combine =  Uw.getCombinedRecommendation();
			combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
			logger.info("------combineRecommended size()-------------"+combineRecommended.size());

			
			
			
					
			
			Set<Recommendation> setRec = Uw.getRecommendations();
			logger.debug("set REc " + setRec.size());

			
			
			Map<String,List<Recommendation>> mapofrec=	RecommedationOperation.getRecList(setRec);
			
			logger.info("---------------map -------------"+mapofrec.size());
			
			
			arrayFixed	=(ArrayList<Recommendation>) mapofrec.get("arrayFixed");
			logger.info("-------------arrayFixed-----------"+arrayFixed.size());
			arrayVariable	=(ArrayList<Recommendation>) mapofrec.get("arrayVariable");
			logger.info("-------------arrayVariable-----------"+arrayVariable.size());
				
			
			
			
			
		} catch (Exception e) {
			logger.error("while in editPost() for json data get  occured", e);

		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		
		String  companyName="";
		//TestDevCRM test = new TestDevCRM();
		
		/*try{
			companyName = test.getCompanyName(opp_id);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
		
		try{
			companyName=HttpsConnectionCase.getCompanyName(opp_id);
		}catch(Exception e){
			logger.error("error in get company name"+e.getMessage());;
		}
		
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

	}
	
	
	
	
	public  Result  editandSavemaxpur(){
		logger.debug("inside editandSavemaxpur  :  ");
		
		


		

		DynamicForm data = form().bindFromRequest();

		String opp_id = data.get("opp_id");
		logger.info("opp_id : " + opp_id);
		
		

		String  companyName="";
		//TestDevCRM test = new TestDevCRM();
		
		/*try{
			companyName = test.getCompanyName(opp_id);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
		
		try{
			companyName = HttpsConnectionCase.getCompanyName(opp_id);
		}catch(Exception e){
			logger.error("error in get company name"+e.getMessage());
		}
		
		String debtReduction = data.get("debtReduction");

		String productId = data.get("productId");
		logger.info("productId : " + productId);

		String largestDownpaymentHouse = data.get("largestDownpaymentHouse");
		logger.info("largestDownpaymentHouse : " + largestDownpaymentHouse);

		String maximumMortgageNoCondo = data.get("maximumMortgageNoCondo");
		logger.info("maximumMortgageNoCondo : " + maximumMortgageNoCondo);

		String insureAmountMaxMortgage = data.get("insureAmountMaxMortgage");
		logger.info("insureAmountMaxMortgage : " + insureAmountMaxMortgage);

		String variableMortHouseNoDebtRepayValue = data.get("variableMortHouseNoDebtRepayValue");
		
		
		

		RecommendDetails rp = null;
		UwAppAllAlgo Uw = null;
		
		ObjectMapper ob = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		try {
			cbdata = new CouchbaseData();

			logger.debug("in try  catch for getting Connetion To couchbase : ");
			couchOp = new CouchBaseOperation();
			CouchbaseClient cb = couchOp.getConnectionToCouchBase();

			Uw = cbdata.getDataFromCouchbase(opp_id);
			
			logger.info("Get Algo Recommendation : " + Uw.getMarketingNotes());

		} catch (Exception e) {
			logger.error("error", e);
		}
		
		Set<MarketingNotes> marketingnotess = Uw.getMarketingNotes();

		for(MarketingNotes market : marketingnotess){
			
			if(market.getNoteName().equals("MaxMortgageTable")){
				
				JSONObject json =null;
				JSONArray jsarray = null;
				
				try{
					
					json=	new JSONObject(market.getNoteContent());
					jsarray = new JSONArray();
				//	System.out.println("jsontoString() :"+ json.toString());
					
				JSONObject jsonVaribalMortgage=	new JSONObject(json.get("variableMortgage(House)").toString());
					
		
			logger.debug("variableMortgage(House)  data if exist------------------------ inside loop");

					
			jsonVaribalMortgage.put("debtReduction", debtReduction);

			jsonVaribalMortgage.put("largestDownpaymentHouse",largestDownpaymentHouse);
		
			jsonVaribalMortgage.put("maximumMortgageNoCondo",maximumMortgageNoCondo);
			jsonVaribalMortgage.put("insureAmountMaxMortgage",insureAmountMaxMortgage);
			jsonVaribalMortgage.put("variableMortHouseNoDebtRepayValue",variableMortHouseNoDebtRepayValue);
			json.put("variableMortgage(House)",jsonVaribalMortgage);
			System.out.println("updated varibale "+json);
			
			JSONObject jsonVaribalMortgage2=	new JSONObject(json.get("fixedMortgage(House)").toString());
			
		 
			jsonVaribalMortgage2.put("debtReduction", debtReduction);

			jsonVaribalMortgage2.put("largestDownpaymentHouse",largestDownpaymentHouse);
			
			jsonVaribalMortgage2.put("maximumMortgageNoCondo",maximumMortgageNoCondo);
			jsonVaribalMortgage2.put("insureAmountMaxMortgage",insureAmountMaxMortgage);
			jsonVaribalMortgage2.put("variableMortHouseNoDebtRepayValue",variableMortHouseNoDebtRepayValue);
			json.put("fixedMortgage(House)",jsonVaribalMortgage2);
		}
				
		catch(Exception e){
			logger.error("error while submit the original details : "+e.getMessage());
		}
		market.setNoteContent(json.toString());
		

		}
	
		
		}
			
	
		
		
	


		try {

			logger.info(" getRecommendations : "
					+ ob.writeValueAsString(Uw));

		} catch (Exception e) {
			logger.error("Recommnedation : "+e.getMessage());

		}
		try {

			JSONObject js = new JSONObject(ob.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"
					+ opp_id, js);
		} catch (Exception e) {
			logger.error("error", e);

		}

		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		   ArrayList<CombinedRecommendation> combineRecommended=null;
		
		
		Set<MarketingNotes> morketNotes = null;
		RecommendDetails recdetials = null;
		OrignalDetails detials = null;

		try {
			
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			combineRecommended = new ArrayList<>();
			morketNotes = Uw.getMarketingNotes();
			
			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
			
			listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
			logger.info("------------listofMax--------------"+listofmax);
			
			Set<CombinedRecommendation> combine =  Uw.getCombinedRecommendation();
			combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
			logger.info("------combineRecommended size()-------------"+combineRecommended.size());

		
			Set<Recommendation> setRec = Uw.getRecommendations();
			logger.debug("set REc " + setRec.size());

			Map<String,List<Recommendation>> mapofrec=	RecommedationOperation.getRecList(setRec);
			
			logger.info("---------------map -------------"+mapofrec.size());
			
			
			try{
				arrayLOC	=(ArrayList<Recommendation>) mapofrec.get("arrayLoc");
				logger.info("-------------arrayLoc-----------"+arrayLOC.size());
				}catch( NullPointerException e){logger.error("null "+e.getMessage());}
			arrayFixed	=(ArrayList<Recommendation>) mapofrec.get("arrayFixed");
			logger.info("-------------arrayFixed-----------"+arrayFixed.size());
			arrayVariable	=(ArrayList<Recommendation>) mapofrec.get("arrayVariable");
			logger.info("-------------arrayVariable-----------"+arrayVariable.size());
				
			
			
			
		} catch (Exception e) {
			logger.error("while in editPost() for json data get  occured", e);

		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		
		
		
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

		
	
		
		
		
		
	}

	public Result editandSaveRecommendDetails(){

		logger.debug("inside when edit data to post :  ");

		DynamicForm data = form().bindFromRequest();

		String opp_id = data.get("opp_id");
		logger.info("opp_id : " + opp_id);
		

		String  companyName="";
		//TestDevCRM test = new TestDevCRM();
		
		/*try{
			companyName = test.getCompanyName(opp_id);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
		
		try{
			companyName = HttpsConnectionCase.getCompanyName(opp_id);
		}catch(Exception e){
			logger.error("error in get company name"+e.getMessage());
		}
		
		String propertyValue = data.get("propertyValue");
		logger.info("propertyValue : " + propertyValue);
		
		String productId = data.get("productId");
		logger.info("productId : " + productId);
		
		String productId1= data.get("productId1");
		logger.info("productId1 : " + productId1);
		
		

		String downpaymentEquity = data.get("downpaymentEquity");
		logger.info("downpaymentEquity : " + downpaymentEquity);

		String mortgageAmount = data.get("mortgageAmount");
		logger.info("mortgageAmount : " + mortgageAmount);
		
		
		String mortgageAmount1 = data.get("mortgageAmount1");
		logger.info("mortgageAmount1 : " + mortgageAmount1);

		String insuranceAmount = data.get("insuranceAmount");
		logger.info("insuranceAmount : " + insuranceAmount);

		String totalMortgage = data.get("totalMortgage");
		logger.info("totalMortgage : " + totalMortgage);
		

		String amortization = data.get("amortization");
		logger.info("amortization : " + amortization);
		String amortization1 = data.get("amortization1");
		logger.info("amortization1 : " + amortization1);

		String mortgageType = data.get("mortgageType");
		logger.info("mortgageType : " + mortgageType);
		
		String mortgageType1 = data.get("mortgageType1");
		logger.info("mortgageType1 : " + mortgageType1);

		String mortgageTerm = data.get("mortgageTerm");
		logger.info("mortgageTerm : " + mortgageTerm);
		
		String mortgageTerm1 = data.get("mortgageTerm1");
		logger.info("mortgageTerm1: " + mortgageTerm1);
		
		
		String paymentAmount = data.get("paymentAmount");
		logger.info("paymentAmount : " + paymentAmount);
		

		String paymentAmount1 = data.get("paymentAmount1");
		logger.info("paymentAmount1 : " + paymentAmount1);
		
		String totalMortgageAmount = data.get("totalMortgageAmount");
		logger.info("totalMortgageAmount : " + totalMortgageAmount);
		String totalPayment = data.get("totalPayment");
		logger.info("totalPayment : " + totalPayment);
		
		
		
		String interestRate = data.get("interestRate");
		logger.info("interestRate : " + interestRate);
		
		String interestRate1 = data.get("interestRate1");
		logger.info("interestRate1 : " + interestRate1);
		
		String productName = data.get("productName");
		logger.info("productName : " + productName);
		
		String productName1 = data.get("productName1");
		logger.info("productName1 : " + productName1);
		
		String lender = data.get("lender");
		logger.info("lender : " + lender);
		
		
		
		

	/*	String intiialforapproval = data.get("intiialforapproval");
		logger.info("intiialforapproval : " + intiialforapproval);
*/
		
		
		RecommendDetails rp = null;
		UwAppAllAlgo Uw = null;

		ObjectMapper ob = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;
		
		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		try {
			cbdata = new CouchbaseData();

			logger.debug("in try  catch for getting Connetion To couchbase : ");
			couchOp = new CouchBaseOperation();
			//CouchbaseClient cb = couchOp.getConnectionToCouchBase();

			Uw = cbdata.getDataFromCouchbase(opp_id);
			
			logger.info("Get Algo Recommendation : " + Uw.getMarketingNotes());

		} catch (Exception e) {
			logger.error("error", e);
		}
		
		Set<MarketingNotes> marketingnotess = Uw.getMarketingNotes();

		for(MarketingNotes market : marketingnotess){
			
			if(market.getNoteName().equals("RecommendDetails")){
				JSONObject json =null;
				try{
					json=	new JSONObject(market.getNoteContent());
		
					
		json.put("propertyValue", propertyValue);

		json.put("downpaymentEquity",downpaymentEquity);
		
		json.put("mortgageAmount",mortgageAmount);
		json.put("mortgageAmount1",mortgageAmount1);
		
		json.put("insuranceAmount",insuranceAmount);
		json.put("totalPayment",totalPayment);
		
		
		json.put("totalMortgageAmount",totalMortgageAmount);
		json.put("totalMortgage",totalMortgage);
		json.put("amortization",amortization);
		json.put("amortization1",amortization1);
		json.put("mortgageType",mortgageType);
		json.put("mortgageType1",mortgageType1);
		
		json.put("mortgageTerm",mortgageTerm);
		json.put("mortgageTerm1",mortgageTerm1);
		json.put("paymentAmount",paymentAmount);
		json.put("paymentAmount1",paymentAmount1);
		json.put("interestRate",interestRate);
		json.put("interestRate1",interestRate1);
		json.put("productName",productName);
		json.put("productName1",productName1);
		json.put("productID",productId);
		json.put("productID1",productId1);
		
		json.put("lender",lender);
		
				}
		catch(Exception e){
			logger.error("error while submit the original details : "+e);
		}
		market.setNoteContent(json.toString());
		

		}
	
		
		}
			
	
		
	

		try {

			System.out.println(" getRecommendations : "
					+ ob.writeValueAsString(Uw));

		} catch (Exception e) {

		}
		try {

			JSONObject js = new JSONObject(ob.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"+ opp_id, js);
					
		} catch (Exception e) {
			logger.error("error"+e.getMessage());

		}

		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		ArrayList<CombinedRecommendation> combineRecommended =null;
		Set<MarketingNotes> morketNotes = null;
		RecommendDetails recdetials = null;
		OrignalDetails detials = null;

		try {
			
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			 combineRecommended = new ArrayList<>();
			morketNotes = Uw.getMarketingNotes();
			
			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
			
			listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
			logger.info("------------listofMax--------------"+listofmax);
			
			Set<CombinedRecommendation> combine =  Uw.getCombinedRecommendation();
			combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
			logger.info("------combineRecommended size()-------------"+combineRecommended.size());

			

			
			
			Set<Recommendation> setRec = Uw.getRecommendations();
			logger.debug("set REc " + setRec.size());

			for (Recommendation recommendation : setRec) {

				logger.debug("recommendation : " + recommendation);

				if (recommendation.getMortgageType().equalsIgnoreCase("2")) {
					logger.debug("array Variable before  : "
							+ arrayVariable.size());
					String term =recommendation.getTerm();
					String sterm =setTerm2(term);
					recommendation.setTerm(sterm);
					arrayVariable.add(recommendation);
					Collections.sort(arrayVariable, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					logger.debug("array Variable after  : " + arrayVariable.size());
					

				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"3")) {
					String term =recommendation.getTerm();
					String sterm =setTerm2(term);
					recommendation.setTerm(sterm);
					//logger.debug("array fixed before 3  : " + arrayFixed.size());

					arrayFixed.add(recommendation);
					Collections.sort(arrayFixed, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					//logger.debug("array fixed after 3  : " + arrayFixed.size());

					
				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"1")) {
					//logger.debug("array array before LOC : " + arrayLOC.size());

					arrayLOC.add(recommendation);
					//logger.debug("array array after LOC : " + arrayLOC.size());
					

				}
			}
		} catch (Exception e) {
			logger.error("while in editPost() for json data get  occured", e);

		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		
		
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

		
	}

	public Result editPost() {
		logger.debug("inside when edit data to post :  ");

		DynamicForm data = form().bindFromRequest();

		String opp_id = data.get("opp_id");
		logger.debug("opp_id : " + opp_id);

		String  companyName="";
		//TestDevCRM test = new TestDevCRM();
		/*
		try{
			companyName = test.getCompanyName(opp_id);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
		
		try{
			companyName = HttpsConnectionCase.getCompanyName(opp_id);
		}catch(Exception e){
			logger.error("error in get company name"+e.getMessage());
		}
		
		String productId = data.get("productId");
		logger.debug("productId : " + productId);

		String mortagetype = data.get("mortgage");
		logger.debug("mortagetype : " + mortagetype);

		String lender = data.get("lender");
		logger.debug("lender : " + lender);

		String productName = data.get("productname");
		logger.debug("productName : " + productName);

		String term = data.get("term");
		logger.debug("productName : " + productName);
		logger.debug("term : " + term);

		String amortization = data.get("amortization");
		logger.info("amortization : " + amortization);

		String monthlypayment = data.get("payment");
		logger.info("monthlypayment : " + monthlypayment);

		String interestrate = data.get("interestrate");
		logger.info("interestrate : " + interestrate);

		String intiialforapproval = data.get("intiialforapproval");
		logger.info("intiialforapproval : " + intiialforapproval);

		Recommendation rp = null;
		UwAppAllAlgo Uw = null;

		ObjectMapper ob = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;
		
		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		try {
			cbdata = new CouchbaseData();

			logger.debug("in try  catch for getting Connetion To couchbase : ");
			couchOp = new CouchBaseOperation();
			CouchbaseClient cb = couchOp.getConnectionToCouchBase();

			Uw = cbdata.getDataFromCouchbase(opp_id);
			logger.info("Get Algo Recommendation : " + Uw.getRecommendations());

		} catch (Exception e) {
			logger.error("error", e);
		}

		Set<Recommendation> recomendtionList = Uw.getRecommendations();

		for (Iterator iterator = recomendtionList.iterator(); iterator
				.hasNext();)

		{

			Recommendation recommendation = (Recommendation) iterator.next();

			logger.info("before recomendtionList.size() :"
					+ recomendtionList.size());

			if (recommendation.getProductID().equals(productId)) {

				System.out.println("recommendation.getProductID():"
						+ recommendation.getProductID());
				Uw.getRecommendations().remove(recommendation);
				break;
			}
			logger.info(" after recomendtionList.size() :"
					+ recomendtionList.size());

		}

		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String reportDate = df.format(today);

		rp = new Recommendation();

		rp.setLender(lender);
		rp.setProductID(productId);
		rp.setMaximumAmortization(amortization);
		rp.setProduct(productName);
		rp.setTerm(term);
		rp.setInitialforApproval(intiialforapproval);
		rp.setInterestRate(interestrate);
		// rp.setMortgageAmount(monthlypayment);
		rp.setPayment(monthlypayment);
		rp.setMortgageType(mortagetype);
		rp.setDate_Edited(reportDate);

		Uw.getRecommendations().add(rp);

		try {

			System.out.println(" getRecommendations : "
					+ ob.writeValueAsString(Uw));

		} catch (Exception e) {

		}
		try {

			JSONObject js = new JSONObject(ob.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"
					+ opp_id, js);
		} catch (Exception e) {
			logger.error("error", e);

		}

		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		
		ArrayList<CombinedRecommendation> combineRecommended =null;
		Set<MarketingNotes> morketNotes = null;
		RecommendDetails recdetials = null;
		OrignalDetails detials = null;

		try {
			
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			 combineRecommended = new ArrayList<>();
			morketNotes = Uw.getMarketingNotes();
			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
			
			listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
			logger.info("------------listofMax--------------"+listofmax);
			
			Set<CombinedRecommendation> combine =  Uw.getCombinedRecommendation();
			combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
			logger.info("------combineRecommended size()-------------"+combineRecommended.size());

			
			
			
			
			
			Set<Recommendation> setRec = Uw.getRecommendations();
			logger.debug("set REc " + setRec.size());

			for (Recommendation recommendation : setRec) {

				logger.debug("recommendation : " + recommendation);

				if (recommendation.getMortgageType().equalsIgnoreCase("2")) {
					logger.debug("array Variable before  : "
							+ arrayVariable.size());
					String term2 =recommendation.getTerm();
					String sterm =setTerm2(term2);
					recommendation.setTerm(sterm);
					arrayVariable.add(recommendation);
					Collections.sort(arrayVariable, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					logger.debug("array Variable after  : " + arrayVariable.size());
					

				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"3")) {
					String term2 =recommendation.getTerm();
					String sterm =setTerm2(term2);
					recommendation.setTerm(sterm);
					//logger.debug("array fixed before 3  : " + arrayFixed.size());

					arrayFixed.add(recommendation);
					Collections.sort(arrayFixed, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					//logger.debug("array fixed after 3  : " + arrayFixed.size());

					
				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"1")) {
					//logger.debug("array array before LOC : " + arrayLOC.size());

					arrayLOC.add(recommendation);
					//logger.debug("array array after LOC : " + arrayLOC.size());
					

				}
			}
		} catch (Exception e) {
			logger.error("while in editPost() for json data get  occured", e);

		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		
		
		
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

	}
	public Result editCombinePost(){

		logger.debug("inside when edit combine Recommendation to post   ");

		DynamicForm data = form().bindFromRequest();

		String opp_id = data.get("opp_id");
		logger.info("opp_id : " + opp_id);

		
		String productId = data.get("productId");
		logger.info("productId : " + productId);
		
		String addproductId = data.get("addproductId");
		logger.info("additional prodicctid: "+addproductId);

		String baselender = data.get("baselender");
		logger.info("baselender : " + baselender);

		String addlender = data.get("addlender");
		logger.info("addlender : " + addlender);

		
		String totalMortgageAmount = data.get("totalMortgageAmount");
		logger.info("totalMortgageAmount : " + totalMortgageAmount);

		String basePosition = data.get("basePosition");
		logger.info("basePosition : " + basePosition);
		

		String additionalPosition = data.get("additionalPosition");
		logger.info("additionalPosition : " + additionalPosition);

		String baseCashbackPercent = data.get("baseCashbackPercent");
		logger.info("baseCashbackPercent : " + baseCashbackPercent);

		String addcashback = data.get("addcashback");
		logger.info("addcashback : " + addcashback);
		
		String baseMortgageType = data.get("baseMortgageType");
		logger.info("baseMortgageType : " + baseMortgageType);
		
		String addMortgageType = data.get("addMortgageType");
		logger.info("addMortgageType : " + addMortgageType);
		
		String baseproductname = data.get("baseproductname");
		logger.info("baseproductname : " + baseproductname);
		
		String addproductname = data.get("addproductname");
		logger.info("addproductname : " + addproductname);
		
		String baseterm = data.get("baseterm");
		logger.info("baseterm : " + baseterm);
		
		String addterm = data.get("addterm");
		logger.info("addterm : " + addterm);
		
		String baseamortization = data.get("baseamortization");
		logger.info("baseamortization : " + baseamortization);
		
		
		String addamortization = data.get("addamortization");
		logger.info("addamortization : " + addamortization);

		String basemortgageAmount = data.get("basemortgageAmount");
		logger.info("basemortgageAmount : " + basemortgageAmount);
		
		String addmortgageAmount = data.get("addmortgageAmount");
		logger.info("addmortgageAmount : " + addmortgageAmount);
		String basemonthlypayment = data.get("basemonthlypayment");
		logger.info("basemonthlypayment : " + basemonthlypayment);
		
		String addmonthlypayment = data.get("addmonthlypayment");
		logger.info("addmonthlypayment : " + addmonthlypayment);
		
		
		String baseinterestrate = data.get("baseinterestrate");
		logger.info("baseinterestrate : " + baseinterestrate);
		
		String addinterestrate = data.get("addinterestrate");
		logger.info("addinterestrate : " + addinterestrate);
		
		String totalPayment = data.get("totalPayment");
		logger.info("totalPayment : " + totalPayment);
		
		/*String intiialforapproval = data.get("intiialforapproval");
		logger.info("intiialforapproval : " + intiialforapproval);
*/
		CombinedRecommendation rp = null;
		UwAppAllAlgo Uw = null;

		ObjectMapper ob = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;
		
		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		try {
			cbdata = new CouchbaseData();

			logger.debug("in try  catch for getting Connetion To couchbase : ");
			couchOp = new CouchBaseOperation();
			CouchbaseClient cb = couchOp.getConnectionToCouchBase();

			Uw = cbdata.getDataFromCouchbase(opp_id);
			logger.info("Get Algo Recommendation : " + Uw.getRecommendations());

		} catch (Exception e) {
			logger.error("error", e);
		}

		Set<CombinedRecommendation> combinerecomendtionList = Uw.getCombinedRecommendation();

		for (Iterator iterator = combinerecomendtionList.iterator(); iterator
				.hasNext();)

		{

			CombinedRecommendation comrecommendation = (CombinedRecommendation) iterator.next();

			logger.info("before recomendtionList.size() :"
					+ combinerecomendtionList.size());
			
			if (comrecommendation.getBaseProductID().equals(productId)&&(comrecommendation.getAdditionalProductID().equals(addproductId))) {

				logger.info("combine_recommendation.getBaseProductID():"
						+ comrecommendation.getBaseProductID() + "  combine_recommendation.getAdditionalProductID(): "+comrecommendation.getAdditionalProductID());
				Uw.getCombinedRecommendation().remove(comrecommendation);
				break;
				
			}
			logger.info(" after recomendtionList.size() :"
					+ combinerecomendtionList.size());

		}

		logger.info(" after 333 recomendtionList.size() :"
				+ combinerecomendtionList.size());

		rp = new CombinedRecommendation();
		
	
		rp.setBaseProductID(productId);
		rp.setAdditionalProductID(addproductId);
		
		rp.setBaseLender(baselender);
		rp.setAdditionalLender(addlender);
		
		rp.setTotalMortgageAmount(totalMortgageAmount);
		
		rp.setBasePosition(basePosition);
		rp.setAdditionalPosition(additionalPosition);
		
		rp.setBaseCashbackPercent(baseCashbackPercent);
		rp.setAdditionalCashbackPercent(addcashback);
		
		
		rp.setBaseMortgageType(baseMortgageType);
		rp.setAdditionalMortgageType(addMortgageType);
		
		rp.setBaseProduct(baseproductname);
		rp.setAdditionalProduct(addproductname);
		
		rp.setBaseTerm(baseterm);
		rp.setAdditionalTerm(addterm);
		
		rp.setBaseAmortization(baseamortization);
		rp.setAdditionalAmortization(addamortization);
		
		rp.setBaseMortgageAmount(basemortgageAmount);
		rp.setAdditionalMortgageAmount(addmortgageAmount);
		
		rp.setBasePayment(basemonthlypayment);
		rp.setAdditionalPayment(addmonthlypayment);
		
		rp.setBaseInterestRate(baseinterestrate);
		rp.setAdditionalInterestRate(addinterestrate);
		
		
		rp.setTotalPayment(totalPayment);
		Uw.getCombinedRecommendation().add(rp);

		try {

			System.out.println(" getCombineRecommendations : "
					+ ob.writeValueAsString(Uw));

		} catch (Exception e) {

		}
		try {

			JSONObject js = new JSONObject(ob.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"
					+ opp_id, js);
		} catch (Exception e) {
			logger.error("error"+e.getMessage());

		}

		
		String  companyName="";
		//TestDevCRM test = new TestDevCRM();
		
	/*	try{
			companyName = test.getCompanyName(opp_id);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}
		*/
		try{
			companyName = HttpsConnectionCase.getCompanyName(opp_id);
		}catch(Exception e){
			logger.error("error in get company name"+e.getMessage());
		}
		
		
		
		
		
		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		
		ArrayList<CombinedRecommendation> combineRecommended =null;
		Set<MarketingNotes> morketNotes = null;
		RecommendDetails recdetials = null;
		OrignalDetails detials = null;

		try {
			
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			 combineRecommended = new ArrayList<>();
			morketNotes = Uw.getMarketingNotes();
			
			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
			
			listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
			logger.info("------------listofMax--------------"+listofmax);
			
			Set<CombinedRecommendation> combine =  Uw.getCombinedRecommendation();
			combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
			logger.info("------combineRecommended size()-------------"+combineRecommended.size());

			
				
			
			Set<Recommendation> setRec = Uw.getRecommendations();
			logger.debug("set REc " + setRec.size());

			for (Recommendation recommendation : setRec) {

				logger.debug("recommendation : " + recommendation);

				if (recommendation.getMortgageType().equalsIgnoreCase("2")) {
					logger.debug("array Variable before  : "
							+ arrayVariable.size());
					String term =recommendation.getTerm();
					String sterm =setTerm2(term);
					recommendation.setTerm(sterm);
					arrayVariable.add(recommendation);
					
					Collections.sort(arrayVariable, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					logger.debug("array Variable after  : " + arrayVariable.size());
					

				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"3")) {
					String term =recommendation.getTerm();
					String sterm =setTerm2(term);
					recommendation.setTerm(sterm);
					logger.debug("array fixed before 3  : " + arrayFixed.size());

					arrayFixed.add(recommendation);
					
					Collections.sort(arrayFixed, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					//logger.debug("array fixed after 3  : " + arrayFixed.size());

					
				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"1")) {
					//logger.debug("array array before LOC : " + arrayLOC.size());

					arrayLOC.add(recommendation);
				//	logger.debug("array array after LOC : " + arrayLOC.size());
					

				}
			}
		} catch (Exception e) {
			logger.error("while in editPost() for json data get  occured", e);

		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		
		
		
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

	
	} 
	public Result editExpDate(){
		
		
		
		String id=request().getQueryString("id");

		logger.debug("Id  is"+id);
		
		
		return ok(editExpiryDate.render(id));

	
	}
	
	public  Result saveExpDate(){
		
		CouchbaseData cbdata = new CouchbaseData();

		String oppertunityId = null;
		DynamicForm data = form().bindFromRequest();
		
		String  exp=data.get("exp_date");
		
		
		oppertunityId=data.get("opp_id");
		logger.debug("save oppertunityId :"+oppertunityId);
		logger.debug("New  Exp  Date  is : "+exp);
		
		String  companyName="";
		/*TestDevCRM test = new TestDevCRM();
		
		try{
			companyName = test.getCompanyName(oppertunityId);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
		try{
			companyName=	HttpsConnectionCase.getCompanyName(oppertunityId);
			
					//companyName = test.getCompanyName(oppertunityId);
					logger.info("company Name : "+companyName);
					
					
				}
				
				catch(Exception  e){
					//System.out.println("while getting the company name from odoo : " +e);
					logger.error("while getting the company name from odoo : " +e);
					
				}
		
		
		String  expiryDate=cbdata.getDataCouchbase(oppertunityId);
		
		
		JSONObject  json=null;

		
		String  expirayTime=null;
				try{
		   json=new JSONObject(expiryDate);
		   
		   
           json.put("ExpiryDateTime",exp);
           
           cbdata.UpdateDataCouchbase(oppertunityId,json.toString());
           
				}catch(JSONException e){
					logger.error("Expiray  Date  Type Not  Found");
					
				}
				
				CouchbaseData data2 = new CouchbaseData();

				//String oppertunityId = request().getQueryString("opp_id");

				System.out.println("oppertunityId :" + oppertunityId);

				String clientName = request().getQueryString("action");
				System.out.println("clientName :" + clientName);

				UwAppAllAlgo algo = data2.getDataFromCouchbase(oppertunityId);

				
				OrignalDetails detials = new OrignalDetails();

				RecommendDetails recdetials = new RecommendDetails();
				ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

			
				ArrayList<Recommendation> arrayVariable = new ArrayList<Recommendation>();
				ArrayList<Recommendation> arrayFixed = new ArrayList<Recommendation>();
				ArrayList<Recommendation> arrayLOC = new ArrayList<Recommendation>();
				 ArrayList<CombinedRecommendation> combineRecommended=new ArrayList<>();
				Set<MarketingNotes> morketNotes = algo.getMarketingNotes();
				
				for (MarketingNotes marketingNotes : morketNotes) {
					if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
						detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
						
						
					} else if (marketingNotes.getNoteName().equalsIgnoreCase(
							"RecommendDetails")) {
					recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
					}
				}
				
				listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
				logger.info("------------listofMax--------------"+listofmax);
				
				Set<CombinedRecommendation> combine =  algo.getCombinedRecommendation();
				combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
				logger.info("------combineRecommended size()-------------"+combineRecommended.size());

				
				
				
					

				Set<Recommendation> setRec = algo.getRecommendations();
				for (Recommendation recommendation : setRec) {

					if (recommendation.getMortgageType().equalsIgnoreCase("2")) {
						arrayVariable.add(recommendation);
						String term =recommendation.getTerm();
						String sterm =setTerm2(term);
						recommendation.setTerm(sterm);
						Collections.sort(arrayVariable, new RecommendationChainComparater(
				                new RecommendationTermComparator()
				                ));
					} else if (recommendation.getMortgageType().equalsIgnoreCase("3")) {
					
						String term =recommendation.getTerm();
						String sterm =setTerm2(term);
						recommendation.setTerm(sterm);
						arrayFixed.add(recommendation);
						
						Collections.sort(arrayFixed, new RecommendationChainComparater(
				                new RecommendationTermComparator()
				                ));
				
					} else if (recommendation.getMortgageType().equalsIgnoreCase("1")) {
						arrayLOC.add(recommendation);

					}
				}
				int i=0;
				if(listofmax.size()!=0){
					i=1;
				}
			
				
			
		return ok(indexbroker.render(algo, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,exp,companyName));
		
	}
	public Result editmaxpur(){
//MODIFY THIS

		String opp_id = request().getQueryString("opp_id");
		logger.info("opp_id : " + opp_id);

		String debtReduction = request().getQueryString("debetReduct");
		logger.info("debtReduction : " + debtReduction);

		String largestDownpaymentHouse = request().getQueryString("largestDownPayment");
		logger.info("largestDownpaymentHouse : " + largestDownpaymentHouse);

		String maximumMortgageNoCondo = request().getQueryString("maximumMortgageNoCondo");
		logger.info("maximumMortgageNoCondo : " + maximumMortgageNoCondo);

		String insureAmountMaxMortgage = request().getQueryString("insuranceAmount");
		logger.info("insureAmountMaxMortgage : " + insureAmountMaxMortgage);

		String variableMortHouseNoDebtRepayValue = request().getQueryString("variablemortage");
		logger.info("variableMortHouseNoDebtRepayValue : " + variableMortHouseNoDebtRepayValue);

	
		MaximumPurchase rc = new MaximumPurchase();
		rc.setDebtReduction(debtReduction);
		rc.setLargestDownpaymentHouse(largestDownpaymentHouse);
		rc.setMaximumMortgageNoCondo(maximumMortgageNoCondo);
		rc.setInsureAmountMaxMortgage(insureAmountMaxMortgage);
		rc.setVariableMortHouseNoDebtRepayValue(variableMortHouseNoDebtRepayValue);
		
		
	

		
		return ok(maximunedit.render(rc, opp_id));

	
	}

	public Result UpdateProduct() {

		DynamicForm data = form().bindFromRequest();
		String productid[] = null;
		try {
			productid = request().body().asFormUrlEncoded().get("productid");
			logger.info("Size Of  ProductID  is" + productid.length);

		} catch (Exception e) {
			logger.error("product should be null ", e);
		}

		String agreed[] = null;
		try {
			agreed = request().body().asFormUrlEncoded().get("product_id");
			logger.info("Size Of  ProductID  is" + agreed.length);

		} catch (Exception e) {
			logger.error("error", e);
		}

		for (int i = 0; i < agreed.length; i++) {

			logger.info("product id array " + productid[i]);
			try {
				logger.info("Inital  Value is" + "\t" + agreed[i]);

			} catch (Exception e) {
				logger.error("error", e);
			}
			if (!agreed[i].isEmpty()) {
				logger.debug("*******inside  Agreed **********");

				logger.info("agreed_product id" + productid[i]);
				String oppId = data.get("id");

				logger.info("Goint  to  update  openERP  Product");

				// int produc_updateid = Integer.parseInt(productid[i]);
				try {
					HttpsConnectionCase.updateProduct(oppId, productid[i]);
					
									} catch (Exception e) {
					logger.error("error", e);

				}
			}
		}

		/*
		 * String opportunityId=data.get("id"); String
		 * productid=data.get("product_id");
		 */
		logger.info("Data  value  is" + data.get("id"));

		return ok("Updated");

	}

	public Result submitandSave() {
		logger.debug("inside submitandSave()  ");

		DynamicForm data = form().bindFromRequest();
		String productid[] = null;
		try {
			productid = request().body().asFormUrlEncoded().get("productid");

			logger.info("Size Of  ProductID  is" + productid.length);
		} catch (Exception e) {
			logger.error("error", e);
		}

		String agreed[] = null;
		try {
			agreed = request().body().asFormUrlEncoded().get("product_id");
			logger.info("Size Of  ProductID  is" + agreed.length);

		} catch (Exception e) {
			logger.error("error", e);
		}

		for (int i = 0; i < agreed.length; i++) {
			logger.info("product id" + productid[i]);

			try {
				logger.debug("Inital  Value is" + "\t" + agreed[i]);

			} catch (Exception e) {
				logger.error("error", e);
			}
			if (!agreed[i].isEmpty()) {
				logger.debug("*******inside  Agreed **********");
				logger.info("agreed_product id" + productid[i]);

				String oppId = data.get("id");
				logger.info("Goint  to  update  openERP  Product");

				// int produc_updateid = Integer.parseInt(productid[i]);
				try {
					// HttpsConnectionCase.updateProduct(oppId, productid[i]);
				} catch (Exception e) {
					logger.error("error", e);

				}
			}
		}

		/*
		 * String opportunityId=data.get("id"); String
		 * productid=data.get("product_id");
		 */

		logger.info("Data  value  is" + data.get("id"));

		return ok("Updated");

		// return ok();

	}

	public Result deleteProductById() {

		logger.debug("++  inside deleteProductById()++");
		String productId = request().getQueryString("productId");
		String opp_id = request().getQueryString("opp_id");
		logger.info("product ID : " + productId);

		Recommendation rp = null;
		UwAppAllAlgo Uw = null;

		ObjectMapper ob = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;
		
		

		try {
			cbdata = new CouchbaseData();
			logger.debug("get connection in CouchBase :");

			couchOp = new CouchBaseOperation();
			CouchbaseClient cb = couchOp.getConnectionToCouchBase();

			Uw = cbdata.getDataFromCouchbase(opp_id);
			// JSONObject js
			// =couchOp.getCouchBaseData("uwapps2couchbase_allProductAlgoJSON_"+opp_id);
			logger.debug("get the Recommendation List : "
					+ Uw.getRecommendations());

		} catch (Exception e) {
			logger.error("error", e);
		}

		try {

			Set<Recommendation> recomendtionList = Uw.getRecommendations();

			for (Iterator<Recommendation> iterator = recomendtionList
					.iterator(); iterator.hasNext();)

			{

				Recommendation recommendation = (Recommendation) iterator
						.next();
				logger.debug(" before recomendtionList.size() :"
						+ recomendtionList.size());

				if (recommendation.getProductID().equals(productId)) {
					logger.info("recommendation.getProductID():"
							+ recommendation.getProductID());
					Uw.getRecommendations().remove(recommendation);
					break;
				}
				logger.debug(" After recomendtionList.size() :"
						+ recomendtionList.size());

			}

		} catch (Exception e) {
			logger.error("error", e);
		}

		// Uw.getRecommendations().add(rp);

		try {
			System.out.println(" getRecommendations : "
					+ ob.writeValueAsString(Uw));

		} catch (Exception e) {

		}
		try {

			JSONObject js = new JSONObject(ob.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"
					+ opp_id, js);
		} catch (Exception e) {
			logger.error("error", e);

		}

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		Set<MarketingNotes> morketNotes = null;
		RecommendDetails recdetials = null;
		 ArrayList<CombinedRecommendation> combineRecommended=null;
		OrignalDetails detials = null;

		try {
			JSONObject jsonObj = null;
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			ObjectMapper mappeer = new ObjectMapper();
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			combineRecommended=new ArrayList<>();
			morketNotes = Uw.getMarketingNotes();
			
			
			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
			
			listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
			logger.info("------------listofMax--------------"+listofmax);
			
			Set<CombinedRecommendation> combine =  Uw.getCombinedRecommendation();
			combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
			logger.info("------combineRecommended size()-------------"+combineRecommended.size());

			
			
			
				
				
			Set<Recommendation> setRec = Uw.getRecommendations();

			System.out.println("set REc " + setRec.size());
			for (Recommendation recommendation : setRec) {

				//System.out.println("recommendation : " + recommendation);

				if (recommendation.getMortgageType().equalsIgnoreCase("2")) {
					String term =recommendation.getTerm();
					String sterm =setTerm2(term);
					recommendation.setTerm(sterm);
					arrayVariable.add(recommendation);
					
					Collections.sort(arrayVariable, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));
					

				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"3")) {
					String term =recommendation.getTerm();
					String sterm =setTerm2(term);
					recommendation.setTerm(sterm);
					//System.out.println("array fixed : " + arrayFixed.size());
					arrayFixed.add(recommendation);
					Collections.sort(arrayFixed, new RecommendationChainComparater(
			                new RecommendationTermComparator()
			                ));

					System.out.println("array fixed : " + arrayFixed.size());
					
				} else if (recommendation.getMortgageType().equalsIgnoreCase(
						"1")) {
					//System.out.println("array fixed : " + arrayLOC.size());
					arrayLOC.add(recommendation);
					//System.out.println("array fixed : " + arrayLOC.size());

				}
			}
		} catch (Exception e) {

		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		String  companyName="";
		/*TestDevCRM test = new TestDevCRM();
		
		try{
			companyName = test.getCompanyName(opp_id);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
		
		try{
			companyName = HttpsConnectionCase.getCompanyName(opp_id);
		}catch(Exception e){
			logger.error("error while get company name"+e.getMessage());
		}
	
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

		// return ok(update.render());
	}

	public Result deleteById(){

		logger.debug("++  inside delete combine ProductById()++");
		String productId = request().getQueryString("productId");
		String opp_id = request().getQueryString("opp_id");
		
		logger.info("product ID : " + productId);
		logger.info("opp_id"+opp_id);
		
		CombinedRecommendation combine =null;
		UwAppAllAlgo Uw = null;
		

		ObjectMapper ob = new ObjectMapper();
		CouchBaseOperation couchOp = null;
		CouchbaseData cbdata = null;

		try {
			cbdata = new CouchbaseData();
			logger.debug("get connection in CouchBase :");

			couchOp = new CouchBaseOperation();
			CouchbaseClient cb = couchOp.getConnectionToCouchBase();

			Uw = cbdata.getDataFromCouchbase(opp_id);
			// JSONObject js
			// =couchOp.getCouchBaseData("uwapps2couchbase_allProductAlgoJSON_"+opp_id);
			logger.debug("get the CombineRecommendation List : "
					+ Uw.getCombinedRecommendation());

		} catch (Exception e) {
			logger.error("error", e);
		}

		try {

			Set<CombinedRecommendation> combinerecomendtionList = Uw.getCombinedRecommendation();
			
			logger.debug("combinerecomendtionList : "+combinerecomendtionList);

			for (Iterator<CombinedRecommendation> iterator = combinerecomendtionList
					.iterator(); iterator.hasNext();)

			{

				CombinedRecommendation recommendation = (CombinedRecommendation) iterator
						.next();
				logger.debug(" before CombinedRecommendation.size() :"
						+ combinerecomendtionList.size());

				if (recommendation.getBaseProductID().equals(productId)) {
					logger.info("recommendation.getProductID():"
							+ recommendation.getBaseProductID());
					Uw.getCombinedRecommendation().remove(recommendation);
					break;
				}
				logger.debug(" After recomendtionList.size() :"
						+ combinerecomendtionList.size());

			}

		} catch (Exception e) {
			logger.error("error", e);
		}

		// Uw.getRecommendations().add(rp);

		try {
			System.out.println(" getCombineRecommendations : "
					+ ob.writeValueAsString(Uw));

		} catch (Exception e) {

		}
		try {

			JSONObject js = new JSONObject(ob.writeValueAsString(Uw));
			couchOp.storeDataInCouchBase("uwapps2couchbase_allProductAlgoJSON_"
					+ opp_id, js);
		} catch (Exception e) {
			logger.error("error", e);

		}

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		ArrayList<Recommendation> arrayVariable = null;
		ArrayList<Recommendation> arrayFixed = null;
		ArrayList<Recommendation> arrayLOC = null;
		ArrayList<CombinedRecommendation> combineRecommended =null;
		Set<MarketingNotes> morketNotes = null;
		RecommendDetails recdetials = null;
		OrignalDetails detials = null;

		try {
			
			detials = new OrignalDetails();

			recdetials = new RecommendDetails();

			
			arrayVariable = new ArrayList<Recommendation>();
			arrayFixed = new ArrayList<Recommendation>();
			arrayLOC = new ArrayList<Recommendation>();
			combineRecommended = new ArrayList<CombinedRecommendation>();
			
			morketNotes = Uw.getMarketingNotes();
			
			for (MarketingNotes marketingNotes : morketNotes) {
				if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
					detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
					
					
				} else if (marketingNotes.getNoteName().equalsIgnoreCase(
						"RecommendDetails")) {
				recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
				}
			}
			
			listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
			logger.info("------------listofMax--------------"+listofmax);
			
			Set<CombinedRecommendation> combine1 =  Uw.getCombinedRecommendation();
			combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine1);
			logger.info("------combineRecommended size()-------------"+combineRecommended.size());

		
			
				
			Set<Recommendation> setRec = Uw.getRecommendations();
			System.out.println("set REc " + setRec.size());
			
			
			Map<String,List<Recommendation>> mapofrec=	RecommedationOperation.getRecList(setRec);
			
			logger.info("---------------map -------------"+mapofrec.size());
			
			
			arrayLOC	=(ArrayList<Recommendation>) mapofrec.get("arrayLoc");
			logger.info("-------------arrayLoc-----------"+arrayLOC.size());
			arrayFixed	=(ArrayList<Recommendation>) mapofrec.get("arrayFixed");
			logger.info("-------------arrayFixed-----------"+arrayFixed.size());
			arrayVariable	=(ArrayList<Recommendation>) mapofrec.get("arrayVariable");
			logger.info("-------------arrayVariable-----------"+arrayVariable.size());
				
			
		} catch (NullPointerException e) {
logger.error("-null occured in uw app ---"+e.getMessage());
		}
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		String  companyName="";
		//TestDevCRM test = new TestDevCRM();
		/*
		try{
			companyName = test.getCompanyName(opp_id);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/

		try{
			companyName = HttpsConnectionCase.getCompanyName(opp_id);
			logger.info("companyName"+companyName);
		}catch(Exception e){
			logger.error("error while get company name"+e.getMessage());
		}
		
		logger.info("companyName in client : "+companyName);
		return ok(index.render(Uw, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,companyName));

		// return ok(update.render());
	}
	
	public  Result expiryData(){
	
	
	
	
	return ok("Please  Check  Expiry  Date");
	
}
	public Result indexclient() {

		CouchbaseData data = new CouchbaseData();

		String oppertunityId = request().getQueryString("opp_id");

		System.out.println("oppertunityId :" + oppertunityId);

		String clientName = request().getQueryString("action");
		System.out.println("clientName :" + clientName);

		UwAppAllAlgo algo = data.getDataFromCouchbase(oppertunityId);

		
		OrignalDetails detials = new OrignalDetails();
		
		String  companyName="";
		/*TestDevCRM test = new TestDevCRM();
		 
		try{
			
			companyName = test.getCompanyName(oppertunityId);
		}catch(Exception e){
			logger.error("inside indexcleint() "+e.getMessage()); 
		}
		*/
		
		logger.info("companyName Client : "+companyName);
		
		try{
			
			
			
	companyName=	HttpsConnectionCase.getCompanyName(oppertunityId);
	
		
			logger.info("company Name : "+companyName);
			
			
		}
		
		catch(Exception  e){
			System.out.println("while getting the company name from odoo : " +e);
			logger.error("while getting the company name from odoo : " +e);
			
		}

	RecommendDetails recdetials = new RecommendDetails();

		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		ObjectMapper mappeer = new ObjectMapper();
		ArrayList<Recommendation> arrayVariable = new ArrayList<Recommendation>();
		ArrayList<Recommendation> arrayFixed = new ArrayList<Recommendation>();
		ArrayList<Recommendation> arrayLOC = new ArrayList<Recommendation>();
		/*change here*/
		ArrayList<CombinedRecommendation> combineRecommended= new ArrayList<>();
		
		Set<MarketingNotes> morketNotes = algo.getMarketingNotes();
		
		for (MarketingNotes marketingNotes : morketNotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
				detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
				
				
			} else if (marketingNotes.getNoteName().equalsIgnoreCase(
					"RecommendDetails")) {
			recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
			}
		}
		
		listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
		logger.info("------------listofMax--------------"+listofmax);
		
		Set<CombinedRecommendation> combine =  algo.getCombinedRecommendation();
		combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
		logger.info("------combineRecommended size()-------------"+combineRecommended.size());

		
		
		
		

		Set<Recommendation> setRec = algo.getRecommendations();
	
		
		Map<String,List<Recommendation>> mapofrec=	RecommedationOperation.getRecList(setRec);
		
		logger.info("---------------map -------------"+mapofrec.size());
		
		
		try{
			arrayLOC	=(ArrayList<Recommendation>) mapofrec.get("arrayLoc");
			logger.info("-------------arrayLoc-----------"+arrayLOC.size());
			}catch( NullPointerException e){logger.error("null "+e.getMessage());}
		arrayFixed	=(ArrayList<Recommendation>) mapofrec.get("arrayFixed");
		logger.info("-------------arrayFixed-----------"+arrayFixed.size());
		arrayVariable	=(ArrayList<Recommendation>) mapofrec.get("arrayVariable");
		logger.info("-------------arrayVariable-----------"+arrayVariable.size());
		
		
		
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		String  expiryDate=data.getDataCouchbase(oppertunityId);
		logger.debug("Form  Couchbase"+expiryDate);
		JSONObject  json=null;
		String  expirayTime=null;
				try{
          json=new JSONObject(expiryDate);
            expirayTime=json.get("ExpiryDateTime").toString();
            logger.debug("ExpiryTime  is"+expirayTime);
		}catch (JSONException e) {
logger.error("Error  in  getting  Expiry  date"+e);		}
				
				
		return ok(indexclient.render(algo, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,expirayTime,companyName));

	}

	public Result indexbroker(String action,String opp_id) {

		logger.info("LLLLLLLLLLiLLLLLLLLLLLLLLLL INSIDE  BROkER LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
		CouchbaseData data = new CouchbaseData();

		
		String 	oppertunityId = opp_id;
		logger.info("inside B opportunity  "+oppertunityId);
		
		String  clientName = action;
		
		logger.info("client name inside broker {} : "+clientName);
		
		String  companyName="";
		
		TestDevCRM test = new TestDevCRM();
		
		/*try{
			companyName = test.getCompanyName(oppertunityId);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}
*/
		try{
			companyName=	HttpsConnectionCase.getCompanyName(oppertunityId);
			
					//companyName = test.getCompanyName(oppertunityId);
			logger.debug("company Name : "+companyName);
					
					
				}
				
				catch(Exception  e){
					//System.out.println("while getting the company name from odoo : " +e);
					logger.error("while getting the company name from odoo : " +e);
					
				}
		
		
		//companyName="WFG";
		UwAppAllAlgo algo = data.getDataFromCouchbase(oppertunityId);

		
		OrignalDetails detials = new OrignalDetails();
	

		RecommendDetails recdetials = new RecommendDetails();
		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		
		ArrayList<Recommendation> arrayVariable = new ArrayList<Recommendation>();
		ArrayList<Recommendation> arrayFixed = new ArrayList<Recommendation>();
		ArrayList<Recommendation> arrayLOC = new ArrayList<Recommendation>();
		
		ArrayList<CombinedRecommendation> combineRecommended= new ArrayList<>();
		
		Set<MarketingNotes> morketNotes = algo.getMarketingNotes();
		
		
		
		
		for (MarketingNotes marketingNotes : morketNotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
				detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
				
				
			} else if (marketingNotes.getNoteName().equalsIgnoreCase(
					"RecommendDetails")) {
			recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
			recdetials.getTotalMortgageAmount();
			}
		}
		
		listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
		logger.info("------------listofMax--------------"+listofmax);
		
		Set<CombinedRecommendation> combine =  algo.getCombinedRecommendation();
		combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
		logger.info("------combineRecommended size()-------------"+combineRecommended.size());

			Set<Recommendation> setRec = algo.getRecommendations();
			
			
			Map<String,List<Recommendation>> mapofrec=	RecommedationOperation.getRecList(setRec);
			
			logger.info("---------------map -------------"+mapofrec.size());
			
			
			try{
				arrayLOC	=(ArrayList<Recommendation>) mapofrec.get("arrayLoc");
				logger.info("-------------arrayLoc-----------"+arrayLOC.size());
				}catch( NullPointerException e){logger.error("null "+e.getMessage());}
			arrayFixed	=(ArrayList<Recommendation>) mapofrec.get("arrayFixed");
			logger.info("-------------arrayFixed-----------"+arrayFixed.size());
			arrayVariable	=(ArrayList<Recommendation>) mapofrec.get("arrayVariable");
			logger.info("-------------arrayVariable-----------"+arrayVariable.size());
			
			

		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}
		
		
		String  expiryDate=data.getDataCouchbase(oppertunityId);
		logger.debug("Form  Couchbase"+expiryDate);
		JSONObject  json=null;
		String  expirayTime=null;
				try{
					json=new JSONObject(expiryDate);
					expirayTime=json.get("ExpiryDateTime").toString();
					logger.debug("ExpiryTime  is"+expirayTime);
				}catch (JSONException e) {
						logger.error("Error  in  getting  Expiry  date"+e.getMessage());		
						}
		
			
			
		return ok(indexbroker.render(algo, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,expirayTime,companyName));

	}

	public Result indexreferal() {

		CouchbaseData data = new CouchbaseData();

		String oppertunityId = request().getQueryString("opp_id");

		System.out.println("oppertunityId :" + oppertunityId);

		String clientName = request().getQueryString("action");
		System.out.println("clientName :" + clientName);

		UwAppAllAlgo algo = data.getDataFromCouchbase(oppertunityId);

		
		OrignalDetails detials = new OrignalDetails();
		
		String  companyName="";
		TestDevCRM test = new TestDevCRM();
		/*
		try{
			companyName = test.getCompanyName(oppertunityId);
			logger.info("company Name : "+companyName);
		}
		
		catch(Exception  e){
			logger.error("while getting the company name from odoo : " +e);
			
		}*/
		
		
		try{
			companyName=	HttpsConnectionCase.getCompanyName(oppertunityId);
			
					//companyName = test.getCompanyName(oppertunityId);
					logger.info("company Name : "+companyName);
					
					
				}
				
				catch(Exception  e){
					System.out.println("while getting the company name from odoo : " +e);
					logger.error("while getting the company name from odoo : " +e.getMessage());
					
				}

		RecommendDetails recdetials = new RecommendDetails();
		ArrayList<MaximumPurchase> listofmax = new ArrayList<MaximumPurchase>();

		
		ArrayList<Recommendation> arrayVariable = new ArrayList<Recommendation>();
		ArrayList<Recommendation> arrayFixed = new ArrayList<Recommendation>();
		ArrayList<Recommendation> arrayLOC = new ArrayList<Recommendation>();
		
		ArrayList<CombinedRecommendation> combineRecommended= new ArrayList<>();
		Set<MarketingNotes> morketNotes = algo.getMarketingNotes();
		
		
		
		
		for (MarketingNotes marketingNotes : morketNotes) {
			if (marketingNotes.getNoteName().equalsIgnoreCase("OriginalDetails")) {
				detials= JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
				
				
			} else if (marketingNotes.getNoteName().equalsIgnoreCase(
					"RecommendDetails")) {
			recdetials =JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
			}
		}
		
		listofmax=	MarketingNotesOperation.getListofMax(morketNotes);
		logger.info("------------listofMax--------------"+listofmax);
		
		Set<CombinedRecommendation> combine =  algo.getCombinedRecommendation();
		Logger.info("combine size : "+combine.size());
		combineRecommended= MarketingNotesOperation.getlistofCombinedRecommendation(combine);
		logger.info("------combineRecommended size()-------------"+combineRecommended.size());

		
		
		

		Set<Recommendation> setRec = algo.getRecommendations();

		Map<String,List<Recommendation>> mapofrec=	RecommedationOperation.getRecList(setRec);
		
		logger.info("---------------map -------------"+mapofrec.size());
		
		
		try{
			arrayLOC	=(ArrayList<Recommendation>) mapofrec.get("arrayLoc");
			logger.info("-------------arrayLoc-----------"+arrayLOC.size());
			}catch( NullPointerException e){logger.error("null "+e.getMessage());}
		arrayFixed	=(ArrayList<Recommendation>) mapofrec.get("arrayFixed");
		logger.info("-------------arrayFixed-----------"+arrayFixed.size());
		arrayVariable	=(ArrayList<Recommendation>) mapofrec.get("arrayVariable");
		logger.info("-------------arrayVariable-----------"+arrayVariable.size());
			
	
		
		
		

	
		int i=0;
		if(listofmax.size()!=0){
			i=1;
		}

		String  expiryDate=data.getDataCouchbase(oppertunityId);
		logger.debug("Form  Couchbase"+expiryDate);
		JSONObject  json=null;
		String  expirayTime=null;
				try{
          json=new JSONObject(expiryDate);
            expirayTime=json.get("ExpiryDateTime").toString();
            logger.debug("ExpiryTime  is"+expirayTime);
		}catch (JSONException e) {
logger.error("Error  in  getting  Expiry  date"+e);		}
		
				
		return ok(indexreferal.render(algo, detials, arrayVariable, arrayFixed,
				combineRecommended, recdetials, listofmax,i,expirayTime,companyName));

	}

	public Result insertProduct() {
		TestDevCRM devcrm = new TestDevCRM();
		StageLead lead = new StageLead();
		String message ="Thank YOU !";
		String crmdata=null;
		String ipaddress = request().remoteAddress();
		

		DynamicForm dynamicForm = form().bindFromRequest();
		String oppertunityId=dynamicForm.get("id");
		
		logger.info("-----------oppId-----------"+oppertunityId);
		

		String product1 = dynamicForm.get("product_id");
		

		
		logger.info("-----------product1-----------"+product1);
		
		
		CouchbaseData data = new CouchbaseData();

		UwAppAllAlgo algo = data.getDataFromCouchbase(oppertunityId);
		

		OrignalDetails detials = new OrignalDetails();
		RecommendDetails recdetials = new RecommendDetails();
		Set<MarketingNotes> morketNotes=null;
		try{
			
			morketNotes = algo.getMarketingNotes();
			logger.debug("------------morketNotes------------"+morketNotes.toString());
		}catch(NullPointerException e){
			logger.error("MarketingNotes  Not  found"+e);
			
		}
		
		
		logger.info("inside getJson(");
	
		try {
			crmdata  =lead.getcrmLeadFrompostgress("370");
		} catch (SQLException e1) {
			logger.info("SQLException "+e1.getMessage());
		} catch (IOException e1) {
			logger.info(" IO Exception"+e1.getMessage());
		}
		RestCall restcall= null;
		JSONObject json  = null;
		JSONObject json1  = new JSONObject();
		
		/*try{
	json=JsonConvertion.getJsonObject(crmdata);
	//logger.info("--------------json-------------------"+json);
	logger.info("id _-----------------"+json.get("id"));
	json1.put("id",json.get("id").toString());
	json1.put("stage_id",json.get("stage_id").toString());
	json1.put("selected_product",json.get("selected_product").toString());
	
	logger.info("before json -----------------"+json1.toString());
	//String forrest=json1.toString();
	logger.info("before restcall -----------------");
	
		}catch(JSONException e){
			logger.error("json ex"+e.getMessage());
		}catch(NullPointerException e){
			logger.error("json ex"+e.getMessage());
		}
*/
		
		restcall = new RestCall(crmdata);
		restcall.restcallTostagMail(crmdata);
		//RestCall.restcallTostagMail(json1.toString());

		String proertyvalue = null;
		String downpayment = null;

		
		Set<Recommendation> setRec = algo.getRecommendations();
		Set<CombinedRecommendation> combineRec = algo.getCombinedRecommendation();

		String productid[] = null;
		String pfor[] = null;
		productid = request().body().asFormUrlEncoded().get("productid");
		 pfor = request().body().asFormUrlEncoded().get("productfor");
		logger.info("---"+pfor.length);
		 
		 
		 logger.info("Size Of  ProductID  is" + productid.length);

		String agreed[] = null;
		agreed = request().body().asFormUrlEncoded().get("product_id");
		
		System.out.println("Size Of  aggredd id   is" + agreed.length);

		for (int i = 0; i < agreed.length; i++) {
			
			if (!agreed[i].isEmpty()) {
				
				logger.info("========================agreed_product mine id=========== "+ productid[i]);
				String pfor1=pfor[i];
				//logger.info("-----------------------pfor1--------------------------"+pfor1);
				System.out.println("pfor id      "+pfor[i] );
				
				try {
					
					//TestDevCRM.updateProduct(oppertunityId, productid[i]);
					HttpsConnectionCase.updateProduct(oppertunityId, productid[i]);
					
									} catch (Exception e) {
					logger.error("error  in  Updating  Product"+e.getMessage());

				}
				for (MarketingNotes marketingNotes : morketNotes) {
					
					
					
					
					logger.info("========================inside ------marketingNotes=========== "+ productid[i]);
					
					//Thread.sleep(20);
					
					//Chnage  stage  to PostSlection
					

					logger.info("Going  To  update  OpenERP  Product"+productid[i]);
					
					
					
					if(pfor1.equals("singleOriginalDetails")){
						logger.info("coming--------------------------singleOriginalDetails");
					if (marketingNotes.getNoteName()
							.equalsIgnoreCase("OriginalDetails")) {
						logger.info("MarketingNotes"+marketingNotes.getNoteName());
						
						try{
							
							//TestDevCRM.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(), oppertunityId);
						HttpsConnectionCase.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(), oppertunityId);
						}catch(Exception e){
				            logger.error("OpenERP Connection  failed"+e.getMessage());				
							}
						
						
						try{
							logger.debug("------------inside try block ------chnageStageToPostSelction------");
							///TestDevCRM.chnageStageToPostSelction(oppertunityId);
							
						//HttpsConnectionCase.chnageStageToPostSelction(oppertunityId);
						}catch(Exception  e){
							 logger.error("OpenERP Connection  failed"+e.getMessage());
							
						}
						
						
						try {
							detials=JsonConvertion.fromJsonforOriginalDetails(marketingNotes.getNoteContent());
							logger.info("---------details;-------"+detials);
							proertyvalue = detials.getPropertyValue();
							downpayment = detials.getDownpaymentEquity();
							
							
							for (Recommendation recommendation : setRec) {
								
								if (recommendation.getProductID().equalsIgnoreCase(
										productid[i].trim())) {
								
									
									Date today = Calendar.getInstance().getTime();
									DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
									String create_date = df.format(today);
									Calendar cal = Calendar.getInstance(); // creates calendar
								    cal.setTime(new Date()); // sets calendar time/date
								    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
								    cal.getTime(); 
									
								    Date newDate = DateUtils.addHours(today, 84);
								    
								    String expiry_date = df.format(newDate);
								      
								    
								    logger.info("Expiry Data    time is"+expiry_date);
								    logger.info(" Data    inser in original ");
									data.insertDataInCouchbase(recommendation,
											oppertunityId, ipaddress, proertyvalue,
											downpayment);
									
									
									
									logger.info("data inserted.");
									
									//Update  Data  in OpenERP
								}

							}
							

							System.out.println("if detials : " + detials);
						} catch (Exception e) {
							logger.error("error in original details "+e.getMessage());
						}
					}
					}
					
					else if(pfor1.equals("singleRecommendDetails")){
							logger.info("coming--------------------------singleRecommendDetails");

						if (marketingNotes.getNoteName()
								.equalsIgnoreCase("RecommendDetails")) {
							logger.debug("MarketingNotes"+marketingNotes.getNoteName());
							
							try{
								
								//TestDevCRM.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(), oppertunityId);
							HttpsConnectionCase.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(), oppertunityId);
							}catch(Exception e){
					            logger.error("OpenERP Connection  failed"+e.getMessage());				
								}
							
							try{
								logger.debug("------------inside try block ------chnageStageToPostSelction------");
								//TestDevCRM.chnageStageToPostSelction(oppertunityId);
								
							//HttpsConnectionCase.chnageStageToPostSelction(oppertunityId);
							}catch(Exception  e){
								 logger.error("OpenERP Connection  failed"+e.getMessage());
								
							}
							
							try {
								recdetials=JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
								
								proertyvalue = recdetials.getPropertyValue();
								downpayment = recdetials.getDownpaymentEquity();
								
								
								for (Recommendation recommendation : setRec) {
									logger.info("-----------------Recommendation-------------");
									if (recommendation.getProductID().equalsIgnoreCase(
											productid[i].trim())) {
										
										
										Date today = Calendar.getInstance().getTime();
										DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
										String create_date = df.format(today);
										Calendar cal = Calendar.getInstance(); // creates calendar
									    cal.setTime(new Date()); // sets calendar time/date
									    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
									    cal.getTime(); 
										
									    Date newDate = DateUtils.addHours(today, 84);
									    
									    String expiry_date = df.format(newDate);
									      
									    
									    logger.debug("Expiry Data    time is"+expiry_date);
									    logger.info(" Data    inser in recommendation ");
										data.insertDataInCouchbase(recommendation,
												oppertunityId, ipaddress, proertyvalue,
												downpayment);
										
										
										
										logger.info("data inserted.");
										
										//Update  Data  in OpenERP
									}

								}
								

								
							} catch (Exception e) {
								logger.error("error", e);
							}
						}
						
					}else if(pfor1.equals("Combine")){
							logger.info("coming--------------------------Combine");

						if (marketingNotes.getNoteName()
								.equalsIgnoreCase("RecommendDetails")) {
							logger.debug("MarketingNotes"+marketingNotes.getNoteName());
							
							try{
								
								//TestDevCRM.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(), oppertunityId);
							HttpsConnectionCase.updateDeal(marketingNotes.getNoteName(), marketingNotes.getNoteContent(), oppertunityId);
							}catch(Exception e){
					            logger.error("OpenERP Connection  failed"+e.getMessage());				
								}
							
							
							try{
								logger.debug("------------inside try block ------chnageStageToPostSelction------");
								//TestDevCRM.chnageStageToPostSelction(oppertunityId);
								
							//HttpsConnectionCase.chnageStageToPostSelction(oppertunityId);
							}catch(Exception  e){
								 logger.error("OpenERP Connection  failed"+e.getMessage());
								
							}
							
							try {
								recdetials=JsonConvertion.fromJsonforRecommendDetails(marketingNotes.getNoteContent());
								
								proertyvalue = recdetials.getPropertyValue();
								downpayment = recdetials.getDownpaymentEquity();
								
								
								for (CombinedRecommendation combi : combineRec) {
									
									if (combi.getBaseProductID().equalsIgnoreCase(productid[i].trim())){
										
										
										
										Date today = Calendar.getInstance().getTime();
										DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
										String create_date = df.format(today);
										Calendar cal = Calendar.getInstance(); // creates calendar
									    cal.setTime(new Date()); // sets calendar time/date
									    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
									    cal.getTime(); 
										
									    Date newDate = DateUtils.addHours(today, 84);
									    
									    String expiry_date = df.format(newDate);
									      
									    
									    logger.debug("Expiry Data    time is"+expiry_date);
										
										data.insertDataInCouchbase(combi,oppertunityId, ipaddress);
								}
								
									
									
										
									}
								

								
							} catch (Exception e) {
								logger.error("error"+e.getMessage());
							}
						}
						
					}
					
					//Update  OpenERP  Deal  Notes
					
				
				}
				
				
				
				
				//Update  DelNotes  in  OpenERP
				
				
				
				
				
				
				
				
				
				
				

				

					
			}
		}

		return ok(thankyou.render(message));
	}
	
	private String setTerm2(String term){
		Integer intterm = Integer.parseInt(term.substring(0));
		intterm=intterm-1; 
		String sterm = Integer.toString(intterm);
		
		
		return sterm;
	}
	
	
	public  Result ReUnderwrite(){


		logger.info("start from Original Details : ");
		String opp_id = request().getQueryString("opp_id");
		logger.info("opp_id : " + opp_id);
		CouchbaseData data = new CouchbaseData();
		
		
		UwAppAllAlgo algo  = data.getDataFromCouchbase(opp_id);
		ArrayList<Liability> liablist =new ArrayList<>();
		
		Set<Liability> setlibality = algo.getLiability();
	
		if(setlibality!=null){
	
		for(Liability liab:setlibality){
			String id = liab.getSeq_no();
			logger.info("<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>liab id "+id);
		
			liablist.add(liab);
			logger.info("application name : {} : "+liab.getApplicantName());
		
		}
		
	}else {
		liablist=null;
	}
		
		String productID =  request().getQueryString("productId");
		String downpayment = request().getQueryString("downpayment");
		String amortization = request().getQueryString("amortization");
		String propertyValue = request().getQueryString("propertyValue");
		
		String propertyvaluechane = propertyValue.replace("," ,"");
		
		
		String downpaymentchage = downpayment.replace(",", "");
		Double longddown = Double.parseDouble(downpaymentchage);
		double longer=Math.abs(longddown);
		
		
		String downPaymetcange1 = String.valueOf(longer);
		
		//System.out.println("product Id recommend "+productID);
		//System.out.println("downpayment : "+downpayment);
		
		RecommendDetails recommend = new RecommendDetails();
		recommend.setProductID(productID);
		recommend.setDownpaymentEquity(downPaymetcange1);
		recommend.setAmortization(amortization);
		recommend.setPropertyValue(propertyvaluechane);
		
		
		
		return ok(underwriteGet.render(recommend, opp_id,liablist));
	}
	
	public Result ReUnderwritesubmit(){
		
		logger.debug("Before storing in OpenErp");
		
		DynamicForm data = form().bindFromRequest();
		
		
		String productID= data.get("productID");
		String opputunityID= 	data.get("opp_id");
		String propertyValue = data.get("propertyValue");
		String downpropertyValue = data.get("downpropertyValue");
		String amortization = data.get("amortization");
		
		String penalty= data.get("penalty");
		String clientName = data.get("action");
		
		
	/*Source */
		String bankAccount =data.get("bank_account");
		String sale_existing =data.get("sale_existing");
		String personal_cash =data.get("personal_cash");
		
		String existing_equity =data.get("existing_equity");
		String rrsp_tsfa =data.get("rrsp_tsfa");
		String sweat_equity =data.get("sweat_equity");
		
		String gifted =data.get("gifted");
		String secondary_fin =data.get("secondary_fin");
		String borrowed =data.get("borrowed");
		
		String other_vtb =data.get("other_vtb");
		

		
		//liablity
		
		
		
		
	
		
		
		
		logger.info("opputunityID : "+opputunityID+"\n"+ 
					"propertyValue {} "+propertyValue+"\n"+
					"downpropertyValue {} "+downpropertyValue+"\n"+
					"amortization {}" +amortization+"\n"+
					"penalty {} "+penalty+
					"productID {} "+productID
					);
		
		
		// this is for server test 
		try {
			HttpsConnectionCase.updateforReunderwriteRecommmend(opputunityID, propertyValue, downpropertyValue, amortization);
		} catch (Exception e1) {

			logger.error("while creating new record on re_underwrite {} "+e1.getMessage());
		}

// this is for local  test  		
		/*TestDevCRM crm = new TestDevCRM();
		logger.info("start here ");
		try {
			
			crm.updateforReunderwriteRecommmend(opputunityID, propertyValue, downpropertyValue, amortization);
		} catch (Exception e) {
			logger.error("error has occured during opernerp update {} :  "+e.getMessage());
			
			e.printStackTrace();
		} 
		logger.info("end here ");*/
		
		
		
		
		
		
		SourcesReUnderwrite source = new SourcesReUnderwrite();
		source.setBankAccount(bankAccount);
		source.setSaleExisting(sale_existing);
		source.setPersonalCash(personal_cash);
		source.setExistingEquity(existing_equity);
		source.setRrspTsfa(rrsp_tsfa);
		source.setSweatEquity(sweat_equity);
		source.setGifted(gifted);
		source.setSecondaryFin(secondary_fin);
		source.setBorrowed(borrowed);
		source.setOtherVtb(other_vtb);
		
		
		//logger.info("sourece information :>>>>>>>>>>>>>>>>>>>>>>>>>>>.   "+source.toString());
		String payoff = data.get("pay_off");
		String seq_no = data.get("seq_no");
		logger.info("getting from from {} "  +payoff);
		logger.info("getting from from {} "  +seq_no);
		logger.info("***********>>>>>>>>>>>>>>>>>>>>*****************");		
		
		
		//for local test 
		/*TestDevCRM crm = new TestDevCRM();
		logger.info("start here ");
		try {
			
			crm.updateforSource(source, opputunityID);
		} catch (Exception e) {
			logger.error("error has occured during opernerp update {} :  "+e.getMessage());
			
			
		} */
		// for productiion test
		try {
			HttpsConnectionCase.updateforSource(source, opputunityID);
		} catch (Exception e1) {

			logger.error("while creating new record on re_underwrite Source  {} "+e1.getMessage());
		}
		
		
		//logger.info("source object {}: "+source.toString());

		String[] arrayvalue=request().body().asFormUrlEncoded().get("pay_off");
	
		String[] seq_no12=request().body().asFormUrlEncoded().get("seq_no");
		
		TestDevCRM crm = new TestDevCRM();
		logger.info("start here ");
		
		int applicantID=0;
		
		/*
		try {
			
			 applicantID  = crm.getApplicantID(opputunityID);
			logger.info("applicant ID get in application controller {} "+applicantID);
		} catch (Exception e) {
			logger.error("error has occured during opernerp update {} :  "+e.getMessage());
		
		}
		*/
		
		
			try {
				applicantID = HttpsConnectionCase.getApplicantID(opputunityID);
			} catch (Exception e1) {

				logger.error("while creating new record on re_underwrite {} "+e1.getMessage());
			}
			
		
		if(applicantID!=0){
			/*try{
			String app =	String.valueOf(applicantID);
				crm.updateforpayOff(seq_no, app, payoff);
				
			}catch(Exception e){
				logger.error("while error has got applicant Id {} for update {}  : "+e.getMessage());
			}*/
			
			try{
				String app =	String.valueOf(applicantID);
					HttpsConnectionCase.updateforpayOff(seq_no, app, payoff);
					
				}catch(Exception e){
					logger.error("while error has got applicant Id {} for update {}  : "+e.getMessage());
				}
			
		}
		return indexbroker("B",opputunityID);
		
	}
	

	
	

	/*public static Result editcombineProduct(){
		return ok("");
	}*/
	
}
