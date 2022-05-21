package com.planet.develop.Controller;

import com.planet.develop.DTO.StatisticsDto;
import com.planet.develop.DTO.StatisticsEcoDto;
import com.planet.develop.Entity.User;
import com.planet.develop.Enum.EcoEnum;
import com.planet.develop.Enum.TIE;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Security.DTO.AuthMemberDTO;
import com.planet.develop.Service.ExpenditureDetailService;
import com.planet.develop.Service.IncomeService;
import com.planet.develop.Service.StatisticsDetailService;
import com.planet.develop.Service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
//@PreAuthorize("hasRole('USER')")

public class StatisticsController {

    private final StatisticsDetailService statisticsDetailService;
    private final UserRepository userRepository;
    private final IncomeService incomeService;
    private final StatisticsService statisticsService;
    private final ExpenditureDetailService expenditureDetailService;

    /** 친/반환경 태그 통계 */
    @GetMapping("/statistics/{id}/{year}/{month}/{day}")
    public Result statistics(@PathVariable("id") String id, @PathVariable("year") int year, @PathVariable("month") int month,@PathVariable("day") int day){
        User user = userRepository.findById(id).get();
        Long incomeTotal = incomeService.totalMonth(user,year, month);
        Long expenditureTotal = expenditureDetailService.totalMonth(user,year,month);
        Map<String,Object> ecoBoard = statisticsService.getEcoCountComparedToLast(user,year,month);
        Map<Integer, Long> ecoCount = statisticsService.getYearEcoCount(user, EcoEnum.G,year);
        Long guessCount=statisticsService.getGuessMonthEcoCount(user,year,month,day);
        ecoCount.replace(month,guessCount);
        List<List<Object[]>> fiveTagCounts = statisticsService.getFiveTagCounts(user, year, month);
        Object ecoDifference = ecoBoard.get("ecoDifference");
        Object noEcoDifference = ecoBoard.get("noEcoDifference");
        Object percentage = ecoBoard.get("percentage");
        Object nowEcoCount=ecoBoard.get("nowEcoCount");
        Object nowNoneEcoCount=ecoBoard.get("noneEcoCount");
        List<Object[]> ecoTagCounts=fiveTagCounts.get(0);
        List<Object[]> noEcoTagCounts=fiveTagCounts.get(1);
        return new Result(user.getUserName(),incomeTotal,expenditureTotal,ecoDifference,noEcoDifference,ecoCount,nowEcoCount,nowNoneEcoCount,percentage,ecoTagCounts,noEcoTagCounts);
    }

    /** 친환경 태그 통계 */
    @GetMapping("/statistics/ecoCountsDetail/{year}/{month}")
    public statisticsEcoRequestDto statisticsEcoDetail(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,@PathVariable("year") int year,@PathVariable("month") int month) {
        User user = userRepository.findById(authMemberDTO.getEmail()).get();
        List<Object[]> tagCategoryList = statisticsService.getTagCategoryList(user, year, month, EcoEnum.G);

        return new statisticsEcoRequestDto(tagCategoryList);
    }

    /** 반환경 태그 통계 */
    @GetMapping("/statistics/noEcoCountsDetail/{year}/{month}")
    public statisticsEcoRequestDto statisticsNoEcoDetail(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,@PathVariable("year") int year,@PathVariable("month") int month) {
        User user = userRepository.findById(authMemberDTO.getEmail()).get();
        List<Object[]> tagCategoryList = statisticsService.getTagCategoryList(user, year, month, EcoEnum.R);
        return new statisticsEcoRequestDto(tagCategoryList);
    }

    /** 지난 달 대비 수입/지출 차액 + 한 달 일별 상세 내역 페이지 */
    @GetMapping("/statistics/total/{year}/{month}")
    public StatisticsDto findTotalStatistics(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @PathVariable("year") int year, @PathVariable("month") int month){

        return statisticsDetailService.functionByMonth(authMemberDTO.getEmail(), year, month, TIE.T);
    }

    /** 비고) 수입 내역 필터링 */
    @GetMapping("/statistics/total/income/{year}/{month}")
    public StatisticsDto filteringIncome(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionByMonth(authMemberDTO.getEmail(), year, month, TIE.I);
    }

    /** 비고) 지출 내역 페이지 */
    @GetMapping("/statistics/total/expenditure/{year}/{month}")
    public StatisticsDto filteringExpenditure(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionByMonth(authMemberDTO.getEmail(), year, month, TIE.E);
    }

    /** 수입 페이지 */
    @GetMapping("/statistics/income/{year}/{month}")
    public StatisticsDto findIncomeStatistics(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionByMonth(authMemberDTO.getEmail(), year, month, TIE.I);
    }

    /** 지출 페이지 */
    @GetMapping("/statistics/expenditure/{year}/{month}")
    public StatisticsEcoDto findExpenditureStatistics(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @PathVariable("year") int year, @PathVariable("month") int month){
        return statisticsDetailService.functionEcoByMonth(authMemberDTO.getEmail(), year, month);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T userName;
        private T incomeTotal;
        private T expenditureTotal;
        private T ecoDifference;
        private T noEcoDifference;
        private T ecoCount;
        private T nowEcoCount;
        private T nowNoneEcoCount;
        private T percentage;
        private T ecoTagCounts;
        private T noEcoTagCounts;
    }

    @Data
    @AllArgsConstructor
    static class statisticsEcoRequestDto<T>{
        private T tagList;
    }

}