package com.bsd.evaluation_java;

import com.bsd.evaluation_java.models.Convention;
import com.bsd.evaluation_java.models.Salarie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bsd.evaluation_java.models.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class EvaluationJavaApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
		objectMapper = new ObjectMapper();
	}

	// Test : Connexion avec un utilisateur valide
	@Test
	void connexion_avecUtilisateurValide_reponse200() throws Exception {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("a@a.com");
		utilisateur.setPassword("root");

		mvc.perform(post("/connexion")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(utilisateur)))
				.andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhQGEuY29tIn0.WLK31zXu66JK56jOUK91gZg4HZv3InjphZVqCf80XlU")));
	}

	@Test
	void recuperation_utilisateurParId_utilisateurTrouve() throws Exception {
		// ID de l'utilisateur à récupérer
		Integer userId = 1;

		// Effectuer une requête GET pour récupérer un utilisateur par son ID
		mvc.perform(get("/utilisateurs/" + userId)
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("id")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("email")));
	}

	@Test
	void creation_salarie_parUtilisateurNonAutorise_reponse403() throws Exception {
		// Préparation des données
		Salarie salarie = new Salarie();
		salarie.setMatricule("MAT123");
		salarie.setCodeBarre("123456789");
		salarie.setConvention(new Convention(1, "Convention A", 10, new ArrayList<>()));

		// Effectuer la requête POST avec un utilisateur ayant un rôle différent (par exemple UTILISATEUR)
		mvc.perform(post("/salaries/create")
						.with(user("user").roles("UTILISATEUR"))
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(salarie)))
				.andExpect(status().isForbidden());
	}
}