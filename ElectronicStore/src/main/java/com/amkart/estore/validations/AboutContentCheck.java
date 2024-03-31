package com.amkart.estore.validations;



import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AboutContentCheck implements ConstraintValidator<AboutValidator, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value.isBlank()) return false;
		return true;
	}

}
