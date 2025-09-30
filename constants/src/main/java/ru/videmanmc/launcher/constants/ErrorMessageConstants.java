package ru.videmanmc.launcher.constants;

import org.intellij.lang.annotations.Language;

public class ErrorMessageConstants {

    @Language("html")
    public static final String REPORT_ERROR = """
            <p>Скопируйте текст ошибки ниже (Ctr + C) и перейдите по ссылке ниже</p>
            <p></p>
            <p>Message: %s</p>
            <p>Stacktrace: %s</p>
            <p></p>
            <a href="https://github.com/VidemanMC/launcher/issues"
               target="_blank"
               rel="noopener noreferrer"
               style="display:inline-block; padding:10px 20px; background-color:#007bff; color:#fff; text-decoration:none;">
                Сообщить об ошибке
            </a>
            """;

    @Language("html")
    public static final String INSTALL_CLIENT_MANUALLY = """
            <h1>Лаунчер не смог скачать сборку</h1>
            <p>Пожалуйста, следуйте инструкции по ссылке ниже, чтобы установить сборку вручную.</p>
            <p></p>
            <a href=""
               target="_blank"
               rel="noopener noreferrer"
               style="display:inline-block; padding:10px 20px; background-color:#007bff; color:#fff; text-decoration:none;">
                Открыть инструкцию
            </a>
            """;

}
