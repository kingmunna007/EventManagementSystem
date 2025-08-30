//package com.eventmanagement.backend.service;
//
//import com.eventmanagement.backend.entity.Venues;
//import com.eventmanagement.backend.repository.VenueRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class VenuesServiceTest {
//    @Mock
//    private VenueRepository venueRepository;
//
//    @InjectMocks
//    private VenuesService venuesService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllVenues() {
//        Venues v1 = new Venues(1L, "Venue1", "Addr1", new BigDecimal("100.00"), 100, "img1", "vid1");
//        Venues v2 = new Venues(2L, "Venue2", "Addr2", new BigDecimal("200.00"), 200, "img2", "vid2");
//        when(venueRepository.findAll()).thenReturn(Arrays.asList(v1, v2));
//        List<Venues> venues = venuesService.getAllVenues();
//        assertEquals(2, venues.size());
//    }
//
//    @Test
//    void testGetVenueById() {
//        Venues v = new Venues(1L, "Venue1", "Addr1", new BigDecimal("100.00"), 100, "img1", "vid1");
//        when(venueRepository.findById(1)).thenReturn(Optional.of(v));
//        Optional<Venues> found = venuesService.getVenueById(1);
//        assertTrue(found.isPresent());
//        assertEquals("Venue1", found.get().getName());
//    }
//
//    @Test
//    void testSaveVenue() {
//        Venues v = new Venues(null, "Venue1", "Addr1", new BigDecimal("100.00"), 100, "img1", "vid1");
//        Venues saved = new Venues(1L, "Venue1", "Addr1", new BigDecimal("100.00"), 100, "img1", "vid1");
//        when(venueRepository.save(v)).thenReturn(saved);
//        Venues result = venuesService.saveVenue(v);
//        assertNotNull(result.getId());
//    }
//
//    @Test
//    void testDeleteVenue() {
//        doNothing().when(venueRepository).deleteById(1);
//        venuesService.deleteVenue(1);
//        verify(venueRepository, times(1)).deleteById(1);
//    }
//}
