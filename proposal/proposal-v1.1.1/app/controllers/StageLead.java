package controllers;



import java.io.IOException;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.biz.rest.PostgressConnection;

import play.Logger;




public class StageLead {
	private static org.slf4j.Logger logger = Logger.underlying();
	private Connection conn;
	
	String result=null;
	public String getcrmLeadFrompostgress(String opportunityId) throws SQLException, IOException {
			logger.info("getcrmLeadFrompostgress"+opportunityId);
		
		conn=	PostgressConnection.getPostGressConnection();

			

			try {
				Statement stmt11 = conn.createStatement();
				ResultSet rs11 = stmt11
						.executeQuery("select row_to_json(crm_lead) as crm_lead from crm_lead where id ="+opportunityId);
				// log.debug("********hr_applicant_backup_tbl Data IS*****"+rs11.getString("hr_applicant_backup_tbl").toString());
				while (rs11.next()) {
					result=	rs11.getString("crm_lead");
					logger.info(rs11.getString("crm_lead"));
				}
				rs11.close();
			} catch (Exception e) {
				
				
					closeDbConnection();
				} 
			
			return result;
			}
			
	public void closeDbConnection(){
		try{
			conn.close();
		}catch(Exception e){
			
		}
	}

			
			

		
		

	
	
	
	public static void main(String[] args) {
		System.out.println("te3st");
	}
}
