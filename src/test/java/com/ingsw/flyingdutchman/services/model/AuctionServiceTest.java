package com.ingsw.flyingdutchman.services.model;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.AuctionService;
import com.ingsw.flyingdutchman.repository.AuctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionService auctionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAuction() {
        // Arrange
        Product product = new Product();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Act
        auctionService.createAuction(timestamp, product);

        // Assert
        verify(auctionRepository, times(1)).save(any(Auction.class));
    }

    @Test
    public void testDeleteAuction() {
        // Arrange
        Auction auction = new Auction();
        auction.setDeleted('N');

        // Act
        auctionService.deleteAuction(auction);

        // Assert
        assertEquals('Y', auction.getDeleted());
        verify(auctionRepository, times(1)).save(auction);
    }

    @Test
    public void testUpdateAuction() {
        // Arrange
        Auction auction = new Auction();

        // Act
        auctionService.updateAuction(auction);

        // Assert
        verify(auctionRepository, times(1)).save(auction);
    }

    @Test
    public void testFindAuctionById() {
        // Arrange
        Auction auction = new Auction();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        // Act
        Auction result = auctionService.findAuctionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(auction, result);
        verify(auctionRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByProductOpenNotDeleted() {
        // Arrange
        Product product = new Product();
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        List<Auction> auctions = Arrays.asList(auction1, auction2);
        when(auctionRepository.findByProductOpenNotDeleted(product)).thenReturn(auctions);

        // Act
        List<Auction> result = auctionService.findByProductOpenNotDeleted(product);

        // Assert
        assertEquals(2, result.size());
        assertEquals(auctions, result);
        verify(auctionRepository, times(1)).findByProductOpenNotDeleted(product);
    }

    @Test
    public void testFindByOwner() {
        // Arrange
        User user = new User();
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        List<Auction> auctions = Arrays.asList(auction1, auction2);
        when(auctionRepository.findByOwner(user)).thenReturn(auctions);

        // Act
        List<Auction> result = auctionService.findByOwner(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals(auctions, result);
        verify(auctionRepository, times(1)).findByOwner(user);
    }

    @Test
    public void testFindOpenAuctionsByOwnerNotDeleted() {
        // Arrange
        User user = new User();
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        List<Auction> auctions = Arrays.asList(auction1, auction2);
        when(auctionRepository.findOpenAuctionsByOwnerNotDeleted(user)).thenReturn(auctions);

        // Act
        List<Auction> result = auctionService.findOpenAuctionsByOwnerNotDeleted(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals(auctions, result);
        verify(auctionRepository, times(1)).findOpenAuctionsByOwnerNotDeleted(user);
    }

    @Test
    public void testFindAllOpenAuctionsExceptUser() {
        // Arrange
        User user = new User();
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        List<Auction> auctions = Arrays.asList(auction1, auction2);
        when(auctionRepository.findAllOpenAuctionsExceptUser(user)).thenReturn(auctions);

        // Act
        List<Auction> result = auctionService.findAllOpenAuctionsExceptUser(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals(auctions, result);
        verify(auctionRepository, times(1)).findAllOpenAuctionsExceptUser(user);
    }

    @Test
    public void testFindAuctionByProductDescription() {
        // Arrange
        String description = "Sample description";
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        List<Auction> auctions = Arrays.asList(auction1, auction2);
        when(auctionRepository.findAuctionByProductDescription(description)).thenReturn(auctions);

        // Act
        List<Auction> result = auctionService.findAuctionByProductDescription(description);

        // Assert
        assertEquals(2, result.size());
        assertEquals(auctions, result);
        verify(auctionRepository, times(1)).findAuctionByProductDescription(description);
    }

    @Test
    public void testFindAuctionsByCategory() {
        // Arrange
        Category category = new Category();
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        List<Auction> auctions = Arrays.asList(auction1, auction2);
        when(auctionRepository.findAuctionsByCategory(category)).thenReturn(auctions);

        // Act
        List<Auction> result = auctionService.findAuctionsByCategory(category);

        // Assert
        assertEquals(2, result.size());
        assertEquals(auctions, result);
        verify(auctionRepository, times(1)).findAuctionsByCategory(category);
    }
}
