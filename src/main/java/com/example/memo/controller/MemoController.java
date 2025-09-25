package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


//memo 생성
@RestController
@RequestMapping("/memos")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();


    @PostMapping
    public ResponseEntity<MemoResponseDto>  createMemo(@RequestBody MemoRequestDto dto){
        // 식별자 1씩 증가
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        //Request 데이터 객체 생성
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        //메모 저장
        memoList.put(memoId, memo);

        return  new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED) ;
    }

    @GetMapping
    public List<MemoResponseDto> findAllMemos(){

        //List 초기화
        List<MemoResponseDto> responseList = new ArrayList<>();

        //HashMap<Memo> -> List<MemoResponseDto>
        for (Memo memo:memoList.values()){
            MemoResponseDto responseDto = new MemoResponseDto(memo);
            responseList.add(responseDto);
        }
        //Map to List
//        responseList = memoList.values().stream().map(MemoResponseDto::new).toList();
        return  responseList;
    }

    //메모 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id){

        Memo memo = memoList.get(id);

        //예외 처리
        if (memo == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    //메모 수정
//    @PutMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateMemoById(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto
    ){
        Memo memo = memoList.get(id);

        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (dto.getTitle() == null || dto.getContents() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        memo.update(dto);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateTitle(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto
    ){
        Memo memo = memoList.get(id);

        //NEP 방지
        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (dto.getTitle() == null || dto.getContents() != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        memo.updateTitle(dto);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long id){

        // memoList의 key값에 id를 포함하면
        if (memoList.containsKey(id)){
            memoList.remove(id);


            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
