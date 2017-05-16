/**
 * 
 */
package com.example.oauthproject.model.ng;
/**
 * @author Raja Rajeswari
 *
 */
public class ProviderManagedIdentifiersInfo {
	private String ContributingSystem;
	private String Id;
	private String IdTypeCode;
	private int ManagedIdIndex;
	private String ManagedIdentifiersId;
	public String getContributingSystem() {
		return ContributingSystem;
	}
	public void setContributingSystem(String contributingSystem) {
		ContributingSystem = contributingSystem;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getIdTypeCode() {
		return IdTypeCode;
	}
	public void setIdTypeCode(String idTypeCode) {
		IdTypeCode = idTypeCode;
	}
	public int getManagedIdIndex() {
		return ManagedIdIndex;
	}
	public void setManagedIdIndex(int managedIdIndex) {
		ManagedIdIndex = managedIdIndex;
	}
	public String getManagedIdentifiersId() {
		return ManagedIdentifiersId;
	}
	public void setManagedIdentifiersId(String managedIdentifiersId) {
		ManagedIdentifiersId = managedIdentifiersId;
	}
}
