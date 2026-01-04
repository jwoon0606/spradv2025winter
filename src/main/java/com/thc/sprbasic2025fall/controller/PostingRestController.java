package com.thc.sprbasic2025fall.controller;

import com.thc.sprbasic2025fall.domain.Posting;
import com.thc.sprbasic2025fall.dto.DefaultDto;
import com.thc.sprbasic2025fall.dto.PostingDto;
import com.thc.sprbasic2025fall.service.PostingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/posting")
@RestController
public class PostingRestController {

    final PostingService postingService;

    @PostMapping("")
    public ResponseEntity<DefaultDto.CreateResDto> create(@RequestBody PostingDto.CreateReqDto param, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if(userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        else param.setUserId(userId);

        //return postingService.create(param);
        //return ResponseEntity.status(HttpStatus.OK).body(postingService.create(param));
        return ResponseEntity.ok(postingService.create(param));
    }
    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody PostingDto.UpdateReqDto param, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("userId = " + userId);
        if(userId == null){
            //로그인 안되었을때 돌려보내기!
            System.out.println("userId is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {

        }

        postingService.update(param, userId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("")
    public ResponseEntity<Void> delete(@RequestBody PostingDto.UpdateReqDto param, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        postingService.delete(param,userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<PostingDto.DetailResDto> detail(DefaultDto.DetailReqDto param, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(postingService.detail(param,userId));
    }
    @GetMapping("/list")
    public ResponseEntity<List<PostingDto.DetailResDto>> list(PostingDto.ListReqDto param, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(postingService.list(param, userId));
    }
    @GetMapping("/pagedList")
    public ResponseEntity<DefaultDto.PagedListResDto> pagedList(PostingDto.PagedListReqDto param) {
        return ResponseEntity.ok(postingService.pagedList(param));
    }
    @GetMapping("/scrollList")
    public ResponseEntity<List<PostingDto.DetailResDto>> scrollList(PostingDto.ScrollListReqDto param) {
        return ResponseEntity.ok(postingService.scrollList(param));
    }

}
