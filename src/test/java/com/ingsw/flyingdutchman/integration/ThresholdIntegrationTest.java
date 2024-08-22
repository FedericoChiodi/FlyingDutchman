package com.ingsw.flyingdutchman.integration;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.repository.*;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
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
public class ThresholdIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ThresholdRepository thresholdRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public void delete_test() throws Exception {
        User loggedUser = userRepository.findByUsername("loggedUser");

        List<Threshold> thresholds = thresholdRepository.findByUser(loggedUser);

        mockMvc.perform(post("/thresholdManagement/delete")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("thresholdID",thresholds.get(0).getThresholdID().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Eliminata correttamente!"));

        List<Threshold> deleted = thresholdRepository.findByUser(loggedUser);
        assert deleted.size() == thresholds.size() - 1;
    }

    @Test
    public void insert_test() throws Exception {
        List<Auction> auctions = auctionRepository.findByOwner(userRepository.findByUsername("loggedUser"));
        List<Threshold> thresholds = thresholdRepository.findByUser(userRepository.findByUsername("loggedUser"));

        mockMvc.perform(post("/thresholdManagement/insert")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("auctionID",auctions.get(0).getAuctionID().toString())
                        .param("price","2.5"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"));

        List<Threshold> added = thresholdRepository.findByUser(userRepository.findByUsername("loggedUser"));
        assert added.size() == thresholds.size() + 1;
        assert added.get(1).getPrice().equals(2.5f);
    }

    @Test
    public void modify_test() throws Exception {
        List<Threshold> thresholds = thresholdRepository.findByUser(userRepository.findByUsername("loggedUser"));

        mockMvc.perform(post("/thresholdManagement/modify")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("thresholdID",thresholds.get(0).getThresholdID().toString())
                        .param("price","9.5"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"));

        List<Threshold> modified = thresholdRepository.findByUser(userRepository.findByUsername("loggedUser"));
        assert modified.get(0).getPrice().equals(9.5f);
    }

}
