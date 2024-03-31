package com.amkart.estore.services;

import java.util.List;

import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.UserDto;

public interface UserService {
	UserDto createUserDto(UserDto userdto);

	UserDto updateUserDto(UserDto userdto, String userid);

	void deleteUserDto(String userid);

	UserDto getUsersFromDtoByID(String userid);

	UserDto getUsersFromDtoByEmailID(String emailid);

	UserDto getUsersFromDtoByEmailIDAndPassword(String emailid, String password);

	List<UserDto> searchUsers(String pattern);

	PageDetailsResponse<UserDto> getAllUsersFromDto(int pagenumber, int pagesize, String sortby, String sortdir);
}
