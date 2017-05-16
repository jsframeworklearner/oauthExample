/**
 * 
 */
package com.example.oauthproject.model.ng;
/**
 * @author Raja Rajeswari
 *
 */
public class ProviderSpecialtiesInfo {
	private String ContributingSystem;
	private String SpecialDescription;
	private String SpecialtiesId;
	private int SpecialtiesIndex;
	private String Id;
	public String getContributingSystem() {
		return ContributingSystem;
	}
	public void setContributingSystem(String contributingSystem) {
		ContributingSystem = contributingSystem;
	}
	public String getSpecialDescription() {
		return SpecialDescription;
	}
	public void setSpecialDescription(String specialDescription) {
		SpecialDescription = specialDescription;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getSpecialtiesId() {
		return SpecialtiesId;
	}
	public void setSpecialtiesId(String specialtiesId) {
		SpecialtiesId = specialtiesId;
	}
	public int getSpecialtiesIndex() {
		return SpecialtiesIndex;
	}
	public void setSpecialtiesIndex(int specialtiesIndex) {
		SpecialtiesIndex = specialtiesIndex;
	}
}
