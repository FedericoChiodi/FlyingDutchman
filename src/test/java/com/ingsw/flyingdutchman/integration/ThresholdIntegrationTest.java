package com.ingsw.flyingdutchman.integration;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.AuctionRepository;
import com.ingsw.flyingdutchman.repository.ThresholdRepository;
import com.ingsw.flyingdutchman.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    public void setup() {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
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

        Product product = new Product();
        product.setProductID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);
        auction.setProduct_sold('N');
        auction.setOpening_timestamp(Timestamp.valueOf(LocalDateTime.now()));
        auction.setClosing_timestamp(Timestamp.valueOf(LocalDateTime.now()));
        auction.setDeleted('N');
        auction.setProduct_auctioned(product);
        auctionRepository.save(auction);

        Threshold threshold = new Threshold();
        threshold.setThresholdID(1L);
        threshold.setUser(loggedUser);
        threshold.setReservationDate(Timestamp.valueOf(LocalDateTime.now()));
        threshold.setPrice(10.0f);
        threshold.setAuction(auction);
        thresholdRepository.save(threshold);
    }

    @Test
    public void delete_test() throws Exception {
        mockMvc.perform(post("/thresholdManagement/delete")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("thresholdID","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Eliminata correttamente!"));

        User loggedUser = new User();
        loggedUser.setUserID(1L);
        loggedUser.setUsername("loggedUser");

        List<Threshold> deleted = thresholdRepository.findByUser(loggedUser);
        assert deleted.isEmpty();
    }

    @Test
    public void insert_test() throws Exception {
        mockMvc.perform(post("/thresholdManagement/insert")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("auctionID","1")
                        .param("price","10.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"));

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        List<Threshold> deleted = thresholdRepository.findByAuction(auction);
        assert deleted.size() == 2;
        assert deleted.get(1).getPrice().equals(10.0f);
    }

    @Test
    public void modify_test() throws Exception {
        mockMvc.perform(post("/thresholdManagement/modify")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("thresholdID","1")
                        .param("price","199.5"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"));

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        List<Threshold> deleted = thresholdRepository.findByAuction(auction);
        assert deleted.size() == 1;
        assert deleted.get(0).getPrice().equals(199.5f);
    }

}
