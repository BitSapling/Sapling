package com.github.bitsapling.sapling.util;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@Data
public class TraceCaller {
    @NotNull
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 3);
    @NotNull
    private final String threadName;
    @NotNull
    private final String className;
    @NotNull
    private final String methodName;
    private final int lineNumber;

    public TraceCaller(@NotNull String threadName, @NotNull String className, @NotNull String methodName, int lineNumber) {
        this.threadName = threadName;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
    }

    @NotNull
    public static TraceCaller create() {
        return create(3);
    }

    @NotNull
    public static TraceCaller create(int steps) {
        List<StackWalker.StackFrame> caller = STACK_WALKER.walk(frames -> frames.limit(steps + 1L).toList());
        StackWalker.StackFrame frame = caller.get(steps);
        String threadName = Thread.currentThread().getName();
        String className = frame.getClassName();
        String methodName = frame.getMethodName();
        int codeLine = frame.getLineNumber();
        return new TraceCaller(threadName, className, methodName, codeLine);
    }
}
