package com.jimbolix.april.mq.exception;

/**
 * @Description
 * @ClassName MessageException
 * @Author liruihui
 * @date 2020.04.18 18:33
 */
public class MessageException extends Exception {
    private static final long serialVersionUID = -7273927782124917371L;

    public MessageException() {
        super();
    }

    public MessageException(String message){
        super(message);
    }

    public MessageException(String message,Throwable cause){
        super(message,cause);
    }

    public MessageException(Throwable cause){
        super(cause);
    }
}
