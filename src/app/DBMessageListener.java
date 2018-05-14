package app;

import java.util.EventListener;

public interface DBMessageListener extends EventListener {
	public void doMessage(DBMessageEvent event);
}
