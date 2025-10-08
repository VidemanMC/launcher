package ru.videmanmc.launcher.gui.component;

import lombok.SneakyThrows;
import org.intellij.lang.annotations.Language;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Dialog for showing user message when an exception occurs
 */
public class ExceptionDialog extends JFrame {

    @Language("html")
    private static final String HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta charset="UTF-8" />
            <style>
              p, a, h1, h2 {
                font-size: 25px;
              }
            </style>
            </head>
            <body>
                %s
            </body>
            </html>
            """;

    public ExceptionDialog(String htmlMessage) {
        super("ОШИБКА");
        showExceptionDialog(HTML_TEMPLATE.formatted(htmlMessage));
    }

    private void showExceptionDialog(String message) {
        JEditorPane editorPane = createHtmlEditorPane(message);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        JButton okButton = new JButton("Закрыть");
        okButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setSize(500, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        var imageUrl = getClass().getClassLoader().getResource("icon64.png");
        var icon = new ImageIcon(imageUrl);
        setIconImage(icon.getImage());

        setVisible(true);
    }

    private JEditorPane createHtmlEditorPane(String htmlContent) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditorKit(new HTMLEditorKit());
        editorPane.setText(htmlContent);
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    openWebpage(e.getURL()
                                 .toURI());
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return editorPane;
    }

    @SneakyThrows
    private void openWebpage(URI uri) {
        var desktop = Desktop.getDesktop();

        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        } else {
            Runtime.getRuntime()
                   .exec(new String[]{"xdg-open", uri.toString()});
        }
    }

}
