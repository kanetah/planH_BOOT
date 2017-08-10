package com.kanetah.planH.info;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TaskInfoAttribute {

    TASK_ID("taskId"),
    SUBJECT("subject"),
    TITLE("title"),
    CONTENT("content"),
    DEADLINE("deadline"),
    SUBMIT("submitDate"),
    FILENAME("submitFileName");

    public static final List<TaskInfoAttribute> AllTaskInfoAttribute = new ArrayList<>();
    static {
        AllTaskInfoAttribute.addAll(Arrays.asList(TaskInfoAttribute.values()));
    }

    private final String value;
    private final String method;

    TaskInfoAttribute(String value) {
        this.value = value;
        this.method = "get" + value.substring(0,1).toUpperCase() + value.substring(1);
    }

    public Object invokeMargetMethod(TaskInfo taskInfo) {

        try {
            Method method = TaskInfo.class.getMethod(getMethod());
            return method.invoke(taskInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getValue() {
        return value;
    }

    public String getMethod() {
        return method;
    }

    public static void main(String[] args) {
        Field[] fields = TaskInfo.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            System.out.println("成员变量" + i + "类型 : " + fields[i].getType().getName());
            System.out.println("\t成员变量" + i + "变量名: " + fields[i].getName() + "\t");
        }
    }
}
