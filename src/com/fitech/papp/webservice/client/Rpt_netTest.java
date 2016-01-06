package com.fitech.papp.webservice.client;

import com.fitech.papp.webservice.client.UserWebServiceStub.InsertUser;
import com.fitech.papp.webservice.client.UserWebServiceStub.InsertUserResponse;
import com.fitech.papp.webservice.client.UserWebServiceStub.WebSysUser;




public class Rpt_netTest {

	public static void main(String args[]) {

		try {
//			Set<String> keys = Config.baseMainInfoMap.keySet();
//			for (Iterator it = keys.iterator(); it.hasNext();) {
//		           String s = (String) it.next();
//		           //wurl非空时
//		           if(Config.baseMainInfoMap.get(s).getWburl() != null && Config.baseMainInfoMap.get(s).getIsEnabled().equalsIgnoreCase("true")){
//		        	   
		        	// 以下刘松新加 webservice
						UserWebServiceStub oss = new UserWebServiceStub("http://localhost:8888/PISA/services/UserWebService?wsdl");
						
						
						InsertUser io = new InsertUser();
						WebSysUser wso = new WebSysUser();
						wso.setUsername("zjj");
						wso.setPassword("1234");
						wso.setRealName("realname");
						//wso.setOrgId(param)
						wso.setTelphoneNumber("110");
						wso.setOrgId("0011");
						wso.setIsSuper("1");
						wso.setDepartment("9900");
						
						io.setOperator(wso);
						InsertUserResponse iir = oss.insertUser(io);
						System.out.println(iir.get_return());
						
//						DeleteUser du=new DeleteUser();
//						WebSysUser wso = new WebSysUser();
//						wso.setUsername("xx");
//						du.setOperator(wso);
//						DeleteUserResponse is=oss.deleteUser(du);
//						System.out.println(is.get_return());
//						
//						UpdateUser uu=new UpdateUser();
//						WebSysUser wso = new WebSysUser();
//						wso.setUsername("xx");
//						wso.setTelphoneNumber("119");
//						wso.setRealName("张无忌");
//						wso.setOrgId("01900");
//						wso.setPassword("111111");
//						uu.setOperator(wso);
//						UpdateUserResponse ds=oss.updateUser(uu);
//						System.out.println(ds.get_return());

		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
}
