package com.fitech.papp.webservice.client;

import java.rmi.RemoteException;

import com.fitech.papp.webservice.util.Blowfish;

public class Demo {

	/**
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) {
		// insertUser();
		// updateUser();
		deleteUser();
		Blowfish blowfish = new Blowfish();
		System.out.println(blowfish.decode("AQEAyTK71cjDx-4"));
		// AQYAIskgJBfw-KQ 123456
		// ISGMyneATSuhkiwz4BURBQ==
	}

	private static void deleteUser() {
		try {
			UserWebServiceStub userManagerBusinessStub = new UserWebServiceStub(
					"http://localhost:8888/PISA/services/UserWebService?wsdl");
			com.fitech.papp.webservice.client.UserWebServiceStub.DeleteUser deleteUser = new com.fitech.papp.webservice.client.UserWebServiceStub.DeleteUser();
			// com.fitech.papp.webservice.client.UserManagerBusinessStub.OperatorForm
			// sysUser = new
			// com.fitech.papp.webservice.client.UserManagerBusinessStub.OperatorForm();
			UserWebServiceStub.WebSysUser sysUser = new UserWebServiceStub.WebSysUser();
			sysUser.setAddress("qqa");
			sysUser.setPassword("123");
			sysUser.setOrgId("9900");
			sysUser.setDepartment("department");
			sysUser.setEmail("3@163.com");
			sysUser.setEmployeeId("112");
			sysUser.setIsSuper("true");
			sysUser.setPostcode("222137");
			sysUser.setUsername("77abc");
			sysUser.setRealName("yu");
			sysUser.setUpdateDate("1999-12-12 12:22:01");

			deleteUser.setOperator(sysUser);
			userManagerBusinessStub.deleteUser(deleteUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void updateUser() {
		try {
			// TODO Auto-generated method stub
			UserWebServiceStub userManagerBusinessStub = new UserWebServiceStub(
					"http://localhost:8888/PISA/services/UserWebService?wsdl");
			com.fitech.papp.webservice.client.UserWebServiceStub.UpdateUser updteUser = new com.fitech.papp.webservice.client.UserWebServiceStub.UpdateUser();
			// com.fitech.papp.webservice.client.UserManagerBusinessStub.OperatorForm
			// sysUser = new
			// com.fitech.papp.webservice.client.UserManagerBusinessStub.OperatorForm();
			UserWebServiceStub.WebSysUser sysUser = new UserWebServiceStub.WebSysUser();
			sysUser.setAddress("qqa");
			sysUser.setPassword("123");
			sysUser.setOrgId("9900");
			sysUser.setDepartment("department");
			sysUser.setEmail("6@163.com");
			sysUser.setEmployeeId("112");
			sysUser.setIsSuper("true");
			sysUser.setPostcode("9998");
			sysUser.setUsername("77abc");
			sysUser.setRealName("yuyu");
			sysUser.setUpdateDate("1999-12-12 12:22:01");

			updteUser.setOperator(sysUser);
			userManagerBusinessStub.updateUser(updteUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void insertUser() {
		try {
			// TODO Auto-generated method stub
			UserWebServiceStub userManagerBusinessStub = new UserWebServiceStub(
					"http://localhost:8888/PISA/services/UserWebService?wsdl");
			com.fitech.papp.webservice.client.UserWebServiceStub.InsertUser insertUser = new com.fitech.papp.webservice.client.UserWebServiceStub.InsertUser();
			// com.fitech.papp.webservice.client.UserManagerBusinessStub.OperatorForm
			// sysUser = new
			// com.fitech.papp.webservice.client.UserManagerBusinessStub.OperatorForm();
			UserWebServiceStub.WebSysUser sysUser = new UserWebServiceStub.WebSysUser();
			sysUser.setAddress("qqq");
			sysUser.setPassword("123w");
			sysUser.setOrgId("9900");
			sysUser.setDepartment("department");
			sysUser.setEmail("ywz@163.com");
			sysUser.setEmployeeId("1113");
			sysUser.setIsSuper("true");
			sysUser.setPostcode("222137");
			sysUser.setUsername("77abc");
			sysUser.setRealName("yu");
			sysUser.setUpdateDate("1999-12-12 12:22:01");

			insertUser.setOperator(sysUser);
			userManagerBusinessStub.insertUser(insertUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
