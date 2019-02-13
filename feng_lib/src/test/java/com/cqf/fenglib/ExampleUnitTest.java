package com.cqf.fenglib;

import com.cqf.fenglib.utils.MyUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void fun(){
        String newChar= MyUtils.replaceChar("17858805466","****",3,7);
        System.out.print(newChar);
    }
}