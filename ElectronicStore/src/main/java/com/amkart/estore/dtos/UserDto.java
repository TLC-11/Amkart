package com.amkart.estore.dtos;

import com.amkart.estore.validations.AboutValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter

public class UserDto {

	@Size(min = 5, max = 20, message = "userid not accepted, 4<length>21")
	private String userid;
	@Size(min = 2, max = 20, message = "username not accepted, 4<length>21")
	private String name;
	@NotBlank
	private String password;
	@NotBlank
	private String gender;
	private String profilepicture;
	@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "invalid emailid")
	private String emailid;
	@AboutValidator
	private String about;

}
