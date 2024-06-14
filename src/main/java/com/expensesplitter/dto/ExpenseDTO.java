package com.expensesplitter.dto;

import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExpenseDTO {

	private Long id;

	@NotBlank(message = "Description is required")
	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;

	@NotNull(message = "Amount is required")
	@Min(value = 0, message = "Amount must be positive")
	private Double amount;

	@NotNull(message = "Payer is required")
	private Long paidById;

	@NotNull(message = "Shared with users is required")
	private Set<Long> sharedWithIds;

	@NotNull(message = "Settlement status is required")
	private Boolean isSettled;

	public ExpenseDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getPaidById() {
		return paidById;
	}

	public void setPaidById(Long paidById) {
		this.paidById = paidById;
	}

	public Set<Long> getSharedWithIds() {
		return sharedWithIds;
	}

	public void setSharedWithIds(Set<Long> sharedWithIds) {
		this.sharedWithIds = sharedWithIds;
	}

	public Boolean getIsSettled() {
		return isSettled;
	}

	public void setIsSettled(Boolean isSettled) {
		this.isSettled = isSettled;
	}

	@Override
	public String toString() {
		return "ExpenseDTO [id=" + id + ", description=" + description + ", amount=" + amount + ", paidById=" + paidById
				+ ", sharedWithIds=" + sharedWithIds + ", isSettled=" + isSettled + "]";
	}
}
