package i5.las2peer.classLoaders;

import i5.las2peer.classLoaders.helpers.LibraryDependency;

/**
 * exception thrown on attempts to load a library which dependencies could not be met by the registered jar repositories
 * 
 */
public class UnresolvedDependenciesException extends ClassLoaderException {

	private static final long serialVersionUID = 4044043650841236234L;

	public UnresolvedDependenciesException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnresolvedDependenciesException(String message) {
		super(message);
	}

	public UnresolvedDependenciesException(String name, LibraryDependency dep) {
		super("Dependency " + dep.toString() + " for library '" + name + "' could not be resolved!");
	}

	public UnresolvedDependenciesException(String name, LibraryDependency dep, Throwable cause) {
		super("Dependency " + dep.toString() + " for library '" + name + "' could not be resolved!", cause);
	}

}
