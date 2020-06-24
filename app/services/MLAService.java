package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import models.Item;
import org.springframework.stereotype.Service;
import play.libs.Json;
import play.libs.ws.WSClient;
import com.typesafe.config.Config;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MLAService {

    private Config config;
    private WSClient ws;
    private String urlApi;

    @Inject
    public MLAService(Config config, WSClient ws) {
        this.ws = ws;
        this.config = config;
        this.urlApi = this.config.getString("mercadolibre.api.url");
    }

    public Item getItem(String itemId) {
        Item item = null;
        CompletionStage<Item> response = ws.url(urlApi + "/" + itemId)
                .get()
                .thenApply(r -> Json.fromJson(r.asJson(),Item.class));
        try {
            item = response.toCompletableFuture().get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return item;
    }

    public List<Item> getItems(List<String> itemsIds) {
        List<Item> result = Lists.newArrayList();
        CompletionStage<List<Item>> response = ws.url(urlApi)
                .addQueryParameter("ids", String.join(",", itemsIds))
                .get()
                .thenApply(r -> {
                    List<Item> items = Lists.newArrayList();
                    JsonNode jsonNode = r.asJson();
                    if(jsonNode.isArray()){
                        jsonNode.forEach(j -> {
                            if(j.get("code").intValue() == 200){
                                items.add(Json.fromJson(j.get("body"), Item.class));
                            }
                        });
                    }
                    return items;
                });
        try {
            result = response.toCompletableFuture().get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return result;
    }

}