package jetoze.jambon.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

/**
 * Represents a folder on disk.
 */
public final class Folder {
	private final Path path;

	public static Folder of(String path) {
		return new Folder(path);
	}
	
	public Folder(String path) {
		this(Paths.get(path));
	}
	
	public Folder(Path path) {
		this.path = checkNotNull(path);
	}

	public void createOnDisk() {
		File f = path.toFile();
		if (!f.exists()) {
			boolean ok = f.mkdir();
			if (!ok) {
				throw new RuntimeException("Failed to create the folder " + path);
			}
		}
	}
	
	public File getFile(String name) {
		return Paths.get(this.path.toString(), name).toFile();
	}
	
	public ImmutableList<File> listAllFiles() {
		return listFiles(f -> true);
	}
	
	public ImmutableList<File> listFiles(FileFilter filter) {
		return streamFiles(filter).collect(ImCollectors.toList());
	}
	
	public Stream<File> streamFiles(FileFilter filter) {
		File[] files = path.toFile().listFiles(filter);
		if (files == null) {
			files = new File[0];
		}
		return Arrays.stream(files);
	}
	
	@Override
	public String toString() {
		return path.toString();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return (obj instanceof Folder) && this.path.equals(((Folder) obj).path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

}
