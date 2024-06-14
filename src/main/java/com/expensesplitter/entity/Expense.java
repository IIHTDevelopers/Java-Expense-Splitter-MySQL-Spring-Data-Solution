package com.expensesplitter.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "description", nullable = false, length = 500)
	private String description;

	@Column(name = "amount", nullable = false)
	private double amount;

	@ManyToOne
	@JoinColumn(name = "paid_by_user_id", nullable = false)
	private User paidBy;

	@ManyToMany
	@JoinTable(name = "expense_shared", joinColumns = @JoinColumn(name = "expense_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> sharedWith;

	@Column(name = "is_settled", nullable = false)
	private boolean isSettled;

	public Expense() {
	}

	public Expense(String description, double amount, User paidBy, Set<User> sharedWith, boolean isSettled) {
		this.description = description;
		this.amount = amount;
		this.paidBy = paidBy;
		this.sharedWith = sharedWith;
		this.isSettled = isSettled;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(User paidBy) {
		this.paidBy = paidBy;
	}

	public Set<User> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(Set<User> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public boolean isSettled() {
		return isSettled;
	}

	public void setSettled(boolean isSettled) {
		this.isSettled = isSettled;
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", description=" + description + ", amount=" + amount + ", paidBy=" + paidBy
				+ ", sharedWith=" + sharedWith + ", isSettled=" + isSettled + "]";
	}
}
