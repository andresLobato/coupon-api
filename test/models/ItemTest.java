package models;

import org.junit.Test;
import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void testCreateSetterAndGetters() {
        Item item = new Item("MLA1", 90f);
        assertTrue(90f == item.getPrice());
        item.setPrice(100f);
        assertTrue(100f == item.getPrice());
        item.setId("MLA2");
        assertEquals("MLA2", item.getId());
    }

}
