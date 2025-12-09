package re1kur.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import re1kur.app.model.dto.MakeFullDto;
import re1kur.app.model.payload.MakeUpdatePayload;
import re1kur.app.service.MakeService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MakeController.class)
class MakeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MakeService makeService;

    private static final Integer makeId = 1;
    private final String BASE_URL = "/makes/%s".formatted(makeId);

    @Test
    void getMakeProfile() throws Exception {
        MakeFullDto mock = MakeFullDto.builder().id(makeId).build();

        when(makeService.read(eq(makeId), nullable(OidcUser.class)))
                .thenReturn(mock);

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .with(oidcLogin().idToken(id -> id.claim("sub", UUID.randomUUID()))))
                .andExpect(view().name("makes/profile.html"))
                .andExpect(model().attributeExists("make"));
    }

    @Test
    void getUpdateMake() throws Exception {
        MakeFullDto mock = MakeFullDto.builder().id(makeId).name("name").build();

        when(makeService.read(eq(makeId), nullable(OidcUser.class)))
                .thenReturn(mock);

        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/update")
                        .with(oidcLogin().idToken(id -> id.claim("sub", UUID.randomUUID()))))
                .andExpect(view().name("makes/update.html"))
                .andExpect(model().attributeExists("make"));
    }

    @Test
    void updateMake() throws Exception {
        MakeUpdatePayload mock = new MakeUpdatePayload("name", null, null, null, null, null, null);
        doNothing().when(makeService).update(eq(mock), eq(makeId), nullable(OidcUser.class));

        mvc.perform(post(BASE_URL + "/update")
                        .with(oidcLogin().idToken(id -> id.claim("sub", UUID.randomUUID())))
                        .with(csrf())
                        .flashAttr("update", mock))
                .andExpect(redirectedUrl(BASE_URL))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteMake() throws Exception {

        doNothing().when(makeService).delete(eq(makeId), nullable(OidcUser.class));

        mvc.perform(delete(BASE_URL)
                        .with(oidcLogin().idToken(id -> id.claim("sub", UUID.randomUUID())))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/makes"));
    }
}