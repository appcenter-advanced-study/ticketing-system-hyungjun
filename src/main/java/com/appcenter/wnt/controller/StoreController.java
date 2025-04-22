package com.appcenter.wnt.controller;

import com.appcenter.wnt.dto.request.CreateStoreRequest;
import com.appcenter.wnt.dto.response.StoreResponse;
import com.appcenter.wnt.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Store",description = "Store API")
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 생성
    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@RequestBody CreateStoreRequest request) {
        StoreResponse response = storeService.createStore(request.userId(), request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStore(@PathVariable("storeId") Long storeId) {
        StoreResponse response = storeService.findStore(storeId);
        return ResponseEntity.ok(response);
    }

    // 가게 전체 목록 조회
    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> response = storeService.findAllStore();
        return ResponseEntity.ok(response);
    }

    // 가게 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable("storeId") Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.noContent().build();
    }

}
