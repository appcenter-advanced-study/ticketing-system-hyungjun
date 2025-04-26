package com.appcenter.wnt.controller;

import com.appcenter.wnt.dto.request.NailReservationRequest;
import com.appcenter.wnt.dto.response.NailReservationResponse;
import com.appcenter.wnt.service.NailReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NailReservation",description = "NailReservation API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nail-reservations")
public class NailController {
    private final NailReservationService nailReservationService;


    @PostMapping
    public ResponseEntity<NailReservationResponse> reserve(@RequestBody NailReservationRequest request) {
        NailReservationResponse response = nailReservationService.reserve(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{nailReservationId}")
    public ResponseEntity<String> cancel(@PathVariable("nailReservationId") Long nailReservationId) {
        nailReservationService.cancel(nailReservationId);
        return ResponseEntity.status(HttpStatus.OK).body("예매 취소 성공");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<NailReservationResponse>> getReservations(@PathVariable("userId") Long userId) {
        List<NailReservationResponse> response = nailReservationService.getReservations(userId);
        return ResponseEntity.ok(response);
    }

}
