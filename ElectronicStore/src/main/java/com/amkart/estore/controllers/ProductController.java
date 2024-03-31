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
import com.amkart.estore.dtos.ProductDto;
import com.amkart.estore.dtos.UserResponse;
import com.amkart.estore.services.Fileservice;
import com.amkart.estore.services.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("e/iot/amkart")
@Tag(name="Product module", description = "Operation related to products")
public class ProductController {
	@Autowired
	Fileservice fileservice;
	@Autowired
	ProductService service;
	@Value("${products.profile.picture}")
	String filepath;

	@PostMapping("/addproduct")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto product) {
		ProductDto resp = service.createProductDto(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@PostMapping("/addproduct/{categoryid}")
	public ResponseEntity<ProductDto> createProductWithCategory(@Valid @RequestBody ProductDto product,
			@PathVariable String categoryid) {
		ProductDto resp = service.createProductDtoWithCategory(product, categoryid);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@PutMapping("/updatecategoryid/product/{productid}/categotyid/{categoryid}")
	public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String productid,
			@PathVariable String categoryid) {
		ProductDto resp = service.updateProductDtoWithCategory(productid, categoryid);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@GetMapping("getproductsfromcategory/categoryid/{categoryid}")
	public ResponseEntity<PageDetailsResponse<ProductDto>> getProductsFromCategory(
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "50") int pagesize,
			@RequestParam(required = false, defaultValue = "productname") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir, @PathVariable String categoryid) {
		PageDetailsResponse<ProductDto> productsdto = service.getProductsFromCtaegory(pagenumber, pagesize, sortby,
				sortdir, categoryid);
		return ResponseEntity.status(HttpStatus.OK).body(productsdto);
	}

	@PutMapping("/updateproduct/{productid}")
	public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto product,
			@PathVariable String productid) {
		ProductDto resp = service.updateProductDto(product, productid);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@DeleteMapping("/delete/product/{productid}")
	public ResponseEntity<UserResponse> deleteProduct(@PathVariable String productid) {
		service.deleteProductDto(productid);
		UserResponse resp = UserResponse.builder().message("deleted successfully").status(productid).build();
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getallproducts")
	public ResponseEntity<PageDetailsResponse<ProductDto>> getallProduct(
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "10") int pagesize,
			@RequestParam(required = false, defaultValue = "productname") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir) {
		PageDetailsResponse<ProductDto> resp = service.getAllProductsFromDto(pagenumber, pagesize, sortby, sortdir);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getallliveproducts/{live}")
	public ResponseEntity<PageDetailsResponse<ProductDto>> getallliveProduct(
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "10") int pagesize,
			@RequestParam(required = false, defaultValue = "productname") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir, @PathVariable boolean live) {
		PageDetailsResponse<ProductDto> resp = service.getAllLiveProductsFromDto(pagenumber, pagesize, sortby, sortdir,
				live);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getallstockproducts/{stock}")
	public ResponseEntity<PageDetailsResponse<ProductDto>> getallstockProduct(
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "10") int pagesize,
			@RequestParam(required = false, defaultValue = "productname") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir, @PathVariable boolean stock) {
		PageDetailsResponse<ProductDto> resp = service.getAllInStockProductsFromDto(pagenumber, pagesize, sortby,
				sortdir, stock);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getproduct/productid/{productid}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable String productid) {
		ProductDto resp = service.getProductFromDtoByID(productid);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getproduct/productname/{productname}")
	public ResponseEntity<ProductDto> getProductWithName(@PathVariable String productname) {
		ProductDto resp = service.getProductFromDtoByName(productname);
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}

	@GetMapping("/getproducts/pricelimit/{limit}")
	public ResponseEntity<List<ProductDto>> getProductUnderPrice(@PathVariable float limit) {
		List<ProductDto> products = service.getProductFromDtoUnderPrice(limit);
		return ResponseEntity.status(HttpStatus.OK).body(products);

	}

	@GetMapping("/getproducts/discountpricelimit/{limit}")
	public ResponseEntity<List<ProductDto>> getProductUnderDiscountPrice(@PathVariable float limit) {
		List<ProductDto> products = service.getProductFromDtoUnderDiscountprice(limit);
		return ResponseEntity.status(HttpStatus.OK).body(products);

	}

	@GetMapping("/productimage/{productid}")
	public void getImgById(@PathVariable String productid, HttpServletResponse response) {
		ProductDto productdto = service.getProductFromDtoByID(productid);
		String filename = productdto.getProductimg();
		InputStream inputstream = fileservice.getFile(filepath, filename);
		// response.setContentType(JPEGHuffmanTable);
		try {
			StreamUtils.copy(inputstream, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PostMapping("/addproductdp/{productid}")
	public ResponseEntity<UserResponse> uploadDp(@PathVariable String productid, MultipartFile file) {
		ProductDto productdto = service.getProductFromDtoByID(productid);
		String filename = fileservice.uploadFile(file, filepath);

		productdto.setProductimg(filename);
		service.updateProductDto(productdto, productid);
		UserResponse resp = UserResponse.builder().message(filename).status("uploaded successfully").build();
		return ResponseEntity.status(HttpStatus.OK).body(resp);

	}
}
