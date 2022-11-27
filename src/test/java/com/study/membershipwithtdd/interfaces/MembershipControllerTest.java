package com.study.membershipwithtdd.interfaces;

import static com.study.membershipwithtdd.common.constansts.MembershipConstants.USER_ID_HEADER;
import static com.study.membershipwithtdd.common.response.MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER;
import static com.study.membershipwithtdd.domain.membership.Membership.MembershipType.NAVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.study.membershipwithtdd.common.exception.MembershipException;
import com.study.membershipwithtdd.common.response.CommonControllerAdvice;
import com.study.membershipwithtdd.common.response.CommonResponse;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import com.study.membershipwithtdd.domain.membership.MembershipService;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipRequest;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipResponse;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @WebMvcTest 해당 어노테이션을 이용할 수 있지만, 속도가 느리기에 직접 MockMvc를 생성
@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;
    @Mock
    private MembershipService membershipService;
    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
            .setControllerAdvice(new CommonControllerAdvice())
            .build();
        gson = new Gson();
    }

    @Test
    void mockMvc가_null_아님() {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void 멤버십등록실패_사용자식별값_헤더에_없음() throws Exception {
        // given
        String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(gson.toJson(membershipRequest(10000, NAVER)))
                .contentType(APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private MembershipRequest membershipRequest(Integer point, MembershipType membershipType) {
        return MembershipRequest.builder()
            .point(point)
            .membershipType(membershipType)
            .build();
    }

    @Test
    void 멤버십등록실패_point_null() throws Exception {
        // given
        String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(-1, NAVER)))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_membershipType_null() throws Exception {
        // given
        String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, null)))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_MembershipService_Throw() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(DUPLICATED_MEMBERSHIP_REGISTER))
            .when(membershipService)
            .addMembership("12345", NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, NAVER)))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    void 멤버십등록실패_잘못된_파라미터(Integer point, MembershipType membershipType) throws Exception {
        // given
        String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(point, membershipType)))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
            Arguments.of(null, NAVER),
            Arguments.of(-1, NAVER),
            Arguments.of(10000, null)
        );
    }

    @Test
    void 멤버십등록성공() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipResponse membershipResponse = MembershipResponse.builder()
            .id(-1L)
            .membershipType(NAVER)
            .build();

        doReturn(membershipResponse).when(membershipService).addMembership("12345", NAVER, 10000);

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, NAVER)))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());

        Object response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), CommonResponse.class)
            .getData();

        assertThat(response).isNotNull();
    }
}
