package com.javarush.jira.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import com.javarush.jira.profile.internal.Profile;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.ProfileUtil;
import com.javarush.jira.profile.web.ProfileRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.javarush.jira.login.internal.web.UserController.REST_URL;
import static com.javarush.jira.login.internal.web.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user@gmail.com")
public class ProfileRestControllerTest extends AbstractControllerTest {

    public static final String REST_URL = ProfileRestController.REST_URL;

    @Autowired
    protected ProfileMapper profileMapper;

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserDetailsService userDetailsService;

    private AuthUser authUser;

    private ProfileTo testProfileTo;

    @BeforeEach
    public void setUp() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRoles(Set.of(Role.DEV));
        authUser = new AuthUser(testUser);
        testProfileTo = new ProfileTo(1L, Set.of("deadline"), null);
    }

    @Test
//    @WithMockUser(username = "test@test.com", authorities = "DEV")
    void get() throws Exception {

        Profile profile = new Profile();

        Profile newProfile = profileMapper.updateFromTo(profile, testProfileTo);
        profileRepository.save(newProfile);

        perform(MockMvcRequestBuilders.get(REST_URL)
                        .with(user(authUser)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testProfileTo.getId().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mailNotifications[0]").value("deadline"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdate() throws Exception {

        Profile profile = new Profile();

        Profile newProfile = profileMapper.updateFromTo(profile, testProfileTo);
        profileRepository.save(newProfile);


        Set<String> updatedMailNotifications = Set.of("overdue");
        ProfileTo updatedProfileTo = new ProfileTo(testProfileTo.getId(), updatedMailNotifications, null);

        perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProfileTo))
                        .with(user(authUser)))
                .andExpect(status().isNoContent());

        Profile updatedProfile = profileRepository.getOrCreate(testProfileTo.getId());
        assertThat(updatedProfile.getMailNotifications()).isEqualTo(ProfileUtil.notificationsToMask(updatedMailNotifications));
    }

}
