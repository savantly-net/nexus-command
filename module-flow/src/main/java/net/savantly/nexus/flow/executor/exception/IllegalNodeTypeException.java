package net.savantly.nexus.flow.executor.exception;

public class IllegalNodeTypeException extends RuntimeException{

    public IllegalNodeTypeException(String nodeType) {
        super(nodeType);
    }
    
}
