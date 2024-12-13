package com.bsd.evaluation_java;

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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
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
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Bearer")));
	}
}