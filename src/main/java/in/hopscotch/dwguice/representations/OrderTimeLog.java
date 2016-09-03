package in.hopscotch.dwguice.representations;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class OrderTimeLog {
	@NotBlank
	private Integer orderId;
	
	@NotBlank
	private Integer orderItemId;
	
	@NotBlank
	private String unitType;
	
	@NotBlank
	@Length(max=5)
	private String statusCode;
	private String milestone;
	private String remark;
	private Date actualDate;
	private Date promisedDate;
	private Date expectedDate;
	private Date createdDate;
	private String createdBy;
	
	
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMilestone() {
		return milestone;
	}
	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	public Date getPromisedDate() {
		return promisedDate;
	}
	public void setPromisedDate(Date promisedDate) {
		this.promisedDate = promisedDate;
	}
	public Date getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}
	
}
