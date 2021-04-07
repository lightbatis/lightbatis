package test.jsv;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.apache.commons.io.FileUtils;
import titan.lightbatis.jsv.*;
import titan.lightbatis.jsv.engine.V8EngineServer;
import titan.lightbatis.jsv.transform.JavaScriptTransform;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSVMain {


    private static void testVariableStatement() throws IOException {
        String query = " risk_level=0";

        ANTLRInputStream stream =new ANTLRInputStream(query);
        JSVLexer lexer = new JSVLexer(stream);

        TokenStream tokenStream = new CommonTokenStream(lexer);

        JSVParser parser = new JSVParser(tokenStream);

        System.out.println(parser.program().toStringTree(parser));

    }
    private static void testTransform() throws IOException{
        String path = "lightbatis-jsvisual/src/test/resources";
        File dir = new File(System.getProperty("user.dir"), path);
        System.out.println(dir);
        File file = new File(dir, "occ.jsv");
        file = new File("/Users/lifei/workspace/lightbatis/lightbatis-jsvisual/src/test/resources/diabetes.jsv");
        file = new File("/Users/lifei/workspace/lightbatis/lightbatis-jsvisual/src/test/resources/chd.jsv");

        file = new File("/Users/lifei/workspace/lightbatis/lightbatis-jsvisual/src/test/resources/stroke.jsv");

        JavaScriptTransform transform = new JavaScriptTransform();
        transform.transform(file, "main");

        File outFile = new File(file.getAbsolutePath()+".js");
        transform.outputToFile(outFile);
       // String script = FileUtils.readFileToString(outFile, "UTF-8");
        //System.out.println(script);

//        StringBuffer buffer = new StringBuffer();
//        buffer.append("function test() {\r\n\t");
//        buffer.append(script);
//        buffer.append("\r\n}");
//
//        V8EngineServer v8Engine = V8EngineServer.getInstance();
//        v8Engine.register(buffer.toString());
//
//        HashMap params = new HashMap();
//        params.put("diab_his", 1);
//
//
//        Object result = v8Engine.executeScript(buffer.toString(), "test", params);
//        System.out.println("result = " + result);


    }
    public static void main(String[] args) {
        try {
            testTransform();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
