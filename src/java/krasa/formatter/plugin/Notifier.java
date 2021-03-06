package krasa.formatter.plugin;

import krasa.formatter.settings.ProjectSettingsComponent;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author Vojtech Krasa
 */
public class Notifier {

	public static final String NO_FILE_TO_FORMAT = "No file to format";

	public void notifyFailedFormatting(PsiFile psiFile, boolean formattedByIntelliJ, Exception e) {
		String error = e.getMessage() == null ? "" : e.getMessage();
		notifyFailedFormatting(psiFile, formattedByIntelliJ, error);
	}

	public void notifyFailedFormatting(PsiFile psiFile, boolean formattedByIntelliJ, final String reason) {
		String content;
		if (!formattedByIntelliJ) {
			content = psiFile.getName() + " failed to format with Eclipse code formatter. " + reason + "\n";
		} else {
			content = psiFile.getName() + " failed to format with IntelliJ code formatter.\n" + reason;
		}
		Notification notification = new Notification(ProjectSettingsComponent.GROUP_DISPLAY_ID_ERROR, "", content,
				NotificationType.ERROR);
		showNotification(notification, psiFile.getProject());
	}

	void notifyFormattingWasDisabled(PsiFile psiFile) {
		Notification notification = new Notification(ProjectSettingsComponent.GROUP_DISPLAY_ID_INFO, "",
				psiFile.getName() + " - formatting was disabled for this file type", NotificationType.WARNING);
		showNotification(notification, psiFile.getProject());
	}

	void notifySuccessFormatting(PsiFile psiFile, boolean formattedByIntelliJ) {
		String content;
		if (formattedByIntelliJ) {
			content = psiFile.getName() + " formatted successfully by IntelliJ code formatter";
		} else {
			content = psiFile.getName() + " formatted successfully by Eclipse code formatter";
		}
		Notification notification = new Notification(ProjectSettingsComponent.GROUP_DISPLAY_ID_INFO, "", content,
				NotificationType.INFORMATION);
		showNotification(notification, psiFile.getProject());
	}

	void showNotification(final Notification notification, final Project project) {
		ApplicationManager.getApplication().invokeLater(new Runnable() {
			@Override
			public void run() {
				Notifications.Bus.notify(notification, project);
			}
		});
	}

	public void notifyBrokenImportSorter(Project project) {
		String content = "Formatting failed due to new Import optimizer.";
		Notification notification = new Notification(ProjectSettingsComponent.GROUP_DISPLAY_ID_ERROR, "", content,
				NotificationType.ERROR);
		showNotification(notification, project);

	}

	public static void notifyDeletedSettings(final Project project) {
		String content = "Eclipse formatter settings profile was deleted for project " + project.getName()
				+ ". Default settings is used now.";
		final Notification notification = new Notification(ProjectSettingsComponent.GROUP_DISPLAY_ID_ERROR, "",
				content, NotificationType.ERROR);
		ApplicationManager.getApplication().invokeLater(new Runnable() {
			@Override
			public void run() {
				Notifications.Bus.notify(notification, project);
			}
		});

	}

}
