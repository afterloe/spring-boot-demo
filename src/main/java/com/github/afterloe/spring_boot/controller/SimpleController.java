/**
 * create by afterloe
 */
package com.github.afterloe.spring_boot.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

	private static final String template = "%s this is %s access";
	
	@RequestMapping("/")
	public String index(@RequestParam(value = "name", defaultValue="localhost") String name) {
		return String.format(template, new Date().toString(), name);
	}
}