package ru.videmanmc.launcher.constants;

import org.intellij.lang.annotations.Language;

public class ErrorMessageConstants {

    @Language("html")
    public static final String GENERAL_ERROR = """
            <h1>Скопируйте текст ошибки ниже (Ctr + C) и перейдите по ссылке ниже</h1>
            <p></p>
            <h2>Message</h2>
            <p>%s</p>
            <h2>Stacktrace</h2>
            <p>%s</p>
            <p></p>
            <a href="https://github.com/VidemanMC/launcher/issues/new?template=ошибка.md"
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
            <a href="https://videmanmc.ru/docs/mods/intro#ручная-установка-сборки"
               target="_blank"
               rel="noopener noreferrer"
               style="display:inline-block; padding:10px 20px; background-color:#007bff; color:#fff; text-decoration:none;">
                Открыть инструкцию
            </a>
            """;

}
