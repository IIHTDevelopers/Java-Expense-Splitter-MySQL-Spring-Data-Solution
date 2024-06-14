package com.expensesplitter.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensesplitter.dto.UserDTO;
import com.expensesplitter.entity.User;
import com.expensesplitter.exception.ResourceNotFoundException;
import com.expensesplitter.repo.UserRepository;
import com.expensesplitter.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper modelMapper;

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDTO createUser(UserDTO userDto) {
		User user = convertToEntity(userDto);
		User savedUser = userRepository.save(user);
		return convertToDto(savedUser);
	}

	@Override
	public UserDTO getUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return convertToDto(user);
	}

	@Override
	@Transactional
	public UserDTO updateUser(Long userId, UserDTO userDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		User updatedUser = userRepository.save(user);
		return convertToDto(updatedUser);
	}

	@Override
	@Transactional
	public boolean deleteUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		userRepository.delete(user);
		return true;
	}

	@Override
	public Page<UserDTO> listUsers(Pageable pageable) {
		Page<User> users = userRepository.findAllByOrderByNameAsc(pageable);
		return users.map(user -> modelMapper.map(user, UserDTO.class));
	}

	private UserDTO convertToDto(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		return dto;
	}

	private User convertToEntity(UserDTO userDto) {
		User user = new User();
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		return user;
	}
}
