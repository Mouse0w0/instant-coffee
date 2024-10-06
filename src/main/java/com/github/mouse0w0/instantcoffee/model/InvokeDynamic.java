package com.github.mouse0w0.instantcoffee.model;

import com.github.mouse0w0.instantcoffee.Location;

public class InvokeDynamic extends Located {
    public String name;
    public MethodType methodType;
    public MethodHandle bootstrapMethodHandle;
    public Object[] bootstrapMethodArguments;

    public InvokeDynamic(Location location, String name, MethodType methodType, MethodHandle bootstrapMethodHandle, Object[] bootstrapMethodArguments) {
        super(location);
        this.name = name;
        this.methodType = methodType;
        this.bootstrapMethodHandle = bootstrapMethodHandle;
        this.bootstrapMethodArguments = bootstrapMethodArguments;
    }
}
