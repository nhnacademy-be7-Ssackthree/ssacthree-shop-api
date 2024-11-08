package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name="point_history")
public class PointHistory {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pointHistoryId;

    @Null
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="point_save_rule_id")
    private PointSaveRule pointSaveRule;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Member member;

    @NotNull
    private int pointAmount;

    @NotNull
    private LocalDateTime pointChangeDate = LocalDateTime.now();

    @NotNull
    @Size(max = 50)
    private String pointChangeReason;
}
