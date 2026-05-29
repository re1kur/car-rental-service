package re1kur.app.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import re1kur.app.config.FirebaseProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PushConfigController {

    private final FirebaseProperties properties;
    private final ObjectMapper mapper;

    // Public Firebase web config + VAPID key, served as JS so both the page and the
    // service worker can load it (importScripts). No secrets here.
    @GetMapping(value = "/push-config.js", produces = "application/javascript")
    public String config() throws Exception {
        FirebaseProperties.Web w = properties.getWeb();
        Map<String, Object> firebase = new LinkedHashMap<>();
        firebase.put("apiKey", nz(w.getApiKey()));
        firebase.put("authDomain", nz(w.getAuthDomain()));
        firebase.put("projectId", nz(w.getProjectId()));
        firebase.put("storageBucket", nz(w.getStorageBucket()));
        firebase.put("messagingSenderId", nz(w.getMessagingSenderId()));
        firebase.put("appId", nz(w.getAppId()));

        Map<String, Object> config = new LinkedHashMap<>();
        config.put("firebase", firebase);
        config.put("vapidKey", nz(properties.getVapidKey()));
        config.put("enabled", w.getApiKey() != null && !w.getApiKey().isBlank());

        return "self.__PUSH_CONFIG__ = " + mapper.writeValueAsString(config) + ";";
    }

    private String nz(String value) {
        return value == null ? "" : value;
    }
}
