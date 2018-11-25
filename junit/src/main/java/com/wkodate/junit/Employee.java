package com.wkodate.junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wkodate on 2018/11/21.
 */
public class Employee {
    private String firstName;
    private String lastName;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static List<Employee> load(InputStream input) {
        List<Employee> list = new LinkedList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input));
            for (; ; ) {
                String line = reader.readLine();
                if (line == null) break;
                String[] values = line.split(",");
                Employee employee = new Employee();
                employee.setFirstName(values[0]);
                employee.setLastName(values[1]);
                employee.setEmail(values[2]);
                list.add(employee);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                  /* do nothing */
                }
            }
        }
    }
}
