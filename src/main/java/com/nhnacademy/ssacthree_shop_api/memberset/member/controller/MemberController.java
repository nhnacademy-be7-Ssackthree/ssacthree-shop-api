package com.nhnacademy.ssacthree_shop_api.memberset.member.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MessageResponse> registerMember(
        @RequestBody MemberRegisterRequest memberRegisterRequest) {

        memberService.registerMember(memberRegisterRequest);
        MessageResponse messageResponse = new MessageResponse("생성 성공");
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteMember(@RequestHeader(name="X-USER-ID") String memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.deleteMember(memberId));
    }


    @GetMapping("/my-page")
    public ResponseEntity<MemberInfoGetResponse> getMemberInfo(@RequestHeader(name = "X-USER-ID") String memberId) {

        MemberInfoGetResponse memberInfoGetResponse = memberService.getMemberInfoById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberInfoGetResponse);

    }

    @PutMapping("/my-page")
    public ResponseEntity<MessageResponse> updateMemberInfo(@RequestHeader(name = "X-USER-ID") String memberId, @RequestBody MemberInfoUpdateRequest  memberInfoUpdateRequest) {
        MessageResponse messageResponse = memberService.updateMember(memberId, memberInfoUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
}
