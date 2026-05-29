package re1kur.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.app.controller.view.SomeEndpointsController;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SomeEndpointsController.class)
class SomeEndpointsControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SomeEndpointsController controller;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(controller, "accountUrl", "http://localhost:9090/realms/rental-car-service/account");
    }

    @Test
    void redirectAccount() throws Exception {
        mvc.perform(get("/oauth2/account")
                        .with(oidcLogin().idToken(id -> id.claim("sub", UUID.randomUUID()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:9090/realms/rental-car-service/account"));
    }
}