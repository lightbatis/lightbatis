package test.jsv;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import titan.lightbatis.jsv.engine.V8EngineServer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V8Main {

    public static void testV8() throws IOException {
        File file = new File("/Users/lifei/workspace/lightbatis/lightbatis-jsvisual/src/test/resources/diabetes.jsv");
        File outFile = new File(file.getAbsolutePath()+".js");
        String script = FileUtils.readFileToString(outFile, "UTF-8");
        //System.out.println(script);
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("function test() {\r\n\t");
//        buffer.append(script);
//        buffer.append("\r\n}");

        V8EngineServer v8Engine = V8EngineServer.getInstance();

        HashMap params = new HashMap();
        List<NameValuePair> paraList = URLEncodedUtils.parse("age=40&gender=1&diab_his_family=2&act=3&bmi=25&fbg=35&hdl=15&tg=18&waist=100&smoke_status=1&vegefru=1&diab_his=2&hbp_his=2", Charset.forName("UTF-8"));
        for(NameValuePair pair: paraList) {
            params.put(pair.getName(), pair.getValue());
        }
        System.out.println(params);
        Object result = v8Engine.executeScript(script, "main", params);
        System.out.println("result = " + result);
    }

    public static void testJS() {
        String script = " 10 < bmi < 50 ";
        Map<String,Object> params = new HashMap<>();
        params.put("bmi", 25);
        V8EngineServer v8Engine = V8EngineServer.getInstance();
        Boolean result = v8Engine.executeBoolean(script, params);
        System.out.println(result);
    }


    public static void testFun() throws IOException {
        File file = new File("/Users/lifei/workspace/lightbatis/lightbatis-jsvisual/src/test/resources/diabetes.jsv.js");
        String script = FileUtils.readFileToString(file, "UTF-8");
        System.out.println(script);
        HashMap<String, Object> params = new HashMap<>();
        params.put("diab_his", 1);
        Object obj = V8EngineServer.getInstance().executeScript(script, "main", params);
        System.out.println(obj);

    }

    private static void testExpr() throws IOException {
        File file = new File("/Users/lifei/myspace/lightbatis/lightbatis-jsvisual/src/test/resources/waist.js");
        String script = FileUtils.readFileToString(file, "UTF-8");
        //System.out.println(script);
        HashMap<String, Object> params = new HashMap<>();
        params.put("waist", 35);
        params.put("gender", 1);
        HashMap<String,Object> resultMap = V8EngineServer.getInstance().executeMap(script, params,"mwaist","swaist");


        System.out.println(resultMap);
    }
    public static void main(String[] args) throws IOException{
        //testV8();
        //testJS();
        //testFun();
        testExpr();
    }
}
