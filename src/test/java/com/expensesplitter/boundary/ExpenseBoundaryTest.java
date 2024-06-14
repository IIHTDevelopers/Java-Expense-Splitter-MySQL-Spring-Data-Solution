package com.expensesplitter.boundary;

import static com.expensesplitter.utils.TestUtils.boundaryTestFile;
import static com.expensesplitter.utils.TestUtils.currentTest;
import static com.expensesplitter.utils.TestUtils.testReport;
import static com.expensesplitter.utils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.expensesplitter.dto.ExpenseDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ExpenseBoundaryTest {

	private static Validator validator;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@After
	public void afterAll() {
		testReport();
	}

	@Test
	public void testDescriptionNotBlank() throws IOException {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setDescription(""); // Blank description to trigger the NotBlank validation
		expenseDTO.setAmount(100.0);
		expenseDTO.setPaidById(1L);
		expenseDTO.setSharedWithIds(new HashSet<>(Set.of(2L, 3L)));
		expenseDTO.setIsSettled(true);
		Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testAmountNotNullAndPositive() throws IOException {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setDescription("Valid Description");
		expenseDTO.setAmount(null); // Null amount to trigger the NotNull validation
		expenseDTO.setPaidById(1L);
		expenseDTO.setSharedWithIds(new HashSet<>(Set.of(2L, 3L)));
		expenseDTO.setIsSettled(true);
		Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testPayerIdNotNull() throws IOException {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setDescription("Valid Description");
		expenseDTO.setAmount(100.0);
		expenseDTO.setPaidById(null); // Null payerId to trigger the NotNull validation
		expenseDTO.setSharedWithIds(new HashSet<>(Set.of(2L, 3L)));
		expenseDTO.setIsSettled(true);
		Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testSharedWithUsersNotNull() throws IOException {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setDescription("Valid Description");
		expenseDTO.setAmount(100.0);
		expenseDTO.setPaidById(1L);
		expenseDTO.setSharedWithIds(null); // Null sharedWithIds to trigger the NotNull validation
		expenseDTO.setIsSettled(true);
		Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testSettlementStatusNotNull() throws IOException {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setDescription("Valid Description");
		expenseDTO.setAmount(100.0);
		expenseDTO.setPaidById(1L);
		expenseDTO.setSharedWithIds(new HashSet<>(Set.of(2L, 3L)));
		expenseDTO.setIsSettled(null); // Null isSettled to trigger the NotNull validation
		Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testDescriptionSizeExceeded() throws IOException {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		// Create a description string longer than the maximum allowed size of 500
		// characters
		String longDescription = new String(new char[501]).replace('\0', 'a');
		expenseDTO.setDescription(longDescription);
		expenseDTO.setAmount(100.0);
		expenseDTO.setPaidById(1L);
		expenseDTO.setSharedWithIds(new HashSet<>(Set.of(2L, 3L)));
		expenseDTO.setIsSettled(true);
		Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testAmountBelowMinimum() throws IOException {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setDescription("Valid Description");
		// Set amount below the minimum valid value of 0 (for testing negative amounts)
		expenseDTO.setAmount(-1.0);
		expenseDTO.setPaidById(1L);
		expenseDTO.setSharedWithIds(new HashSet<>(Set.of(2L, 3L)));
		expenseDTO.setIsSettled(true);
		Set<ConstraintViolation<ExpenseDTO>> violations = validator.validate(expenseDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

}
