package services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import static org.mockito.Mockito.*;
import com.typesafe.config.Config;
import models.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.routing.RoutingDsl;
import play.server.Server;

import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

import static play.mvc.Results.ok;

public class MLAServiceTest {

    private Config mockConfig = mock(Config.class);
    private MLAService mlaService;
    private WSClient ws;
    private Server server;

    private List<Item> itemList = Lists.newArrayList(
            new Item("MLA1", 100f),
            new Item("MLA2", 210f),
            new Item("MLA3", 260f),
            new Item("MLA4", 80f),
            new Item("MLA5", 90f));


    @Before
    public void setup() {
        server =
                Server.forRouter(
                        (components) ->
                                RoutingDsl.fromComponents(components)
                                        .GET("https://api.mercadolibre.com/items")
                                        .routingTo(
                                                request -> {
                                                    String json = "[{\"code\":200,\"body\":{\"id\":\"MLA1\",\"price\":4790}},{\"code\":200,\"body\":{\"id\":\"MLA2\",\"price\":4790}},{\"code\":404,\"body\":{\"id\":\"MLA3\",\"message\":\"Item with id MLA3 not found\",\"error\":\"not_found\",\"status\":404,\"cause\":[]}}]";

                                                    /*ArrayNode items = Json.newArray();

                                                    ObjectNode itemOK1 = Json.newObject();
                                                    itemOK1.put("code", 200);
                                                    itemOK1.put("body", "{\"id\":\"MLA1\",\"price\":4790}}");

                                                    ObjectNode itemOK2 = Json.newObject();
                                                    itemOK2.put("code", 200);
                                                    itemOK2.put("body", "{\"id\":\"MLA2\",\"price\":4790}}");

                                                    ObjectNode itemNotFound = Json.newObject();
                                                    itemNotFound.put("code", 404);
                                                    itemNotFound.put("body", "{\"id\":\"MLA3\",\"message\":\"Item with id MLA3 not found\",\"error\":\"not_found\",\"status\":404,\"cause\":[]}");

                                                    items.add(itemOK1);
                                                    items.add(itemOK2);
                                                    items.add(itemNotFound);*/
                                                    return ok(json);
                                                })
                                        .build());
        ws = play.test.WSTestClient.newClient(server.httpPort());

        when(mockConfig.getString("mercadolibre.api.url")).thenReturn("https://api.mercadolibre.com/items");

        this.mlaService = new MLAService(mockConfig, ws);
    }

    @After
    public void tearDown() throws IOException {
        try {
            ws.close();
        } finally {
            server.stop();
        }
    }

    //@Test
    public void testGetItems() {
        List<String> itemIds = Lists.newArrayList("MLA1", "MLA2", "MLA3");
        List<Item> items = mlaService.getItems(itemIds);
        assertEquals(2, items.size());
    }

}
