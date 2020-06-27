package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import models.Item;
import org.junit.Test;
import org.mockito.Mockito;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MLAServiceTest {

    private Config mockConfig = mock(Config.class);
    private MLAService mlaService;
    private WSClient ws = mock(WSClient.class);


    @Test
    public void testParseResponse() {
        when(mockConfig.getString("mercadolibre.api.url")).thenReturn("https://api.mercadolibre.com/items");
        final WSResponse wsResponseMock = Mockito.mock(WSResponse.class);
        Mockito.doReturn(200).when(wsResponseMock).getStatus();
        String jsonStr = "[{\"code\":200,\"body\":{\"id\":\"MLA1\",\"price\":4790}},{\"code\":200,\"body\":{\"id\":\"MLA2\",\"price\":4790}},{\"code\":404,\"body\":{\"id\":\"MLA3\",\"message\":\"Item with id MLA3 not found\",\"error\":\"not_found\",\"status\":404,\"cause\":[]}}]";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mockito.doReturn(jsonNode).when(wsResponseMock).asJson();
        this.mlaService = new MLAService(mockConfig, ws);
        List<Item> items = this.mlaService.parseWsResponse(wsResponseMock);
        assertEquals(2, items.size());
    }

    @Test
    public void testExceptionResponses() {
        final WSResponse wsResponseMock = Mockito.mock(WSResponse.class);
        when(wsResponseMock.asJson()).thenThrow(Exception.class);
        this.mlaService = new MLAService(mockConfig, ws);
        List<Item> items = this.mlaService.parseWsResponse(wsResponseMock);
        assertEquals(0, items.size());
    }

}
