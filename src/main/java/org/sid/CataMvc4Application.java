package org.sid;

import org.sid.dao.*;
import org.sid.entities.Produit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CataMvc4Application {

	public static void main(String[] args) {
		
		// IOC Container
		ApplicationContext ctx = SpringApplication.run(CataMvc4Application.class, args);
		
		// Donne moi l'objet qui implémente cette interface
		ProduitRepository produitRepository = ctx.getBean(ProduitRepository.class);
		produitRepository.save(new Produit("LX 4352",101,100));
		produitRepository.save(new Produit("HP 322",200,78));
		produitRepository.save(new Produit("Imprimente Epson",300,63));
		produitRepository.save(new Produit("Imp HP 452",400,99));
		
		produitRepository.findAll().forEach(p -> System.out.println(p.getDesignation()));
	}

}
