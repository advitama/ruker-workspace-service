package com.ruker.workspace.auth.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String USER_API_URL = System.getenv("AUTH_API_URL") + "/user/";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null; // Consider implementing this method
    }

    public UserDetails loadUserByUserId(String id, String token) throws UsernameNotFoundException {
        Map<String, Object> userData = getUserDataFromApi(id, token);
        String email = (String) userData.get("email");
        return new User(email, "", Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserDataFromApi(String id, String token) throws UsernameNotFoundException {
        HttpHeaders headers = createHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.exchange(USER_API_URL + id, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        validateResponse(body, id);

        return Optional.ofNullable((Map<String, Object>) body.get("data"))
                .orElseThrow(() -> new UsernameNotFoundException("User data is missing for Id: " + id));
    }

    public void updateUserStatus(String token) {
        HttpHeaders headers = createHeaders(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(USER_API_URL + "/update-status", HttpMethod.GET, entity, Map.class);
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + token);
        return headers;
    }

    private void validateResponse(Map<String, Object> body, String id) {
        if (body == null || body.get("error") != null) {
            log.error("User not found with Id: {}", id);
            throw new UsernameNotFoundException("User not found with Id: " + id);
        }
    }

}
