package com.expensesplitter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.expensesplitter.dto.UserDTO;

public interface UserService {
	UserDTO createUser(UserDTO userDto);

	UserDTO getUserById(Long userId);

	UserDTO updateUser(Long userId, UserDTO userDto);

	boolean deleteUser(Long userId);

	Page<UserDTO> listUsers(Pageable pageable);
}
