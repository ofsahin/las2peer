package i5.las2peer.persistency.helper;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.Set;

import i5.las2peer.api.StorageCollisionHandler;
import i5.las2peer.api.StorageExceptionHandler;
import i5.las2peer.api.StorageStoreResultHandler;
import i5.las2peer.api.exceptions.EnvelopeAlreadyExistsException;
import i5.las2peer.api.exceptions.StopMergingException;
import i5.las2peer.persistency.Envelope;

public class StoreProcessHelper implements StorageStoreResultHandler, StorageCollisionHandler, StorageExceptionHandler {

	private Exception exception;
	private int successfulOperations = -1;

	@Override
	public void onResult(Serializable serializable, int successfulOperations) {
		synchronized (this) {
			this.successfulOperations = successfulOperations;
		}
	}

	@Override
	public Serializable onCollision(Envelope toStore, Envelope inNetwork, long numberOfCollisions)
			throws StopMergingException {
		synchronized (this) {
			exception = new EnvelopeAlreadyExistsException("Envelope '" + toStore.getIdentifier() + "' with version ("
					+ toStore.getVersion()
					+ ") already exists in network! Use a collision handler or fetch latest version before storing.");
			// stop merging process
			throw new StopMergingException();
		}
	}

	@Override
	public Set<PublicKey> mergeReaders(Set<PublicKey> toStoreReaders, Set<PublicKey> inNetworkReaders) {
		// nothing to merge here in usual store operations there should be nothing to merge
		return new HashSet<>();
	}

	@Override
	public Set<Long> mergeGroups(Set<Long> toStoreGroups, Set<Long> inNetworkGroups) {
		// nothing to merge here in usual store operations there should be nothing to merge
		return new HashSet<>();
	}

	@Override
	public void onException(Exception e) {
		synchronized (this) {
			if (exception == null) {
				exception = e;
			}
		}
	}

	public int getResult() throws Exception {
		synchronized (this) {
			if (exception != null) {
				throw exception;
			}
			return successfulOperations;
		}
	}

}
