package com.nhnacademy.ssacthree_shop_api.memberset.member.service;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;

public interface MemberService {

    MessageResponse registerMember(MemberRegisterRequest memberCreateRequest);

    MemberInfoGetResponse getMemberInfoById(String memberLoginId);

    MessageResponse updateMember(String memberLoginId, MemberInfoUpdateRequest memberInfoUpdateRequest);

    MessageResponse deleteMember(String memberLoginId);



}
