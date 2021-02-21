package org.msdemt.tacocloud.test;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.msdemt.tacocloud.pojo.vo.Ingredient;
import org.msdemt.tacocloud.pojo.vo.Ingredient.Type;
import org.msdemt.tacocloud.pojo.vo.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class DesignTacoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private List<Ingredient> ingredients;

  private Taco design;

  @BeforeAll
  public void setup() {
    ingredients = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
        new Ingredient("COTO", "Corn Tortilla", Type.WRAP), new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
        new Ingredient("CARN", "Carnitas", Type.PROTEIN), new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
        new Ingredient("LETC", "Lettuce", Type.VEGGIES), new Ingredient("CHED", "Cheddar", Type.CHEESE),
        new Ingredient("JACK", "Monterrey Jack", Type.CHEESE), new Ingredient("SLSA", "Salsa", Type.SAUCE),
        new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

    design = new Taco();
    design.setName("Test Taco");
    design.setIngredients(Arrays.asList("FLTO", "GRBF", "CHED"));
  }

  @Test
  public void testShowDesignForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/design")).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("design"))
        .andExpect(MockMvcResultMatchers.model().attribute("wrap", ingredients.subList(0, 2)))
        .andExpect(MockMvcResultMatchers.model().attribute("protein", ingredients.subList(2, 4)))
        .andExpect(MockMvcResultMatchers.model().attribute("veggies", ingredients.subList(4, 6)))
        .andExpect(MockMvcResultMatchers.model().attribute("cheese", ingredients.subList(6, 8)))
        .andExpect(MockMvcResultMatchers.model().attribute("sauce", ingredients.subList(8, 10)));
  }

  @Test
  public void processDesign() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/design").content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.header().stringValues("Location", "/orders/current"));
  }
}
