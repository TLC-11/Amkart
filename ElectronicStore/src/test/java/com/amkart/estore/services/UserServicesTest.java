package com.amkart.estore.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.UserDto;
import com.amkart.estore.entities.User;
import com.amkart.estore.repositiries.UserRepository;

@SpringBootTest
public class UserServicesTest {

	@MockBean
	private UserRepository repo;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private UserService userservice;

	private UserDto userdto;
	private User user;
	private List<User> userList = new ArrayList<>();

	@BeforeEach
	public void init() {
		userdto = UserDto.builder().name("John Jackob").password("secret").gender("Male").profilepicture("profile.jpg")
				.emailid("johnj@example.com").about("Some information about John").build();

		userList.add(
				User.builder().name("Michael Johnson").password("secret1").gender("Male").profilepicture("profile1.jpg")
						.emailid("michaelj1@example.com").about("Some information about Michael 1").build());

		userList.add(
				User.builder().name("Alice Smith").password("secret2").gender("Female").profilepicture("profile2.jpg")
						.emailid("alice@example.com").about("Some information about Alice").build());

		userList.add(User.builder().name("Bob Brown").password("secret3").gender("Male").profilepicture("profile3.jpg")
				.emailid("bob@example.com").about("Some information about Bob").build());

		userList.add(
				User.builder().name("Emily Johnson").password("secret4").gender("Female").profilepicture("profile4.jpg")
						.emailid("emily@example.com").about("Some information about Emily").build());
	}

	@Test
	public void createUserDtoTest() {
		// TODO Auto-generated method stub
		user = mapper.map(userdto, User.class);
		Mockito.when(repo.save(Mockito.any())).thenReturn(user);
		UserDto updateUserDto = userservice.createUserDto(userdto);
		Assertions.assertEquals(updateUserDto.getUserid(), userdto.getUserid());
		Assertions.assertNotNull(updateUserDto);
	}

	@Test
	public void updateUserDtoTest() {
		// TODO Auto-generated method stub
		UserDto userdto1 = UserDto.builder().name("John Jackob").password("secret").gender("Male")
				.profilepicture("profile.jpg").emailid("johnj@example.com").about("Some information about John")
				.build();
		user = mapper.map(userdto1, User.class);
		Mockito.when(repo.findById(Mockito.any())).thenReturn(Optional.of(user));
		Mockito.when(repo.save(Mockito.any())).thenReturn(user);
		UserDto updateUserDto = userservice.updateUserDto(userdto1, "122");
		Assertions.assertNotNull(updateUserDto);
	}

	@Test
	public void deleteUserDtotest() {
		// TODO Auto-generated method stub
		user = mapper.map(userdto, User.class);
		Mockito.when(repo.findById(Mockito.any())).thenReturn(Optional.of(user));
		userservice.deleteUserDto(Mockito.any());
		Mockito.verify(repo, Mockito.times(1)).delete(user);

	}

	@Test
	public void getAllUsersFromDto() {
		// TODO Auto-generated method stub
		Mockito.when(repo.findAll()).thenReturn(userList);
		Page<User> page = new PageImpl<>(userList);

		Mockito.when(repo.findAll(PageRequest.of(0, 10, Sort.by("name").ascending()))).thenReturn(page);

		// Call the method
		PageDetailsResponse<UserDto> response = userservice.getAllUsersFromDto(0, 10, "name", "asc");

		// Assert the result
		Assertions.assertEquals(userList.size(), response.getContent().size());
		Assertions.assertEquals(userList.get(0).getName(), response.getContent().get(0).getName());

	}

}
