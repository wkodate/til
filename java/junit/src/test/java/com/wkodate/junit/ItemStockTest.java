package com.wkodate.junit;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by wkodate on 2018/11/19.
 */
@RunWith(Enclosed.class)
public class ItemStockTest {

    public static class emptyItem {

        ItemStock itemStock;
        Item item;

        @Before
        public void setUp() {
            item = new Item("item1", 100);
            itemStock = new ItemStock();
        }

        @Test
        public void testAdd() throws Exception {
            itemStock.add(item);
            assertThat(itemStock.getNum(item), is(1));
        }

        @Test
        public void testGetNum() throws Exception {
            assertThat(itemStock.getNum(item), is(0));
        }

    }

    public static class oneItem {

        ItemStock itemStock;
        Item item;
        Item item2;

        @Before
        public void setUp() {
            item = new Item("item1", 100);
            item2 = new Item("item2", 200);
            itemStock = new ItemStock();
            itemStock.add(item);
        }

        @Test
        public void testAdd() throws Exception {
            itemStock.add(item);
            assertThat(itemStock.getNum(item), is(2));
        }

        @Test
        public void testAddAnotherItem() throws Exception {
            itemStock.add(item2);
            assertThat(itemStock.getNum(item2), is(1));
        }

        @Test
        public void testGetNum() throws Exception {
            assertThat(itemStock.getNum(item), is(1));
        }

    }
}