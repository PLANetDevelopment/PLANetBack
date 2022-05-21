package com.planet.develop.Controller;

import com.planet.develop.DTO.*;
import com.planet.develop.Entity.Quote;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.AnniversaryRepository;
import com.planet.develop.Repository.QuoteRepository;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Security.DTO.AuthMemberDTO;
import com.planet.develop.Service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('USER')")
public class CalendarController {

    private final UserRepository userRepository;
    private final CalendarService calendarService;
    private final QuoteRepository quoteRepository;
    private final AnniversaryRepository anniversaryRepository;

    Random random = new Random();

    /** 월별 수입/지출 조회 함수 */
    @GetMapping("/calendar/{year}/{month}")
    public CalendarResponseDto findCalendar(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @PathVariable("year") int  year, @PathVariable("month") int month){
        User user = userRepository.findById(authMemberDTO.getEmail()).get();
        CalendarDto calendar = calendarService.findCalendar(user.getUserId(),year,month);
        int qno = random.nextInt(40) + 1;
        Quote quote = quoteRepository.findById(qno).get();
        Optional<List<Object[]>> anniversaryList = Optional.ofNullable(anniversaryRepository.getAnniversaryList(month));
        return new CalendarResponseDto(anniversaryList, calendar, quote.getContent());
    }

    /** 일별 조회 (세부 조회) */
    @GetMapping("/calendar/{year}/{month}/{day}")
    public Result findIncomeDetail(@AuthenticationPrincipal AuthMemberDTO authMemberDTO,@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day){
        User user = userRepository.findById(authMemberDTO.getEmail()).get();
        return  calendarService.findDayExTypeDetail(user.getUserId(),year,month,day);
    }

}