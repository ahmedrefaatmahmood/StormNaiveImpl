package edu.purdue.cs.text.bolt;

/**
 * A simple example of an in-memory search using Lucene.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class InMemoryFullTextFilter {
	private final IndexWriter writer;
	private IndexSearcher searcher;
	private DirectoryReader reader ;
	
	public  InMemoryFullTextFilter() throws IOException
	{
		RAMDirectory idx = new RAMDirectory();// there exist better options.
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_0,new StandardAnalyzer());
    	config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        writer =  new IndexWriter(idx,config);
        writer.commit();
        reader = DirectoryReader.open(idx);
        searcher = new IndexSearcher (reader);
	}
	
	public void registerQuery(String id, String content)
	{
		Document doc = createDocument(id, content);
		try {
			writer.addDocument(doc);
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void finish(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Searches current Index
	 * @param queryString
	 * @return matching set of queryId's 
	 */
	public Collection<String> search(String queryString) 
	{
        ArrayList<String> result=null;
		try {
			// Build a Query object
			DirectoryReader reader2 =  DirectoryReader.openIfChanged(reader);
			reader = reader2 == null? reader:reader2;
			searcher = new IndexSearcher (reader);
			Query query;
				query = new QueryParser( "content", new StandardAnalyzer())
						.parse(QueryParser.escape(queryString));

			// Search for the query
			TopDocs hits = searcher.search(query,100000);

			int hitCount = hits.totalHits;
			result = new ArrayList<String>();
			for (int i = 0; i < hitCount; i++) {
				Document	doc = searcher.doc(hits.scoreDocs[i].doc);
			    result.add(doc.get("id"));
			}
			
			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

    public static void main(String[] args) {
        // Construct a RAMDirectory to hold the in-memory representation
        // of the index.

        try {
        	InMemoryFullTextFilter filter = new InMemoryFullTextFilter();
        	 

            // Add some Document objects containing quotes
        	filter.registerQuery("Theodore Roosevelt", 
                "It behooves every man to remember that the work of the " +
                "critic, is of altogether secondary importance, and that, " +
                "in the end, progress is accomplished by the man who does " +
                "things.");
        	filter.registerQuery("Friedrich Hayek",
                "The case for individual freedom rests largely on the " +
                "recognition of the inevitable and universal ignorance " +
                "of all of us concerning a great many of the factors on " +
                "which the achievements of our ends and welfare depend.");
        	filter.registerQuery("Ayn Rand",
                "There is nothing to take a man's freedom away from " +
                "him, save other men. To be free, a man must be free " +
                "of his brothers.");
        	filter.registerQuery("Mohandas Gandhi",
                "Freedom is not worth having if it does not connote " +
                "freedom to err.");

        	System.out.println("freedom: appreared");
            // Run some queries
            for (String id : filter.search("freedom"))
            	System.out.println("freedom: appreared in "+id);
            for(String id : filter.search("free"))
            	System.out.println("free: appeared in" + id);
            for(String id : filter.search("progress or achievements"))
            	System.out.println("free: appeared in" + id);

        	filter.finish();
        }
        catch(IOException ioe) {
            // In this example we aren't really doing an I/O, so this
            // exception should never actually be thrown.
            ioe.printStackTrace();
        }
    }

    /**
     * Make a Document object with an un-indexed title field and an
     * indexed content field.
     */ 
    private static Document createDocument(String id, String content) {
        Document doc = new Document();
        doc.add(new StringField("id", id,Store.YES));
        doc.add(new TextField("content", content,Store.YES));
        return doc;
    }

  

}