package com.yh.web.controller.admin;

import com.yh.web.Utils;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;
import com.yh.web.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Controller("admin.boardController")
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BoardController {

    private final AdminService adminService;
    public BoardController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * @param field  검색타입
     * @param query  검색 하고자 하는 내용
     * @param p_     페이지 숫자
     * @return       게시글 리스트
     */
    @GetMapping(path = {"/boards"})
    public ModelAndView boardList(@RequestParam(name = "f",required = false,defaultValue = "title") String field
            , @RequestParam(name = "q",required = false,defaultValue = "") String query
            , @RequestParam(name = "p",required = false,defaultValue = "1") String p_){
        long page;
        try{  //p_ 문자열이 숫자로 변환이 안되거나 1보다 작을 경우 1로 초기화
            page = Long.parseLong(p_);
            if(page < 1) page = 1;
        } catch (NumberFormatException e){
            page = 1;
        }
        //필드명이 유효하지 않은경우 title로 초기화
        Set<String> fieldOption = new HashSet<>(Arrays.asList("title", "writer"));
        if(!fieldOption.contains(field)){
            field = "title";
        }

        long listTotalCount = adminService.getBoardListCount(field, query);			//검색된 게시글 총개수
        long pageMaxNum =  (long) Math.ceil((listTotalCount/(double)adminService.listNum)); 	//67개일경우 7
        pageMaxNum = (pageMaxNum ==0) ? 1 : pageMaxNum;
        if(page > pageMaxNum){											//param p가 페이지 끝 번호보다 큰경우
            ModelAndView mav = new ModelAndView();
            Utils.redirectErrorPage(mav,"존재하지 않는 페이지입니다.\\n확인 후 다시 시도하시기 바랍니다.","/admin/boards");
            return mav;
        }
        //게시글 검색값 가져오기
        List<BoardList> list = adminService.getBoardList(field, query, page); //게시글 리스트

        int currentPageListCount = list.size();  							//현재 페이지 게시글 개수  ? <= 10
        boolean[] isNow = new boolean[currentPageListCount];

        LocalDate now = LocalDate.now(); //현재 일과 게시글 일을 비교한다.
        for (int i = 0; i < currentPageListCount; i++) {
            LocalDate date = list.get(i).getRegDate().toLocalDate();
            if(ChronoUnit.DAYS.between(date,now) == 0)
                isNow[i] = true;
        }

        ModelAndView mav = new ModelAndView("/admin/board/boardList");
        mav.addObject("page_title", "관리자 게시판");
        mav.addObject("f",field);
        mav.addObject("p",page);
        mav.addObject("list",list);
        mav.addObject("listTotalCount",listTotalCount);
        mav.addObject("pageMaxNum",pageMaxNum);
        mav.addObject("isNow",isNow);

        return mav;
    }

    /**
     * @param articleNo_ 글번호
     * @return 글번호에 해당하는 상세 페이지
     */
    @GetMapping(path = "/boards/{articleNo}")
    public ModelAndView boardDetail(@PathVariable("articleNo") String articleNo_
                                    , HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        long articleNo;

        try {
            articleNo = Long.parseLong(articleNo_);
        }catch (NumberFormatException e){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/admin/boards");
            return mav;
        }

        BoardDetail b = adminService.getBoardDetail(articleNo);
        if(b == null){
            Utils.redirectErrorPage(mav, "올바른 접근이 아닙니다.", "/admin/boards");
        }else{
            mav.addObject("page_title", b.getTitle());
            mav.setViewName("/admin/board/boardDetail");
            mav.addObject("b",b);
            mav.addObject("qs", Utils.getPreQS(request));
        }

        return mav;
    }

    /**         체크된 글번호는 공개처리 체크 안된건 비공개처리
     * allNo    현재 페이지 모든 글번호
     * openNo   체크된 글번호
     */
    @PutMapping(path = "/boards/edit/AllPub")
    public ResponseEntity<Boolean> updateBoardsPub(@RequestBody Map<String,String> param){

        String allNo = param.get("allNo");     //모든 글 번호
        String openNo = param.get("openNo");   //체크된 글 번호

        boolean result = adminService.updateBoardAllPub(allNo,openNo);
        return ResponseEntity.ok(result);
    }
}
