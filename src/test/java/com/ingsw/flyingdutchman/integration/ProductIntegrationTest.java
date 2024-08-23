package com.ingsw.flyingdutchman.integration;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.repository.*;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableConfigurationProperties
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ProductIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ThresholdRepository thresholdRepository;

    @BeforeEach
    public void setup() {
        User loggedUser = new User();
        loggedUser.setUsername("loggedUser");
        loggedUser.setPassword("password");
        loggedUser.setFirstname("First");
        loggedUser.setSurname("Last");
        loggedUser.setBirthdate(Date.valueOf("1990-01-01"));
        loggedUser.setAddress("123 Street");
        loggedUser.setCivic_number((short) 1);
        loggedUser.setCap((short) 123);
        loggedUser.setCity("City");
        loggedUser.setState("State");
        loggedUser.setEmail("loggedUser@example.com");
        loggedUser.setCel_number("1234567890");
        loggedUser.setRole("USER");
        loggedUser.setDeleted('N');
        userRepository.save(loggedUser);

        Category category = new Category();
        category.setName("test_name");
        categoryRepository.save(category);

        Product product = new Product();
        product.setCurrent_price(5.0f);
        product.setMin_price(1.0f);
        product.setStarting_price(10.0f);
        product.setDeleted('N');
        product.setCategory(category);
        product.setDescription("test_desc");
        product.setImage("/home/sanpc/uploads/loggedUser/test_desc.png");
        product.setOwner(loggedUser);
        productRepository.save(product);

        Auction auction = new Auction();
        auction.setProduct_sold('N');
        auction.setOpening_timestamp(Timestamp.valueOf(LocalDateTime.now()));
        auction.setClosing_timestamp(Timestamp.valueOf(LocalDateTime.now()));
        auction.setDeleted('N');
        auction.setProduct_auctioned(product);
        auctionRepository.save(auction);

        Threshold threshold = new Threshold();
        threshold.setUser(loggedUser);
        threshold.setReservationDate(Timestamp.valueOf(LocalDateTime.now()));
        threshold.setPrice(10.0f);
        threshold.setAuction(auction);
        thresholdRepository.save(threshold);
    }

    @Test
    public void insert_test() throws Exception {
        Category category = categoryRepository.findByName("test_name");
        assert category != null;

        User user = userRepository.findByUsername("loggedUser");
        List<Product> products = productRepository.findByOwner(user);

        mockMvc.perform(post("/productManagement/insert")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("description","test_desc_2")
                        .param("min_price","15.0f")
                        .param("starting_price","25.0f")
                        .param("current_price","25.0f")
                        .param("categoryID",category.getCategoryID().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"));

        List<Product> added = productRepository.findByOwner(user);

        assert added.size() == products.size() + 1;
    }

    @Test
    public void delete_test() throws Exception {
        List<Product> products = productRepository.findByOwner(userRepository.findByUsername("loggedUser"));
        assert products.get(0).getDeleted().equals('N');

        mockMvc.perform(post("/productManagement/delete")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("productID",products.get(0).getProductID().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"));

        List<Product> deleted = productRepository.findByOwner(userRepository.findByUsername("loggedUser"));
        assert deleted.size() == products.size(); // Soft delete!
        assert deleted.get(0).getDeleted().equals('Y');
    }

}
