package tdd.vendingMachine.Service;

import lombok.Data;

/**
 * Uniform transfer object to be sent by machine-service  to client, modelled after codeforces api,
 * http://codeforces.com/api/help
 *
 */
@Data
public class Response {
    ResponseStatus status;
    String comment;
    Object result;

    //Constructing OK response
    public Response() {
        status = ResponseStatus.OK;
    }

    //Constructing and response FAILED
    public Response(String failureReason) {
        status = ResponseStatus.FAILED;
        comment = failureReason;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
