package com.amkart.estore.config;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.amkart.estore.dtos.PageDetailsResponse;

@Component
public class PageDetailsResponseConfig {

	@Autowired
	ModelMapper mapper;

	public <S, D> PageDetailsResponse<D> getResponse(Page<S> pages, Class<D> dtype) {
		List<S> entity = pages.getContent();
		List<D> dto = entity.stream().map(obj -> mapper.map(obj, dtype)).collect(Collectors.toList());
		PageDetailsResponse<D> resp = new PageDetailsResponse<>();
		resp.setContent(dto);
		resp.setNumber(pages.getNumber());
		resp.setTotalElements(pages.getTotalElements());
		resp.setTotalPages(pages.getTotalPages());
		resp.setSize(pages.getSize());
		resp.setLast(pages.isLast());
		return resp;
	}

}
