package com.nhnacademy.ssacthree_shop_api.member_set.address.domain;

import com.nhnacademy.ssacthree_shop_api.member_set.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Address {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Member member;

    @Null
    @Size(max = 15)
    private String addressAlias;

    @NotNull
    @Size(max = 35)
    private String addressDetail;

    @NotNull
    @Size(min = 5, max = 5)
    private String addressPostalNumber;

}
