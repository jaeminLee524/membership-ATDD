package com.study.membershipwithtdd.interfaces;

import static com.study.membershipwithtdd.common.constansts.MembershipConstants.USER_ID_HEADER;

import com.study.membershipwithtdd.common.response.CommonResponse;
import com.study.membershipwithtdd.domain.membership.MembershipService;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipAddResponse;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipDetailResponse;
import com.study.membershipwithtdd.interfaces.MembershipDto.MembershipRequest;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public CommonResponse<MembershipAddResponse> addMembership(
        @RequestHeader(USER_ID_HEADER) String userId,
        @RequestBody @Valid MembershipRequest membershipRequest
    ) {
        MembershipAddResponse membershipAddResponse =
            membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return CommonResponse.success(membershipAddResponse);
    }

    @GetMapping("/api/v1/memberships")
    public CommonResponse<List<MembershipDetailResponse>> getMembershipList(
        @RequestHeader(USER_ID_HEADER) String userId
    ) {
        List<MembershipDetailResponse> membershipList = membershipService.getMembershipList(userId);

        return CommonResponse.success(membershipList);
    }

    @GetMapping("/api/v1/memberships/{id}")
    public CommonResponse<MembershipDetailResponse> getMembership(
        @RequestHeader(USER_ID_HEADER) String userId,
        @PathVariable("id") Long id
    ) {
        MembershipDetailResponse detailResponse = membershipService.getMembership(id, userId);

        return CommonResponse.success(detailResponse);
    }

    @DeleteMapping("/api/v1/memberships")
    public CommonResponse deleteMembership(
        @RequestHeader(USER_ID_HEADER) String userId,
        @PathVariable("id") Long id
    ) {
        membershipService.removeMembership(id, userId);

        return CommonResponse.success("OK");
    }
}
