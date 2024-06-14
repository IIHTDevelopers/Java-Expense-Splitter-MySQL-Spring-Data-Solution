package com.expensesplitter.functional;

import static com.expensesplitter.utils.MasterData.asJsonString;
import static com.expensesplitter.utils.MasterData.getExpenseDTO;
import static com.expensesplitter.utils.TestUtils.businessTestFile;
import static com.expensesplitter.utils.TestUtils.currentTest;
import static com.expensesplitter.utils.TestUtils.testReport;
import static com.expensesplitter.utils.TestUtils.yakshaAssert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.expensesplitter.controller.ExpenseController;
import com.expensesplitter.dto.ExpenseDTO;
import com.expensesplitter.service.ExpenseService;

@WebMvcTest(ExpenseController.class)
@AutoConfigureMockMvc
public class ExpenseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ExpenseService expenseService;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testCreateExpense() throws Exception {
		ExpenseDTO expenseDTO = getExpenseDTO();
		when(expenseService.createExpense(any(ExpenseDTO.class))).thenReturn(expenseDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/expenses").content(asJsonString(expenseDTO))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), result.getResponse().getStatus() == HttpStatus.CREATED.value() ? true : false,
				businessTestFile);
	}

	@Test
	public void testUpdateExpense() throws Exception {
		ExpenseDTO expenseDTO = getExpenseDTO();
		when(expenseService.updateExpense(anyLong(), any(ExpenseDTO.class))).thenReturn(expenseDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/expenses/" + expenseDTO.getId())
				.content(asJsonString(expenseDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), result.getResponse().getStatus() == HttpStatus.OK.value() ? true : false,
				businessTestFile);
	}

	@Test
	public void testGetExpenseById() throws Exception {
		ExpenseDTO expenseDTO = getExpenseDTO();
		when(expenseService.getExpenseById(anyLong())).thenReturn(expenseDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/expenses/" + expenseDTO.getId())
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				result.getResponse().getContentAsString().equals(asJsonString(expenseDTO)) ? true : false,
				businessTestFile);
	}

	@Test
	public void testDeleteExpense() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/expenses/1")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), result.getResponse().getStatus() == HttpStatus.OK.value() ? true : false,
				businessTestFile);
	}

	@Test
	public void testListExpenses() throws Exception {
		List<ExpenseDTO> expenses = Arrays.asList(getExpenseDTO());
		when(expenseService.listExpenses()).thenReturn(expenses);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/expenses").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), !result.getResponse().getContentAsString().isEmpty() ? true : false,
				businessTestFile);
	}

	@Test
	public void testSettleExpense() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/expenses/1/settle")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		// Assuming settleExpense doesn't return a body and only sets HTTP status to OK
		yakshaAssert(currentTest(), result.getResponse().getStatus() == HttpStatus.OK.value() ? true : false,
				businessTestFile);
	}

	@Test
	public void testListExpensesByUserId() throws Exception {
		List<ExpenseDTO> expenses = Arrays.asList(getExpenseDTO());
		when(expenseService.listExpensesByUserId(anyLong())).thenReturn(expenses);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/expenses/user/1")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), !result.getResponse().getContentAsString().isEmpty() ? true : false,
				businessTestFile);
	}

	@Test
	public void testListUserBalances() throws Exception {
		Map<String, Double> balances = Map.of("User1", 100.0, "User2", -100.0);
		when(expenseService.listUserBalances(anyLong())).thenReturn(balances);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/expenses/balances/1")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), !result.getResponse().getContentAsString().isEmpty() ? true : false,
				businessTestFile);
	}

	@Test
	public void testCalculateBalanceBetweenTwoUsers() throws Exception {
		Double balance = 50.0; // Assuming this is the balance between user 1 and user 2
		when(expenseService.calculateBalanceBetweenTwoUsers(anyLong(), anyLong())).thenReturn(balance);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/expenses/balance/1/2")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), result.getResponse().getContentAsString().equals(balance.toString()) ? true : false,
				businessTestFile);
	}

}
