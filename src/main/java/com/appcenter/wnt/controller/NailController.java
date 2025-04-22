package com.appcenter.wnt.controller;

import com.appcenter.wnt.dto.request.NailReserveRequest;
import com.appcenter.wnt.dto.response.NailReserveResponse;
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
    public ResponseEntity<NailReserveResponse> reserveNail(@RequestBody NailReserveRequest request) {
        NailReserveResponse response = nailReservationService.reserveNail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{nailReservationId}")
    public ResponseEntity<String> cancelNail(@PathVariable("nailReservationId") Long nailReservationId) {
        nailReservationService.cancelNail(nailReservationId);
        return ResponseEntity.status(HttpStatus.OK).body("예매 취소 성공");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NailReserveResponse>> getNailReservations(@PathVariable("userId") Long userId) {
        List<NailReserveResponse> response = nailReservationService.getNailReservations(userId);
        return ResponseEntity.ok(response);
    }

}
