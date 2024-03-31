package com.amkart.estore.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDetailsResponse<T> {

	List<T> Content;
	int Size;
	long TotalElements;
	int TotalPages;
	int Number;
	boolean isLast;

}
