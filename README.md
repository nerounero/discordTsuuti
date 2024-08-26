# DiscordTsuuti
## このプラグインについて
このプラグインはサーバーの起動時や停止時、プレイヤーの参加および退出時にDiscordのwebhookを用いて通知するものです。  
DiscordSRVとは違い、botを作成する手間がかからない分お手軽だと思います。  
プラグイン制作初心者のため、機能は最低限です。すまんそ。  

## 設定
pluginsフォルダの中にdiscordTsuutiというフォルダができて、その中に`config.yml`,`ja-jp.yml`,`en-us.yml`が作成されます。  
いじるべきファイルは`config.yml`ですかね。  
```yaml:config.yml
# The webhook URL to send Discord notifications.
webhook-url: "https://discord.com/api/webhooks/your-webhook-url"

# The name of the bot that will appear in Discord notifications.
bot-name: "ServerBot"

# Colors for different types of notifications. Specify the color in RGB hex format.
colors:
  server-start: "#00FF00" # Notification color when the server starts (green).
  server-stop: "#FF0000"  # Notification color when the server stops (red).
  player-join: "#00FF00"  # Notification color when a player joins (green).
  player-quit: "#FFFF00"  # Notification color when a player quits (yellow).

# The language file to use (ja-jp.yml, en-us.yml, etc.).
language: "ja-jp"
```

`webhook-url` にwebhookのURLを、`bot-name`は通知する際の名前です。  
また`language`は.ymlを除いた言語用ファイルで、日本語、英語は標準でついてきます。
