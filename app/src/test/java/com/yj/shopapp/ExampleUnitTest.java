package com.yj.shopapp;

import com.squareup.okhttp.FormEncodingBuilder;
import com.yj.shopapp.http.OkHttpClientManager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testHttp(){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("1","1");
        System.out.println("str = "+builder.toString());
    }


}