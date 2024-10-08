package com.tenco.blog_v2.board;


import com.tenco.blog_v2.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class BoardController {
    @Autowired
    private final BoardNativeRepository boardNativeRepository;
    private final BoardRepository boardRepository;
    private final HttpSession session;

    public BoardController(BoardNativeRepository boardNativeRepository, BoardRepository boardRepository, HttpSession session) {
        this.boardNativeRepository = boardNativeRepository;
        this.boardRepository = boardRepository;
        this.session = session;
    }

    //주소설계 - http://localhost:8080/board/10/delete (form 활용이기 때문에 delete 선언)
    // form 태그에서는 GET, POST 방식만 지원하기 때문이다.
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id){
        //boardNativeRepository.delete(id);
        //유효성, 인증검사
        //세션에서 로그인 사용자 정보 가져오기 -> 인증(로그인 여부), 인가(권한- 내글?)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null ){
            return "redirect:/login-form";
        }
        // 권한 체크
        Board board =  boardRepository.findById(id);
        if(board == null){
            return "redirect:/error-404";
        }
        if( ! board.getUser().getId().equals(sessionUser.getId()) ){
            return "redirect:/error-403"; // 추후 수정
        }


        boardRepository.delete(id);
        return "redirect:/";
    }


    @GetMapping("/")
    public String index(Model model) {

        //List<Board> boardList = boardNativeRepository.findAll();
        List<Board> boardList = boardRepository.findAll();
        model.addAttribute("boardList", boardList);

        return "index";
    }

    // 주소설계 - http://localhost:8080/board/save-form
    // 게시글 작성 화면 연결
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    // 주소설계 - http://localhost:8080/board/save
    // 게시글 저장
    @PostMapping("/board/save")
    public String save(BoardDTO.SaveDTO reqDTO) {
       User sessionUser = (User) session.getAttribute("sessionUser");
       if(sessionUser == null) {
           return "redirect:/login-form";
       }
        //boardNativeRepository.save(title,content);
        //SaveDTO 에서 toEntity 사용해서 Board 엔티티로 변환하고 인수 값으로 USer 정보 를 넣었다.
        // 결국 Board 엔티티로 반환이된다.
        boardRepository.save(reqDTO.toEntity(sessionUser));
        return "redirect:/";
    }

    
    
    // 주소설계 - http://localhost:8080/board/10
    // 특정 게시글 요청 화면
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        //JAP API 사용
        //Board board =  boardRepository.findById(id);
        Board board = boardRepository.findByIdJoinUser(id);


        //JPQL FETCH join 사용

        request.setAttribute("board", board);
       return "board/detail";
    }





    // 게시글 수정 화면 요청
    // board/id/update
    @GetMapping("/board/{id}/update")
    public String updateForm(@PathVariable(name="id")Integer id, HttpServletRequest request) {
        Board board =  boardNativeRepository.findById(id);
        request.setAttribute("board", board);
        return "board/update-form"; // src/main/resources/templates/board/update-form.mustache
    }

    // 게시글 수정 요청 기능
    // board/{id}/update
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable(name = "id") Integer id,
                         @RequestParam(name = "title") String title,@RequestParam(name = "content") String content) {
        boardNativeRepository.updateById(id,title,content);
        // 상세화면
        return"redirect:/board/"+id;
    }



}
