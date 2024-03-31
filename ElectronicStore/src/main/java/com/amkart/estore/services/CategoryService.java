package com.amkart.estore.services;

import com.amkart.estore.dtos.CategoryDto;
import com.amkart.estore.dtos.PageDetailsResponse;

public interface CategoryService {
	CategoryDto createCategoryDto(CategoryDto categorydto);

	CategoryDto updateCategoryDto(CategoryDto categorydto, String categoryid);

	void deleteCategoryDto(String categoryid);

	CategoryDto getCategoryFromDtoByID(String categoryid);

	CategoryDto getCategoryFromDtoByName(String Name);

	PageDetailsResponse<CategoryDto> searchCategories(String pattern, int pagenumber, int pagesize, String sortby,
			String sortdir);

	PageDetailsResponse<CategoryDto> getAllCategoriesFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir);
}
