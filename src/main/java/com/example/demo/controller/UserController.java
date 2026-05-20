package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {

	private final HttpSession session;
	private final Account account;
	private final UserRepository userRepository;

	public UserController(
			HttpSession session,
			Account account,
			UserRepository userRepository) {
		this.session = session;
		this.account = account;
		this.userRepository = userRepository;
	}

	// ログイン画面を表示
	@GetMapping({ "/", "/login", "/logout" })
	public String index() {
		// セッション情報を全てクリアする
		session.invalidate();

		return "login";
	}

	// ログインを実行
	@PostMapping("/login")
	public String login(
			@RequestParam String email,
			@RequestParam String password,
			Model model) {
		// 名前が空の場合にエラーとする
		if (email.length() == 0 || password.length() == 0) {
			model.addAttribute("message", "入力してください");
			return "login";
		}

		List<User> userList = userRepository.findByEmailAndPassword(email, password);
		if (userList == null || userList.size() == 0) {
			// 存在しなかった場合
			model.addAttribute("message", "メールアドレスとパスワードが一致しませんでした");
			return "login";
		}
		User user = userList.get(0);

		// セッション管理されたアカウント情報にIDと名前をセット
		account.setId(user.getId());
		account.setName(user.getName());

		// 「/recipes」へのリダイレクト
		return "redirect:/recipes";
	}

	// 会員登録画面の表示
	@GetMapping("/account")
	public String create() {
		return "accountForm";
	}

	// 会員登録実行
	@PostMapping("/account")
	public String store(
			@RequestParam String name,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam String passwordConfirm,
			Model model) {

		// エラーチェック
		List<String> errorList = new ArrayList<>();
		if (name.length() == 0) {
			errorList.add("名前は必須です");
		}
		if (email.length() == 0) {
			errorList.add("メールアドレスは必須です");
		}
		// メールアドレス存在チェック
		List<User> userList = userRepository.findByEmail(email);
		if (userList != null && userList.size() > 0) {
			// 登録済みのメールアドレスが存在した場合
			errorList.add("登録済みのメールアドレスです");
		}
		// パスワードが空の場合にエラーとする
		if (password.length() == 0) {
			errorList.add("パスワードは必須です");
		}
		// パスワードの文字数チェック
		if (password.length() > 0 && password.length() < 7) {
			errorList.add("パスワードは7文字以上で入力してください");
		}
		if (password.length() > 255) {
			errorList.add("パスワードは255文字以下で入力してください");
		}
		//パスワードとパスワード（確認）が一致するか
		if (!password.equals(passwordConfirm)) {
			errorList.add("パスワードとパスワード（確認用）が一致しません");
		}

		// エラー発生時はお問い合わせフォームに戻す
		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("name", name);
			model.addAttribute("email", email);
			return "accountForm";
		}

		User user = new User(name, email, password);
		userRepository.save(user);

		return "redirect:/";
	}
}
