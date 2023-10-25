public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }
}
