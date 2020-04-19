package com.jimbolix.april.mq.exception;

/**
 * @Description
 * @ClassName MessageRunTimeException
 * @Author liruihui
 * @date 2020.04.18 18:37
 */
public class MessageRunTimeException extends RuntimeException {
    private static final long serialVersionUID = -7370771255784422913L;

    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message){
        super(message);
    }

    public MessageRunTimeException(String message,Throwable cause){
        super(message,cause);
    }

    public MessageRunTimeException(Throwable cause){
        super(cause);
    }
}
