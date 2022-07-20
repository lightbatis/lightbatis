package titan.lightbatis.mybatis.scanner;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileDynamicMapperScanner extends DefaultDynamicMapperScanner implements InitializingBean  {

    @Value("${lightbatis.dynamic.dir:./dynamic-mapper}")
    private String mapperDir = "./dynamic-mapper";

    protected File scanDir = null;

    private FileAlterationMonitor monitor = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        scanDir = new File(mapperDir);
        if (!scanDir.exists()) {
            scanDir.mkdir();
        }
        monitor = new FileAlterationMonitor(1000);
        FileAlterationObserver observer = new FileAlterationObserver(scanDir);
        monitor.addObserver(observer);
        observer.addListener(new FileListener(this));
    }

    @Override
    protected void doStart() {
        try {
            loadFiles();
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doStop() {
        try {
            super.doStop();
            monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void loadFiles(){
        File[] files = scanDir.listFiles();
        for (File f: files) {
            if (f.exists()) {
                addFile(f);
            }
        }
    }
    public File getScanDir () {
        return scanDir;
    }



    private class FileListener extends FileAlterationListenerAdaptor {
        private FileDynamicMapperScanner mapperScanner = null;
        public FileListener(FileDynamicMapperScanner mapperScanner) {
            this.mapperScanner = mapperScanner;
        }

        @Override
        public void onFileCreate(File file) {
            super.onFileCreate(file);
            addFile(file);
        }

        @Override
        public void onFileChange(File file) {
            super.onFileChange(file);
            addFile(file);
        }

        @Override
        public void onFileDelete(File file) {
            super.onFileDelete(file);
            removeFile(file);
        }

        @Override
        public void onStop(FileAlterationObserver observer) {
            super.onStop(observer);
        }
    }
}
