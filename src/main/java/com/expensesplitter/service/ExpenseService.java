package com.expensesplitter.service;

import java.util.List;
import java.util.Map;

import com.expensesplitter.dto.ExpenseDTO;

public interface ExpenseService {
	ExpenseDTO createExpense(ExpenseDTO expenseDto);

	ExpenseDTO getExpenseById(Long expenseId);

	ExpenseDTO updateExpense(Long expenseId, ExpenseDTO expenseDto);

	boolean deleteExpense(Long expenseId);

	List<ExpenseDTO> listExpenses();

	void settleExpense(Long expenseId);

	List<ExpenseDTO> listExpensesByUserId(Long userId);

	Map<String, Double> listUserBalances(Long userId);

	Double calculateBalanceBetweenTwoUsers(Long userId, Long otherUserId);
}
