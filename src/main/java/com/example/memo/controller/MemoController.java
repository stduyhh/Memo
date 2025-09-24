package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


//memo 생성
@RestController
@RequestMapping("/memos")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();


    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto dto){
        // 식별자 1씩 증가
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        //Request 데이터 객체 생성
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        //메모 저장
        memoList.put(memoId, memo);

        return  new MemoResponseDto(memo);
    }

    //메모 조회
    @GetMapping("/{id}")
    public MemoResponseDto findMemoById(@PathVariable Long id){

        Memo memo = memoList.get(id);

        return new MemoResponseDto(memo);
    }

    //메모 수정
    @PutMapping("/{id}")
    public MemoResponseDto updateMemoById(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto
    ){
        Memo memo = memoList.get(id);

        memo.update(dto);

        return new MemoResponseDto(memo);
    }


    @DeleteMapping("/{id}")
    public void deleteMemo(@PathVariable Long id){
        memoList.remove(id);
    }


}
