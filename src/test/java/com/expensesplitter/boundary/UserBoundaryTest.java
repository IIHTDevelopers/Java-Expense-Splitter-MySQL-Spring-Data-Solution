package com.expensesplitter.boundary;

import static com.expensesplitter.utils.TestUtils.boundaryTestFile;
import static com.expensesplitter.utils.TestUtils.currentTest;
import static com.expensesplitter.utils.TestUtils.testReport;
import static com.expensesplitter.utils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.expensesplitter.dto.UserDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserBoundaryTest {

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
	public void testNameNotBlank() throws IOException {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(""); // Empty name to trigger the NotBlank validation
		userDTO.setEmail("valid.email@example.com");
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testNameSizeExceeded() throws IOException {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(new String(new char[201]).replace('\0', 'a')); // Name longer than 200 characters
		userDTO.setEmail("valid.email@example.com");
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testEmailNotBlank() throws IOException {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("Valid Name");
		userDTO.setEmail(""); // Empty email to trigger the NotBlank validation
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testEmailFormatInvalid() throws IOException {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("Valid Name");
		userDTO.setEmail("invalid-email"); // Invalid email format
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}

	@Test
	public void testEmailSizeExceeded() throws IOException {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("Valid Name");
		String longEmail = "a".repeat(191) + "@example.com"; // Email longer than 200 characters
		userDTO.setEmail(longEmail);
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
		try {
			yakshaAssert(currentTest(), !violations.isEmpty(), boundaryTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, boundaryTestFile);
		}
	}
}
