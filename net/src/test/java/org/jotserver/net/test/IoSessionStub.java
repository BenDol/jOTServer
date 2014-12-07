package org.jotserver.net.test;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Set;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.DefaultCloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

public class IoSessionStub implements IoSession {
	
	private HashMap<Object, Object> attributes;
	
	public IoSessionStub() {
		attributes = new HashMap<Object, Object>();
	}
	
	
	public CloseFuture close() {
		return close(true);
	}

	
	public CloseFuture close(boolean arg0) {
		return new DefaultCloseFuture(this);
	}

	
	public CloseFuture closeOnFlush() {
		return new DefaultCloseFuture(this);
	}

	
	public boolean containsAttribute(Object arg0) {
		return attributes.containsKey(arg0);
	}

	
	public Object getAttachment() {
		return null;
	}

	
	public Object getAttribute(Object arg0) {
		return attributes.get(arg0);
	}

	
	public Object getAttribute(Object arg0, Object arg1) {
		return containsAttribute(arg0) ? getAttribute(arg0) : arg1;
	}

	
	public Set<Object> getAttributeKeys() {
		return attributes.keySet();
	}

	
	public int getBothIdleCount() {
		return 0;
	}

	
	public CloseFuture getCloseFuture() {
		return null;
	}

	
	public IoSessionConfig getConfig() {
		return null;
	}

	
	public long getCreationTime() {
		return 0;
	}

	
	public Object getCurrentWriteMessage() {
		return null;
	}

	
	public WriteRequest getCurrentWriteRequest() {
		return null;
	}

	
	public IoFilterChain getFilterChain() {
		return null;
	}

	
	public IoHandler getHandler() {
		return null;
	}

	
	public long getId() {
		return 0;
	}

	
	public int getIdleCount(IdleStatus arg0) {
		return 0;
	}

	
	public long getLastBothIdleTime() {
		return 0;
	}

	
	public long getLastIdleTime(IdleStatus arg0) {
		return 0;
	}

	
	public long getLastIoTime() {
		return 0;
	}

	
	public long getLastReadTime() {
		return 0;
	}

	
	public long getLastReaderIdleTime() {
		return 0;
	}

	
	public long getLastWriteTime() {
		return 0;
	}

	
	public long getLastWriterIdleTime() {
		return 0;
	}

	
	public SocketAddress getLocalAddress() {
		return null;
	}

	
	public long getReadBytes() {
		return 0;
	}

	
	public double getReadBytesThroughput() {
		return 0;
	}

	
	public long getReadMessages() {
		return 0;
	}

	
	public double getReadMessagesThroughput() {
		return 0;
	}

	
	public int getReaderIdleCount() {
		return 0;
	}

	
	public SocketAddress getRemoteAddress() {
		return null;
	}

	
	public long getScheduledWriteBytes() {
		return 0;
	}

	
	public int getScheduledWriteMessages() {
		return 0;
	}

	
	public IoService getService() {
		return null;
	}

	
	public SocketAddress getServiceAddress() {
		return null;
	}

	
	public TransportMetadata getTransportMetadata() {
		return null;
	}

	
	public int getWriterIdleCount() {
		return 0;
	}

	
	public long getWrittenBytes() {
		return 0;
	}

	
	public double getWrittenBytesThroughput() {
		return 0;
	}

	
	public long getWrittenMessages() {
		return 0;
	}

	
	public double getWrittenMessagesThroughput() {
		return 0;
	}

	
	public boolean isBothIdle() {
		return false;
	}

	
	public boolean isClosing() {
		return false;
	}

	
	public boolean isConnected() {
		return false;
	}

	
	public boolean isIdle(IdleStatus arg0) {
		return false;
	}

	
	public boolean isReaderIdle() {
		return false;
	}

	
	public boolean isWriterIdle() {
		return false;
	}

	
	public ReadFuture read() {
		return null;
	}

	
	public Object removeAttribute(Object arg0) {
		return attributes.remove(arg0);
	}

	
	public boolean removeAttribute(Object arg0, Object arg1) {
		return false;
	}

	
	public boolean replaceAttribute(Object arg0, Object arg1, Object arg2) {
		return false;
	}

	
	public void resumeRead() {

	}

	
	public void resumeWrite() {

	}

	
	public Object setAttachment(Object arg0) {
		return setAttribute("", arg0);
	}

	
	public Object setAttribute(Object arg0) {
		return setAttribute(arg0, Boolean.TRUE);
	}

	
	public Object setAttribute(Object arg0, Object arg1) {
		return attributes.put(arg0, arg1);
	}

	
	public Object setAttributeIfAbsent(Object arg0) {
		return null;
	}

	
	public Object setAttributeIfAbsent(Object arg0, Object arg1) {
		return null;
	}

	
	public void suspendRead() {

	}

	
	public void suspendWrite() {

	}

	
	public WriteFuture write(Object arg0) {
		return null;
	}

	
	public WriteFuture write(Object arg0, SocketAddress arg1) {
		return null;
	}


	public WriteRequestQueue getWriteRequestQueue() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean isReadSuspended() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isWriteSuspended() {
		// TODO Auto-generated method stub
		return false;
	}


	public void setCurrentWriteRequest(WriteRequest arg0) {
		// TODO Auto-generated method stub
		
	}


	public void updateThroughput(long arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

}
