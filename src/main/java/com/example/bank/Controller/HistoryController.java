package com.example.bank.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.Model.History;
import com.example.bank.Service.HistoryService;

@RestController
@RequestMapping(path = "/api/v1/bank/history")
public class HistoryController {

	@Autowired
	private HistoryService historyService;

	//to get all history
	@GetMapping
	public Iterable<History> getHistory() {
		return historyService.allHistory();
	}
	
	
	
}
