package com.biz.syml;



import org.apache.xmlrpc.XmlRpcException;

import play.Logger;

import com.debortoliwines.openerp.api.FilterCollection;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Row;
import com.debortoliwines.openerp.api.RowCollection;
import com.debortoliwines.openerp.api.Session;


public class HttpsConnectionCase {
	private static org.slf4j.Logger logger = Logger.underlying();
	public static void updateProduct(String oppertunutyId,String selected_productID) throws Exception  {
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		//Session session = new Session("local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");

		session.startSession();
		try{
			
			logger.debug("Inside Update  OpenERP  Methods");
			System.out.println("Inside Update  OpenERP  Methods");
		
	    		ObjectAdapter product=session.getObjectAdapter("crm.lead");
	    		
	    		FilterCollection productData=new FilterCollection();
	    		productData.add("id","=",oppertunutyId);
	    		logger.debug("productData");

	    		
	    		
	    		RowCollection productDataRow=product.searchAndReadObject(productData, new String[]{"id","name","selected_product"});
	    		logger.debug("productDataRow"+productDataRow.size());

	    		Row rowproduct=productDataRow.get(0);

	    		logger.debug("Selected  Product"+rowproduct.get("selected_product"));

	    		rowproduct.put("selected_product", selected_productID);
	    		
	    		product.writeObject(rowproduct, true);
	    	
	    		logger.debug("OpenERP  product  Updated");
	     }catch(Exception e){
	    	 logger.error("Error in  Udating  product"+e);
	     }
	}
	public static void  updateDeal(String name,String  marketing_field,String oppertunutyId ) throws Exception{
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		//Session session = new Session("local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");

		session.startSession();

		try{
			logger.debug("Inside Update  OpenERP  Methods");
		
	    		ObjectAdapter product=session.getObjectAdapter("deal");
	    		
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
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		session.startSession();
		try{
			logger.debug("Inside Update  OpenERP  Methods");
		
			ObjectAdapter opprtunity = session
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
	    	 logger.error("Error in  Udating  product"+e);
	     }
	}
	
	public static String  getCompanyName(String oppertunutyId ) throws Exception{
		logger.debug("inside Company name ");
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		String companyName = "";
		System.out.println("the getCompany session has before start :");
		logger.info("the getCompany session has before start :");
		session.startSession();
		try{
			System.out.println("the getCompany session has after  start :");
			logger.info("the getCompany session has after  start :");
			logger.debug("Inside getCompany Name  OpenERP  Methods");
			System.out.println("Inside getCompany Name  OpenERP  Methods");
		
			ObjectAdapter opprtunity = session
					.getObjectAdapter("crm.lead");

	    		FilterCollection filter = new FilterCollection();
	    		filter.add("application_no", "=", oppertunutyId);
	    		RowCollection row = opprtunity.searchAndReadObject(filter,
	    				new String[] {"x_company"});
	    		Row rowData = row.get(0);
	    		companyName =	rowData.get("x_company").toString();
	    		logger.debug("company  name  get to select Opp_id");
	    		System.out.println("company  name  get to select Opp_id");
	    	   }catch(Exception e){
	    	 logger.error("Error in  get company name"+e);
	    	 System.out.println("error in get Company name insid getCompanyName() : "+e.getMessage());
	     }
		return companyName;
	}
	
	
	public static void  updateforReunderwriteRecommmend(String oppertunutyId,String propertyValue,String downpropertyValue,String amortization  ) throws Exception{
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		
		
		logger.info("the getCompany session has before start :");
		session.startSession();
		
		
		
		try{
	
			logger.info("the  session has after  start :");
			logger.debug("store in openerp insde openerp connection {} : ");
			
		
			ObjectAdapter opprtunity = session
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
	

	public static void  updateforSource(SourcesReUnderwrite source,String oppertunutyId ) throws Exception{
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		
		
		logger.debug("Before inserting the Source IN openERP");
		session.startSession();
	logger.debug("session has started "+session.getDatabaseList("openerp.crm.local", 8069));	
		
		
		try{
	
		
			//logger.debug("store in openerp insde openerp connection {} : ");
			
		
			ObjectAdapter opprtunity = session
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
	    		   
	    	 logger.error("error create while insert the Source DownPayment {} : "+e.getMessage());
	    	
	     }
		
		
	} 
	
	
	public static void main(String[] args) throws Exception {
		
		//String name  = HttpsConnectionCase.getCompanyName("4406");
	//	logger.info("name of company  In HttpsConnection Case: "+name);
		
		//HttpsConnectionCase.updateProduct("4406", 2115);
	}
	
	public static void updateforpayOff(String seqno,String applicantid,String payoff) throws XmlRpcException, OpeneERPApiException{
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		logger.info("inside updateOff for applicant Liabilities {}  : ");
		
		
	
		
		
		
		
		try{
			ObjectAdapter opprtunity = session
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
	
	
	public static int getApplicantID(String opputunityID) {
		Session session = new Session("openerp.crm.local", 8069, "symlsys", "guy@visdom.ca", "VisdomTesting");
		
		
		logger.debug("inside get Applicant ID  () {} "); 
		int payoffid=0;
		try{
			ObjectAdapter opprtunity = session
					.getObjectAdapter("crm.lead");
			
			

    		FilterCollection filter = new FilterCollection();
    		filter.add("id", "=", opputunityID);
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
}
