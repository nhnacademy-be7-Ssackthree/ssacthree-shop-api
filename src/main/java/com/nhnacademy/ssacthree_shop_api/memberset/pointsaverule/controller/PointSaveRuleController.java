package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.service.PointSaveRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/admin/point-save-rules")
@RequiredArgsConstructor
public class PointSaveRuleController {

    private final PointSaveRuleService pointSaveRuleService;

    @GetMapping
    public ResponseEntity<List<PointSaveRuleGetResponse>> getAllPointSaveRules() {
        return ResponseEntity.ok().body(pointSaveRuleService.getAllPointSaveRules());
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createPointSaveRule(
            @Valid @RequestBody PointSaveRuleCreateRequest request) {

        pointSaveRuleService.createPointSaveRule(request);
        MessageResponse messageResponse = new MessageResponse("생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updatePointSaveRule(
            @Valid @RequestBody PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest) {

        pointSaveRuleService.updatePointSaveRule(pointSaveRuleUpdateRequest);
        MessageResponse messageResponse = new MessageResponse("수정 성공");

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
}
