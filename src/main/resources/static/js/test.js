

  function threadStart(){
     $.ajax({
         type : "GET",
         url : "http://localhost:8080/runningTest",
        complete : function(){
            location.replace("http://localhost:8080/test-ndp/result");
        }
     });
     }

  function postInit(){
       $.ajax({
           type : "POST",
           url : "http://localhost:8080/",
           contentType: "application/json",
           dataType: "json",
           success : function (data) {
              console.log('통신결과');
              console.log(data);
           }
       });
  }

  function resetData(){
         $.ajax({
             type : "POST",
             url : "http://localhost:8080/resetDB",
             contentType: "application/json",
             dataType: "json",
             complete : function(){
                         location.replace("http://localhost:8080/test-ndp/main");
                     }
         });
    }