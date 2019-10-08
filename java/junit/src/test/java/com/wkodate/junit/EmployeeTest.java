package com.wkodate.junit;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by wkodate on 2018/11/21.
 */
public class EmployeeTest {

    @Test
    public void testLoad() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("employee.txt");
        List<Employee> actual = Employee.load(inputStream);
        assertThat(actual.get(0).getFirstName(), is("Ichiro"));
        assertThat(actual.get(0).getLastName(), is("Tanaka"));
        assertThat(actual.get(0).getEmail(), is("ichiro@example.com"));
    }

}