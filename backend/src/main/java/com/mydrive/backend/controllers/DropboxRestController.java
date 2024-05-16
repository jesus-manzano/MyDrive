package com.mydrive.backend.controllers;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/dropbox")
@PropertySource("classpath:application-secrets.properties")
public class DropboxRestController {

    @Value("${dropbox.clientId}")
    private String clientId;

    @Value("${dropbox.clientSecret}")
    private String clientSecret;

    @Value("${dropbox.redirectUri}")
    private String redirectUri;

    DbxClientV2 client;

    private static final Logger logger = LoggerFactory.getLogger(DropboxRestController.class);

    @GetMapping("/oauth/authorize")
    public ModelAndView authorizeDropbox() {
        // Create a Dropbox authorization URL
        String authorizeUrl = "https://www.dropbox.com/oauth2/authorize"
                + "?client_id=" + clientId
                + "&token_access_type=offline"
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        // Redirect to Dropbox authorization URL
        return new ModelAndView(new RedirectView(authorizeUrl));
    }

    @GetMapping("/oauth/token")
    public ModelAndView exchangeCodeForToken(@RequestParam("code") String code) throws Exception {
        // Exchange the code for an access token
        String tokenUrl = "https://www.dropbox.com/oauth2/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", redirectUri);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Parse the JSON response to extract the access token
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            String accessToken = rootNode.get("access_token").asText();

            // Use the access token to authenticate with Dropbox
            DbxRequestConfig config = new DbxRequestConfig("MyDrive");
            client = new DbxClientV2(config, accessToken);

            return new ModelAndView(new RedirectView("/api/dropbox/user"));
        } else {
            return new ModelAndView(new RedirectView("/error"));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserInfo() throws Exception {
        FullAccount account = client.users().getCurrentAccount();
        return ResponseEntity.ok(account.getName().getDisplayName());
    }
}
