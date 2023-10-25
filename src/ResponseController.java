public class ResponseController {
    public Result getData(Object object){
        Result result =new Result(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getMsg(),object);
        return result;
    }
}


