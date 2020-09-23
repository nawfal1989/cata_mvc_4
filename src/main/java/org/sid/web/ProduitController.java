package org.sid.web;

import org.sid.dao.ProduitRepository;
import org.sid.entities.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

import javax.validation.Valid;

@Controller
public class ProduitController {
	@Autowired
	private ProduitRepository produitRepository;
	
	@RequestMapping(value="/user/index")
	public String index(Model model, 
			@RequestParam(name="page", defaultValue = "0")int pPage, 
			@RequestParam(name="size", defaultValue = "5")int pSize,
			@RequestParam(name="mc", defaultValue="")String pName) {

		Page<Produit> pageProduits = produitRepository.chercher("%"+pName+"%", PageRequest.of(pPage, pSize));
		model.addAttribute("listProduits", pageProduits.getContent());
		int[] pages = new int[pageProduits.getTotalPages()];
		model.addAttribute("pages", pages);
		model.addAttribute("size", pSize);
		model.addAttribute("pageCourante", pPage);
		model.addAttribute("mc", pName);
		return "produits";
	}
	
	@RequestMapping(value="/admin/delete", method=RequestMethod.GET)
	public String delete(
			Long id, 
			@RequestParam(name="page", defaultValue = "0")int pPage, 
			@RequestParam(name="size", defaultValue = "5")int pSize,
			@RequestParam(name="mc", defaultValue="")String pName) {
		produitRepository.deleteById(id);
		return "redirect:/user/index?page="+pPage+"&size="+pSize+"&mc="+pName;
	}
	
	@RequestMapping(value="/admin/form", method=RequestMethod.GET)
	public String formProduit(Model model) {
		model.addAttribute("produit", new Produit());
		return "FormProduit";
	}
	
	@RequestMapping(value="/admin/edit", method=RequestMethod.GET)
	public String edit(Model model, Long id) {
		Optional<Produit> produit = produitRepository.findById(id);
		model.addAttribute("produit", produit.get());
		return "EditProduit";
	}
	
	@RequestMapping(value="/admin/save", method=RequestMethod.POST)
	public String save(Model model, @Valid Produit produit, 
			BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "FormProduit";
		Produit produitResult = produitRepository.save(produit);
		model.addAttribute("produit", produitResult);
		return "Confirmation";
	}
	
	@RequestMapping(value="/admin/save", method=RequestMethod.PUT)
	public String Edit(Model model, @Valid Produit produit, 
			BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "FormProduit";
		Produit produitResult = produitRepository.save(produit);
		model.addAttribute("produit", produitResult);
		return "Confirmation";
	}
	
	@RequestMapping(value="/")
	public String home() {
		return "redirect:/user/index";
	}

	@RequestMapping(value="/403")
	public String accessDenied() {
		return "403";
	}
	
	@RequestMapping(value="/login")
	public String login() {
		return "login";
	}
}
