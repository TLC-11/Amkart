package com.amkart.estore.controllers;

import java.io.IOException;
import java.io.InputStream;

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

import com.amkart.estore.dtos.CategoryDto;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.UserResponse;
import com.amkart.estore.services.CategoryService;
import com.amkart.estore.services.Fileservice;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("e/iot/amkart")
@Tag(name="Category module", description = "Operation related to catagories")
public class CategoryController {

	@Autowired
	CategoryService service;
	@Autowired
	Fileservice fileservice;

	@Value("${categories.profile.picture}")
	String filepath;

	@PostMapping("/addcategory")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto category) {
		CategoryDto resp = service.createCategoryDto(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@PutMapping("/updatecategory/{categoryid}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto category,
			@PathVariable String categoryid) {
		CategoryDto resp = service.updateCategoryDto(category, categoryid);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@DeleteMapping("/deletecategory/{categoryid}")
	public ResponseEntity<UserResponse> deleteCategory(@PathVariable String categoryid) {
		service.deleteCategoryDto(categoryid);
		UserResponse resp = UserResponse.builder().message("deleted successfully").status(categoryid).build();
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	
	@GetMapping("/getallcategories")
	public ResponseEntity<PageDetailsResponse<CategoryDto>> getallCategories(
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "10") int pagesize,
			@RequestParam(required = false, defaultValue = "name") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir) {
		PageDetailsResponse<CategoryDto> resp = service.getAllCategoriesFromDto(pagenumber, pagesize, sortby, sortdir);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getcategorysnamehaving/{infix}")
	public ResponseEntity<PageDetailsResponse<CategoryDto>> getCategories(@PathVariable String infix,
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "10") int pagesize,
			@RequestParam(required = false, defaultValue = "name") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir) {
		PageDetailsResponse<CategoryDto> resp = service.searchCategories(infix, pagenumber, pagesize, sortby, sortdir);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getcategory/categoryid/{categoryid}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryid) {
		CategoryDto resp = service.getCategoryFromDtoByID(categoryid);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getcategory/categoryname/{categoryname}")
	public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String categoryname) {
		CategoryDto resp = service.getCategoryFromDtoByName(categoryname);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/categoryimage/{categoryid}")
	public void getImgById(@PathVariable String categoryid, HttpServletResponse response) {
		CategoryDto categorydto = service.getCategoryFromDtoByID(categoryid);
		String filename = categorydto.getImgname();
		InputStream inputstream = fileservice.getFile(filepath, filename);
		// response.setContentType(JPEGHuffmanTable);
		try {
			StreamUtils.copy(inputstream, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/addcategorydp/{categoryid}")
	public ResponseEntity<UserResponse> uploadDp(@PathVariable String categoryid, MultipartFile file) {

		String filename = fileservice.uploadFile(file, filepath);
		CategoryDto categorydto = service.getCategoryFromDtoByID(categoryid);
		categorydto.setImgname(filename);
		service.updateCategoryDto(categorydto, categoryid);
		UserResponse resp = UserResponse.builder().message(filename).status("uploaded successfully").build();
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

}
