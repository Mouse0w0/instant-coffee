package com.github.mouse0w0.instantcoffee.model.insn;

import com.github.mouse0w0.instantcoffee.Constants;
import com.github.mouse0w0.instantcoffee.Location;
import com.github.mouse0w0.instantcoffee.model.IntegerLiteral;
import com.github.mouse0w0.instantcoffee.model.Type;

public class MultiANewArrayInsn extends BaseInsn {
    public Type type;
    public IntegerLiteral numDimensions;

    public MultiANewArrayInsn(Location location, Type type, IntegerLiteral numDimensions) {
        super(location, Constants.opcodeToName(Constants.MULTIANEWARRAY));
        this.type = type;
        this.numDimensions = numDimensions;
    }
}
