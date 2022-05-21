package com.planet.develop.Controller;

import com.planet.develop.DTO.IncomeRequestDto;
import com.planet.develop.DTO.IncomeResponseDto;
import com.planet.develop.Security.DTO.AuthMemberDTO;
import com.planet.develop.Service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('USER')")
public class IncomeController {

    private final IncomeService incomeService;

    /** 수입 데이터 저장*/
    /** id는 사용자 id가 아니라 income의 pk값*/
    @PostMapping("/income/{id}/new")
    public IncomeResponseDto create_income(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @RequestBody IncomeRequestDto dto) {
        dto.setUserId(authMemberDTO.getEmail());
        Long incomeId = incomeService.save(dto);
        return new IncomeResponseDto(incomeId);
    }

    /**수입 데이터 수정*/
    @PostMapping("/income/{id}/update")
    public IncomeResponseDto update_income(@PathVariable("id") Long id, @RequestBody IncomeRequestDto request){
        incomeService.update(id,request.getIn_cost(),request.getIn_way(),
                request.getIn_type(),request.getMemo(),request.getDate());

        return new IncomeResponseDto(id);
    }

    /** 수입 데이터 삭제*/
    @DeleteMapping("/calendar/{month}/income/{id}")
    public void delete_income(@PathVariable("id") Long id){
        incomeService.delete(id);
    }

}