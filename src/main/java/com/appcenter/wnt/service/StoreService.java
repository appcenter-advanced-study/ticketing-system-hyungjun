package com.appcenter.wnt.service;

import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.response.StoreResponse;
import com.appcenter.wnt.repository.StoreRepository;
import com.appcenter.wnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponse createStore(Long userId, String name){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저가 존재하지 않습니다."));
        storeRepository.findByName(name).ifPresent(n ->{
            throw new RuntimeException("이미 존재하는 가게입니다.");
        });

        Store store= Store.of(user, name);
        return StoreResponse.from(store);
    }

    @Transactional(readOnly = true)
    public StoreResponse findStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("가게가 존재하지 않습니다."));
        return StoreResponse.from(store);
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> findAllStore() {
        return storeRepository.findAll().stream().map(StoreResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public void deleteStore(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(()-> new RuntimeException("존재하지 않은 가게입니다."));
        storeRepository.delete(store);
    }
}
