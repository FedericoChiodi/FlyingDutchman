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
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderIntegrationTest {

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

    @Autowired
    private OrderRepository orderRepository;

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
    public void pay_test() throws Exception {
        User loggedUser = userRepository.findByUsername("loggedUser");
        List<Auction> auctions = auctionRepository.findByOwner(loggedUser);
        assert auctions.size() == 1;
        assert auctions.get(0).getClosing_timestamp() == null;
        assert auctions.get(0).getProduct_sold().equals('N');

        List<Order> orders = orderRepository.findByBuyer(loggedUser);
        assert orders.isEmpty();

        mockMvc.perform(post("/orderManagement/pay")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("auctionID",auctions.get(0).getAuctionID().toString()))
                .andExpect(status().isOk())
                .andExpect(request().attribute("applicationMessage","Il tuo ordine è stato inserito!"))
                .andExpect(view().name("orderManagement/view"));

        List<Auction> updatedAuctions = auctionRepository.findByOwner(loggedUser);
        assert updatedAuctions.size() == 1;
        assert updatedAuctions.get(0).getProduct_sold().equals('Y');
        assert updatedAuctions.get(0).getClosing_timestamp() != null;

        List<Order> updatedOrders = orderRepository.findByBuyer(loggedUser);
        assert updatedOrders.size() == 1;
        assert Objects.equals(updatedOrders.get(0).getProduct().getProductID(), auctions.get(0).getProduct_auctioned().getProductID());
    }

    @Test
    public void buyPremium_test() throws Exception {
        User loggedUser = userRepository.findByUsername("loggedUser");
        assert !Objects.equals(loggedUser.getRole(), "Premium");

        List<Order> orders = orderRepository.findByBuyer(loggedUser);
        assert orders.isEmpty();

        mockMvc.perform(post("/orderManagement/premium")
                        .cookie(new Cookie("loggedUser","loggedUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("orderManagement/premium"));


        User updatedUser = userRepository.findByUsername("loggedUser");
        assert Objects.equals(updatedUser.getRole(), "Premium");

        List<Order> updatedOrders = orderRepository.findByBuyer(loggedUser);
        assert updatedOrders.size() == 1;
        assert updatedOrders.get(0).getProduct().getDescription().equals("Premium Membership");
    }

}
