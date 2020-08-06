package suncodes.utils;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class HDFSUtilTest {
    @Test
    public void f() throws IOException {
        List<String> fileOrDirList = HDFSUtil.getFileOrDirList("/");
        for (String s : fileOrDirList) {
            System.out.println(s);
        }
    }
}