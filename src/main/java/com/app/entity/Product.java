package com.app.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	private int prodId;
	private String prodName;
	private Double prodCost;
	private Double prodGst;
	private Double prodDiscount;
}
