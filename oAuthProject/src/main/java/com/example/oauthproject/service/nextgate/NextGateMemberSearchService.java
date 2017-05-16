package com.example.oauthproject.service.nextgate;
import java.util.ArrayList;
import java.util.List;

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
import com.example.oauthproject.model.ng.MemberAuxInfo;
import com.example.oauthproject.model.ng.MemberEmailInfo;
import com.example.oauthproject.model.ng.MemberInfo;
import com.example.oauthproject.model.ng.MemberPhoneInfo;
import com.example.oauthproject.service.common.NGRESTTemplateService;
import com.example.oauthproject.util.LoggerUtil;
@Service
/**
 * NextGate Member Search Service class
 * @version 1.0 17 Oct 2016
 * @author ****
 */
public class NextGateMemberSearchService {
	@Value("${nextgate.memberSearch.url}")
	private String nextgateMemberSearchURL;
	@Value("${nextgate.getMemberInfo.url}")
	private String nextgateMemberInfoURL;
	@Value("${nextgate.membersSearch.url}")
	private String nextgateMembersInfoURL;
	@Value("${nextgate.contentType}")
	private String nextgateContentType;
	@Autowired
	NGRESTTemplateService ngRestTemplateService;
	RestTemplate restTemplate = new RestTemplate();
	// TODO: REFACTOR
	// Search for Member
	public List<MemberInfo> ngSearchMember(MemberInfo memberInfo) throws JSONException 
	{
		List<MemberInfo> memberSearchResults = new ArrayList<MemberInfo>();
		if (NGRESTTemplateService.cookies.get("Cookie") != null) 
		{
			JSONObject memberSearchData = new JSONObject();
			// Hard Coding Values for Testing Purposes. These should be provided as Input
			//	LoggerUtil.logInfo("memberInfo.getLastName()"+memberInfo.getLastName());
			LoggerUtil.logInfo("memberInfo.getSearchText"+memberInfo.getSearchText());
			if(memberInfo.getSearchText()!=null && !memberInfo.getSearchText().isEmpty()){
				String patientName=memberInfo.getSearchText();
				String[] patientNames = new String[2];
				if(patientName != null && !patientName.isEmpty()){
					if(patientName.contains(",")){
						String[] names = patientName.split(",");
						names[0].trim();names[1].trim();
						if(!names[0].isEmpty()){
							patientNames[0] = names[0];
							memberSearchData.put("LastName",patientNames[0]);
						}
						if(!names[1].isEmpty()){
							patientNames[1] = names[1];
							memberSearchData.put("FirstName",patientNames[1]);
						}
					}else{
						patientNames[0] = patientName.trim();patientNames[1] = patientName.trim();
						memberSearchData.put("LastName",patientName);
					//	memberSearchData.put("FirstName",patientName);
					}
				}	
			}
			if(memberInfo.getLastName()!=null && !memberInfo.getLastName().isEmpty()){
				memberSearchData.put("LastName",memberInfo.getLastName());
			}
	//		LoggerUtil.logInfo("memberInfo.getFirstName()"+memberInfo.getFirstName());
			if(memberInfo.getFirstName()!=null && !memberInfo.getFirstName().isEmpty()){
				memberSearchData.put("FirstName",memberInfo.getFirstName());
			}
	//		LoggerUtil.logInfo("memberInfo.getGender()"+memberInfo.getGender());
			if(memberInfo.getGender()!=null && !memberInfo.getGender().isEmpty()){
				memberSearchData.put("Gender",memberInfo.getGender());
			}
	//		LoggerUtil.logInfo("dob"+memberInfo.getDOB());
			if(memberInfo.getDOB()!=null && !memberInfo.getDOB().isEmpty()){
				memberSearchData.put("DOB",memberInfo.getDOB());
			}
			if(memberInfo.getPhoneNumber()!=null && !memberInfo.getPhoneNumber().isEmpty()){
				JSONObject phoneData = new JSONObject();
				phoneData.put("Phone", memberInfo.getPhoneNumber());
				phoneData.put("PhoneType", "CH");
				JSONArray phoneArray = new JSONArray();
				phoneArray.put(phoneData);
				memberSearchData.put("Phone", phoneArray);			
			}
		//	LoggerUtil.logInfo("Member Reg RequestBody " + memberSearchData.toString());
			HttpEntity<String> memberSearchEntity = new HttpEntity<String>(memberSearchData.toString(),
					ngRestTemplateService.setHeadersforAPICalls());
			try 
			{
				ResponseEntity<String> memberSearchResponse = restTemplate.exchange(nextgateMemberSearchURL, HttpMethod.POST, memberSearchEntity, String.class);
		//		LoggerUtil.logInfo("Status code ' + " + memberSearchResponse.getStatusCode());
				if (memberSearchResponse.getStatusCode() == HttpStatus.OK) 
				{
					JSONObject result = new JSONObject(memberSearchResponse.getBody());
				//	LoggerUtil.logInfo(memberSearchResponse.getBody());
					long resultCount = result.getLong("resultsFound");
					LoggerUtil.logInfo("resultsFound:    " + resultCount);
					if(resultCount==0){
							if(memberInfo.getSearchText()!=null && !memberInfo.getSearchText().isEmpty()){
								String patientName=memberInfo.getSearchText();
								LoggerUtil.logInfo("if second "+patientName);
								memberSearchData.put("FirstName",patientName);
							}
					}	
					memberSearchEntity = new HttpEntity<String>(memberSearchData.toString(),
								ngRestTemplateService.setHeadersforAPICalls());
					memberSearchResponse = restTemplate.exchange(nextgateMemberSearchURL, HttpMethod.POST, memberSearchEntity, String.class);
					//		LoggerUtil.logInfo("Status code ' + " + memberSearchResponse.getStatusCode());
					if (memberSearchResponse.getStatusCode() == HttpStatus.OK) 
					{
						result = new JSONObject(memberSearchResponse.getBody());
							//	LoggerUtil.logInfo(memberSearchResponse.getBody());
						resultCount = result.getLong("resultsFound");
						LoggerUtil.logInfo("resultsFound:    " + resultCount);
						JSONArray searchResult = (JSONArray) result.get("searchResults");
						if (searchResult.length() > 1)
						{
							JSONArray euids = new JSONArray();
							for (int i = 0; i < searchResult.length(); i++) 
							{
								JSONObject memberRecord = (JSONObject) searchResult.get(i);
								LoggerUtil.logInfo("euid:   " + memberRecord.getString("euid"));
								euids.put(memberRecord.getString("euid"));
							}
							memberSearchResults = getMembersInfo(euids);
						}
					else
					{
						for (int i = 0; i < searchResult.length(); i++) 
						{
							JSONObject memberRecord = (JSONObject) searchResult.get(i);
							memberSearchResults.add(getMemberInfo(memberRecord.getString("euid")));
							LoggerUtil.logInfo("euid:   " + memberRecord.getString("euid"));
						}
					}
				} 
				else if (memberSearchResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) 
				{
					// Update Access token Here
					LoggerUtil.logInfo("Un authorized : ");
				}
				}else if (memberSearchResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) 
				{
					// Update Access token Here
					LoggerUtil.logInfo("Un authorized : ");
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				LoggerUtil.logInfo("Exception found : " + e.getMessage());
				if (e.getMessage().equalsIgnoreCase("401 Unauthorized")) 
				{
					ngRestTemplateService.nextGateAuthentication();
					memberSearchResults = ngSearchMember(memberInfo);
				}
			}
		} 
		else 
		{
			ngRestTemplateService.nextGateAuthentication();
			memberSearchResults = ngSearchMember(memberInfo);
		}
		return memberSearchResults;
	}
	// Get Member SBR using EUID
	private MemberInfo getMemberInfo(String euid) 
	{
		MemberInfo memberInfo = new MemberInfo();
		if (NGRESTTemplateService.cookies.get("Cookie") != null) 
		{
			HttpEntity<String> memberSearchEntity = new HttpEntity<String>(ngRestTemplateService.setHeadersforAPICalls());
			try 
			{
				ResponseEntity<String> memberSearchResponse = restTemplate.exchange(nextgateMemberInfoURL + euid, HttpMethod.GET, memberSearchEntity, String.class);
			///	LoggerUtil.logInfo("Status code ' + " + memberSearchResponse.getStatusCode());
				if (memberSearchResponse.getStatusCode() == HttpStatus.OK) 
				{
				//	memberInfo.setEuid(euid);
					JSONObject result = new JSONObject(memberSearchResponse.getBody());
					LoggerUtil.logInfo(memberSearchResponse.getBody());
					result.put("euid", euid);
					memberInfo = setMemberDemographics(result,euid);
					memberInfo.setEuid(euid);
				} 
				else if (memberSearchResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) 
				{
					// Update Access token Here
					LoggerUtil.logInfo("Un authorized : ");
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				LoggerUtil.logInfo("Exception found : " + e.getMessage());
				if (e.getMessage().equalsIgnoreCase("401 Unauthorized")) 
				{
					ngRestTemplateService.nextGateAuthentication();
					getMemberInfo(euid);
				}
			}
		} 
		else 
		{
			ngRestTemplateService.nextGateAuthentication();
			getMemberInfo(euid);
		}
		return memberInfo;
	}
	// Set Member Demographics to display
	public static MemberInfo setMemberDemographics(JSONObject result,String euid) throws JSONException 
	{
		MemberInfo memberInfo = new MemberInfo();
		JSONObject memberDetails = (JSONObject) result.get("Person");
		memberInfo.setEuid(euid);
		if (result.has("CreateDateTime")) 
		{
			if (result.getString("CreateDateTime") != null) 
			{
				memberInfo.setCreateDateTime(result.getString("CreateDateTime"));
			//	LoggerUtil.logInfo("CreateDateTime:    " + result.getString("CreateDateTime"));
			} else 
			{
				memberInfo.setCreateDateTime("");
			}
		}
		if (result.has("CreateSystem")) 
		{
			if (result.getString("CreateSystem") != null) 
			{
				memberInfo.setCreateSystem(result.getString("CreateSystem"));
				//LoggerUtil.logInfo("Create System:    " + result.getString("CreateSystem"));
			} else 
			{
				memberInfo.setCreateSystem("");
			}
		}
		if (result.has("CreateUser")) 
		{
			if (result.getString("CreateUser") != null) 
			{
				memberInfo.setCreateUser(result.getString("CreateUser"));
			//	LoggerUtil.logInfo("Create User:    " + result.getString("CreateUser"));
			} else 
			{
				memberInfo.setCreateUser("");
			}
		}
		if (result.has("Status")) 
		{
			if (result.getString("Status") != null) 
			{
				memberInfo.setStatus(result.getString("Status"));
				//LoggerUtil.logInfo("Status:    " + result.getString("Status"));
			} else 
			{
				memberInfo.setStatus("");
			}
		}
		if (memberDetails.has("LastName")) 
		{
			if (memberDetails.getString("LastName") != null) 
			{
				memberInfo.setLastName(memberDetails.getString("LastName"));
			//	LoggerUtil.logInfo("LastName:    " + memberDetails.getString("LastName"));
			} else 
			{
				memberInfo.setLastName("");
			}
		}
		if (memberDetails.has("MiddleName")) 
		{
			if (memberDetails.getString("MiddleName") != null) 
			{
				memberInfo.setMiddleName(memberDetails.getString("MiddleName"));
			//	LoggerUtil.logInfo("MiddleName:    " + memberDetails.getString("MiddleName"));
			} else 
			{
				memberInfo.setMiddleName("");
			}
		}
		if (memberDetails.has("FirstName")) 
		{
			if (memberDetails.getString("FirstName") != null) 
			{
				memberInfo.setFirstName(memberDetails.getString("FirstName"));
			//	LoggerUtil.logInfo("First Name:    " + memberDetails.getString("FirstName"));
			} else 
			{
				memberInfo.setFirstName("");
			}
		}
		if (memberDetails.has("Gender")) 
		{
			if (memberDetails.getString("Gender") != null) 
			{
				memberInfo.setGender(memberDetails.getString("Gender"));
				//LoggerUtil.logInfo("Gender:    " + memberDetails.getString("Gender"));
			} else 
			{
				memberInfo.setGender("");
			}
		}
		if (memberDetails.has("DOB")) 
		{
			if (memberDetails.getString("DOB") != null) 
			{
				memberInfo.setDOB(memberDetails.getString("DOB"));
			//	LoggerUtil.logInfo("Date of Birth:    " + memberDetails.getString("DOB"));
			} else 
			{
				memberInfo.setDOB("");
			}
		}
		if (memberDetails.has("SSN")) 
		{
			if (memberDetails.getString("SSN") != null) 
			{
				memberInfo.setSsn(memberDetails.getString("SSN"));
			//	LoggerUtil.logInfo(":    " + memberDetails.getString("SSN"));
			} else 
			{
				memberInfo.setSsn("");
			}
		}
		if (memberDetails.has("Race")) 
		{
			if (memberDetails.getString("Race") != null) 
			{
				memberInfo.setRace(memberDetails.getString("Race"));
			//	LoggerUtil.logInfo("Race:    " + memberDetails.getString("Race"));
			} else 
			{
				memberInfo.setRace("");
			}
		}
		if (memberDetails.has("Language")) 
		{
			if (memberDetails.getString("Language") != null) 
			{
				memberInfo.setPreferredLanguage(memberDetails.getString("Language"));
			//	LoggerUtil.logInfo("Language:    " + memberDetails.getString("Language"));
			} else 
			{
				memberInfo.setPreferredLanguage("");
			}
		}
		if (memberDetails.has("Ethnic")) 
		{
			if (memberDetails.getString("Ethnic") != null) 
			{
				memberInfo.setEthnic(memberDetails.getString("Ethnic"));
				//LoggerUtil.logInfo("Ethnic:    " + memberDetails.getString("Ethnic"));
			} else 
			{
				memberInfo.setEthnic("");
			}
		}
		if (memberDetails.has("MStatus")) 
		{
			if (memberDetails.getString("MStatus") != null) 
			{
				memberInfo.setmStatus(memberDetails.getString("MStatus"));
			//	LoggerUtil.logInfo("MStatus:    " + memberDetails.getString("MStatus"));
			} else 
			{
				memberInfo.setmStatus("");
			}
		}
		if (memberDetails.has("Maiden")) 
		{
			if (memberDetails.getString("Maiden") != null) 
			{
				memberInfo.setMaidenName(memberDetails.getString("Maiden"));
			//	LoggerUtil.logInfo(":    " + memberDetails.getString("Maiden"));
			} else 
			{
				memberInfo.setMaidenName("DriversLicense");
			}
		}
		if (memberDetails.has("DriversLicense")) 
		{
			if (memberDetails.getString("DriversLicense") != null) 
			{
				memberInfo.setDriversLicense(memberDetails.getString("DriversLicense"));
			//	LoggerUtil.logInfo("DriversLicense:    " + memberDetails.getString("DriversLicense"));
			} else 
			{
				memberInfo.setDriversLicense("");
			}
		}
	//	LoggerUtil.logInfo("memberDetails:    " + memberDetails);
		if (memberDetails.has("Address")) 
		{
			JSONArray addressResult = (JSONArray) memberDetails.get("Address");
		//	LoggerUtil.logInfo("Address:     " + addressResult);
			List<MemberAddressInfo> memberAddressInfoList = new ArrayList<MemberAddressInfo>();
			for (int i = 0; i < addressResult.length(); i++) 
			{
				MemberAddressInfo memberAddressInfo = new MemberAddressInfo();
				JSONObject addressDetails = (JSONObject) addressResult.get(i);
				if (addressDetails.has("City")) 
				{
					if (addressDetails.getString("City") != null) 
					{
						memberAddressInfo.setCity(addressDetails.getString("City"));
				//		LoggerUtil.logInfo("City:    " + addressDetails.getString("City"));
					} 
					else 
					{
						memberAddressInfo.setCity("");
					}
				}
				if (addressDetails.has("AddressLine1")) 
				{
					if (addressDetails.getString("AddressLine1") != null) 
					{
						memberAddressInfo.setAddressLine1(addressDetails.getString("AddressLine1"));
					//	LoggerUtil.logInfo("AddressLine1:    " + addressDetails.getString("AddressLine1"));
					} else 
					{
						memberAddressInfo.setAddressLine1("");
					}
				}
				if (addressDetails.has("AddressType")) 
				{
					if (addressDetails.getString("AddressType") != null) 
					{
						memberAddressInfo.setAddressType(addressDetails.getString("AddressType"));
					//	LoggerUtil.logInfo("AddressType:    " + addressDetails.getString("AddressType"));
					} 
					else 
					{
						memberAddressInfo.setAddressType("");
					}
				}
				if (addressDetails.has("PostalCode")) 
				{
					if (addressDetails.getString("PostalCode") != null) 
					{
						memberAddressInfo.setPostalCode(addressDetails.getString("PostalCode"));
				//		LoggerUtil.logInfo("Postal Code:    " + addressDetails.getString("PostalCode"));
					} else 
					{
						memberAddressInfo.setPostalCode("");
					}
				}
				if (addressDetails.has("StateCode")) 
				{
					if (addressDetails.getString("StateCode") != null) 
					{
						memberAddressInfo.setState(addressDetails.getString("StateCode"));
				//		LoggerUtil.logInfo("State:    " + addressDetails.getString("StateCode"));
					} else 
					{
						memberAddressInfo.setState("");
					}
				}
				memberAddressInfoList.add(memberAddressInfo);
			}
			memberInfo.setMemberAddressInfo(memberAddressInfoList);
		}
		if (memberDetails.has("Phone")) 
		{
			JSONArray phoneResult = (JSONArray) memberDetails.get("Phone");
		//	LoggerUtil.logInfo("Phone:     " + phoneResult);
			List<MemberPhoneInfo> memberPhoneInfoList = new ArrayList<MemberPhoneInfo>();
			for (int i = 0; i < phoneResult.length(); i++) 
			{
				MemberPhoneInfo memberPhoneInfo = new MemberPhoneInfo();
				JSONObject phoneDetails = (JSONObject) phoneResult.get(i);
				if (phoneDetails.has("Phone")) 
				{
					if (phoneDetails.getString("Phone") != null) 
					{
						memberPhoneInfo.setPhoneNumber(phoneDetails.getString("Phone"));
				//		LoggerUtil.logInfo("PhoneNumber:    " + phoneDetails.getString("Phone"));
					} 
					else 
					{
						memberPhoneInfo.setPhoneNumber("");
					}
					if (phoneDetails.getString("PhoneType") != null) 
					{
						memberPhoneInfo.setPhoneType(phoneDetails.getString("PhoneType"));
					//	LoggerUtil.logInfo("Phone Type:    " + phoneDetails.getString("PhoneType"));
					} 
					else 
					{
						memberPhoneInfo.setPhoneType("");
					}
				}
				memberPhoneInfoList.add(memberPhoneInfo);
			}
			memberInfo.setMemberPhoneInfo(memberPhoneInfoList);
		}
		if (memberDetails.has("Email")) 
		{
			JSONArray emailResult = (JSONArray) memberDetails.get("Email");
		//	LoggerUtil.logInfo("Email:     " + emailResult);
			List<MemberEmailInfo> memberEmailInfoList = new ArrayList<MemberEmailInfo>();
			for (int i = 0; i < emailResult.length(); i++) 
			{
				MemberEmailInfo memberEmailInfo = new MemberEmailInfo();
				JSONObject emailDetails = (JSONObject) emailResult.get(i);
				if (emailDetails.has("EmailAddress")) 
				{
					if (emailDetails.getString("EmailAddress") != null) 
					{
						memberEmailInfo.setEmailAddress(emailDetails.getString("EmailAddress"));
					//	LoggerUtil.logInfo("Email Address:    " + emailDetails.getString("EmailAddress"));
					} 
					else 
					{
						memberEmailInfo.setEmailAddress("");
					}
					if (emailDetails.getString("EmailType") != null) 
					{
						memberEmailInfo.setEmailType(emailDetails.getString("EmailType"));
				//		LoggerUtil.logInfo("Email Type:    " + emailDetails.getString("EmailType"));
					} 
					else 
					{
						memberEmailInfo.setEmailType("");
					}
				}
				memberEmailInfoList.add(memberEmailInfo);
			}
			memberInfo.setMemberEmailInfo(memberEmailInfoList);
		}
		if (memberDetails.has("AuxId")) 
		{
			JSONArray AuxResult = (JSONArray) memberDetails.get("AuxId");
			LoggerUtil.logInfo("AuxId:     " + AuxResult);
			List<MemberAuxInfo> memberAuxInfoList = new ArrayList<MemberAuxInfo>();
			for (int i = 0; i < AuxResult.length(); i++) 
			{
				MemberAuxInfo memberAuxInfo = new MemberAuxInfo();
				JSONObject AuxDetails = (JSONObject) AuxResult.get(i);
				if (AuxDetails.has("AssigningAuthorityNamespace")) 
				{
					if (AuxDetails.getString("AssigningAuthorityNamespace") != null) 
					{
						memberAuxInfo.setAssigningAuthorityNamespace(AuxDetails.getString("AssigningAuthorityNamespace"));
				//		LoggerUtil.logInfo("Assigning Authority Namespace:    "+ AuxDetails.getString("AssigningAuthorityNamespace"));
					} 
					else 
					{
						memberAuxInfo.setAssigningAuthorityNamespace("");
					}
					if (AuxDetails.getString("AssigningAuthorityUID") != null) 
					{
						memberAuxInfo.setAssigningAuthorityUID(AuxDetails.getString("AssigningAuthorityUID"));
					//	LoggerUtil.logInfo("AssigningAuthorityUID:    " + AuxDetails.getString("AssigningAuthorityUID"));
					} 
					else 
					{
						memberAuxInfo.setAssigningAuthorityUID("");
					}
					if (AuxDetails.getString("AssigningAuthorityUIDType") != null) 
					{
						memberAuxInfo.setAssigningAuthorityUIDType(AuxDetails.getString("AssigningAuthorityUIDType"));
				//		LoggerUtil.logInfo("AssigningAuthorityUIDType:    " + AuxDetails.getString("AssigningAuthorityUIDType"));
					} 
					else 
					{
						memberAuxInfo.setAssigningAuthorityUIDType("");
					}
					if (AuxDetails.getString("AuxIdDef") != null) 
					{
						memberAuxInfo.setAuxIdDefinition(AuxDetails.getString("AuxIdDef"));
				//		LoggerUtil.logInfo("AuxId Definition:    " + AuxDetails.getString("AuxIdDef"));
					} 
					else 
					{
						memberAuxInfo.setAuxIdDefinition("");
					}
					if (AuxDetails.getString("Id") != null) 
					{
						memberAuxInfo.setAuxId(AuxDetails.getString("Id"));
					//	LoggerUtil.logInfo("AuxId:    " + AuxDetails.getString("Id"));
					} 
					else 
					{
						memberAuxInfo.setAuxId("");
					}
				}
				memberAuxInfoList.add(memberAuxInfo);
			}
			memberInfo.setMemberAuxInfo(memberAuxInfoList);
		}
		return memberInfo;
	}
	// Get Multiple Member SBRs with single API call: /sbrsgetter, Input: EUIDs, Output: SBRs 
	private List<MemberInfo> getMembersInfo(JSONArray euids) throws JSONException
	{
		List<MemberInfo> membersSearchResults = new ArrayList<MemberInfo>();
		if (NGRESTTemplateService.cookies.get("Cookie") != null) 
		{
			HttpEntity<String> memberSearchEntity = new HttpEntity<String>(euids.toString(), ngRestTemplateService.setHeadersforAPICalls());
			try 
			{
				ResponseEntity<String> memberSearchResponse = restTemplate.exchange(nextgateMembersInfoURL, HttpMethod.POST, memberSearchEntity, String.class);
			//	LoggerUtil.logInfo("Status code ' + " + memberSearchResponse.getStatusCode());
				if (memberSearchResponse.getStatusCode() == HttpStatus.OK) 
				{
					JSONArray searchResult = new JSONArray(memberSearchResponse.getBody());
					LoggerUtil.logInfo(memberSearchResponse.getBody());
					for (int i = 0; i < searchResult.length(); i++) 
					{
						JSONObject memberRecord = (JSONObject) searchResult.get(i);
						LoggerUtil.logInfo("euid:   " + memberRecord.getString("EUID"));			
						membersSearchResults.add(setMemberDemographics(memberRecord,memberRecord.getString("EUID")));						
					}
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				LoggerUtil.logInfo("Exception found : " + e.getMessage());
			}
		}
		else 
		{
			ngRestTemplateService.nextGateAuthentication();
			getMembersInfo(euids);
		}
		return membersSearchResults;
	}
}
