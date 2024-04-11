package net.thevpc.scholar.hadruplot;

public class TextContent {
    private String contentType;
    private String text;

    public TextContent(String contentType, String text) {
        this.contentType = contentType;
        this.text = text;
    }

    public String getContentType() {
        return contentType;
    }

    public String getText() {
        return text;
    }
}
