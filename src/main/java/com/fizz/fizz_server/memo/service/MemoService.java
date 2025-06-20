package com.fizz.fizz_server.memo.service;

import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import com.fizz.fizz_server.memo.dto.request.MemoRequestDto;
import com.fizz.fizz_server.memo.dto.response.MemoResponseDto;
import com.fizz.fizz_server.memo.entity.Memo;
import com.fizz.fizz_server.memo.repository.MemoRepository;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.store.repository.StoreRepository;
import com.fizz.fizz_server.user.entity.User;
import com.fizz.fizz_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public MemoResponseDto createMemo(Long userId, MemoRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.USER_NOT_FOUND));
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new BusinessException(ExceptionType.STORE_NOT_FOUND));

        Memo memo = memoRepository.save(requestDto.toEntity(user, store));

        return MemoResponseDto.fromEntity(memo);
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getAllMemos(Long requesterId, Long ownerId) {
        validateUserAccess(requesterId, ownerId);
        return memoRepository.findAllByUserId(ownerId)
                .stream().map(MemoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getMemosByStoreId(Long requesterId, Long storeId) {
        return memoRepository.findAllByUserIdAndStoreId(requesterId, storeId)
                .stream().map(MemoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemoResponseDto getMemoById(Long requesterId, Long memoId) {
        Memo memo = memoRepository.findByIdAndUserId(memoId, requesterId)
                .orElseThrow(() -> new BusinessException(ExceptionType.FORBIDDEN));
        return MemoResponseDto.fromEntity(memo);
    }

    public MemoResponseDto updateMemo(Long userId, Long memoId, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findByIdAndUserId(memoId, userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.FORBIDDEN));
        memo.update(requestDto);
        return MemoResponseDto.fromEntity(memo);
    }

    public void deleteMemo(Long userId, Long memoId) {
        Memo memo = memoRepository.findByIdAndUserId(memoId, userId)
                .orElseThrow(() -> new BusinessException(ExceptionType.FORBIDDEN));
        memoRepository.delete(memo);
    }


    private void validateUserAccess(Long requesterId, Long ownerId) {
        if (!requesterId.equals(ownerId)) {
            throw new BusinessException(ExceptionType.FORBIDDEN);
        }
    }
}
