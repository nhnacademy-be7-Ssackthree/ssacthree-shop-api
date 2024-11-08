package com.nhnacademy.ssacthree_shop_api.memberset.member.domain;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "member")
public class Member {

    public Member(
        Customer customer,
        String loginId,
        String password,
        String birthdate
    ) {
        this.customer = customer;
        this.memberLoginId = loginId;
        this.memberPassword = password;
        this.memberBirthdate = birthdate;
    }

    @Id
    private long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @Setter
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_grade_id")
    @Setter
    private MemberGrade memberGrade;


    private String memberLoginId;


    private String memberPassword;


    private String memberBirthdate;


    private LocalDateTime memberCreatedAt = LocalDateTime.now();


    private LocalDateTime memberLastLoginAt;


    @Enumerated(EnumType.STRING)
    @Setter
    private MemberStatus memberStatus = MemberStatus.ACTIVE;

    private int memberPoint = 0;


}
