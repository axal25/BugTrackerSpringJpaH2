package axal25.oles.jacek.controller;

import axal25.oles.jacek.config.AppInfoListener;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.json.JsonProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static axal25.oles.jacek.constant.Constants.EndpointPaths.APPLICATION_CONTROLLER;
import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8089")
@AutoConfigureMockMvc
public class ApplicationControllerTest {

    private String applicationControllerEnpointPathFull;

    @Autowired
    private AppInfoListener appInfoListener;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        applicationControllerEnpointPathFull =
                appInfoListener.getFullHostAddress() +
                        APPLICATION_CONTROLLER;
    }

    @Test
    public void addApplication_responseStatusCreated_applicationWithId_headerLocationGetApplication() throws Exception {
        ApplicationEntity inputBodyApplication = ApplicationEntityFactory.produce(
                "addApplication_responseStatusCreated_applicationWithId_headerLocationGetApplication",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.NULL);
        ApplicationEntity responseApplication =
                addApplicationResponseStatusCreatedApplicationWithIdHeaderLocationGetApplication(
                        inputBodyApplication);
    }

    private ApplicationEntity addApplicationResponseStatusCreatedApplicationWithIdHeaderLocationGetApplication(
            ApplicationEntity inputBodyApplication) throws Exception {
        assertThat(inputBodyApplication.getId()).isNull();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(applicationControllerEnpointPathFull)
                .accept(ALL)
                .contentType(APPLICATION_JSON)
                .content(inputBodyApplication.toJsonString());

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(any(Integer.class), Integer.class))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(mvcResult.getResponse().getHeader(HttpHeaders.LOCATION))
                .startsWith(applicationControllerEnpointPathFull + "/");
        assertThat(mvcResult.getResponse().getHeader(HttpHeaders.LOCATION))
                .matches(".*/[0-9]*");
        assertThat(mvcResult.getResponse().getContentType()).isEqualTo(APPLICATION_JSON_VALUE);

        ApplicationEntity responseApplication = JsonProvider.getObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                ApplicationEntity.class);
        assertThat(responseApplication.getId()).isNotNull();
        assertThat(responseApplication.getName()).isEqualTo(inputBodyApplication.getName());
        assertThat(responseApplication.getDescription()).isEqualTo(inputBodyApplication.getDescription());
        assertThat(responseApplication.getOwner()).isEqualTo(inputBodyApplication.getOwner());

        return responseApplication;
    }

    @Test
    public void getApplicationById_response() throws Exception {
        ApplicationEntity addInputBodyApplication = ApplicationEntityFactory.produce(
                "getApplicationById_response",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.NULL);
        ApplicationEntity addResponseApplication =
                addApplicationResponseStatusCreatedApplicationWithIdHeaderLocationGetApplication(
                        addInputBodyApplication);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(applicationControllerEnpointPathFull + "/" + addResponseApplication.getId())
                .accept(ALL);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(addResponseApplication.getId()))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(mvcResult.getResponse().getContentType()).isEqualTo(APPLICATION_JSON_VALUE);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(addResponseApplication.toJsonString());
    }

    @Test
    public void updateApplication() throws Exception {
        ApplicationEntity addInputBodyApplication = ApplicationEntityFactory.produce(
                "updateApplication",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.NULL);
        ApplicationEntity addResponseApplication =
                addApplicationResponseStatusCreatedApplicationWithIdHeaderLocationGetApplication(
                        addInputBodyApplication);

        ApplicationEntity updateInputBodyApplication = addResponseApplication.toBuilder()
                .name(addResponseApplication.getName() + " updated")
                .description(addResponseApplication.getDescription() + " updated")
                .owner(addResponseApplication.getOwner() + " updated")
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(applicationControllerEnpointPathFull)
                .accept(ALL)
                .contentType(APPLICATION_JSON)
                .content(updateInputBodyApplication.toJsonString());

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(addResponseApplication.getId()))
                .andExpect(jsonPath("$.id").value(updateInputBodyApplication.getId()))

                .andExpect(jsonPath("$.name").value(updateInputBodyApplication.getName()))
                .andExpect(jsonPath("$.name").value(matchesPattern("^.* updated$"), String.class))

                .andExpect(jsonPath("$.description").value(updateInputBodyApplication.getDescription()))
                .andExpect(jsonPath("$.description").value(matchesPattern("^.* updated$"), String.class))

                .andExpect(jsonPath("$.owner").value(updateInputBodyApplication.getOwner()))
                .andExpect(jsonPath("$.owner").value(matchesPattern("^.* updated$"), String.class))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(updateInputBodyApplication.toJsonString());
    }

    @Test
    public void deleteApplication() throws Exception {
        ApplicationEntity addInputBodyApplication = ApplicationEntityFactory.produce(
                "deleteApplication",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.NULL);
        ApplicationEntity addResponseApplication =
                addApplicationResponseStatusCreatedApplicationWithIdHeaderLocationGetApplication(
                        addInputBodyApplication);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(applicationControllerEnpointPathFull + "/" + addResponseApplication.getId())
                .accept(ALL);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
