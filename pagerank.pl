previousPageRank = LOAD '$INPUT' USING PigStorage('\\t')
	AS (url:chararray, pagerank:float, links:chararray)
;
previousPageRank = FOREACH previousPageRank
	GENERATE
		url,
		pagerank,
		TOKENIZE(links,' ') AS links:{link:tuple(url:chararray)},
		links AS strLinks
;
outlinks = FOREACH previousPageRank
	GENERATE
		pagerank/((float)COUNT(links)) AS pagerank,
		FLATTEN(links) as url
;
newPageRank = FOREACH ( COGROUP outlinks BY url, previousPageRank BY url INNER)
	GENERATE
		FLATTEN(previousPageRank.url),
		( 1 - 0.85 ) + 0.85 * SUM ( outlinks.pagerank ) AS pagerank,
		FLATTEN(previousPageRank.strLinks)
;
STORE newPageRank
	INTO '$OUTPUT'
	USING PigStorage('\t');
