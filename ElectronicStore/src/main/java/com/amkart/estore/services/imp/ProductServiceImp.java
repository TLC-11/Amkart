package com.amkart.estore.services.imp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amkart.estore.config.PageDetailsResponseConfig;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.ProductDto;
import com.amkart.estore.entities.Category;
import com.amkart.estore.entities.Product;
import com.amkart.estore.exceptions.ResourseNotFoundException;
import com.amkart.estore.repositiries.CategoryRepository;
import com.amkart.estore.repositiries.ProductRepository;
import com.amkart.estore.services.ProductService;

@Service
public class ProductServiceImp implements ProductService {

	@Autowired
	PageDetailsResponseConfig pageconfig;
	@Autowired
	ProductRepository repo;
	@Autowired
	CategoryRepository crepo;
	@Autowired
	ModelMapper mapper;
	@Value("${products.profile.picture}")
	String filepath;
	Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

	@Override
	public ProductDto createProductDto(ProductDto productDto) {

		productDto.setProductid(UUID.randomUUID().toString());
		productDto.setAddeddate(new Date());
		Product product = mapper.map(productDto, Product.class);
		Product resp = repo.save(product);

		return mapper.map(resp, ProductDto.class);
	}

	@Override
	public ProductDto updateProductDto(ProductDto productDto, String productid) {

		Product target = repo.findById(productid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid product id"));
		productDto.setProductid(target.getProductid());
		productDto.setAddeddate(target.getAddeddate());
		Product product = mapper.map(productDto, Product.class);
		Product resp = repo.save(product);
		return mapper.map(resp, ProductDto.class);

	}

	@Override
	public void deleteProductDto(String productid) {

		Product resp = repo.findById(productid).orElseThrow(() -> new ResourseNotFoundException("invalid product id"));
		String imgpath = filepath + File.separator + resp.getProductimg();
		try {
			Files.delete(Paths.get(imgpath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repo.delete(resp);
	}

	@Override
	public ProductDto getProductFromDtoByID(String productid) {

		Product resp = repo.findById(productid).orElseThrow(() -> new ResourseNotFoundException("invalid product id"));
		return mapper.map(resp, ProductDto.class);
	}

	@Override
	public ProductDto getProductFromDtoByName(String productname) {

		Product resp = repo.findByProductname(productname)
				.orElseThrow(() -> new ResourseNotFoundException("invalid product id"));
		return mapper.map(resp, ProductDto.class);
	}

	@Override
	public List<ProductDto> getProductFromDtoUnderPrice(float price) {

		List<Product> products = repo.getAllProductsUnderPrice(price);
		return products.stream().map(p -> mapper.map(p, ProductDto.class)).collect(Collectors.toList());

	}

	@Override
	public List<ProductDto> getProductFromDtoUnderDiscountprice(float price) {

		List<Product> products = repo.getAllProductsUnderDiscountprice(price);
		return products.stream().map(p -> mapper.map(p, ProductDto.class)).collect(Collectors.toList());
	}

	@Override
	public PageDetailsResponse<ProductDto> getAllLiveProductsFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir, boolean live) {
		Sort sort = (sortdir.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Product> pages = repo.findByIslive(live, pageable);

		return pageconfig.getResponse(pages, ProductDto.class);
	}

	@Override
	public PageDetailsResponse<ProductDto> getAllInStockProductsFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir, boolean stock) {

		Sort sort = (sortdir.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Product> pages = repo.findByStock(stock, pageable);

		return pageconfig.getResponse(pages, ProductDto.class);
	}

	@Override
	public PageDetailsResponse<ProductDto> getAllProductsFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir) {
		// TODO Auto-generated method stub
		Sort sort = (sortby.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Product> pages = repo.findAll(pageable);
		logger.error("product {}", pages.getContent());
		return pageconfig.getResponse(pages, ProductDto.class);
	}

	@Override
	public ProductDto createProductDtoWithCategory(ProductDto productDto, String categoryid) {
		// TODO Auto-generated method stub
		Category category = crepo.findById(categoryid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid category id"));
		Product product = mapper.map(productDto, Product.class);
		product.setAddeddate(new Date());
		product.setProductid(UUID.randomUUID().toString());
		product.setCategory(category);
		repo.save(product);
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public ProductDto updateProductDtoWithCategory(String productid, String categoryid) {
		// TODO Auto-generated method stub
		Category category = crepo.findById(categoryid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid category id"));
		Product product = repo.findById(productid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid product id"));
		product.setCategory(category);
		Product resp = repo.save(product);
		return mapper.map(resp, ProductDto.class);
	}

	@Override
	public PageDetailsResponse<ProductDto> getProductsFromCtaegory(int pagenumber, int pagesize, String sortby,
			String sortdir, String categoryid) {
		// TODO Auto-generated method stub
		Category category = crepo.findById(categoryid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid category id"));
		Sort sort = (sortby.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pagenumber, pagesize, sort);
		Page<Product> pages = repo.findByCategory(category, pageable);
		return pageconfig.getResponse(pages, ProductDto.class);
	}

}
