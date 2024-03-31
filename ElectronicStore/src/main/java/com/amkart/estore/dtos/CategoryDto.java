package com.amkart.estore.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	@NotBlank
	private String id;
	@NotBlank
	private String name;
	@Size(min = 10, message = "atleast 10 char needed")
	private String description;
	private String imgname;

}
