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
import com.study.membershipwithtdd.common.response.MembershipErrorResult;
import com.study.membershipwithtdd.domain.membership.Membership;
import com.study.membershipwithtdd.domain.membership.Membership.MembershipType;
import com.study.membershipwithtdd.domain.membership.MembershipService;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipAddResponse;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipDetailResponse;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipAddRequest;
import com.study.membershipwithtdd.interfaces.MembershipDto.PointAccumulateRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

// @WebMvcTest ?????? ?????????????????? ????????? ??? ?????????, ????????? ???????????? ?????? MockMvc??? ??????
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
    void mockMvc???_null_??????() {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void ?????????????????????_??????????????????_?????????_??????() throws Exception {
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

    private MembershipAddRequest membershipRequest(Integer point, MembershipType membershipType) {
        return MembershipAddRequest.builder()
            .point(point)
            .membershipType(membershipType)
            .build();
    }

    @Test
    void ?????????????????????_point_null() throws Exception {
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
    void ?????????????????????_membershipType_null() throws Exception {
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
    void ?????????????????????_MembershipService_Throw() throws Exception {
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
    void ?????????????????????_?????????_????????????(Integer point, MembershipType membershipType) throws Exception {
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
    void ?????????????????????() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipAddResponse = MembershipAddResponse.builder()
            .id(-1L)
            .membershipType(NAVER)
            .build();

        doReturn(membershipAddResponse).when(membershipService).addMembership("12345", NAVER, 10000);

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

    @Test
    void ???????????????????????????_??????????????????_???????????????() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void ?????????????????????_??????() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
            MembershipDetailResponse.builder().build(),
            MembershipDetailResponse.builder().build(),
            MembershipDetailResponse.builder().build()
        )).when(membershipService).getMembershipList("12345");

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ?????????????????????_??????_??????????????????_???????????????() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void ?????????????????????_??????_??????????????????????????????() throws Exception {
        // given
        final String url = "/api/v1/memberships/-1";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
            .when(membershipService).getMembership(-1L, "12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void ???????????????_??????() throws Exception {
        // given
        final String url = "/api/v1/memberships/-1";
        doReturn(
            MembershipDetailResponse.builder().build()
        ).when(membershipService).getMembership(-1L, "12345");

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header(USER_ID_HEADER, "12345")
                .param("membershipType", NAVER.name())
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ???????????????_??????_??????????????????_???????????????() throws Exception {
        // given
        String url = "/api/v1/memberships/-1";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void ???????????????_??????() throws Exception {
        // given
        String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete(url)
                .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ???????????????_??????_??????????????????_???????????????() throws Exception {
        // given
        final String url = "/api/v1/memberships/-1/accumulate";

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(gson.toJson(membershipRequest(10000)))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void ???????????????_??????_??????????????????() throws Exception {
        // given
        final String url = "/api/v1/memberships/-1/accumulate";

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(-10000, NAVER)))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void ???????????????_??????() throws Exception {
        // given
        final String url = "/api/v1/memberships/-1/accumulate";

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "1345")
                .content(gson.toJson(PointAccumulateRequest.builder().point(10000).build()))
                .contentType(APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    public static Membership membershipRequest(int point) {
        return Membership.builder()
            .point(point)
            .build();
    }
}