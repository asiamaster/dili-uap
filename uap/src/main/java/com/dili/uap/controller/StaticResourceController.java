package com.dili.uap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 客户端下载
 */
@Controller
@RequestMapping("/static")
public class StaticResourceController {

	/**
	 * 客户端下载页
	 * @return
	 */
	@GetMapping("/clientDownload.html")
	public String hzscdownload() {
		return "staticdownload/clientDownload";
	}
}
