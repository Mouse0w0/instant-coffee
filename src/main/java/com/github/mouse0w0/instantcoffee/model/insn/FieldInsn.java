package com.github.mouse0w0.instantcoffee.model.insn;

import com.github.mouse0w0.instantcoffee.Location;
import com.github.mouse0w0.instantcoffee.model.Type;

public class FieldInsn extends BaseInsn {
    public Type owner;
    public String name;
    public Type type;

    public FieldInsn(Location location, String opcode, Type owner, String name, Type type) {
        super(location, opcode);
        this.owner = owner;
        this.name = name;
        this.type = type;
    }
}
