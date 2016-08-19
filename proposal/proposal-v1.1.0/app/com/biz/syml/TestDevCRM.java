package com.biz.syml;
import play.Logger;

import org.apache.xmlrpc.XmlRpcException;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;

public class TestDevCRM {
	
	
	private static org.slf4j.Logger logger = Logger.underlying();
	public  String  getCompanyName(String  opp_id) throws XmlRpcException, OpeneERPApiException{
		logger.debug("inside getCompanyName()");	
		String companyName="";

	
	
		OpenERPSessionUtil util = new OpenERPSessionUtil();
		ExtendedSession openERPSession =   util.initSession();

	

	
	try {
		ObjectAdapter partnerAd = openERPSession.getObjectAdapter("crm.lead");
	    
	    FilterCollection filters = new FilterCollection();
	    filters.add("application_no","=",opp_id);
	    RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"x_company"});
	    
	    Row row = partners.get(0);
	    companyName=row.get("x_company").toString();
	    logger.debug("-----------CompanyName:-----------" +companyName);
/*	    for (Row row : partners){
	        System.out.println("Row ID: " + row.getID());
	        companyName = row.get("x_company").toString();
	        
	        
	    }
*/	   
	    ////// 
	    ////// Example code snippet goes here
	    //////
	} catch (NullPointerException e) {
	    System.out.println("Error while reading data from server:\n\n" + e.getMessage());
	}
	return companyName;
	}
	
	
	public  void  updateforReunderwriteRecommmend(String oppertunutyId,String propertyValue,String downpropertyValue,String amortization) throws XmlRpcException, OpeneERPApiException{
		logger.debug("inside update for reunderwriting {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

		
		
		
		
		
		try{
	
			
		
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("application_no", "=", oppertunutyId);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"property_value","down_payment_amount","desired_amortization"});
	    		Row rowData = row.get(0);
	    		rowData.put("property_value", propertyValue);
	    		rowData.put("down_payment_amount", downpropertyValue);
	    		rowData.put("desired_amortization", amortization);
	    		
	    		logger.debug("property value {}:"+rowData.get("property_value"));
	    		logger.debug("down_payment_amount value {}:"+rowData.get("down_payment_amount"));
	    		logger.debug("desired_amortization value {}:"+rowData.get("desired_amortization"));
	    		
	    		
	    		//rowData.put("company",rowData);
	    		
	    		opprtunity.writeObject(rowData, true);
	    		logger.debug("update successfully :");
	    		
	    	   }catch(Exception e){
	    		   
	    	 logger.error("Error in  get company name"+e.getMessage());
	    	
	     }
		
		
	}
	
	
	public  void  updateforSource(SourcesReUnderwrite source,String oppertunutyId )  throws XmlRpcException, OpeneERPApiException{
		logger.debug("inside update for Sourece Reunderwirtbe Test class {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

		logger.info("insise SourceReundserWrute <>>>>>>>>>>>>>>>> "+source.toString());
		
		
		
		
		try{
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("application_no", "=", oppertunutyId);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"bank_account","personal_cash_amount","rrsp_amount","gifted_amount",
	    				"borrowed_amount","sale_of_existing_amount","existing_equity_amount","sweat_equity_amount",
	    				"secondary_financing_amount","other_amount"
	    								
	    								});
	    		Row rowData = row.get(0);
	    		rowData.put("bank_account", source.getBankAccount());
	    		rowData.put("personal_cash_amount", source.getPersonalCash());
	    		rowData.put("rrsp_amount", source.getRrspTsfa());
	    		rowData.put("gifted_amount", source.getGifted());
	    		rowData.put("borrowed_amount", source.getBorrowed());
	    		rowData.put("sale_of_existing_amount", source.getSaleExisting());
	    		rowData.put("existing_equity_amount", source.getExistingEquity());
	    		rowData.put("sweat_equity_amount", source.getSweatEquity());
	    		rowData.put("secondary_financing_amount", source.getSecondaryFin());
	    		rowData.put("other_amount", source.getOtherVtb());	
	    		
	    		logger.debug("get Id if row Inserted IN source  {} "+rowData.getID());
	    		
	    		//rowData.put("company",rowData);
	    		
	    		opprtunity.writeObject(rowData, true);
	    		logger.debug("update successfully :");
	    		
		}catch(Exception e){
	    		   
	    	 logger.error("Error in  get Sourece DownPayment name"+e.getMessage());
	    	
	     }
		
		
	}
	public static void updateProduct(String oppertunutyId,String selected_productID) throws Exception  {
	
		logger.info("inside update for Sourece update product Test class {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

		
		
		try{
			
			
			System.out.println("Inside Update  OpenERP  Methods");
		
	    		ObjectAdapter product=openERPSession.getObjectAdapter("crm.lead");
	    		
	    		FilterCollection productData=new FilterCollection();
	    		productData.add("id","=",oppertunutyId);
	    		logger.info("productData");

	    		
	    		
	    		RowCollection productDataRow=product.searchAndReadObject(productData, new String[]{"id","name","selected_product"});
	    		logger.debug("productDataRow"+productDataRow.size());

	    		Row rowproduct=productDataRow.get(0);

	    		logger.info("Selected  Product"+rowproduct.get("selected_product"));

	    		rowproduct.put("selected_product", selected_productID);
	    		
	    		product.writeObject(rowproduct, true);
	    	
	    		logger.info("OpenERP  product  Updated");
	     }catch(Exception e){
	    	 logger.error("Error in  Udating  product"+e.getMessage());
	     }
	}
	public static void  updateDeal(String name,String  marketing_field,String oppertunutyId ) throws Exception{
		
	logger.info("inside update for Sourece update deal Test class {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();



		try{
			logger.info("Inside Update  deal OpenERP  Methods");
		
	    		ObjectAdapter product=openERPSession.getObjectAdapter("deal");
	    		
	    		 Row newPartner = product.getNewRow(new String[]{"name", "marketing_field","opportunity_id"});
	 		    newPartner.put("name", name);
	 		    newPartner.put("marketing_field", marketing_field);
	 		    newPartner.put("opportunity_id", oppertunutyId);

	 		   product.createObject(newPartner);
	    	
	    		logger.debug("OpenERP  product  Updated");
	     }catch(Exception e){
	    	 logger.error("Error in  Udating  product"+e);
	     }
	}
	
	public static void  chnageStageToPostSelction(String oppertunutyId ) throws Exception{
logger.info("inside update for changeStageToPostSelection Test class {} : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();
try{
			logger.info("Inside Update  chnageStageToPostSelction  Methods");
		
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("id", "=", oppertunutyId);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"stage_id","id" });
	    		Row rowData = row.get(0);
	    		
	    		rowData.put("stage_id",23);
	    		
	    		opprtunity.writeObject(rowData, true);
	    		logger.debug("Stage  chnages  To  post  Selection");
	    	   }catch(Exception e){
	    	 logger.error("Error in  Udating  product"+e.getMessage());
	     }
	}
	public int getApplicantID(String oppertunutyId){
		logger.info("inside get Applicant ID  () {} "); 
		int payoffid=0;
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

		//logger.info("insise getApplicabtID  <>>>>>>>>>>>>>>>> ");
		try{
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("crm.lead");
			
			

    		FilterCollection filter = new FilterCollection();
    		filter.add("id", "=", oppertunutyId);
    		RowCollection partners = opprtunity.searchAndReadObject(filter,new String[] {"app_rec_ids"});
    		
    		
    		Row row = partners.get(0);
    		Object[] object =  (Object[]) row.get("app_rec_ids");
    		
    		for(Object ob :object){
    			//logger.info("object [] ((((((((((((0)))))))))))"+ob.toString());
    			 payoffid=Integer.parseInt(ob.toString());
    		}
    		    System.out.println("Applicantid:" + row.get("app_rec_ids"));
    		  
    		
    						/*Row rw = row.get("app_rec_ids");		
    						payoffid=	rw.getID();	*/
			
		}catch(Exception e){
			logger.error(""+e.getMessage());
		}
		
		logger.debug("logger payoffIf "+payoffid);
		return payoffid;
	}
	
	public void updateforpayOff(String seqno,String applicantid,String payoff) throws XmlRpcException, OpeneERPApiException{

		logger.info("inside updateOff for applicant Liabilities {}  : ");
		
		OpenERPSessionUtil  utill=new  OpenERPSessionUtil();
		
		ExtendedSession openERPSession=	utill.initSession();

	
		
		
		
		
		try{
			ObjectAdapter opprtunity = openERPSession
					.getObjectAdapter("applicant.liabilities");

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("seq_no", "=", seqno);
	    		filter.add("applicant_id","=",applicantid);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"pay_off"
	    								
	    								});
	    		Row rowData = row.get(0);
	    		rowData.put("pay_off",payoff);
	    		
	    		logger.debug("get Id if row Inserted IN source  {} "+rowData.getID());
	    		
	    		//rowData.put("company",rowData);
	    		
	    		opprtunity.writeObject(rowData, true);
	    		logger.debug("update successfully :");
	    		
		}catch(Exception e){
	    		   
	    	 logger.error("Error in  get Sourece DownPayment name"+e.getMessage());
	    	
	     }
		
		
	
	}
	
	
	
	
	public static void main(String[] args) throws XmlRpcException, OpeneERPApiException {
		
		/*TestDevCRM crm = new TestDevCRM();
		String name = crm.("4406");
		Logger.info("the main() compant name : "+name);
		System.out.println("the main() compant name : "+name);*/
		//new TestDevCRM().insertData();
	}

}
