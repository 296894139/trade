package jinjiang.exception;


import jinjiang.response.WrongResponse;

public class SystemException extends Exception {
    private WrongResponse response = new WrongResponse(10001, "System is error.");

    public WrongResponse getResponse() {
        return response;
    }
}
