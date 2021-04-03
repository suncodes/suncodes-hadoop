import javafx.scene.layout.HBox;
import org.junit.Test;
import suncodes.weibo.constants.Constants;
import suncodes.weibo.dao.HBaseDao;
import suncodes.weibo.util.HBaseUtil;

import java.io.IOException;

public class WeiBoTest {

    @Test
    public void f() throws IOException {
        HBaseUtil.createNamespace(Constants.NAMESPACE);
        HBaseUtil.createTable(Constants.CONTENT_TABLE, Constants.CONTENT_TABLE_VERSIONS,
                Constants.CONTENT_TABLE_CF);
        HBaseUtil.createTable(Constants.RELATION_TABLE, Constants.RELATION_TABLE_VERSIONS,
                Constants.RELATION_TABLE_CF1, Constants.RELATION_TABLE_CF2);
        HBaseUtil.createTable(Constants.INBOX_TABLE, Constants.INBOX_TABLE_VERSIONS,
                Constants.INBOX_TABLE_CF);
    }

    @Test
    public void f2() throws IOException {
        HBaseUtil.truncate(Constants.CONTENT_TABLE);
        HBaseUtil.truncate(Constants.RELATION_TABLE);
        HBaseUtil.truncate(Constants.INBOX_TABLE);
    }

    /**
     * 1、1001 发布微博
     * 2、1002 关注 1001 和 1003
     * 3、获取 1002 初始化页面
     * 4、1003 发布4条微博，同时 1001 发布2条
     * 5、获取 1002 初始化页面
     * 6、1002 取关 1003
     * 7、获取 1002 初始化页面
     * 8、1002 再次关注 1003
     * 9、获取 1002 初始化页面
     * 10、获取 1001 全部微博
     */
    @Test
    public void f1() throws IOException {
        HBaseUtil.truncate(Constants.CONTENT_TABLE);
        HBaseUtil.truncate(Constants.RELATION_TABLE);
        HBaseUtil.truncate(Constants.INBOX_TABLE);
        System.out.println("----------1、1001 发布微博---------");
        HBaseDao.publishWeiBo("1001", "1001的第一条微博");
        System.out.println("----------1002 关注 1001 和 1003---------");
        HBaseDao.addAttends("1002", "1001", "1003");
        System.out.println("----------获取 1002 初始化页面---------");
        HBaseDao.getInitPageData("1002");
        System.out.println("----------1003 发布4条微博，同时 1001 发布2条---------");
        HBaseDao.publishWeiBo("1003", "1003的第一条微博");
        HBaseDao.publishWeiBo("1003", "1003的第二条微博");
        HBaseDao.publishWeiBo("1003", "1003的第三条微博");
        HBaseDao.publishWeiBo("1003", "1003的第四条微博");
        HBaseDao.publishWeiBo("1001", "1001的第二条微博");
        HBaseDao.publishWeiBo("1001", "1001的第三条微博");
        System.out.println("-----------获取 1002 初始化页面--------");
        HBaseDao.getInitPageData("1002");
        System.out.println("-----------1002 取关 1003--------");
        HBaseDao.deleteAttends("1002", "1003");
        System.out.println("-----------获取 1002 初始化页面--------");
        HBaseDao.getInitPageData("1002");
        System.out.println("-----------1002 再次关注 1003--------");
        HBaseDao.addAttends("1002", "1003");
        System.out.println("-----------获取 1002 初始化页面--------");
        HBaseDao.getInitPageData("1002");
        System.out.println("-----------获取 1001 全部微博--------");
        HBaseDao.getAllWeiBoByUid("1001");
    }
}
