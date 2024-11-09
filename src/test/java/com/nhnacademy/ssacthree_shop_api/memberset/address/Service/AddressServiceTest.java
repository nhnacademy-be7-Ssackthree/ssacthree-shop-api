package com.nhnacademy.ssacthree_shop_api.memberset.address.Service;

import com.nhnacademy.ssacthree_shop_api.memberset.address.domain.Address;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.address.exception.DuplicateAddressException;
import com.nhnacademy.ssacthree_shop_api.memberset.address.exception.OverAddressException;
import com.nhnacademy.ssacthree_shop_api.memberset.address.repository.AddressRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.impl.AddressServiceImpl;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member member;
    private Address address;
    private AddressCreateRequest addressCreateRequest;

    @BeforeEach
    void setUp() {
        // Mock Customer 객체 생성 및 Member 초기화
        Customer customer = mock(Customer.class);
        member = new Member(customer, "testUserId", "password123", "1990-01-01");

        // 테스트용 AddressCreateRequest 및 Address 객체 초기화
        addressCreateRequest = new AddressCreateRequest("alias", "detail", "roadname", "postalNumber");
        address = new Address(member, "alias", "detail", "roadname", "postalNumber");
    }

    @Test
    void testCreateAddress_Success() {
        // 필요한 경우에만 MemberRepository에 대한 Mock 설정
        when(memberRepository.findByMemberLoginId("testUserId")).thenReturn(Optional.of(member));

        // AddressCreateRequest에 설정된 값과 동일한 인수로 AddressRepository에 대한 Mock 설정
        when(addressRepository.findByAddressRoadnameAndMember(addressCreateRequest.getAddressRoadname(), member))
            .thenReturn(Optional.empty());
        when(addressRepository.findAllByMember(member)).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // createAddress 호출 및 결과 검증
        AddressResponse response = addressService.createAddress("testUserId", addressCreateRequest);

        assertNotNull(response);
        assertEquals("alias", response.getAddressAlias());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testCreateAddress_MemberNotFoundException() {
        // MemberRepository가 Optional.empty()를 반환하도록 설정
        when(memberRepository.findByMemberLoginId("nonExistingUserId")).thenReturn(Optional.empty());

        // MemberNotFoundException이 발생하는지 검증
        assertThrows(MemberNotFoundException.class, () ->
            addressService.createAddress("nonExistingUserId", addressCreateRequest)
        );
    }

    @Test
    void testCreateAddress_DuplicateAddressException() {
        // MemberRepository와 AddressRepository에 대한 Mock 설정
        when(memberRepository.findByMemberLoginId("testUserId")).thenReturn(Optional.of(member));
        when(addressRepository.findByAddressRoadnameAndMember(addressCreateRequest.getAddressRoadname(), member))
            .thenReturn(Optional.of(address));

        // DuplicateAddressException이 발생하는지 검증
        assertThrows(DuplicateAddressException.class, () ->
            addressService.createAddress("testUserId", addressCreateRequest)
        );
    }

    @Test
    void testCreateAddress_OverAddressException() {
        // MemberRepository와 AddressRepository에 대한 Mock 설정
        when(memberRepository.findByMemberLoginId("testUserId")).thenReturn(Optional.of(member));
        when(addressRepository.findAllByMember(member)).thenReturn(List.of(
            new Address(), new Address(), new Address(), new Address(), new Address(),
            new Address(), new Address(), new Address(), new Address(), new Address()
        ));

        // OverAddressException이 발생하는지 검증
        assertThrows(OverAddressException.class, () ->
            addressService.createAddress("testUserId", addressCreateRequest)
        );
    }

    @Test
    void testGetAddressesByUserId_Success() {
        // MemberRepository와 AddressRepository에 대한 Mock 설정
        when(memberRepository.findByMemberLoginId("testUserId")).thenReturn(Optional.of(member));
        when(addressRepository.findAllByMember(member)).thenReturn(List.of(address));

        // 주소 목록 가져오기 및 검증
        List<AddressResponse> responses = addressService.getAddressesByUserId("testUserId");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("alias", responses.getFirst().getAddressAlias());
    }

    @Test
    void testDeleteAddressById_Success() {
        // AddressRepository에 대한 Mock 설정
        when(addressRepository.findAddressByRegisteredAddressId(1L)).thenReturn(address);

        // 주소 삭제 호출 및 검증
        addressService.deleteAddressById("1");

        verify(addressRepository, times(1)).delete(address);
    }
}

