package com.planet.develop.Controller;

import com.planet.develop.DTO.ExpenditureRequestDto;
import com.planet.develop.DTO.ExpenditureResponseDto;
import com.planet.develop.Entity.Expenditure;
import com.planet.develop.Entity.ExpenditureDetail;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.ExpenditureDetailRepository;
import com.planet.develop.Repository.ExpenditureRepository;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Security.DTO.AuthMemberDTO;
import com.planet.develop.Service.EcoService;
import com.planet.develop.Service.ExpenditureDetailService;
import com.planet.develop.Service.ExpenditureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('USER')")
public class ExpenditureController {

    private final UserRepository userRepository;
    private final ExpenditureDetailRepository detailRepository;
    private final ExpenditureRepository expenditureRepository;
    private final ExpenditureDetailService detailService;
    private final ExpenditureService expenditureService;
    private final EcoService ecoService;

    /** 지출 데이터 저장 */
    @PostMapping("/expenditure/new")
    public ExpenditureResponseDto create_expenditure(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                                                @RequestBody ExpenditureRequestDto reuqest) {
        User user = userRepository.findById(authMemberDTO.getEmail()).get();
        reuqest.setUserId(user.getUserId());
        Long deno = detailService.save(reuqest); // 지출 상세 테이블 저장
        ExpenditureDetail detail = detailRepository.findById(deno).get(); // 지출 테이블과 매핑
        Long eno = expenditureService.save(reuqest, detail); // 지출 테이블 저장
        Expenditure expenditure = expenditureRepository.findById(eno).get(); // 에코 테이블과 매핑
        ecoService.save(reuqest, expenditure); // 에코 테이블 저장
        return new ExpenditureResponseDto(eno);
    }

    /** 지출 데이터 수정 후 */
    @PostMapping("/expenditure/{id}/update")
    public ExpenditureResponseDto update_expenditure(@PathVariable("id") Long id, // 여기서 id는 eno를 의미
                                                     @RequestBody ExpenditureRequestDto request) throws IllegalAccessException {
        Long eno = detailService.update(id, request);
        return new ExpenditureResponseDto(eno);
    }

    /** 지출 데이터 삭제 */
    @DeleteMapping("calendar/{month}/expenditure/{id}")
    public void delete_income(@PathVariable("id") Long id){
        detailService.delete(id);
    }

}
