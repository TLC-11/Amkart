package com.amkart.estore.services.imp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.amkart.estore.config.PageDetailsResponseConfig;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.UserDto;
import com.amkart.estore.entities.User;
import com.amkart.estore.exceptions.ResourseNotFoundException;
import com.amkart.estore.repositiries.UserRepository;
import com.amkart.estore.services.UserService;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	UserRepository repo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	PageDetailsResponseConfig pagedetailsresp;

	@Value("${users.profile.picture}")
	String filepath;

	@Override
	public UserDto createUserDto(UserDto userdto) {
		// TODO Auto-generated method stub
		User user = mapper.map(userdto, User.class);
		User userresp = repo.save(user);
		return mapper.map(userresp, UserDto.class);
	}

	@Override
	public UserDto updateUserDto(UserDto userdto, String userid) {
		// TODO Auto-generated method stub
		User targetuser = repo.findById(userid)
				.orElseThrow(() -> new ResourseNotFoundException("No user found with given id"));
		targetuser.setAbout(userdto.getAbout());
		targetuser.setEmailid(userdto.getEmailid());
		targetuser.setGender(userdto.getGender());
		targetuser.setName(userdto.getName());
		targetuser.setPassword(userdto.getPassword());
		targetuser.setProfilepicture(userdto.getProfilepicture());
		User resp = repo.save(targetuser);
		return mapper.map(resp, UserDto.class);
	}

	@Override
	public void deleteUserDto(String userid) {
		// TODO Auto-generated method stub
		User user = repo.findById(userid)
				.orElseThrow(() -> new ResourseNotFoundException("No user found with given id"));
		String imgfullpath = filepath + File.separator + user.getProfilepicture();
		Path path = Paths.get(imgfullpath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("no such files exist " + e.getMessage());
		}
		repo.delete(user);

	}

	@Override
	public PageDetailsResponse<UserDto> getAllUsersFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir) {
		// TODO Auto-generated method stub
		Sort sort = (sortdir.equalsIgnoreCase("desc")) ? (Sort.by(sortby)).descending() : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<User> pages = repo.findAll(pageable);
		return pagedetailsresp.getResponse(pages, UserDto.class);

	}

	@Override
	public UserDto getUsersFromDtoByID(String userid) {
		// TODO Auto-generated method stub
		User userresp = repo.findById(userid)
				.orElseThrow(() -> new ResourseNotFoundException("No user found with given id"));
		return mapper.map(userresp, UserDto.class);
	}

	@Override
	public UserDto getUsersFromDtoByEmailID(String emailid) {
		// TODO Auto-generated method stub
		User user = repo.findByemailid(emailid)
				.orElseThrow(() -> new RuntimeException("No user found with given emailid"));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUsersFromDtoByEmailIDAndPassword(String emailid, String password) {
		// TODO Auto-generated method stub
		User user = repo.findByEmailidAndPassword(emailid, password)
				.orElseThrow(() -> new RuntimeException("No user found with given emailid and password"));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> searchUsers(String pattern) {
		// TODO Auto-generated method stub
		List<User> users = repo.findBynameContaining(pattern)
				.orElseThrow(() -> new RuntimeException("No user found with given emailid"));
		return users.stream().map(user -> mapper.map(user, UserDto.class)).collect(Collectors.toList());

	}

}
