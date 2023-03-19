package com.ndptest.tool;

import com.ndptest.tool.apiMethod.GET.threadPoolTest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import static com.ndptest.tool.apiMethod.GET.UserService.*;
import static com.ndptest.tool.apiMethod.GET.threadPoolTest.*;

import com.ndptest.tool.apiMethod.GET.threadPoolTest.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

//import static com.ndptest.tool.apiMethod.GET.threadPoolTest.threadPool;

@RequiredArgsConstructor
@Controller
public class ResultController {
    String testSubject;

    private final ResultService resultService;
    @Autowired
    private final ResultRepository resultRepository;


    //테스트 페이지
    @GetMapping("/test-ndp/main")
    public String test(){
        return "/content/main";
    }

    @RequestMapping(value="/test-ndp/loading", method={RequestMethod.GET, RequestMethod.POST})
    public String testResult(@ModelAttribute("selectDto") SelectDto selectDto, Model model, Model model1)throws Exception{
        model.addAttribute("subject", selectDto.getSubject());
        testSubject = String.valueOf(selectDto.getSubject());
        return "/content/loading";
    }

    @GetMapping("/runningTest")
    public String runTest() throws Exception {
        threadPool(testSubject);
        return "redirect:/test-ndp/result";
    }


    @RequestMapping(value="/test-ndp/result", method={RequestMethod.GET, RequestMethod.POST})
    public String testResult(Model model)throws Exception{
        ArrayList<Result> resultList = new ArrayList<>();
        IntStream.rangeClosed(0, idList.size()-1).forEach(i -> {
            Result result = Result.builder()
                    .number(i+1)
                    .username(idList.get(i))
                    .responseTime(responseList.get(i))
                    .strokeCount(strokeCountList.get(i))
                    .build();
            resultList.add(result);
        });
        resultRepository.saveAll(resultList);
        List<Result> testResultList = this.resultService.getList();
        model.addAttribute("subject", testSubject);
        model.addAttribute("testResultList", testResultList);
        return "/content/result";
    }


    @RequestMapping(value="/resetDB", method={RequestMethod.POST})
    public String reset()throws Exception{
        resultService.truncateResultTable();
        resetResultList();
        return "redirect:/test-ndp/main";
    }
}