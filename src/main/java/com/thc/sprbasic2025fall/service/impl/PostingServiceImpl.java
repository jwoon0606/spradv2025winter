package com.thc.sprbasic2025fall.service.impl;

import com.thc.sprbasic2025fall.domain.Posting;
import com.thc.sprbasic2025fall.dto.DefaultDto;
import com.thc.sprbasic2025fall.dto.PostimgDto;
import com.thc.sprbasic2025fall.dto.PostingDto;
import com.thc.sprbasic2025fall.mapper.PostingMapper;
import com.thc.sprbasic2025fall.repository.PostingRepository;
import com.thc.sprbasic2025fall.service.PostimgService;
import com.thc.sprbasic2025fall.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PostingServiceImpl implements PostingService {

    final PostingRepository postingRepository;
    final PostingMapper postingMapper;
    final PostimgService postimgService;

    @Override
    public DefaultDto.CreateResDto create(PostingDto.CreateReqDto param) {

        if(param.getImgs() != null && !param.getImgs().isEmpty()){
            param.setImg(param.getImgs().get(0));
        }

        DefaultDto.CreateResDto res = postingRepository.save(param.toEntity()).toCreateResDto();

        for(String img : param.getImgs()){
            postimgService.create(PostimgDto.CreateReqDto.builder().postingId(res.getId()).img(img).build());
        }

        return res;
    }

    @Override
    public void update(PostingDto.UpdateReqDto param, Long userId) {
        Posting posting = postingRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException("no data"));
        if(!userId.equals(posting.getUserId())) {
            throw new RuntimeException("you don't have permission to update posting");
        }
        posting.update(param);
        postingRepository.save(posting);
    }

    @Override
    public void delete(PostingDto.UpdateReqDto param, Long userId) {
        update(PostingDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), userId);
    }


    @Override
    public PostingDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long userId) {
        Posting posting = postingRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException("no data"));
        if(!userId.equals(posting.getUserId())) {
            throw new RuntimeException("you don't have permission to access the posting");
        }
        return get(param);
    }

    public PostingDto.DetailResDto get(DefaultDto.DetailReqDto param) {
        PostingDto.DetailResDto res = postingMapper.detail(param.getId());
        System.out.println("res??? : " + res);
        res.setImgs(postimgService.list(PostimgDto.ListReqDto.builder().deleted(false).postingId(res.getId()).build()));
        return res;
    }

    public List<PostingDto.DetailResDto> addlist(List<PostingDto.DetailResDto> list) {
        List<PostingDto.DetailResDto> newList = new ArrayList<>();
        for (PostingDto.DetailResDto posting : list) {
            newList.add(get(DefaultDto.DetailReqDto.builder().id(posting.getId()).build()));
        }
        return newList;
    }

    @Override
    public List<PostingDto.DetailResDto> list(PostingDto.ListReqDto param, Long userId) {
        List<PostingDto.DetailResDto> postings = postingMapper.list(param);

        List<PostingDto.DetailResDto> filtered = new ArrayList<>();
        for (PostingDto.DetailResDto posting : postings) {
            if(Objects.equals(posting.getUserId(), userId)) filtered.add(posting);
        }

        return addlist(filtered);
    }
    @Override
    public DefaultDto.PagedListResDto pagedList(PostingDto.PagedListReqDto param) {
        DefaultDto.PagedListResDto res = param.init(postingMapper.listCount(param));
        res.setList(addlist(postingMapper.pagedList(param)));
        return res;
    }

    @Override
    public List<PostingDto.DetailResDto> scrollList(PostingDto.ScrollListReqDto param) {
        return addlist(postingMapper.scrollList(param));
    }
}
