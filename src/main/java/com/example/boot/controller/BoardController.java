package com.example.boot.controller;


import com.example.boot.dto.BoardDTO;
import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;

/*    @GetMapping("/register")*/
/*    public String register(){
//        /board/register => (board 컨트롤러에서 register라는 getmapping으로 연결
//        들어오는 경로와 나가는 경로가 같을 경루 생략가능
        return "/board/register";*/

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO) {
        log.info(">> boardDTO {}", boardDTO);
        Long bno = boardService.insert(boardDTO);
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public void list(Model model){
        List<BoardDTO> list = boardService.getList();
        model.addAttribute("list",list);
    }

    @GetMapping("/detail")
    public  void  detail(@RequestParam("bno") Long bno, Model model){
        BoardDTO boardDTO = boardService.getDetail(bno);
        model.addAttribute("board", boardDTO);
    }

    @PostMapping("/update")
    public  String update(BoardDTO boardDTO, RedirectAttributes redirectAttributes){
        boardService.update(boardDTO);

        // redirect시 해당 위치로 객체를 보내주는 역할
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/remove")
    public  String remove(@RequestParam("bno") Long bno){
        boardService.remove(bno);
        return "redirect:/board/list";
    }
}
