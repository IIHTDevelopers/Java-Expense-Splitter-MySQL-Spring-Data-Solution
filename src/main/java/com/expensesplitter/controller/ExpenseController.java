package com.expensesplitter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensesplitter.dto.ExpenseDTO;
import com.expensesplitter.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

	private final ExpenseService expenseService;

	@Autowired
	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@PostMapping
	public ResponseEntity<ExpenseDTO> createExpense(@Valid @RequestBody ExpenseDTO expenseDto) {
		ExpenseDTO createdExpense = expenseService.createExpense(expenseDto);
		return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
	}

	@PutMapping("/{expenseId}")
	public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long expenseId,
			@Valid @RequestBody ExpenseDTO expenseDto) {
		ExpenseDTO updatedExpense = expenseService.updateExpense(expenseId, expenseDto);
		return ResponseEntity.ok(updatedExpense);
	}

	@DeleteMapping("/{expenseId}")
	public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId) {
		expenseService.deleteExpense(expenseId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{expenseId}")
	public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long expenseId) {
		ExpenseDTO expenseDto = expenseService.getExpenseById(expenseId);
		return ResponseEntity.ok(expenseDto);
	}

	@GetMapping
	public ResponseEntity<List<ExpenseDTO>> listExpenses() {
		List<ExpenseDTO> expenses = expenseService.listExpenses();
		return ResponseEntity.ok(expenses);
	}

	@PostMapping("/{expenseId}/settle")
	public ResponseEntity<Void> settleExpense(@PathVariable Long expenseId) {
		expenseService.settleExpense(expenseId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ExpenseDTO>> listExpensesByUserId(@PathVariable Long userId) {
		List<ExpenseDTO> expenses = expenseService.listExpensesByUserId(userId);
		return ResponseEntity.ok(expenses);
	}

	@GetMapping("/balances/{userId}")
	public ResponseEntity<Map<String, Double>> listUserBalances(@PathVariable Long userId) {
		Map<String, Double> balances = expenseService.listUserBalances(userId);
		return ResponseEntity.ok(balances);
	}

	@GetMapping("/balance/{userId}/{otherUserId}")
	public ResponseEntity<Double> calculateBalanceBetweenTwoUsers(@PathVariable Long userId,
			@PathVariable Long otherUserId) {
		Double balance = expenseService.calculateBalanceBetweenTwoUsers(userId, otherUserId);
		return ResponseEntity.ok(balance);
	}
}
