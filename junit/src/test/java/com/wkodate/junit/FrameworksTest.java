package com.wkodate.junit;

import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by wkodate on 2018/11/26.
 */
@RunWith(Theories.class)
public class FrameworksTest {

    @DataPoints
    public static ApplicationServer[] APP_SERVERS = ApplicationServer.values();
    @DataPoints
    public static Database[] DATABASES = Database.values();

    public static Map<String, List<String>> MAP = new HashMap<>();

    @BeforeClass
    public static void setUpClass() {
        BufferedReader reader;
        InputStream input = FrameworksTest.class.getClassLoader().getResourceAsStream("support.txt");
        reader = new BufferedReader(new InputStreamReader(input));
        try {
            for (; ; ) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] values = line.split(",");
                List<String> list;
                if (MAP.containsKey(values[0])) {
                    list = MAP.get(values[0]);
                } else {
                    list = new ArrayList<>();
                }
                list.add(values[1]);
                MAP.put(values[0], list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Theory
    public void testIsSupport(ApplicationServer applicationServer, Database database) throws Exception {
        String app = applicationServer.toString();
        if (MAP.containsKey(app) && MAP.get(app).contains(database.toString())) {
            assertThat(Frameworks.isSupport(applicationServer, database), is(true));
        } else {
            assertThat(Frameworks.isSupport(applicationServer, database), is(false));
        }
    }

}