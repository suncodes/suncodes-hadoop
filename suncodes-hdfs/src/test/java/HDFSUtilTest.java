import org.apache.hadoop.fs.FileStatus;
import org.junit.Test;

public class HDFSUtilTest {

    @Test
    public void f() throws Exception {
        HDFSUtil hdfsUtil = new HDFSUtil();
        FileStatus[] fileStatuses = hdfsUtil.listFiles("/");
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath());
        }
    }

    @Test
    public void f1() throws Exception {
        HDFSParamUtil hdfsUtil = new HDFSParamUtil();
        FileStatus[] fileStatuses = hdfsUtil.listFiles("/");
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath());
        }
    }

    @Test
    public void f2() throws Exception {
        HDFSPropertiesUtil hdfsUtil = new HDFSPropertiesUtil();
        FileStatus[] fileStatuses = hdfsUtil.listFiles("/");
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath());
        }
    }

    @Test
    public void f3() throws Exception {
        HDFSClassPathXmlUtil hdfsUtil = new HDFSClassPathXmlUtil();
        FileStatus[] fileStatuses = hdfsUtil.listFiles("/");
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath());
        }
    }

    @Test
    public void f4() throws Exception {
        HDFSNoClassPathXmlUtil hdfsUtil = new HDFSNoClassPathXmlUtil();
        FileStatus[] fileStatuses = hdfsUtil.listFiles("/");
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath());
        }
    }
}