package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.services.SongService;

@Controller
public class SongController {
	@Autowired
	SongService service;

	@PostMapping("/addSong")
	public String addSong(@ModelAttribute Song song) {

		boolean songStatus = service.songExists(song.getName());
		if (songStatus == false) {
			service.addSong(song);
		} else {
			System.out.println("Song " + song.getName() + " is already existed");
		}
		return "adminHome";
	}

	@GetMapping("/viewSong")
	public String viewSong(Model model) {
		List<Song> songLists = service.fetchAllSongs();
		System.out.println("Song List =" + songLists);
		model.addAttribute("songs", songLists);

		return "displaySong";
	}

	@GetMapping("/playSong")
	public String playSong(Model model) {
		Users user = new Users();
		boolean premiumUser = user.isPrimium();
		if (premiumUser == true) {
			List<Song> songLists = service.fetchAllSongs();

			model.addAttribute("songs", songLists);

			return "displaySong";
		} else {
			return "makePayment";
		}
	}

}
