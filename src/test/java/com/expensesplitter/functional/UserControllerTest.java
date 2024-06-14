package com.expensesplitter.functional;

import static com.expensesplitter.utils.MasterData.asJsonString;
import static com.expensesplitter.utils.MasterData.getUserDTO;
import static com.expensesplitter.utils.MasterData.getUserDTOPage;
import static com.expensesplitter.utils.TestUtils.businessTestFile;
import static com.expensesplitter.utils.TestUtils.currentTest;
import static com.expensesplitter.utils.TestUtils.testReport;
import static com.expensesplitter.utils.TestUtils.yakshaAssert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.expensesplitter.controller.UserController;
import com.expensesplitter.dto.UserDTO;
import com.expensesplitter.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testCreateUser() throws Exception {
		UserDTO userDTO = getUserDTO();
		when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users").content(asJsonString(userDTO))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), result.getResponse().getStatus() == HttpStatus.CREATED.value() ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testUpdateUser() throws Exception {
		UserDTO userDTO = getUserDTO();
		when(userService.updateUser(any(Long.class), any(UserDTO.class))).thenReturn(userDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/users/" + userDTO.getId())
				.content(asJsonString(userDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), result.getResponse().getStatus() == HttpStatus.OK.value() ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testGetUserById() throws Exception {
		UserDTO userDTO = getUserDTO();
		when(userService.getUserById(any(Long.class))).thenReturn(userDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/" + userDTO.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(),
				result.getResponse().getContentAsString().contentEquals(asJsonString(userDTO)) ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testDeleteUser() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), result.getResponse().getStatus() == HttpStatus.OK.value() ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testListUsers() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		Page<UserDTO> usersDTOList = getUserDTOPage(0, 10);
		when(userService.listUsers(any(Pageable.class))).thenReturn(usersDTOList);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), !result.getResponse().getContentAsString().isEmpty() ? "true" : "false",
				businessTestFile);
	}
}
