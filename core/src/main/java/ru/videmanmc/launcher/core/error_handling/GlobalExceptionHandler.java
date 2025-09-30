package ru.videmanmc.launcher.core.error_handling;

import ru.videmanmc.launcher.constants.ErrorMessageConstants;
import ru.videmanmc.launcher.core.exception.AutomaticInstallationImpossible;
import ru.videmanmc.launcher.gui.component.ExceptionDialog;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ru.videmanmc.launcher.constants.ErrorMessageConstants.GENERAL_ERROR;


public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        new ExceptionDialog(getHtmlMessage(e));
    }

    private String getHtmlMessage(Throwable throwable) {
        if (throwable instanceof AutomaticInstallationImpossible) {
            return ErrorMessageConstants.INSTALL_CLIENT_MANUALLY;
        }

        return formatGeneral(throwable);
    }

    private String formatGeneral(Throwable throwable) {
        var stacktrace = Arrays.stream(throwable.getStackTrace())
                               .map(StackTraceElement::toString)
                               .collect(Collectors.joining("<br>"));
        return GENERAL_ERROR
                .formatted(
                        throwable.getMessage(),
                        stacktrace
                );
    }
}
