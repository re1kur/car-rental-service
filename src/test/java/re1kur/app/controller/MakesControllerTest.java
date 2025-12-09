package re1kur.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.model.dto.MakeDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.payload.MakePayload;
import re1kur.app.service.MakeService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MakesController.class)
@AutoConfigureMockMvc(addFilters = false)
class MakesControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MakeService makeService;

    private String BASE_URL = "/makes";

    @Test
    void redirectListSlash() throws Exception {
        mvc.perform(get(BASE_URL + "/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/makes"));
    }

    @Test
    void getMakes() throws Exception {
        List<MakeDto> mockContent = List.of(MakeDto.builder().id(1).build());
        PageDto<MakeDto> mockPage = new PageDto<>(mockContent, 0, 1, 1, 0, 0, 0, 0);
        String mockName = "name";

        when(makeService.readAllAsPage(any(String.class), any(Pageable.class), nullable(OidcUser.class)))
                .thenReturn(mockPage);

        mvc.perform(get(BASE_URL)
                        .with(oidcLogin().idToken(id -> id.claim("sub", UUID.randomUUID())))
                        .param("name", mockName))
                .andExpect(view().name("makes/list.html"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("name"))
                .andExpect(status().isOk());
    }

    @Test
    void getCreateMake() throws Exception {
        mvc.perform(get(BASE_URL + "/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("makes/create.html"))
                .andExpect(model().attributeExists("make"));
    }

    @Test
    void createMake() throws Exception {
        MakePayload mockPayload = new MakePayload("Name", "country", null, null , null, null);
        Integer makeId = 1;

        when(makeService.create(any(MakePayload.class), nullable(MultipartFile.class), isNull(), nullable(OidcUser.class)))
                .thenReturn(makeId);

        mvc.perform(post(BASE_URL + "/create")
                        .flashAttr("makePayload", mockPayload)
                        .with(oidcLogin().idToken(id -> id.claim("sub", UUID.randomUUID()))))
                .andExpect(redirectedUrl("/makes/" + makeId))
                .andExpect(status().is3xxRedirection());
    }
}