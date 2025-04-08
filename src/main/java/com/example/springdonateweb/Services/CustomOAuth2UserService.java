package com.example.springdonateweb.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.UsersEntity;
import com.example.springdonateweb.Repositories.UsersRepository;
import com.example.springdonateweb.Services.mappers.UsersMapper;

import java.security.AuthProvider;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        // Log all attributes
        System.out.println("OAuth2 User Attributes: " + oauth2User.getAttributes());
        String email = oauth2User.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        UsersEntity user = userRepository.findByEmail(email)
            .orElseGet(() -> createUser(oauth2User));
                
        return new DefaultOAuth2User(
            List.of(new SimpleGrantedAuthority(user.getRoleId().toString())),
            oauth2User.getAttributes(),
            "email"
        );
    }

    private UsersEntity createUser(OAuth2User oauth2User) {
        UsersEntity newUser = new UsersEntity();
        newUser.setEmail(oauth2User.getAttribute("email"));
        newUser.setName(oauth2User.getAttribute("name"));
        newUser.setRoleId(2); 
        newUser.setStatus(true);
        newUser.setPassword("");
        
        return userRepository.save(newUser);
    }
}
