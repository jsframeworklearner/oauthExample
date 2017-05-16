package com.example.oauthproject.model.ng;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateInfo 
{
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int transactionId;
	private String euid;
	private String lid;
	private String systemCode;
	private String recordChanged;
	private String overlay;
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public String getEuid() {
		return euid;
	}
	public void setEuid(String euid) {
		this.euid = euid;
	}
	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getRecordChanged() {
		return recordChanged;
	}
	public void setRecordChanged(String recordChanged) {
		this.recordChanged = recordChanged;
	}
	public String getOverlay() {
		return overlay;
	}
	public void setOverlay(String overlay) {
		this.overlay = overlay;
	}
}