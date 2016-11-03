package cindy.http.handler;

import java.io.File;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Map;

public class CachedFileHandler extends SimpleFileHandler {
	
	private final Map cache = new Hashtable();
	private final ReferenceQueue queue = new ReferenceQueue();
	
	private class Reference extends SoftReference{
		private final long lastModified;
		
		public Reference(ByteBuffer buffer, long lastModified){
			super(buffer, queue);
			this.lastModified = lastModified;
		}
	}
	
	protected ByteBuffer getContent(String uri)throws IOException{
		File file = getFile(uri);
		String canonicalPath = file.getCanonicalPath();
		expungeStaleEntries();
		
		long lastModified = file.lastModified();
		Reference reference = (Reference)cache.get(canonicalPath);
		if(reference!=null){
			ByteBuffer buffer = (ByteBuffer)reference.get();
			if(buffer!=null & lastModified==reference.lastModified){
				return buffer;
			}
		}
		ByteBuffer buffer = getContent(uri, file);
		if(buffer!=null)
			cache.put(canonicalPath, new Reference(buffer, lastModified));
		return buffer;
	}
	
	private void expungeStaleEntries(){
		Object obj = null;
		while((obj=queue.poll())!=null){
			cache.values().remove(obj);
		}
	}

}
