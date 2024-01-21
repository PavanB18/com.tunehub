package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.services.SongService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
	@Autowired
	UsersService service;
	@Autowired
	SongService songService;

	@PostMapping("/register")
	public String addUsers(@ModelAttribute Users user) {
		boolean userStatus = service.existEmail(user.getEmail());
		if (userStatus == false) {
			service.addUser(user);
			System.out.println("User added successfully");
		} else {
			System.out.println(user.getEmail() + "User already existed");

		}
		return "login";

		// System.out.println(user.getUsername()+" "+user.getEmail()+"
		// "+user.getPassword()+" "+user.getGender()+" "+user.getRole()+"
		// "+user.getAddress());

	}

	@PostMapping("/validate")
	public String validate(@RequestParam String email, @RequestParam String password, HttpSession session,
			Model model) {

		if (service.validateUser(email, password) == true) {
			String role = service.getRole(email);
			session.setAttribute("email", email);

			if (role.equals("admin")) {
				return "adminHome";
			} else {
				Users user = service.getUser(email);
				// if(user.isPrimium()) {

				// model.addAttribute("songs", songList);
				// return "displaySong";
				boolean userStatus1 = user.isPrimium();
				List<Song> songList = songService.fetchAllSongs();
				model.addAttribute("songs", songList);
				model.addAttribute("isPrimium", userStatus1);
				return "customerHome";
			}

		} else {

			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();

		return "login";
	}

}
