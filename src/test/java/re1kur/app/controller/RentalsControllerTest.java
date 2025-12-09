package re1kur.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.dto.RentalDto;
import re1kur.app.core.filter.RentalAdminFilter;
import re1kur.app.core.filter.RentalFilter;
import re1kur.app.model.payload.RentalPayload;
import re1kur.app.service.RentalService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RentalsController.class)
@AutoConfigureMockMvc(addFilters = true)
class RentalsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RentalService rentalService;

    private final String BASE_URL = "/rentals";

    @Test
    void getRentalsByPrincipal() throws Exception {
        UUID userId = UUID.randomUUID();
        RentalDto build = RentalDto.builder().id(UUID.randomUUID()).build();
        PageDto<RentalDto> mockPage = new PageDto<>(List.of(build), 0, 5, 1, 0, 0, 0, 0);

        when(rentalService.readAllByUser(any(Pageable.class), eq(userId), any(RentalFilter.class)))
                .thenReturn(mockPage);
        when(rentalService.readCarIdsByUser(userId)).thenReturn(List.of(1));

        mvc.perform(get(BASE_URL)
                        .with(oidcLogin().idToken(id -> id.claim("sub", userId.toString()))))
                .andExpect(status().isOk())
                .andExpect(view().name("rentals/list.html"))
                .andExpect(model().attribute("page", mockPage))
                .andExpect(model().attributeExists("filter"))
                .andExpect(model().attributeExists("carIds"));

        verify(rentalService, times(1)).readAllByUser(any(Pageable.class), eq(userId), any(RentalFilter.class));
        verify(rentalService, times(1)).readCarIdsByUser(userId);
    }

    @Test
    void getRentals() throws Exception {
        UUID userId = UUID.randomUUID();
        RentalDto build = RentalDto.builder().id(UUID.randomUUID()).build();
        PageDto<RentalDto> mockPage = new PageDto<>(List.of(build), 0, 5, 1, 0, 0, 0, 0);

        when(rentalService.readAll(any(Pageable.class), any(RentalAdminFilter.class), any(OidcUser.class)))
                .thenReturn(mockPage);

        mvc.perform(get(BASE_URL + "/users")
                        .with(oidcLogin().idToken(id -> id.claim("sub", userId.toString()))))
                .andExpect(status().isOk())
                .andExpect(view().name("rentals/list-admin.html"))
                .andExpect(model().attribute("page", mockPage))
                .andExpect(model().attributeExists("filter"));

        verify(rentalService, times(1)).readAll(any(Pageable.class), any(RentalAdminFilter.class), any(OidcUser.class));
    }

    @Test
    void createRental() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        RentalPayload payload = new RentalPayload(1, now, now.plusDays(1));

        when(rentalService.create(eq(payload), any(OidcUser.class)))
                .thenReturn(rentalId);

        mvc.perform(post(BASE_URL + "/create")
                        .with(csrf())
                        .with(oidcLogin().idToken(id -> id.claim("sub", userId.toString())))
                        .flashAttr("rentalPayload", payload))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rentals/" + rentalId));

        verify(rentalService, times(1)).create(eq(payload), any(OidcUser.class));
    }

    @Test
    void getProfile() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();
        RentalDto mockDto = RentalDto.builder().id(rentalId).build();

        when(rentalService.readById(eq(rentalId), any(OidcUser.class))).thenReturn(mockDto);

        mvc.perform(get(BASE_URL + "/" + rentalId)
                        .with(oidcLogin().idToken(id -> id.claim("sub", userId.toString()))))
                .andExpect(status().isOk())
                .andExpect(model().attribute("rental", mockDto))
                .andExpect(view().name("rentals/profile.html"));

        verify(rentalService, times(1)).readById(eq(rentalId), any(OidcUser.class));
    }

    @Test
    void deleteRental() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID rentalId = UUID.randomUUID();

        doNothing().when(rentalService).deleteById(eq(rentalId), any(OidcUser.class));

        mvc.perform(delete(BASE_URL + "/" + rentalId)
                        .with(csrf())
                        .with(oidcLogin().idToken(id -> id.claim("sub", userId.toString()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rentals"));
    }
}