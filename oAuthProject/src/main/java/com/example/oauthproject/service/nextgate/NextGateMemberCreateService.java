/**
This Service class is used for Creating a New Member Record.
 * API Calls used are /search,/systemrecords,/sbrs
 * 
 */
package com.example.oauthproject.service.nextgate;
/**
 * NextGate Member Create Service class
 * @version 1.0 17 Oct 2016
 * @author ****
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.oauthproject.model.ng.MemberAddressInfo;
import com.example.oauthproject.model.ng.MemberEmailInfo;
import com.example.oauthproject.model.ng.MemberInfo;
import com.example.oauthproject.model.ng.MemberPhoneInfo;
import com.example.oauthproject.service.common.NGRESTTemplateService;
import com.example.oauthproject.util.LoggerUtil;
@Service
public class NextGateMemberCreateService {
	@Value("${nextgate.getMemberInfo.url}")
	private String nextgateMemberInfoURL;
	@Value("${nextgate.memberCreateInfo.url}")
	private String nextgateMemberCreateInfoURL;
	@Value("${nextgate.contentType}")
	private String nextgateContentType;
	@Autowired
	NGRESTTemplateService ngRestTemplateService;
	RestTemplate restTemplate = new RestTemplate();
	// Note: LocalId value is the MRN number value from the HL7 message
	// Method used to create a patient 	
	public List<MemberInfo> ngCreateMember(MemberInfo memberInfo) throws JSONException {
		// TODO Auto-generated method stub
		List<MemberInfo> memberCreateResults = new ArrayList<MemberInfo>();
		if (NGRESTTemplateService.cookies.get("Cookie") != null) {
			//JSON body values are set Mandatory fields for creation are (firstname,lastname,systemcode,status,localid)
			JSONObject memberCreateData = new JSONObject();
			JSONObject personData = new JSONObject();
			LoggerUtil.logInfo("LastName member value in Json Body:"+memberInfo.getLastName());
			if(memberInfo.getLastName()!=null && StringUtils.isNotEmpty(memberInfo.getLastName())){
				personData.put("LastName",memberInfo.getLastName());
			}
			LoggerUtil.logInfo("FirstName member value in JSON Body:"+memberInfo.getFirstName());
			if(memberInfo.getFirstName()!=null && StringUtils.isNotEmpty(memberInfo.getFirstName())){
				personData.put("FirstName",memberInfo.getFirstName());
			}
			LoggerUtil.logInfo("MiddleName member value in JSON Body:"+memberInfo.getMiddleName());
			if(memberInfo.getMiddleName()!=null && StringUtils.isNotEmpty(memberInfo.getMiddleName())){
				personData.put("MiddleName",memberInfo.getMiddleName());
			}
			LoggerUtil.logInfo("Gender member value in JSON Body:"+memberInfo.getGender());
			if(memberInfo.getGender()!=null && StringUtils.isNotEmpty(memberInfo.getGender())){
				personData.put("Gender",memberInfo.getGender());
			}
			LoggerUtil.logInfo("DOB member value in JSON Body:"+memberInfo.getDOB());
			if(memberInfo.getDOB()!=null && StringUtils.isNotEmpty(memberInfo.getDOB())){
				personData.put("DOB",memberInfo.getDOB());
			}
			LoggerUtil.logInfo("Ethnic member value in JSON Body:"+memberInfo.getEthnic());
			if(memberInfo.getEthnic()!=null && StringUtils.isNotEmpty(memberInfo.getEthnic())){
				personData.put("Ethnic",memberInfo.getEthnic());
			}
			LoggerUtil.logInfo("SSN member value in JSON Body:"+memberInfo.getSsn());
			if(memberInfo.getSsn()!=null && StringUtils.isNotEmpty(memberInfo.getSsn())){
				personData.put("SSN",memberInfo.getSsn());
			}
			LoggerUtil.logInfo("SSN4 member value in JSON Body:"+memberInfo.getSSN4());
			if(memberInfo.getSSN4()!=null && StringUtils.isNotEmpty(memberInfo.getSSN4())){
				personData.put("SSN4",memberInfo.getSSN4());
			}
			LoggerUtil.logInfo("Race member value in JSON Body:"+memberInfo.getRace());
			if(memberInfo.getRace()!=null && StringUtils.isNotEmpty(memberInfo.getRace())){
				personData.put("Race",memberInfo.getRace());
			}
			LoggerUtil.logInfo("Marital Status member value in JSON Body:"+memberInfo.getmStatus());
			if(memberInfo.getmStatus()!=null && StringUtils.isNotEmpty(memberInfo.getmStatus())){
				personData.put("MStatus",memberInfo.getmStatus());
			}
			LoggerUtil.logInfo("Language member value in JSON Body:"+memberInfo.getPreferredLanguage());
			if(memberInfo.getPreferredLanguage()!=null && StringUtils.isNotEmpty(memberInfo.getPreferredLanguage())){
				personData.put("Language",memberInfo.getPreferredLanguage());
			}
			LoggerUtil.logInfo("Religion member value in JSON Body:"+memberInfo.getReligion());
			if(memberInfo.getReligion()!=null && StringUtils.isNotEmpty(memberInfo.getReligion())){
				personData.put("Religion",memberInfo.getReligion());
			}
						if (memberInfo.getAddressLine1()!= null && StringUtils.isNotEmpty(memberInfo.getAddressLine1())) {
				JSONObject addressData = new JSONObject();
				addressData.put("AddressLine1", memberInfo.getAddressLine1());
				addressData.put("AddressLine2", memberInfo.getAddressLine2());
				addressData.put("AddressType", "H");
				addressData.put("City", memberInfo.getCity());
				addressData.put("PostalCode", memberInfo.getPostalCode());
				addressData.put("StateCode", memberInfo.getStateCode());
				JSONArray addressArray = new JSONArray();
				addressArray.put(addressData);
				personData.put("Address", addressArray);
			}
			if (memberInfo.getPhone()!= null && StringUtils.isNotEmpty(memberInfo.getPhone())) {
				JSONObject phoneData = new JSONObject();
				phoneData.put("Phone", memberInfo.getPhone());
				phoneData.put("PhoneType", "CH");
				JSONArray phoneArray = new JSONArray();
				phoneArray.put(phoneData);
				personData.put("Phone", phoneArray);
			}
			if (memberInfo.getEmail()!= null && StringUtils.isNotEmpty(memberInfo.getEmail())) {
				JSONObject emailData = new JSONObject();
				emailData.put("EmailAddress", memberInfo.getEmail());
				emailData.put("EmailType", "W");
				JSONArray phoneArray = new JSONArray();
				phoneArray.put(emailData);
				personData.put("Email", phoneArray);
			}
			/
			LoggerUtil.logInfo("personDate:"+personData.toString());
			memberCreateData.put("Person", personData);//all array values added
			// hardcode the localid,status,systemcode for testing
			LoggerUtil.logInfo("Status member value in JSON Body:"+memberInfo.getStatus());
			if(memberInfo.getStatus()!=null && StringUtils.isNotEmpty(memberInfo.getStatus())){
			memberCreateData.put("Status", memberInfo.getStatus());
			}
			LoggerUtil.logInfo("SystemCode member value in JSON Body:"+memberInfo.getSystemCode());
			if(memberInfo.getSystemCode()!=null && StringUtils.isNotEmpty(memberInfo.getSystemCode())){
				memberCreateData.put("SystemCode",memberInfo.getSystemCode());
			}
			LoggerUtil.logInfo("LocalID member value in JSON Body:"+memberInfo.getMrn());
			if(memberInfo.getMrn()!=null && StringUtils.isNotEmpty(memberInfo.getMrn())){
				memberCreateData.put("LocalID", memberInfo.getMrn());
			}
			LoggerUtil.logInfo("Member Create Reg RequestBody " + memberCreateData.toString());
			HttpEntity<String> memberCreateEntity = new HttpEntity<String>(memberCreateData.toString(),
					ngRestTemplateService.setHeadersforAPICalls());
			try {
				//exchange the /search api call
				//String systemCode = "HPMG";
				//String lid = memberInfo.getMrn();
				ResponseEntity<String> memberCreateResponse = restTemplate.exchange(nextgateMemberCreateInfoURL + memberInfo.getSystemCode() + "/" + memberInfo.getMrn(), HttpMethod.POST,
						memberCreateEntity, String.class);
				LoggerUtil.logInfo("Status code ' + " + memberCreateResponse.getStatusCode());
				if(memberCreateResponse.getStatusCode()== HttpStatus.CREATED){
					JSONObject resultRS = new JSONObject(memberCreateResponse.getBody());
					LoggerUtil.logInfo(memberCreateResponse.getBody());
					long resultCode = resultRS.getLong("resultCode");
					LoggerUtil.logInfo("resultCode:    " + resultCode);
					boolean matchFieldChanged = resultRS.getBoolean("matchFieldChanged");
					LoggerUtil.logInfo("matchFieldChanged:    " + matchFieldChanged);
					//Checks if localid exist or not
					if((resultCode == 0) && (matchFieldChanged == false))
					{
						LoggerUtil.logInfo("LocalID already exist for a Member & No matching demographic values.");
						memberCreateResults.add(getMemberInfo(resultRS.getString("euid")));
					}
					//gets the demographic response for a member record
					else{
						LoggerUtil.logInfo("Creationg a new member system record by passing new localid.");
						memberCreateResults.add(getMemberInfo(resultRS.getString("euid")));
					}
				} else if (memberCreateResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					// Update Access token Here
					LoggerUtil.logInfo("Un authorized : ");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LoggerUtil.logInfo("Exception found : " + e.getMessage());
				if (e.getMessage().equalsIgnoreCase("401 Unauthorized")) {
					ngRestTemplateService.nextGateAuthentication();
					ngCreateMember(memberInfo);
				}
			}
		} else {
			ngRestTemplateService.nextGateAuthentication();
			memberCreateResults = ngCreateMember(memberInfo);
		}
		return memberCreateResults;
	}
	//Method gets the memberInfo demographic response values.
	private MemberInfo getMemberInfo(String euid) {
		MemberInfo memberInfo = new MemberInfo();
		if (NGRESTTemplateService.cookies.get("Cookie") != null) {
			HttpEntity<String> memberSearchEntity = new HttpEntity<String>(
					ngRestTemplateService.setHeadersforAPICalls());
			try {
				ResponseEntity<String> memberSearchResponse = restTemplate.exchange(nextgateMemberInfoURL + euid,
						HttpMethod.GET, memberSearchEntity, String.class);
				LoggerUtil.logInfo("Status code ' + " + memberSearchResponse.getStatusCode());
				if (memberSearchResponse.getStatusCode() == HttpStatus.OK) {
					memberInfo.setEuid(euid);
					JSONObject result = new JSONObject(memberSearchResponse.getBody());
					LoggerUtil.logInfo(memberSearchResponse.getBody());
					JSONObject memberDetails = (JSONObject) result.get("Person");
					if (result.has("CreateDateTime")) {
						if (result.getString("CreateDateTime") != null) {
							memberInfo.setCreateDateTime(result.getString("CreateDateTime"));
							LoggerUtil.logInfo("CreateDateTime:    " + result.getString("CreateDateTime"));
						} else {
							memberInfo.setCreateDateTime("");
						}
					}
					if (result.has("CreateFunction")) {
						if (result.getString("CreateFunction") != null) {
							memberInfo.setCreateFunction(result.getString("CreateFunction"));
							LoggerUtil.logInfo("Create Function:    " + result.getString("CreateFunction"));
						} else {
							memberInfo.setCreateFunction("");
						}
					}
					if (result.has("CreateSystem")) {
						if (result.getString("CreateSystem") != null) {
							memberInfo.setCreateSystem(result.getString("CreateSystem"));
							LoggerUtil.logInfo("Create System:    " + result.getString("CreateSystem"));
						} else {
							memberInfo.setCreateSystem("");
						}
					}
					if (result.has("CreateUser")) {
						if (result.getString("CreateUser") != null) {
							memberInfo.setCreateUser(result.getString("CreateUser"));
							LoggerUtil.logInfo("Create User:    " + result.getString("CreateUser"));
						} else {
							memberInfo.setCreateUser("");
						}
					}
					if (result.has("Status")) {
						if (result.getString("Status") != null) {
							memberInfo.setStatus(result.getString("Status"));
							LoggerUtil.logInfo("Status:    " + result.getString("Status"));
						} else {
							memberInfo.setStatus("");
						}
					}
					if (result.has("CreateSystem")) {
						if (result.getString("CreateSystem") != null) {
							memberInfo.setSystemCode(result.getString("CreateSystem"));
							LoggerUtil.logInfo("CreateSystem:    " + result.getString("CreateSystem"));
						} else {
							memberInfo.setSystemCode("");
						}
					}
					if (result.has("LocalID")) {
						if (result.getString("LocalID") != null) {
							memberInfo.setMrn(result.getString("LocalID"));
							LoggerUtil.logInfo("LocalID:    " + result.getString("LocalID"));
						} else {
							memberInfo.setMrn("");
							LoggerUtil.logInfo("LocalID:    " + result.getString("LocalID"));
						}
					}
					if (memberDetails.has("LastName")) {
						if (memberDetails.getString("LastName") != null) {
							memberInfo.setLastName(memberDetails.getString("LastName"));
							LoggerUtil.logInfo("LastName:    " + memberDetails.getString("LastName"));
						} else {
							memberInfo.setLastName("");
						}
					}
					if (memberDetails.has("FirstName")) {
						if (memberDetails.getString("FirstName") != null) {
							memberInfo.setFirstName(memberDetails.getString("FirstName"));
							LoggerUtil.logInfo("First Name:    " + memberDetails.getString("FirstName"));
						} else {
							memberInfo.setFirstName("");
						}
					}
					if (memberDetails.has("MiddleName")) {
						if (memberDetails.getString("MiddleName") != null) {
							memberInfo.setMiddleName(memberDetails.getString("MiddleName"));
							LoggerUtil.logInfo("Middle Name:    " + memberDetails.getString("MiddleName"));
						} else {
							memberInfo.setMiddleName("");
						}
					}
					if (memberDetails.has("Race")) {
						if (memberDetails.getString("Race") != null) {
							memberInfo.setRace(memberDetails.getString("Race"));
							LoggerUtil.logInfo("Race:    " + memberDetails.getString("Race"));
						} else {
							memberInfo.setRace("");
						}
					}
					if (memberDetails.has("PersonId")) {
						if (memberDetails.getString("PersonId") != null) {
							memberInfo.setPersonid(memberDetails.getString("PersonId"));
							LoggerUtil.logInfo("PersonId:    " + memberDetails.getString("PersonId"));
						} else {
							memberInfo.setPersonid("");
						}
					}
					if (memberDetails.has("Gender")) {
						if (memberDetails.getString("Gender") != null) {
							memberInfo.setGender(memberDetails.getString("Gender"));
							LoggerUtil.logInfo("Gender:    " + memberDetails.getString("Gender"));
						} else {
							memberInfo.setGender("");
						}
					}
					if (memberDetails.has("DOB")) {
						if (memberDetails.getString("DOB") != null) {
							memberInfo.setDOB(memberDetails.getString("DOB"));
							LoggerUtil.logInfo("Date of Birth:    " + memberDetails.getString("DOB"));
						} else {
							memberInfo.setDOB("");
						}
					}
					if (memberDetails.has("SSN")) {
						if (memberDetails.getString("SSN") != null) {
							memberInfo.setSsn(memberDetails.getString("SSN"));
							LoggerUtil.logInfo(":    " + memberDetails.getString("SSN"));
						} else {
							memberInfo.setSsn("");
						}
					}
					if (memberDetails.has("Ethnic")) {
						if (memberDetails.getString("Ethnic") != null) {
							memberInfo.setEthnic(memberDetails.getString("Ethnic"));
							LoggerUtil.logInfo(":    " + memberDetails.getString("Ethnic"));
						} else {
							memberInfo.setEthnic("");
						}
					}
					if (memberDetails.has("MStatus")) {
						if (memberDetails.getString("MStatus") != null) {
							memberInfo.setmStatus(memberDetails.getString("MStatus"));
							LoggerUtil.logInfo("MStatus:    " + memberDetails.getString("MStatus"));
						} else {
							memberInfo.setmStatus("");
						}
					}
					if (memberDetails.has("Religion")) {
						if (memberDetails.getString("Religion") != null) {
							memberInfo.setReligion(memberDetails.getString("Religion"));
							LoggerUtil.logInfo("Religion:    " + memberDetails.getString("Religion"));
						} else {
							memberInfo.setReligion("");
						}
					}
					if (memberDetails.has("Language")) {
						if (memberDetails.getString("Language") != null) {
							memberInfo.setPreferredLanguage(memberDetails.getString("Language"));
							LoggerUtil.logInfo("Language:    " + memberDetails.getString("Language"));
						} else {
							memberInfo.setPreferredLanguage("");
						}
					}
					if (memberDetails.has("Address")) {
						JSONArray addressResult = (JSONArray) memberDetails.get("Address");
						LoggerUtil.logInfo("Address:     " + addressResult);
						List<MemberAddressInfo> memberAddressInfoList = new ArrayList<MemberAddressInfo>();
						for (int i = 0; i < addressResult.length(); i++) {
							MemberAddressInfo memberAddressInfo = new MemberAddressInfo();
							JSONObject addressDetails = (JSONObject) addressResult.get(i);
							if (addressDetails.has("City")) {
								if (addressDetails.getString("City") != null) {
									memberAddressInfo.setCity(addressDetails.getString("City"));
									LoggerUtil.logInfo("City:    " + addressDetails.getString("City"));
								} else {
									memberAddressInfo.setCity("");
								}
							}
							if (addressDetails.has("AddressLine1")) {
								if (addressDetails.getString("AddressLine1") != null) {
									memberAddressInfo.setAddressLine1(addressDetails.getString("AddressLine1"));
									LoggerUtil.logInfo("AddressLine1:    " + addressDetails.getString("AddressLine1"));
								} else {
									memberAddressInfo.setAddressLine1("");
								}
							}
							if (addressDetails.has("AddressLine2")) {
								if (addressDetails.getString("AddressLine2") != null) {
									memberAddressInfo.setAddressLine2(addressDetails.getString("AddressLine2"));
									LoggerUtil.logInfo("AddressLine2:    " + addressDetails.getString("AddressLine2"));
								} else {
									memberAddressInfo.setAddressLine1("");
								}
							}
							if (addressDetails.has("AddressType")) {
								if (addressDetails.getString("AddressType") != null) {
									memberAddressInfo.setAddressType(addressDetails.getString("AddressType"));
									LoggerUtil.logInfo("AddressType:    " + addressDetails.getString("AddressType"));
								} else {
									memberAddressInfo.setAddressType("");
								}
							}
							if (addressDetails.has("PostalCode")) {
								if (addressDetails.getString("PostalCode") != null) {
									memberAddressInfo.setPostalCode(addressDetails.getString("PostalCode"));
									LoggerUtil.logInfo("Postal Code:    " + addressDetails.getString("PostalCode"));
								} else {
									memberAddressInfo.setPostalCode("");
								}
							}
							if (addressDetails.has("StateCode")) {
								if (addressDetails.getString("StateCode") != null) {
									memberAddressInfo.setState(addressDetails.getString("StateCode"));
									LoggerUtil.logInfo("State:    " + addressDetails.getString("StateCode"));
								} else {
									memberAddressInfo.setState("");
								}
							}
							memberAddressInfoList.add(memberAddressInfo);
						}
						memberInfo.setMemberAddressInfo(memberAddressInfoList);
					}
					if (memberDetails.has("Phone")) {
						JSONArray phoneResult = (JSONArray) memberDetails.get("Phone");
						LoggerUtil.logInfo("Phone:     " + phoneResult);
						List<MemberPhoneInfo> memberPhoneInfoList = new ArrayList<MemberPhoneInfo>();
						for (int i = 0; i < phoneResult.length(); i++) {
							MemberPhoneInfo memberPhoneInfo = new MemberPhoneInfo();
							JSONObject phoneDetails = (JSONObject) phoneResult.get(i);
							if (phoneDetails.has("Phone")) {
								if (phoneDetails.getString("Phone") != null) {
									memberPhoneInfo.setPhoneNumber(phoneDetails.getString("Phone"));
									LoggerUtil.logInfo("PhoneNumber:    " + phoneDetails.getString("Phone"));
								} else {
									memberPhoneInfo.setPhoneNumber("");
								}
								if (phoneDetails.has("PhoneId")) 
									if (phoneDetails.getString("PhoneId") != null) {
										memberPhoneInfo.setPhoneId(phoneDetails.getString("PhoneId"));
										LoggerUtil.logInfo("PhoneId:    " + phoneDetails.getString("PhoneId"));
									} else {
										memberPhoneInfo.setPhoneId("");
									}
								if (phoneDetails.getString("PhoneType") != null) {
									memberPhoneInfo.setPhoneType(phoneDetails.getString("PhoneType"));
									LoggerUtil.logInfo("Phone Type:    " + phoneDetails.getString("PhoneType"));
								} else {
									memberPhoneInfo.setPhoneType("");
								}
							}
							memberPhoneInfoList.add(memberPhoneInfo);
						}
						memberInfo.setMemberPhoneInfo(memberPhoneInfoList);
					}
					if (memberDetails.has("Email")) {
						JSONArray emailResult = (JSONArray) memberDetails.get("Email");
						LoggerUtil.logInfo("Email:     " + emailResult);
						List<MemberEmailInfo> memberEmailInfoList = new ArrayList<MemberEmailInfo>();
						for (int i = 0; i < emailResult.length(); i++) {
							MemberEmailInfo memberEmailInfo = new MemberEmailInfo();
							JSONObject emailDetails = (JSONObject) emailResult.get(i);
							if (emailDetails.has("EmailAddress")) {
								if (emailDetails.getString("EmailAddress") != null) {
									memberEmailInfo.setEmailAddress(emailDetails.getString("EmailAddress"));
									LoggerUtil.logInfo("Email Address:    " + emailDetails.getString("EmailAddress"));
								} else {
									memberEmailInfo.setEmailAddress("");
								}
								if (emailDetails.getString("EmailType") != null) {
									memberEmailInfo.setEmailType(emailDetails.getString("EmailType"));
									LoggerUtil.logInfo("Email Type:    " + emailDetails.getString("EmailType"));
								} else {
									memberEmailInfo.setEmailType("");
								}
							}
							memberEmailInfoList.add(memberEmailInfo);
						}
						memberInfo.setMemberEmailInfo(memberEmailInfoList);
					}
					LoggerUtil.logInfo("getmemberDetails:    " + memberDetails);
				} else if (memberSearchResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					// Update Access token Here
					LoggerUtil.logInfo("Un authorized : ");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LoggerUtil.logInfo("Exception found : " + e.getMessage());
				if (e.getMessage().equalsIgnoreCase("401 Unauthorized")) {
					ngRestTemplateService.nextGateAuthentication();
					getMemberInfo(euid);
				}
			}
		} else {
			ngRestTemplateService.nextGateAuthentication();
			getMemberInfo(euid);
		}
		return memberInfo;
	}
}