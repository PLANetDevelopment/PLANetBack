package com.planet.develop.Controller;

import com.planet.develop.DTO.CouponDetailDto;
import com.planet.develop.DTO.CouponListDto;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Security.DTO.AuthMemberDTO;
import com.planet.develop.Service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
//@PreAuthorize("hasRole('USER')")
public class CouponController {

    private final CouponService couponService;
    private final UserRepository userRepository;

    /** 쿠폰 리스트 조회 페이지 */
//    @GetMapping("/coupon")
//    public CouponListDto findCoupon(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) throws IllegalAccessException {
//        User user = userRepository.findById(authMemberDTO.getEmail()).get();
//        couponService.remainingDaysUpdate(user.getUserId()); // 쿠폰 조회 전 남은 날짜 먼저 업데이트
//        CouponListDto couponList = couponService.getCouponList(user.getUserId());
//        return couponList;
//    }

    /** 쿠폰 리스트 조회 페이지 - 테스트용 */
    @GetMapping("/{id}/coupon")
    public CouponListDto findCoupon(@PathVariable("id") String id) throws IllegalAccessException {
        User user = userRepository.findById(id).get();
        couponService.remainingDaysUpdate(user.getUserId()); // 쿠폰 조회 전 남은 날짜 먼저 업데이트
        CouponListDto couponList = couponService.getCouponList(user.getUserId());
        return couponList;
    }

    /** 쿠폰 등록 페이지 */
//    @PostMapping("/coupon/register")
//    public String couponRegister(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, @RequestParam("cno") String cno) {
//        User user = userRepository.findById(authMemberDTO.getEmail()).get();
//        couponService.couponRegister(user.getUserId(), cno);
//        return cno;
//    }

    /** 쿠폰 등록 페이지 - 테스트용 */
    @PostMapping("/{id}/coupon/register")
    public String couponRegister(@PathVariable("id") String id, @RequestParam("cno") String cno) {
        User user = userRepository.findById(id).get();
        couponService.couponRegister(user.getUserId(), cno);
        return cno;
    }

    /** 쿠폰 사용 */
    @PostMapping("/coupon/use")
    public String useCoupon(@RequestParam("cno") String cno) throws IllegalAccessException {
        couponService.useCoupon(cno);
        return "쿠폰 사용 완료";
    }

}
