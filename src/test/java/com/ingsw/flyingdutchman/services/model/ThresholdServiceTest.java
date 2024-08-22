package com.ingsw.flyingdutchman.services.model;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.ThresholdService;
import com.ingsw.flyingdutchman.repository.ThresholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ThresholdServiceTest {

    @Mock
    private ThresholdRepository thresholdRepository;

    @InjectMocks
    private ThresholdService thresholdService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateThreshold() {
        Float price = 123.456f;
        Timestamp reservationDate = new Timestamp(System.currentTimeMillis());
        User buyer = new User();
        Auction auction = new Auction();

        thresholdService.createThreshold(price, reservationDate, buyer, auction);

        verify(thresholdRepository, times(1)).save(any(Threshold.class));
    }

    @Test
    public void testUpdateThreshold() {
        Threshold threshold = new Threshold();

        thresholdService.updateThreshold(threshold);

        verify(thresholdRepository, times(1)).save(threshold);
    }

    @Test
    public void testDeleteThreshold() {
        Threshold threshold = new Threshold();

        thresholdService.deleteThreshold(threshold);

        verify(thresholdRepository, times(1)).delete(threshold);
    }

    @Test
    public void testFindThresholdById() {
        Threshold threshold = new Threshold();
        when(thresholdRepository.findById(anyLong())).thenReturn(Optional.of(threshold));

        Threshold result = thresholdService.findThresholdById(1L);

        assertNotNull(result);
        assertEquals(threshold, result);
        verify(thresholdRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindThresholdsByUser() {
        User user = new User();
        Threshold threshold1 = new Threshold();
        Threshold threshold2 = new Threshold();
        List<Threshold> thresholds = Arrays.asList(threshold1, threshold2);
        when(thresholdRepository.findByUser(user)).thenReturn(thresholds);

        List<Threshold> result = thresholdService.findThresholdsByUser(user);

        assertEquals(2, result.size());
        assertEquals(thresholds, result);
        verify(thresholdRepository, times(1)).findByUser(user);
    }

    @Test
    public void testFindThresholdsByAuction() {
        Auction auction = new Auction();
        Threshold threshold1 = new Threshold();
        Threshold threshold2 = new Threshold();
        List<Threshold> thresholds = Arrays.asList(threshold1, threshold2);
        when(thresholdRepository.findByAuction(auction)).thenReturn(thresholds);

        List<Threshold> result = thresholdService.findThresholdsByAuction(auction);

        assertEquals(2, result.size());
        assertEquals(thresholds, result);
        verify(thresholdRepository, times(1)).findByAuction(auction);
    }
}
