package ir.jamareh.tiktok_aa;

public record TiktokResponse<T>(boolean success, String message, T data) {
}