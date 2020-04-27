package com.dili.uap.sdk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/static")
public class StaticResourceController {

	@GetMapping("/staticdownload/hzscdownload.html")
	public String hzscdownload() {
		return "staticdownload/hzscdownload";
	}
}
