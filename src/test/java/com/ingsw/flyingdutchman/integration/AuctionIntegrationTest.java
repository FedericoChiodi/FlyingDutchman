package com.ingsw.flyingdutchman.integration;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.repository.*;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuctionIntegrationTest {

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

        User otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("password");
        otherUser.setFirstname("First");
        otherUser.setSurname("Last");
        otherUser.setBirthdate(Date.valueOf("1990-01-01"));
        otherUser.setAddress("123 Street");
        otherUser.setCivic_number((short) 1);
        otherUser.setCap((short) 123);
        otherUser.setCity("City");
        otherUser.setState("State");
        otherUser.setEmail("otherUser@example.com");
        otherUser.setCel_number("1234567890");
        otherUser.setRole("USER");
        otherUser.setDeleted('N');
        userRepository.save(otherUser);

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
        product.setImage("/home/sanpc/uploads/otherUser/test_desc.png");
        product.setOwner(otherUser);
        productRepository.save(product);

        Product product2 = new Product();
        product2.setCurrent_price(5.0f);
        product2.setMin_price(1.0f);
        product2.setStarting_price(10.0f);
        product2.setDeleted('N');
        product2.setCategory(category);
        product2.setDescription("test_desc");
        product2.setImage("/home/sanpc/uploads/loggedUser/test_desc.png");
        product2.setOwner(loggedUser);
        productRepository.save(product2);

        Auction auction = new Auction();
        auction.setProduct_sold('N');
        auction.setOpening_timestamp(Timestamp.valueOf(LocalDateTime.now()));
        auction.setClosing_timestamp(null);
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
        User loggedUser = userRepository.findByUsername("loggedUser");
        List<Product> products = productRepository.findByOwner(loggedUser); // Niente all'asta
        assert !products.isEmpty();

        List<Auction> auctions = auctionRepository.findByOwner(loggedUser);

        mockMvc.perform(post("/auctionManagement/insert")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("productID",products.get(0).getProductID().toString())
                        .param("opening_timestamp", String.valueOf(Timestamp.valueOf(LocalDateTime.now()))))
                .andExpect(status().isOk())
                .andExpect(request().attribute("applicationMessage","Asta creata correttamente!"))
                .andExpect(view().name("auctionManagement/view"));

        List<Auction> updatedAuctions = auctionRepository.findByOwner(loggedUser);
        assert updatedAuctions.size() == auctions.size() + 1;
        assert updatedAuctions.get(0).getProduct_auctioned().getProductID().equals(products.get(0).getProductID());
    }

    @Test
    public void update_test() throws Exception {
        User otherUser = userRepository.findByUsername("otherUser");

        List<Auction> auctions = auctionRepository.findByOwner(otherUser);
        assert auctions.get(0).getProduct_auctioned().getCurrent_price().equals(5.0f);

        mockMvc.perform(post("/auctionManagement/update")
                        .cookie(new Cookie("loggedUser","otherUser"))
                        .param("auctionID", auctions.get(0).getAuctionID().toString())
                        .param("price", "19.99"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"));

        List<Auction> updatedAuctions = auctionRepository.findByOwner(otherUser);
        assert updatedAuctions.get(0).getProduct_auctioned().getCurrent_price().equals(19.99f);
    }

    @Test
    public void delete_test() throws Exception {
        User otherUser = userRepository.findByUsername("otherUser");

        List<Auction> auctions = auctionRepository.findByOwner(otherUser);
        assert auctions.get(0).getDeleted().equals('N');

        // Ritorna quella unica di loggedUser
        List<Threshold> thresholds = thresholdRepository.findByAuction(auctions.get(0));
        assert !thresholds.isEmpty();


        mockMvc.perform(post("/auctionManagement/delete")
                        .cookie(new Cookie("loggedUser","otherUser"))
                        .param("auctionID", auctions.get(0).getAuctionID().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"));

        List<Threshold> updatedThresholds = thresholdRepository.findByAuction(auctions.get(0));
        assert updatedThresholds.isEmpty();

        List<Auction> updatedAuctions = auctionRepository.findByOwner(otherUser);
        assert updatedAuctions.get(0).getDeleted().equals('Y');
    }

}
