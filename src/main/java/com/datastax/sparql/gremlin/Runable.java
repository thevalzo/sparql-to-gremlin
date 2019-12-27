package com.datastax.sparql.gremlin;

import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Collectors;

import org.apache.tinkerpop.gremlin.jsr223.JavaTranslator;
import org.apache.tinkerpop.gremlin.process.traversal.Bytecode;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;


import org.janusgraph.core.*;
public class Runable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String query = "SELECT * WHERE { ?a v:name 'marko' }";
		Graph graph = JanusGraphFactory.open("conf/janusgraph-hbase-es.properties");
		Traversal<Vertex, ?> traversal = SparqlToGremlinCompiler.convertToGremlinTraversal(graph, query);
		Bytecode traversalByteCode = traversal.asAdmin().getBytecode();
		try {
		printWithHeadline("Result", String.join(System.lineSeparator(),JavaTranslator.of(graph.traversal()).translate(traversalByteCode).toStream().map(Object::toString).collect(Collectors.toList())));
		}catch(Exception e){e.printStackTrace();}
		System.out.println("traversal profile : "+ traversal.profile().toList());
	}
	
	private static void printWithHeadline(final String headline, final Object content) throws IOException {
        final StringReader sr = new StringReader(content != null ? content.toString() : "null");
        final BufferedReader br = new BufferedReader(sr);
        String line;
        System.out.println();
        System.out.println( headline ); 
        System.out.println();
        boolean skip = true;
        while (null != (line = br.readLine())) {
            skip &= line.isEmpty();
            if (!skip) {
                System.out.println("  " + line);
            }
        }
        System.out.println();
        br.close();
        sr.close();
    }
}
