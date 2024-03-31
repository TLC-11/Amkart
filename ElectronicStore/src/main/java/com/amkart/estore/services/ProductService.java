package com.amkart.estore.services;

import java.util.List;

import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.ProductDto;

public interface ProductService {

	ProductDto createProductDto(ProductDto productDto);

	ProductDto createProductDtoWithCategory(ProductDto productDto, String categoryid);

	ProductDto updateProductDtoWithCategory(String productid, String categoryid);

	PageDetailsResponse<ProductDto> getProductsFromCtaegory(int pagenumber, int pagesize, String sortby, String sortdir,
			String Categoryid);

	ProductDto updateProductDto(ProductDto productDto, String productid);

	void deleteProductDto(String productid);

	ProductDto getProductFromDtoByID(String productid);

	ProductDto getProductFromDtoByName(String productname);

	PageDetailsResponse<ProductDto> getAllLiveProductsFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir, boolean live);

	PageDetailsResponse<ProductDto> getAllInStockProductsFromDto(int pagenumber, int pagesize, String sortby,
			String sortdir, boolean stock);

	List<ProductDto> getProductFromDtoUnderPrice(float price);

	List<ProductDto> getProductFromDtoUnderDiscountprice(float price);

	PageDetailsResponse<ProductDto> getAllProductsFromDto(int pagenumber, int pagesize, String sortby, String sortdir);

}
