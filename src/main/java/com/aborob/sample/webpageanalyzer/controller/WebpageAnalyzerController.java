package com.aborob.sample.webpageanalyzer.controller;

import com.aborob.sample.webpageanalyzer.entity.AnalysisSingleResult;
import com.aborob.sample.webpageanalyzer.exception.WebpageAnalyzerException;
import com.aborob.sample.webpageanalyzer.service.WebpageAnalyzerService;
import com.aborob.sample.webpageanalyzer.service.WebpageAnalyzerServiceImpl;
import com.aborob.sample.webpageanalyzer.util.UrlValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.LinkedHashSet;
import java.util.Optional;

/**
 * Simple controller that handles Get and Post in the same Url.
 * No redirecting, sessions, temp data store, or security were taken into account.
 */
@Controller
@RequestMapping("/webpage-analyzer")
public class WebpageAnalyzerController {

	private static final Logger log = LoggerFactory.getLogger(WebpageAnalyzerController.class);

	@Autowired
	private WebpageAnalyzerService webpageAnalyzerService;

	@GetMapping
	public String pageInfoForm(Model model) {

		return "main";
	}

	@PostMapping
	public String pageInfoAnalyzer(@RequestParam("url") String urlString, Model model) {

		final String url = urlString;
		model.addAttribute("url", HtmlUtils.htmlEscape(url));
		try {
			if(UrlValidatorUtil.isValidURL(url)) {
				Optional<LinkedHashSet<AnalysisSingleResult>> analysisSingleResultsOptional =
						webpageAnalyzerService.analyseWebpage(url, WebpageAnalyzerServiceImpl.customCriteria(url));

				if (analysisSingleResultsOptional.isPresent()) {

					model.addAttribute("analysisResults", analysisSingleResultsOptional.get());
				} else {
					model.addAttribute("errorMsg", "No data was found.");
				}
			} else {
				model.addAttribute("errorMsg", "Error: Invalid URL.");
			}

		} catch (WebpageAnalyzerException e) {
			log.info(e.getMessage());
			model.addAttribute("errorMsg", "Error: Wrong URL, timeout, or something went wrong.");
		}
		return "main";
	}

}
