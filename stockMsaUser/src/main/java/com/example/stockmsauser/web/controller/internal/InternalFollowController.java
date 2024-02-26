package com.example.stockmsauser.web.controller.internal;

import com.example.stockmsauser.service.followService.FollowService;
import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.example.stockmsauser.web.dto.response.follow.FollowerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/internal/user")
@RequiredArgsConstructor
public class InternalFollowController {
    private final FollowService followService;

    @PostMapping("/get-my-follower")
    ResponseEntity<List<FollowerDto>> getMyFollower(
            @RequestBody @Valid GetMyFollowersRequestDto requestBody
    ){
        List<FollowerDto> myFollower = followService.getMyFollower(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(myFollower);
    }
}

