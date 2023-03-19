package com.ndptest.tool.apiMethod.GET;

import com.ndptest.tool.Result;
import com.ndptest.tool.ResultRepository;
import com.ndptest.tool.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.concurrent.*;

import static com.ndptest.tool.apiMethod.GET.UserService.*;

@RequiredArgsConstructor
public class threadPoolTest {
    @Autowired
    private final ResultService resultService;

    public static ArrayList<String> idList = new ArrayList<>();
    public static ArrayList<String> responseList = new ArrayList<>();
    public static ArrayList<Integer> strokeCountList = new ArrayList<>();

//    public static ArrayList<String> tpsList = new ArrayList<>();

    public static void resetResultList(){
        idList.clear();
        responseList.clear();
        userIdList.clear();
    }
    private CompletionHandler<Integer, Void> callBack = new CompletionHandler<Integer, Void>(){

        @Override
        public void completed(Integer result, Void attachment) {
            System.out.println("실행 완료" + result);
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            System.out.println("실행 실패" + exc.toString());
        }
    };

    public static void threadPool(String testSubject) throws Exception {
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> response = new ArrayList<>();

        UserService.getUsers();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int x = 0;
        for (int k = 0; k < userIdList.size(); k++) {
            int i = k;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
                    try {
                        String id = userIdList.get(i);
                        long t0 = System.currentTimeMillis();
                        switch (testSubject){
                            case "PROFILE":
//                                System.out.println("프로필 조회 중");
                                UserService.getUserProfile(id);
                                strokeCountList.add(InkStoreService.countUserStrokes(id, 0));
                                break;

                            case "NOTE":
                                UserService.getNoteList(id, 0);
                                break;

                            case "STROKE":
//                                System.out.println("스트로크 조회 중");
                                InkStoreService.getUserStroke(id, 0);
                                strokeCountList.add(InkStoreService.countDots(id, 0));
                                break;

                            case "TAG":
                                UserService.getTags(id);
                                break;
                        }
                        long t1 = System.currentTimeMillis();
                        String responseTime = t1-t0+"ms";
//                        String timePerStroke = String.valueOf((float) (t1-t0)/strokeCount);
                        System.out.println((i + 1) + " : response time: " + responseTime + " > " + id + " > ");

//                        System.out.println(timePerStroke + "ms");
                        idList.add(id);
                        responseList.add(responseTime);
//                        tpsList.add(timePerStroke);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.submit(runnable);
        }
        executorService.shutdown();

//        while( !       executorService.awaitTermination(10, TimeUnit.MINUTES));
        // do something
        try{
            executorService.awaitTermination(10,TimeUnit.MINUTES);
            System.out.println("스레드 종료");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
