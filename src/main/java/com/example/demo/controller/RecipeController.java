package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class RecipeController {

	private final RecipeRepository recipeRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;

	public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository,
			CategoryRepository categoryRepository) {
		this.recipeRepository = recipeRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;

	}

	// 商品一覧表示
	@GetMapping("/recipes")
	public String index1(
			@RequestParam(defaultValue = "") Integer categoryId,
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String keyword,
			Model model) {

		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		// レシピ作成者の名前を取得
		List<User> userList = userRepository.findByName(name);
		model.addAttribute("users", userList);

		// レシピ一覧情報の取得
		List<Recipe> recipeList = null;
		if (keyword.length() > 0 && categoryId != null) {
			recipeList = recipeRepository.findByNameContainingAndCategoryId(keyword, categoryId);
		} else if (keyword.length() > 0) {
			recipeList = recipeRepository.findByNameContaining(keyword);
		} else if (categoryId != null) {
			recipeList = recipeRepository.findByCategoryId(categoryId);
		} else {
			recipeList = recipeRepository.findAll();
		}

		model.addAttribute("keyword", keyword);
		model.addAttribute("recipes", recipeList);

		return "recipes";
	}

	// レシピ詳細画面の表示
	@GetMapping("/recipes/detail")
	public String detail(@RequestParam Integer id, Model model) {
		// idをもとにレシピ情報を取得
		Recipe recipe = recipeRepository.findById(id).get();
		model.addAttribute("recipe", recipe);

		return "detailRecipe";
	}

	// 新規登録画面の表示
	@GetMapping("/recipes/add")
	public String newRecipe(Model model) {
		// 全カテゴリー一覧を取得
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		return "addRecipe";
	}

	// 新規登録処理
	@PostMapping("/recipes/add")
	public String store(
			@RequestParam(defaultValue = "") Category categoryId,
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") Integer userId,
			@RequestParam(defaultValue = "") String recipes) {

		// Recipeオブジェクトの生成
		Recipe recipe = new Recipe(categoryId, name, recipes);
		// recipeテーブルへの反映（INSERT）
		recipeRepository.save(recipe);
		// 「/recipes」にGETでリクエストし直せ（リダイレクト）
		return "redirect:/recipes";
	}

}
