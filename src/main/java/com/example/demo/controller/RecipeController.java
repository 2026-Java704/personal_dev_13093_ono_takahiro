package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Recipe;
import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class RecipeController {

	private final RecipeRepository recipeRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final Account account;
	private final HttpSession session;

	public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository,
			CategoryRepository categoryRepository, Account account, HttpSession session) {
		this.recipeRepository = recipeRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.account = account;
		this.session = session;
	}

	// レシピ一覧表示
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
		model.addAttribute("account", account);

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
	public String create(
			@RequestParam Integer categoryId,
			@RequestParam String name,
			@RequestParam String recipe) {

		Category category = categoryRepository.findById(categoryId).get();

		Recipe newRecipe = new Recipe(category, name, recipe);
		//ログインしていなかったら、ユーザーIDは1（ゲストユーザー）にする
		Integer userId = account.getLoginUserId(session);
		if (userId == null) {
			newRecipe.setUser(userRepository.findById(1).get());
		} else {
			User loginUser = userRepository.findById(account.getId()).get();
			newRecipe.setUser(loginUser);

		}
		// recipesテーブルへの反映（INSERT）
		recipeRepository.save(newRecipe);
		// 「/recipes」にGETでリクエストし直す（リダイレクト）
		return "redirect:/recipes";
	}

	// 編集画面表示
	@GetMapping("/recipes/{id}/edit")
	public String edit(@PathVariable Integer id, Model model) {

		// 1. 未ログインならログイン画面へ（または一覧へリダイレクト）
		if (account.getId() == null) {
			return "login";
		}

		Recipe recipe = recipeRepository.findById(id).get();
		List<Category> categories = categoryRepository.findAll();

		// 2. 投稿者とログインユーザーが一致しない場合は一覧に弾く
		if (!recipe.getUser().getId().equals(account.getId())) {
			return "redirect:/recipes";
		}

		model.addAttribute("recipe", recipe);
		model.addAttribute("categories", categories);

		return "editRecipe";
	}

	// レシピの更新処理
	@PostMapping("/recipes/{id}/edit")
	public String update(
			@PathVariable Integer id,
			@RequestParam Integer categoryId,
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String recipe) {

		// 1. 未ログインチェック
		if (account.getId() == null) {
			return "login";
		}

		Recipe updateRecipe = recipeRepository.findById(id).get();

		// 2. 投稿者チェック（他人のレシピをPOSTで書き換えられるのを防ぐ）
		if (!updateRecipe.getUser().getId().equals(account.getId())) {
			return "redirect:/recipes";
		}

		Category category = categoryRepository.findById(categoryId).get();

		updateRecipe.setName(name);
		updateRecipe.setRecipe(recipe);
		updateRecipe.setCategory(category);

		recipeRepository.save(updateRecipe);

		return "redirect:/recipes";
	}

	// レシピを削除
	@PostMapping("/recipes/{id}/delete")
	public String delete(@PathVariable Integer id) {

		// 1. 未ログインチェック
		if (account.getId() == null) {
			return "login";
		}

		Recipe recipe = recipeRepository.findById(id).get();

		// 2. 投稿者チェック
		if (!recipe.getUser().getId().equals(account.getId())) {
			return "redirect:/recipes";
		}

		recipeRepository.deleteById(id);

		return "redirect:/recipes";
	}

}
