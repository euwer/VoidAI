package com.voidofaesthetic.voidhelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class HelpCommandExecutor implements CommandExecutor {

    private final String apiKey;
    private final String model;

    public HelpCommandExecutor(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String question = String.join(" ", args);
                String response = getAIResponse(question);
                player.sendMessage(response);
                return true;
            } else {
                player.sendMessage("Please provide a question.");
                return false;
            }
        }
        return false;
    }

    private String getAIResponse(String question) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JsonObject json = new JsonObject();
        json.addProperty("model", model);

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", question);

        JsonArray messages = new JsonArray();
        messages.add(message);
        json.add("messages", messages);
        json.addProperty("max_tokens", 200);

        RequestBody body = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonObject responseObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
                JsonArray choices = responseObject.getAsJsonArray("choices");
                if (choices != null && choices.size() > 0) {
                    JsonObject choice = choices.get(0).getAsJsonObject();
                    JsonObject messageObject = choice.getAsJsonObject("message");
                    if (messageObject != null) {
                        return messageObject.get("content").getAsString();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Yapay zekadan cevap alinamadi.";
    }
}