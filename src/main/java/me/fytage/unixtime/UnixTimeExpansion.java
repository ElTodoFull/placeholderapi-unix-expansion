package me.fytage.unixtime;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnixTimeExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "unixtime";
    }

    @Override
    public @NotNull String getAuthor() {
        return "fytage";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        String timeInput;
        String formatPattern;

        if (params.startsWith("{")) {
            int separatorIndex = params.indexOf("}_");

            if (separatorIndex == -1) {
                return "Invalid Format: Missing '_' after placeholder";
            }

            timeInput = params.substring(0, separatorIndex + 1);
            formatPattern = params.substring(separatorIndex + 2);

        } else {
            String[] parts = params.split("_", 2);
            if (parts.length < 2) {
                return "Invalid Format";
            }
            timeInput = parts[0];
            formatPattern = parts[1];
        }

        if (timeInput.startsWith("{") && timeInput.endsWith("}")) {
            String nestedPlaceholder = timeInput.substring(1, timeInput.length() - 1);
            timeInput = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, "%" + nestedPlaceholder + "%");
        }

        String sanitizedTimeInput = timeInput.trim();

        try {
            long timestamp = Long.parseLong(sanitizedTimeInput);

            if (sanitizedTimeInput.length() == 10) {
                timestamp *= 1000L;
            }

            String finalPattern = formatPattern.replace("-", " ");

            SimpleDateFormat dateFormat = new SimpleDateFormat(finalPattern);
            return dateFormat.format(new Date(timestamp));

        } catch (NumberFormatException e) {
            return "Invalid Timestamp: \"" + sanitizedTimeInput + "\"";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
