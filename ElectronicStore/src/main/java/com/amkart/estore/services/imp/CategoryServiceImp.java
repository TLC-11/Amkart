package com.amkart.estore.services.imp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amkart.estore.config.PageDetailsResponseConfig;
import com.amkart.estore.dtos.CategoryDto;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.entities.Category;
import com.amkart.estore.exceptions.ResourseNotFoundException;
import com.amkart.estore.repositiries.CategoryRepository;
import com.amkart.estore.services.CategoryService;

@Service
public class CategoryServiceImp implements CategoryService {

	@Autowired
	ModelMapper mapper;
	@Autowired
	CategoryRepository repo;
	@Autowired
	PageDetailsResponseConfig pageconfig;
	@Value("${categories.profile.picture}")
	String filepath;

	@Override
	public CategoryDto createCategoryDto(CategoryDto categorydto) {
		// TODO Auto-generated method stub
		Category category = mapper.map(categorydto, Category.class);
		Category resp = repo.save(category);
		return mapper.map(resp, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategoryDto(CategoryDto categorydto, String categoryid) {
		// TODO Auto-generated method stub
		Category target = repo.findById(categoryid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid category id"));
		target.setDescription(categorydto.getDescription());
		target.setImgname(categorydto.getImgname());
		target.setName(categorydto.getName());
		repo.save(target);
		return mapper.map(target, CategoryDto.class);
	}

	@Override
	public void deleteCategoryDto(String categoryid) {
		// TODO Auto-generated method stub
		Category target = repo.findById(categoryid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid category id"));
		String fullpath = filepath + File.separator + target.getImgname();
		Path path = Paths.get(fullpath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repo.delete(target);
	}

	@Override
	public CategoryDto getCategoryFromDtoByID(String categoryid) {
		// TODO Auto-generated method stub
		Category target = repo.findById(categoryid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid category id"));
		return mapper.map(target, CategoryDto.class);
	}

	@Override
	public CategoryDto getCategoryFromDtoByName(String Name) {
		// TODO Auto-generated method stub
		Category resp = repo.findByName(Name);
		return mapper.map(resp, CategoryDto.class);
	}

	@Override
	public PageDetailsResponse<CategoryDto> searchCategories(String pattern, int pagenumber, int pagesize,
			String sortby, String sortdir) {
		// TODO Auto-generated method stub
		Sort sort = (sortby.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Category> pages = repo.findByNameContains(pattern, pageable);

		return pageconfig.getResponse(pages, CategoryDto.class);

	}

	@Override
	public PageDetailsResponse<CategoryDto> getAllCategoriesFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir) {
		// TODO Auto-generated method stub
		Sort sort = (sortby.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Category> pages = repo.findAll(pageable);

		return pageconfig.getResponse(pages, CategoryDto.class);
	}

}
