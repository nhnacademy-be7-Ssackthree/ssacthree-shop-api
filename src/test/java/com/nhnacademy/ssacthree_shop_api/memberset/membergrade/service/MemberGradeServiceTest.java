package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.dto.MemberGradeUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.exception.MemberGradeNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.repository.MemberGradeRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
public class MemberGradeServiceTest {

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private MemberGradeService memberGradeService;


    @Test
    void createMemberGrade() {

        //given
        MemberGradeCreateRequest dummy = new MemberGradeCreateRequest("test",true,0.01f);
        //when
        memberGradeService.createMemberGrade(dummy);
        //then
        verify(memberGradeRepository).save(any(MemberGrade.class));
    }


    @Test
    void updateMemberGrade() {
        // given
        Long memberGradeId = 1L;
        MemberGradeUpdateResponse updateRequest = new MemberGradeUpdateResponse("test2", false, 0.02f);
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);

        when(memberGradeRepository.findById(memberGradeId)).thenReturn(Optional.of(existingMemberGrade));
        when(memberGradeRepository.existsById(memberGradeId)).thenReturn(true);

        // when
        memberGradeService.updateMemberGrade(memberGradeId, updateRequest);

        // then
        verify(memberGradeRepository).save(existingMemberGrade);
    }

    @Test
    void updateMemberGradeException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.updateMemberGrade(-1L, null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.updateMemberGrade(0L, null);
        });

        Assertions.assertThrows(MemberGradeNotFoundException.class, () -> {
            when(memberGradeRepository.existsById(1L)).thenReturn(false);
            memberGradeService.updateMemberGrade(1L, null);
        });
    }

    @Test
    void deleteMemberGrade() {

        //given
        Long memberGradeId = 1L;
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);
        when(memberGradeRepository.existsById(memberGradeId)).thenReturn(true);

        //when
        memberGradeService.deleteMemberGradeById(memberGradeId);

        //then
        verify(memberGradeRepository).deleteById(memberGradeId);
    }

    @Test
    void deleteMemberGradeException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.deleteMemberGradeById(-1L);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.deleteMemberGradeById(0L);
        });

        Assertions.assertThrows(MemberGradeNotFoundException.class, () -> {
            when(memberGradeRepository.existsById(1L)).thenReturn(false);
            memberGradeService.deleteMemberGradeById(1L);
        });
    }


    @Test
    void getMemberGradeById() {
        Long memberGradeId = 1L;
        MemberGrade existingMemberGrade = new MemberGrade("test1", true, 0.01f);
        when(memberGradeRepository.findById(memberGradeId)).thenReturn(Optional.of(existingMemberGrade));
        when(memberGradeRepository.existsById(memberGradeId)).thenReturn(true);

        memberGradeService.getMemberGradeById(memberGradeId);
        verify(memberGradeRepository).findById(memberGradeId);
    }

    @Test
    void getMemberGradeByIdException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.getMemberGradeById(-1L);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberGradeService.getMemberGradeById(0L);
        });

        Assertions.assertThrows(MemberGradeNotFoundException.class, () -> {
            when(memberGradeRepository.existsById(1L)).thenReturn(false);
            memberGradeService.getMemberGradeById(1L);
        });
    }

    @Test
    void getAllMemberGrades() {
        // given
        List<MemberGrade> memberGrades = Arrays.asList(
            new MemberGrade("test1", true, 0.01f),
            new MemberGrade("test2", true, 0.02f)
        );

        when(memberGradeRepository.findAll()).thenReturn(memberGrades);

        // when
        List<MemberGradeGetResponse> result = memberGradeService.getAllMemberGrades();

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("test1", result.get(0).getMemberGradeName());
        Assertions.assertEquals(0.01f, result.get(0).getMemberGradePointSave());

        verify(memberGradeRepository).findAll();
    }




}