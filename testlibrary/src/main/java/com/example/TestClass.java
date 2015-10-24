package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by GZK on 2015/10/21.
 */
public class TestClass {

    public static void main(String[] str){
        TestClass testClass=new TestClass();

       // testClass.testWhile();
        testClass.comparableTest();
    }


    private void testWhile(){
        int i=0;
        do {
            if(i<10){
               printOut((i+""));
                i++;
            }
        }while (true);
    }

    private void comparableTest(){
        List<B> listB = new ArrayList<B>();
        B ab = new B();
        ab.setName("ab");
        ab.setOrder("3");
        B ba = new B();
        ba.setName("ba");
        ba.setOrder("2");
        listB.add(ba);
        listB.add(ab);

        Collections.sort(listB, new Comparator<B>() {
            @Override
            public int compare(B b1, B b2) {
                return b1.getOrder().compareTo(b2.getOrder());
            }

        });
        System.out.println(listB);
    }

    class B{
        private String name;
        private String order;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getOrder() {
            return order;
        }
        public void setOrder(String order) {
            this.order = order;
        }
        @Override
        public String toString() {
            return "name is "+name+" order is "+order;
        }
    }

    /**
     * 打印消息
     * @param textstr str
     */
    private void printOut(String textstr){
        System.out.println(textstr);
    }
}
