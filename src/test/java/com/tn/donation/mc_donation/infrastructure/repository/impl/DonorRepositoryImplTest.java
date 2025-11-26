package com.tn.donation.mc_donation.infrastructure.repository.impl;

import com.tn.donation.mc_donation.domain.model.Donor;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.DonorJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.DonorEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.impl.DonorRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DonorRepositoryImplTest {

    @Mock
    DonorJpaRepository donorJpaRepository;

    @InjectMocks
    DonorRepositoryImpl donorRepository;

    @Test
    void saveDonor_ShouldReturnSavedDonor() {
        Donor donor = new Donor(
                "Peter Parker",
                "peterparker@gmail.com"
        );

        when(donorJpaRepository.save(any(DonorEntity.class))).thenAnswer(invocation -> {
            DonorEntity input  = invocation.getArgument(0);
            DonorEntity saved = new DonorEntity();
            saved.setId(1L);
            saved.setFullName(input .getFullName());
            saved.setEmail(input .getEmail());
            saved.setCreatedAt(input .getCreatedAt());
            return saved;
        });

        Donor donorResponse = donorRepository.save(donor);

        assertNotNull(donorResponse);
        assertEquals(1L, donorResponse.getId());
        assertEquals("Peter Parker", donorResponse.getFullName());
        assertEquals("peterparker@gmail.com", donorResponse.getEmail());

        verify(donorJpaRepository).save(argThat(saved ->
                saved.getFullName().equals("Peter Parker") &&
                        saved.getEmail().equals("peterparker@gmail.com")
        ));
    }

    @Test
    void findAll_shouldReturnDonors() {
        Long id = 1L;

        DonorEntity entity = new DonorEntity();
        entity.setId(id);
        entity.setFullName("Peter Parker");
        entity.setEmail("peterparker@gmail.com");
        entity.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));

        when(donorJpaRepository.findAll()).thenReturn(List.of(entity));

        List<Donor> donorEntities = donorRepository.findAll();

        assertEquals(1, donorEntities.size());
        assertNotNull(donorEntities.get(0));

        Donor donor = donorEntities.get(0);

        assertEquals(1L, donor.getId());
        assertEquals("Peter Parker", donor.getFullName());
        assertEquals("peterparker@gmail.com", donor.getEmail());

        verify(donorJpaRepository).findAll();
    }

    @Test
    void findById_shouldReturnDonor() {
        Long id = 1L;

        DonorEntity entity = new DonorEntity();
        entity.setId(id);
        entity.setFullName("Peter Parker");
        entity.setEmail("peterparker@gmail.com");
        entity.setCreatedAt(Instant.parse("2025-01-01T00:00:00Z"));

        when(donorJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Donor> result = donorRepository.findById(id);

        assertTrue(result.isPresent());
        assertNotNull(result.get());

        Donor donor = result.get();

        assertEquals(1L, donor.getId());

        verify(donorJpaRepository).findById(id);
    }

    @Test
    void delete_shouldDeleteDonorSuccessfully() {
        Long id = 1L;

        Donor donor = new Donor(
                id,
                "Peter Parker",
                "peterparker@gmail.com"
        );

        doNothing().when(donorJpaRepository).deleteById(id);

        donorRepository.delete(donor);

        verify(donorJpaRepository).deleteById(id);
        verifyNoMoreInteractions(donorJpaRepository);
    }
}
