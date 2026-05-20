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
@Table(name = "recipes") // 対応するテーブル名
public class Recipe {

	// フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // レシピID

	@Column(name = "category_id")
	private Integer categoryId; // カテゴリーID
	private String name; // 料理名
	private String recipe; // レシピ文

	//Userテーブルとのリレーション
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// コンストラクタ
	public Recipe() {
	}

	public Recipe(Integer id, Integer categoryId, User user, String name, String recipes) {
		this.id = id;
		this.categoryId = categoryId;
		this.name = name;
		this.user = user;
	}

	public Recipe(Integer categoryId, String name, String recipe) {
		this.categoryId = categoryId;
		this.name = name;
		this.recipe = recipe;
	}

	/**
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id セットする id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return categoryId
	 */
	public Integer getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId セットする categoryId
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return userId
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param userId セットする userId
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return recipes
	 */
	public String getRecipe() {
		return recipe;
	}

	/**
	 * @param recipes セットする recipes
	 */
	public void setRecipe(String recipes) {
		this.recipe = recipes;
	}

	public void setUserName(String userName) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
