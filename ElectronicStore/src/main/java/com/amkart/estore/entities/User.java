package com.amkart.estore.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "Users")
public class User {
	@Id
	@Column(name = "userid", unique = true)
	private String userid;
	private String name;
	@Column(name = "password", length = 10)
	private String password;
	private String gender;
	private String profilepicture;
	@Column(name = "emailid", unique = true)
	private String emailid;
	private String about;
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Order> order;

}
