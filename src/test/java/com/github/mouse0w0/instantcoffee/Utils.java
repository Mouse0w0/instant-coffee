package com.github.mouse0w0.instantcoffee;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static void validate(Class<?> clazz) {
        validate(clazz, false, false);
    }

    public static void validate(Class<?> clazz, boolean skipMaxs, boolean print) {
        String decompiledRaw = decompile(clazz);
        String textifiedRaw = textify(clazz, skipMaxs);

        if (print) {
            writeString(Paths.get(clazz.getSimpleName() + "_decompiled_raw.txt"), decompiledRaw);
            writeString(Paths.get(clazz.getSimpleName() + "_textified_raw.txt"), textifiedRaw);
        }

        byte[] recompiled = compile(decompiledRaw);

        String decompiledNew = decompile(recompiled);
        String textifiedNew = textify(recompiled, skipMaxs);

        if (print) {
            writeString(Paths.get(clazz.getSimpleName() + "_decompiled_new.txt"), decompiledNew);
            writeString(Paths.get(clazz.getSimpleName() + "_textified_new.txt"), textifiedNew);
        }

        if (decompiledRaw.equals(decompiledNew) && textifiedRaw.equals(textifiedNew)) {
            return;
        }

        throw new AssertionError();
    }

    public static void writeString(Path path, String string, OpenOption... options) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, options)) {
            writer.write(string);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] compile(String s) {
        return new Compiler().compile(new Parser(new TokenStream(new Scanner(new StringReader(s)))).parseClassDeclaration()).toByteArray();
    }

    public static String decompile(Class<?> clazz) {
        try (InputStream input = openStream(clazz)) {
            StringWriter sw = new StringWriter();
            Unparser.unparse(Decompiler.decompile(new ClassReader(input)), sw);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static String decompile(byte[] bytes) {
        StringWriter sw = new StringWriter();
        Unparser.unparse(Decompiler.decompile(new ClassReader(bytes)), sw);
        return sw.toString();
    }

    public static String textify(Class<?> clazz, boolean skipMaxs) {
        try (InputStream inputStream = openStream(clazz)) {
            StringWriter sw = new StringWriter();
            Printer printer = skipMaxs ? new SkipMaxsTextifier() : new Textifier();
            new ClassReader(inputStream).accept(new TraceClassVisitor(null, printer, new PrintWriter(sw)), ClassReader.SKIP_FRAMES);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static String textify(byte[] bytes, boolean skipMaxs) {
        StringWriter sw = new StringWriter();
        Printer printer = skipMaxs ? new SkipMaxsTextifier() : new Textifier();
        new ClassReader(bytes).accept(new TraceClassVisitor(null, printer, new PrintWriter(sw)), ClassReader.SKIP_FRAMES);
        return sw.toString();
    }

    private static InputStream openStream(Class<?> clazz) {
        String classQualifiedName = clazz.getName();
        int index = classQualifiedName.lastIndexOf('.');
        String className = index != -1 ? classQualifiedName.substring(index + 1) : classQualifiedName;
        return clazz.getResourceAsStream(className + ".class");
    }

    private static final class SkipMaxsTextifier extends Textifier {
        public SkipMaxsTextifier() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            System.out.println();
            // Skip
        }

        @Override
        protected Textifier createTextifier() {
            return new SkipMaxsTextifier();
        }
    }
}
