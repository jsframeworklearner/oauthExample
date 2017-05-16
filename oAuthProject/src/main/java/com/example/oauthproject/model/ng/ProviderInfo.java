package com.example.oauthproject.model.ng;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProviderInfo {
	private String euid;
	private String firstName;
	private String lastName;
	private String DOB;
	private String genderCode;
	private String email;
	private String directAddress;
	private List<String> fax;
	private String npi;
	private String message;
	private List<ProviderAddressInfo> providerAddressInfo;
	private List<ProviderManagedIdentifiersInfo> providerManagedIdentifiersInfo;
	private List<ProviderSpecialtiesInfo> providerSpecialtiesInfo;
	private List<ProviderCommunicationsInfo> providerCommunicationsInfo;
	private List<String> voice;
	private List<String> tin;
	private String status;
	private String createDateTime;
	private String createSystem;
	private String createUser;
	private String createFunction;
	private String systemCode;
	private String typeCode;
	private String providerid;
	private String localid;
	private String addressLine1;
	private String addressLine2;
	private String postalCode;
	private String city;
	private String stateCode;
	private String mrn;
	private String nameTitle;
	private String practitionerGroup;
	public String getEuid() {
		return euid;
	}
	public void setEuid(String euid) {
		this.euid = euid;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dob) {
		DOB = dob;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDirectAddress() {
		return directAddress;
	}
	public void setDirectAddress(String directAddress) {
		this.directAddress = directAddress;
	}
	public String getNpi() {
		return npi;
	}
	public void setNpi(String npi) {
		this.npi = npi;
	}
	public List<String> getVoice() {
		return voice;
	}
	public void setVoice(List<String> voice) {
		this.voice = voice;
	}
	public List<String> getFax() {
		return fax;
	}
	public void setFax(List<String> fax) {
		this.fax = fax;
	}
	public List<ProviderAddressInfo> getProviderAddressInfo() {
		return providerAddressInfo;
	}
	public void setProviderAddressInfo(List<ProviderAddressInfo> providerAddressInfo) {
		this.providerAddressInfo = providerAddressInfo;
	}		
	public List<String> getTin() {
		return tin;
	}
	public void setTin(List<String> tin) {
		this.tin = tin;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}
	public String getCreateSystem() {
		return createSystem;
	}
	public void setCreateSystem(String createSystem) {
		this.createSystem = createSystem;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateFunction() {
		return createFunction;
	}
	public void setCreateFunction(String createFunction) {
		this.createFunction = createFunction;
	}
	public List<ProviderManagedIdentifiersInfo> getProviderManagedIdentifiersInfo() {
		return providerManagedIdentifiersInfo;
	}
	public void setProviderManagedIdentifiersInfo(List<ProviderManagedIdentifiersInfo> providerManagedIdentifiersInfo) {
		this.providerManagedIdentifiersInfo = providerManagedIdentifiersInfo;
	}
	public List<ProviderSpecialtiesInfo> getProviderSpecialtiesInfo() {
		return providerSpecialtiesInfo;
	}
	public void setProviderSpecialtiesInfo(List<ProviderSpecialtiesInfo> providerSpecialtiesInfo) {
		this.providerSpecialtiesInfo = providerSpecialtiesInfo;
	}
	public List<ProviderCommunicationsInfo> getProviderCommunicationsInfo() {
		return providerCommunicationsInfo;
	}
	public void setProviderCommunicationsInfo(List<ProviderCommunicationsInfo> providerCommunicationsInfo) {
		this.providerCommunicationsInfo = providerCommunicationsInfo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getProviderid() {
		return providerid;
	}
	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	public String getLocalid() {
		return localid;
	}
	public void setLocalid(String localid) {
		this.localid = localid;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getMrn() {
		return mrn;
	}
	public void setMrn(String mrn) {
		this.mrn = mrn;
	}
	public String getNameTitle() {
		return nameTitle;
	}
	public void setNameTitle(String nameTitle) {
		this.nameTitle = nameTitle;
	}
	public String getPractitionerGroup() {
		return practitionerGroup;
	}
	public void setPractitionerGroup(String practitionerGroup) {
		this.practitionerGroup = practitionerGroup;
	}
}