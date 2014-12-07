package org.jotserver.ot.model.item;

import java.util.Collection;

public interface ContainerAware {
	public void registerContainer(Container container);
	public void unregisterContainer(Container container);
	public boolean isContainerRegistred(Container container);
	public int getNextContainerId();
	public void suggestNextContainerId(int id);
	public int getContainerId(Container container);
	public Container getContainer(int id);
	public Collection<Container> getContainers();
}
