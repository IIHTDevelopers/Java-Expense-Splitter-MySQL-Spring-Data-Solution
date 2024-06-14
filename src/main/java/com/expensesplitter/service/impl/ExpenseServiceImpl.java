package com.expensesplitter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensesplitter.dto.ExpenseDTO;
import com.expensesplitter.entity.Expense;
import com.expensesplitter.entity.User;
import com.expensesplitter.exception.ResourceNotFoundException;
import com.expensesplitter.repo.ExpenseRepository;
import com.expensesplitter.repo.UserRepository;
import com.expensesplitter.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public ExpenseDTO createExpense(ExpenseDTO expenseDto) {
		Expense expense = convertToEntity(expenseDto);
		Expense savedExpense = expenseRepository.save(expense);
		return convertToDto(savedExpense);
	}

	@Override
	public ExpenseDTO getExpenseById(Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
		return convertToDto(expense);
	}

	@Override
	public ExpenseDTO updateExpense(Long expenseId, ExpenseDTO expenseDto) {
		Expense expense = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

		expense.setDescription(expenseDto.getDescription());
		expense.setAmount(expenseDto.getAmount());
		expense.setPaidBy(new User(expenseDto.getPaidById()));
		Expense updatedExpense = expenseRepository.save(expense);
		return convertToDto(updatedExpense);
	}

	@Override
	public boolean deleteExpense(Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
		expenseRepository.delete(expense);
		return true;
	}

	@Override
	public List<ExpenseDTO> listExpenses() {
		List<Expense> expenses = expenseRepository.findAll();
		return expenses.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public void settleExpense(Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
		expense.setSettled(true);
		expenseRepository.save(expense);
	}

	@Override
	public List<ExpenseDTO> listExpensesByUserId(Long userId) {
		boolean userExists = userRepository.existsById(userId);
		if (!userExists) {
			throw new ResourceNotFoundException("User not found");
		}
		List<Expense> allExpenses = expenseRepository.findExpensesByUserId(userId);
		return allExpenses.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public Map<String, Double> listUserBalances(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		List<Expense> expensesPaid = expenseRepository.findByPaidById(userId);
		List<Expense> sharedExpenses = expenseRepository.findBySharedWithId(userId);
		Map<String, Double> balances = new HashMap<>();
		for (Expense expense : expensesPaid) {
			double splitAmount = expense.getAmount() / (expense.getSharedWith().size() + 1); // Including the payer
			for (User participant : expense.getSharedWith()) {
				balances.put(participant.getName(), balances.getOrDefault(participant.getName(), 0.0) - splitAmount);
			}
		}
		for (Expense expense : sharedExpenses) {
			if (!expense.getPaidBy().getId().equals(userId)) { // Avoid double-counting
				double splitAmount = expense.getAmount() / (expense.getSharedWith().size() + 1); // Including the payer
				balances.put(expense.getPaidBy().getName(),
						balances.getOrDefault(expense.getPaidBy().getName(), 0.0) + splitAmount);
			}
		}
		return balances;
	}

//	@Override
//	public Double calculateBalanceBetweenTwoUsers(Long userId, Long otherUserId) {
//		if (!userRepository.existsById(userId) || !userRepository.existsById(otherUserId)) {
//			throw new ResourceNotFoundException("One or both users not found");
//		}
//		List<Expense> expensesInvolvingUser = expenseRepository.findExpensesInvolvingUsers(userId, otherUserId);
//		List<Expense> expensesInvolvingOtherUser = expenseRepository.findExpensesInvolvingUsers(otherUserId, userId);
//		double userOwesOther = calculateTotalOwed(expensesInvolvingUser, userId, otherUserId);
//		double otherOwesUser = calculateTotalOwed(expensesInvolvingOtherUser, otherUserId, userId);
//		return otherOwesUser - userOwesOther;
//	}
	@Override
	public Double calculateBalanceBetweenTwoUsers(Long userId, Long otherUserId) {
		if (!userRepository.existsById(userId) || !userRepository.existsById(otherUserId)) {
			throw new ResourceNotFoundException("One or both users not found");
		}
		Double netBalance = expenseRepository.findNetBalanceBetweenUsers(userId, otherUserId);
		return netBalance != null ? netBalance : 0.0;
	}

	private double calculateTotalOwed(List<Expense> expenses, Long payerId, Long participantId) {
		double totalOwed = 0.0;
		for (Expense expense : expenses) {
			if (expense.getSharedWith().stream().anyMatch(user -> user.getId().equals(participantId))) {
				int participantsCount = expense.getSharedWith().size() + 1; // Including the payer
				totalOwed += expense.getAmount() / participantsCount;
			}
		}
		return totalOwed;
	}

	private ExpenseDTO convertToDto(Expense expense) {
		ExpenseDTO dto = new ExpenseDTO();
		dto.setId(expense.getId());
		dto.setDescription(expense.getDescription());
		dto.setAmount(expense.getAmount());
		dto.setPaidById(expense.getPaidBy().getId());
		dto.setSharedWithIds(expense.getSharedWith().stream().map(User::getId).collect(Collectors.toSet()));
		dto.setIsSettled(expense.isSettled());
		return dto;
	}

	private Expense convertToEntity(ExpenseDTO expenseDto) {
		Expense expense = new Expense();
		if (expenseDto.getId() != null) { // Handle update case
			expense.setId(expenseDto.getId());
		}
		expense.setDescription(expenseDto.getDescription());
		expense.setAmount(expenseDto.getAmount());
		expense.setSettled(expenseDto.getIsSettled());
		User paidBy = userRepository.findById(expenseDto.getPaidById())
				.orElseThrow(() -> new RuntimeException("User not found"));
		expense.setPaidBy(paidBy);
		if (expenseDto.getSharedWithIds() != null && !expenseDto.getSharedWithIds().isEmpty()) {
			Set<User> sharedWith = expenseDto.getSharedWithIds().stream()
					.map(id -> userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")))
					.collect(Collectors.toSet());
			expense.setSharedWith(sharedWith);
		}
		return expense;
	}
}
