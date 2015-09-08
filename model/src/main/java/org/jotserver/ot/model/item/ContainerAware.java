package org.jotserver.ot.model.item;

import java.util.Collection;

public interface ContainerAware {
	void registerContainer(Container container);
	void unregisterContainer(Container container);
	boolean isContainerRegistred(Container container);
	int getNextContainerId();
	void suggestNextContainerId(int id);
	int getContainerId(Container container);
	Container getContainer(int id);
	Collection<Container> getContainers();
}
