package io.github.lizhangqu.retrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StackTrace {

    private String exception;
    private String message;

    private List<Line> lines = Collections.emptyList();

    private StackTrace causedBy;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public StackTrace getCausedBy() {
        return causedBy;
    }

    public void setCausedBy(StackTrace causedBy) {
        this.causedBy = causedBy;
    }
}
