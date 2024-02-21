package com.example.stockmsauser.web.dto.response.follow;

import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetMyFollowersResponseDto extends ResponseDto {
    List<FollowerDto> followerList;
    public GetMyFollowersResponseDto(List<FollowerDto> followerList) {
        super();
        this.followerList = followerList;
    }
    public static ResponseEntity<GetMyFollowersResponseDto> success(List<FollowerDto> followerList) {
        GetMyFollowersResponseDto responseBody = new GetMyFollowersResponseDto(followerList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseEntity<ResponseDto> response = ResponseDto.databaseError();
        return response;
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
