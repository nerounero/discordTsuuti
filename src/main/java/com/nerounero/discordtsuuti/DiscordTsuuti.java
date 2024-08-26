package com.nerounero.discordtsuuti;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.awt.Color;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;


public final class DiscordTsuuti extends JavaPlugin implements Listener {
    private String webhookUrl;
    private String botName;
    private Color serverStartColor;
    private Color serverStopColor;
    private Color playerJoinColor;
    private Color playerQuitColor;
    private FileConfiguration langConfig;

    @Override
    public void onEnable() {
        // コンフィグを保存
        saveDefaultConfig();

        // 言語ファイルを保存
        saveDefaultLanguageFiles();

        // 設定を読み込む
        FileConfiguration config = getConfig();
        webhookUrl = config.getString("webhook-url");
        botName = config.getString("bot-name");
        serverStartColor = Color.decode(config.getString("colors.server-start", "#00FF00"));
        serverStopColor = Color.decode(config.getString("colors.server-stop", "#FF0000"));
        playerJoinColor = Color.decode(config.getString("colors.player-join", "#00FF00"));
        playerQuitColor = Color.decode(config.getString("colors.player-quit", "#FFFF00"));

        // 言語ファイルをロード
        loadLanguageFile(config.getString("language", "ja-jp"));

        // イベントリスナーを登録
        Bukkit.getPluginManager().registerEvents(this, this);

        // サーバー起動通知を送信
        sendDiscordNotification(langConfig.getString("server-start"), serverStartColor);
    }

    @Override
    public void onDisable() {
        // サーバー停止通知を送信
        if (langConfig != null) {
            sendDiscordNotification(langConfig.getString("server-stop"), serverStopColor);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String message = String.format(langConfig.getString("player-join"), event.getPlayer().getName());
        sendDiscordNotification(message, playerJoinColor);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String message = String.format(langConfig.getString("player-quit"), event.getPlayer().getName());
        sendDiscordNotification(message, playerQuitColor);
    }

    private void sendDiscordNotification(String message, Color color) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            String jsonPayload = String.format(
                    "{\"username\": \"%s\", \"embeds\": [{\"description\": \"%s\", \"color\": %d}]}",
                    botName,
                    message,
                    color.getRGB() & 0xFFFFFF // DiscordではRGBの24bitカラーを使用
            );

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            connection.getResponseCode(); // 実際にリクエストを送信

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLanguageFile(String languageCode) {
        String langFileName = languageCode.toLowerCase(Locale.ROOT) + ".yml";
        File langFile = new File(getDataFolder(), langFileName);

        if (!langFile.exists()) {
            saveResource(langFileName, false);
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);
        if (langConfig == null) {
            getLogger().warning("Could not load language file: " + langFileName);
        }
    }

    private void saveDefaultLanguageFiles() {
        saveLanguageFile("ja-jp.yml");
        saveLanguageFile("en-us.yml");
    }

    private void saveLanguageFile(String fileName) {
        File langFile = new File(getDataFolder(), fileName);
        if (!langFile.exists()) {
            saveResource(fileName, false);
        }
    }

}
