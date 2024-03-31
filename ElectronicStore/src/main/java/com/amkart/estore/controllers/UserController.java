package com.amkart.estore.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.UserDto;
import com.amkart.estore.dtos.UserResponse;
import com.amkart.estore.services.Fileservice;
import com.amkart.estore.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("e/iot/amkart")
@Tag(name = "User module", description = "Operation related to users")
public class UserController {
	@Autowired
	Fileservice fileservice;
	@Autowired
	UserService service;
	@Value("${users.profile.picture}")
	String filepath;

	@Operation(description = "add new user")
	@PostMapping("/adduser")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
		UserDto resp = service.createUserDto(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@Operation(description = "update a user")
	@PutMapping("/updateuser/{userid}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user, @PathVariable String userid) {
		UserDto resp = service.updateUserDto(user, userid);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@Operation(description = "remove a user")
	@DeleteMapping("/delete/{userid}")
	public ResponseEntity<UserResponse> deleteUser(@PathVariable String userid) {
		service.deleteUserDto(userid);
		UserResponse resp = UserResponse.builder().message("deleted successfully").status(userid).build();
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@Operation(description = "get all users")
	@GetMapping("/getallusers")
	public ResponseEntity<PageDetailsResponse<UserDto>> getallUser(
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "5") int pagesize,
			@RequestParam(required = false, defaultValue = "name") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir) {
		PageDetailsResponse<UserDto> resp = service.getAllUsersFromDto(pagenumber, pagesize, sortby, sortdir);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@Operation(description = "find users with searchstring")
	@GetMapping("/getusersnamehaving/{infix}")
	public ResponseEntity<List<UserDto>> getUsers(@PathVariable String infix) {
		List<UserDto> resp = service.searchUsers(infix);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@Operation(description = "get user with email and password")
	@GetMapping("/getuser")
	public ResponseEntity<UserDto> getUserByEmail(@RequestParam String emailid, @RequestParam String password) {
		UserDto resp = service.getUsersFromDtoByEmailIDAndPassword(emailid, password);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@Operation(description = "get user with id")
	@GetMapping("/getuser/userid/{userid}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userid) {
		UserDto resp = service.getUsersFromDtoByID(userid);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@Operation(description = "get user with email")
	@GetMapping("/getuser/emailid/{emailid}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String emailid) {
		UserDto resp = service.getUsersFromDtoByEmailID(emailid);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@Operation(description = "get user image")
	@GetMapping("/userimage/{userid}")
	public void getImgById(@PathVariable String userid, HttpServletResponse response) {
		UserDto userdto = service.getUsersFromDtoByID(userid);
		String filename = userdto.getProfilepicture();
		InputStream inputstream = fileservice.getFile(filepath, filename);
		// response.setContentType(JPEGHuffmanTable);
		try {
			StreamUtils.copy(inputstream, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Operation(description = "upload img for user")
	@PostMapping("/adduserdp/{userid}")
	public ResponseEntity<UserResponse> uploadDp(@PathVariable String userid, MultipartFile file) {

		String filename = fileservice.uploadFile(file, filepath);
		UserDto userdto = service.getUsersFromDtoByID(userid);
		userdto.setProfilepicture(filename);
		service.updateUserDto(userdto, userid);
		UserResponse resp = UserResponse.builder().message(filename).status("uploaded successfully").build();
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}
}
