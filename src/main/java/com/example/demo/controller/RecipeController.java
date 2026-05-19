package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Recipe;
import com.example.demo.repository.RecipeRepository;

@Controller
public class RecipeController {

	private final RecipeRepository recipeRepository;

	public RecipeController(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	// 商品一覧表示
	@GetMapping("/recipes")
	public String index(Model model) {

		// 商品一覧情報の取得
		List<Recipe> recipeList = recipeRepository.findAll();
		model.addAttribute("recipes", recipeList);

		return "recipes";
	}

	//検索結果表示
	@GetMapping("/recipes/{id}")
	public String index(@PathVariable Integer id, Model model) {

		// recipesテーブルをID（主キー）で検索
		Recipe recipe = recipeRepository.findById(id).get();

		model.addAttribute("recipe", recipe);

		return "recipes";
	}

	// 新規登録画面の表示
	@GetMapping("/resipes/add")
	public String create() {
		return "addRecipe";
	}

	// 新規登録処理
	@PostMapping("/recipes/add")
	public String store(
			@RequestParam(defaultValue = "") Integer categoryId,
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

	// 更新画面表示
	@GetMapping("/recipes/{id}/edit")
	public String edit(@PathVariable Integer id, Model model) {

		// recipesテーブルをID（主キー）で検索
		Recipe recipe = recipeRepository.findById(id).get();

		model.addAttribute("recipe", recipe);

		return "editRecipe";
	}

	// 更新処理
	@PostMapping("/recipes/{id}/edit")
	public String update(
			@PathVariable Integer id,
			@RequestParam(defaultValue = "") Integer categoryId,
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") Integer userId,
			@RequestParam(defaultValue = "") String recipes) {

		//recipesテーブルをID（主キー）で検索
		Recipe recipe = recipeRepository.findById(id).get();

		// セッターを利用して、recipeオブジェクトのフィールドを書き換える
		recipe.setCategoryId(categoryId);
		recipe.setName(name);
		recipe.setUserId(userId);
		recipe.setRecipe(recipes);

		// recipeテーブルへの反映（UPDATE）
		recipeRepository.save(recipe);
		// 「/recipes」にGETでリクエストし直せ（リダイレクト）
		return "redirect:/recipes";
	}

}
