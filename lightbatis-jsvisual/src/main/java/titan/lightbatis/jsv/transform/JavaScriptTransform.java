package titan.lightbatis.jsv.transform;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import titan.lightbatis.jsv.*;

import java.io.File;
import java.io.IOException;

public class JavaScriptTransform {
    private JSParserListener parserListener = null;
    public void transform(File file, String functionName) throws IOException {
        CharStream inputStream = CharStreams.fromPath(file.toPath());
        JSVLexer lexer = new JSVLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parserListener = new JSParserListener(tokens, functionName);
    }

    public String transform(String script, String functionName) throws IOException {
        CharStream inputStream = CharStreams.fromString(script);
        JSVLexer lexer = new JSVLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parserListener = new JSParserListener(tokens, functionName);
        return output();
    }

    public String output() {
        String script = parserListener.parse();
        return script;
    }

    public void outputToFile(File file) throws IOException {
        String script = this.output();
        FileUtils.write(file, script, "UTF-8");
    }


}
