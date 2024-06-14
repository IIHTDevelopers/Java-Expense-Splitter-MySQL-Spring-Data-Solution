package com.expensesplitter.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.expensesplitter.dto.ExpenseDTO;
import com.expensesplitter.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MasterData {

	public static UserDTO getUserDTO() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setName("John Doe");
		userDTO.setEmail("john.doe@example.com");
		return userDTO;
	}

	public static Page<UserDTO> getUserDTOPage(int page, int size) {
		List<UserDTO> jobDTOList = getUserDTOList();
		PageRequest pageRequest = PageRequest.of(page, size);
		return new PageImpl<>(jobDTOList, pageRequest, jobDTOList.size());
	}

	public static List<UserDTO> getUserDTOList() {
		return Arrays.asList(getUserDTO());
	}

	public static ExpenseDTO getExpenseDTO() {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setId(1L);
		expenseDTO.setDescription("Lunch Meeting");
		expenseDTO.setAmount(85.75);
		expenseDTO.setPaidById(1L); // Assuming this ID matches the user above
		Set<Long> sharedWith = new HashSet<>(Arrays.asList(2L, 3L)); // Assuming these IDs represent other users
		expenseDTO.setSharedWithIds(sharedWith);
		expenseDTO.setIsSettled(false);
		return expenseDTO;
	}

	public static List<ExpenseDTO> getExpenseDTOList() {
		return Arrays.asList(getExpenseDTO());
	}

	public static String asJsonString(Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
