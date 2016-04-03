package fr.heavencraft.async.queries;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueriesHandler
{
	private static Queue<Query> queries = new ConcurrentLinkedQueue<Query>();

	public static void addQuery(Query query)
	{
		queries.add(query);
	}

	// Package only
	static Query pollQuery()
	{
		return queries.poll();
	}
}