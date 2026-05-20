package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipes")
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// 料理名
	@Column(name = "name")
	private String name;

	// レシピ本文
	@Column(name = "recipe")
	private String recipe;

	// Userテーブルとのリレーション
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// Categoryテーブルとのリレーション
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	public Recipe() {
	}

	public Recipe(Category category, String name, String recipe) {
		this.category = category;
		this.name = name;
		this.recipe = recipe;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecipe() {
		return recipe;
	}

	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}
}
